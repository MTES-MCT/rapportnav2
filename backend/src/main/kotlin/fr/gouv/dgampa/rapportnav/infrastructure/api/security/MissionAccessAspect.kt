package fr.gouv.dgampa.rapportnav.infrastructure.api.security

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.AssertMissionAccess
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.core.DefaultParameterNameDiscoverer
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import java.lang.reflect.Method

/**
 * Enforces [CheckMissionAccess] on controller handlers: resolves the id declared by the annotation and
 * delegates the cloisonnement decision to the pure domain use case [AssertMissionAccess].
 *
 * The aspect only calls the domain — it adds no authorization logic of its own — so a denial throws the
 * exact same [BackendUsageException] (403 / 404 / 400) that an inline call produced, and
 * `ControllersExceptionHandler` maps it unchanged.
 *
 * NOTE: Spring AOP proxies via CGLIB, which cannot override `final` members. This works only because the
 * build applies `kotlin("plugin.spring")` (all-open), which opens `@RestController` classes and methods —
 * the same reason the already-enabled method security can proxy them. Do not drop that plugin.
 */
@Aspect
@Component
class MissionAccessAspect(
    private val assertMissionAccess: AssertMissionAccess,
    childGuards: List<ChildAccessGuard>,
) {
    private val parameterNameDiscoverer = DefaultParameterNameDiscoverer()
    private val childGuardsByResource = childGuards.associateBy { it.resource }

    @Before("@annotation(check)")
    fun enforce(joinPoint: JoinPoint, check: CheckMissionAccess) {
        val method = (joinPoint.signature as MethodSignature).method
        val value = resolveValue(method, joinPoint.args, check.param)
            ?: throw invalidParam("'${check.param}' resolved to null")

        // 1. Access check: does the caller's service own the resource named by `param`?
        when (check.resource) {
            MissionAccessResource.INQUIRY -> assertMissionAccess.executeForInquiry(value.toString())
            MissionAccessResource.MISSION ->
                if (check.collection) {
                    val ids = value as? Collection<*>
                        ?: throw invalidParam("'${check.param}' is not a collection but collection=true")
                    ids.filterNotNull().map { it.toString() }.distinct().forEach(assertMissionAccess::execute)
                } else {
                    assertMissionAccess.execute(value.toString())
                }
        }

        // 2. Containment check: does the child named by `child` belong to that owner?
        if (check.child.isNotBlank()) {
            // A null child id means there is no existing child to contain — e.g. a create whose body
            // carries no id (the server mints one). Nothing to verify: the owner access check above
            // already applies, and the guard itself treats a not-yet-existing child as allowed.
            // (Path-variable children are never null here — a missing path variable fails to route.)
            val childId = resolveValue(method, joinPoint.args, check.child)
            if (childId != null) {
                val guard = childGuardsByResource[check.childResource]
                    ?: throw invalidParam("no ChildAccessGuard for childResource=${check.childResource}")
                guard.assertBelongsToOwner(childId.toString(), value.toString())
            }
        }
    }

    /**
     * Resolves [param] to the actual argument value. A plain name maps to a handler parameter; a dotted
     * `"<param>.<property>"` path first maps the parameter, then walks properties into it (e.g. a field
     * inside a `@RequestBody`).
     */
    private fun resolveValue(method: Method, args: Array<Any?>, param: String): Any? {
        val dot = param.indexOf('.')
        val paramName = if (dot >= 0) param.substring(0, dot) else param
        val propertyPath = if (dot >= 0) param.substring(dot + 1) else null

        val index = parameterIndex(method, paramName)
            ?: throw invalidParam("no handler parameter named '$paramName'")
        val arg = args[index]
        return if (propertyPath == null) arg else readProperty(arg, propertyPath)
    }

    /**
     * Index of the handler parameter named [name], matched against the explicit
     * `@PathVariable`/`@RequestParam` name first, then the Kotlin parameter name (resolved from Kotlin
     * metadata via kotlin-reflect — reliable even without the `-java-parameters` compiler flag).
     */
    private fun parameterIndex(method: Method, name: String): Int? {
        val discovered = parameterNameDiscoverer.getParameterNames(method)
        method.parameters.forEachIndexed { i, p ->
            val pathVar = p.getAnnotation(PathVariable::class.java)
            val reqParam = p.getAnnotation(RequestParam::class.java)
            val explicit = when {
                pathVar != null -> pathVar.name.ifBlank { pathVar.value }
                reqParam != null -> reqParam.name.ifBlank { reqParam.value }
                else -> ""
            }
            val resolved = explicit.ifBlank { discovered?.getOrNull(i) }
            if (resolved == name) return i
        }
        return null
    }

    private fun readProperty(target: Any?, path: String): Any? {
        var current = target
        for (segment in path.split('.')) {
            current = readSingle(current ?: return null, segment)
        }
        return current
    }

    private fun readSingle(target: Any, name: String): Any? {
        val getter = "get" + name.replaceFirstChar { it.uppercaseChar() }
        target.javaClass.methods.firstOrNull { it.parameterCount == 0 && (it.name == getter || it.name == name) }
            ?.let { return it.invoke(target) }
        target.javaClass.declaredFields.firstOrNull { it.name == name }
            ?.let { it.isAccessible = true; return it.get(target) }
        throw invalidParam("cannot read property '$name' on ${target.javaClass.simpleName}")
    }

    private fun invalidParam(reason: String) = BackendUsageException(
        code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
        message = "CheckMissionAccess: $reason",
    )
}
