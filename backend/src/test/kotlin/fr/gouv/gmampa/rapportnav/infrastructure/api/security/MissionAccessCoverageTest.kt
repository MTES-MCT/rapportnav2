package fr.gouv.gmampa.rapportnav.infrastructure.api.security

import fr.gouv.dgampa.rapportnav.infrastructure.api.security.CheckMissionAccess
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.reflect.KClass
import kotlin.reflect.full.memberFunctions

/**
 * Fail-closed guard for every by-id v2 handler (one that takes ANY `@PathVariable`). Two things must hold,
 * or the test turns red instead of shipping a silent IDOR:
 *
 * 1. The handler carries [CheckMissionAccess] (or is listed in [allowlist] with a reason).
 * 2. **Every** id path variable is covered by the annotation — the owner id by `param`, and any *other*
 *    id (a nested child, e.g. `actionId` under `ownerId`) by `child`. An uncovered path variable means the
 *    aspect authorizes one id while the handler operates on another — the exact owner-vs-child IDOR.
 *
 * Why "any path variable" rather than a set of known id names: a name allowlist (`missionId`, `ownerId`, …)
 * silently misses an endpoint that names its id anything else (`actionId`, `reportId`, …). Flagging every
 * path-variable handler and forcing an explicit annotation (or allowlist entry) is the fail-closed posture.
 *
 * Limitation (intentional): this scans **path variables** only. An endpoint that carries the id inside a
 * `@RequestBody` (e.g. the export endpoint) is not covered here — it is covered by its own controller
 * test. Keep such endpoints annotated manually.
 */
class MissionAccessCoverageTest {

    private val basePackage = "fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2"

    private val mappingAnnotations = setOf(
        GetMapping::class, PostMapping::class, PutMapping::class, DeleteMapping::class,
        PatchMapping::class, RequestMapping::class,
    )

    /**
     * By-id handlers that intentionally do NOT enforce mission cloisonnement via the aspect (reason
     * required). Add `"Controller#method"` here — with a comment — only for a deliberate exemption.
     */
    private val allowlist = setOf(
        // Global reference catalogs: a vessel / administration is identical for every service, not
        // per-service data, so there is nothing to cloisonner.
        "VesselRestController#getVesselById",
        "AdministrationController#get",
        // User profile: enforces cloisonnement inline (self / same-service / admin) with a
        // user-ownership model the mission aspect doesn't express. See UserRestController.getUserById.
        "UserRestController#getUserById",
    )

    @Test
    fun `every by-id v2 handler fully declares its mission access via @CheckMissionAccess`() {
        val provider = ClassPathScanningCandidateComponentProvider(false)
        provider.addIncludeFilter(AnnotationTypeFilter(RestController::class.java))

        val controllers = provider.findCandidateComponents(basePackage)
            .map { Class.forName(it.beanClassName).kotlin }

        val violations = controllers.flatMap { controller ->
            controller.memberFunctions
                .filter { fn -> fn.annotations.any { it.annotationClass in mappingAnnotations } }
                .filter { fn -> pathVariableNames(fn).isNotEmpty() } // by-id handlers only
                .mapNotNull { fn -> violationFor(controller, fn) }
        }

        assertTrue(violations.isEmpty()) {
            "By-id v2 handlers with incomplete cloisonnement (annotate, extend child=..., or allowlist with a reason):\n" +
                violations.joinToString("\n") { "  - $it" }
        }
    }

    /** A human-readable violation for a by-id handler, or null when it is fully covered / allowlisted. */
    private fun violationFor(controller: KClass<*>, fn: kotlin.reflect.KFunction<*>): String? {
        val key = "${controller.simpleName}#${fn.name}"
        if (key in allowlist) return null

        val pathVars = pathVariableNames(fn).toSet()
        val check = fn.annotations.filterIsInstance<CheckMissionAccess>().firstOrNull()
            ?: return "$key: by-id handler (path vars $pathVars) is missing @CheckMissionAccess"

        // param covers the owner id (unless it points inside a @RequestBody via a dotted path); child covers
        // the nested id. Any id path variable left uncovered is an owner-vs-child IDOR waiting to happen.
        val covered = buildSet {
            if (check.param.isNotBlank() && !check.param.contains('.')) add(check.param)
            if (check.child.isNotBlank()) add(check.child)
        }
        val uncovered = pathVars - covered
        return if (uncovered.isEmpty()) null
        else "$key: id path variable(s) $uncovered not covered by @CheckMissionAccess (set param/child to match)"
    }

    private fun pathVariableNames(fn: kotlin.reflect.KFunction<*>): List<String> =
        fn.parameters.mapNotNull { param ->
            val pathVar = param.annotations.filterIsInstance<PathVariable>().firstOrNull() ?: return@mapNotNull null
            pathVar.name.ifBlank { pathVar.value }.ifBlank { param.name ?: "" }
        }
}
