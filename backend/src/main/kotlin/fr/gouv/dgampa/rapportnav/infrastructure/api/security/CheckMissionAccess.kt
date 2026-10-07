package fr.gouv.dgampa.rapportnav.infrastructure.api.security

/**
 * The kind of resource the annotated handler operates on, which selects the matching
 * cloisonnement check on [fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.AssertMissionAccess].
 */
enum class MissionAccessResource { MISSION, INQUIRY }

/**
 * The kind of child resource whose containment is verified against the owner (see [CheckMissionAccess.child]).
 * Each value is backed by a [ChildAccessGuard] bean that knows how to resolve that child's owner.
 * [NONE] means no containment check (the default).
 */
enum class MissionChildResource { NONE, NAV_ACTION }

/**
 * Declarative object-level authorization (cloisonnement) for a mission-scoped REST handler.
 *
 * Put this on a controller method that takes a mission / owner / inquiry id and it is enforced by
 * [MissionAccessAspect] *before* the method body runs — the aspect resolves the id from [param] and
 * delegates to the [AssertMissionAccess][fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.AssertMissionAccess]
 * domain use case, throwing the same 403 / 404 / 400 as a manual call would.
 *
 * This replaces the imperative `assertMissionAccess.execute(id)` call at the top of each handler, so a
 * new by-id endpoint only has to *declare* its access requirement. A fail-closed coverage test
 * (`MissionAccessCoverageTest`) guards against forgetting the annotation on a new mission-scoped route.
 *
 * ## Nested (child) resources
 *
 * A handler that operates on a child resource identified by a *second* id — e.g.
 * `/owners/{ownerId}/actions/{actionId}` — must also declare [child]/[childResource]. The aspect then
 * verifies the child actually belongs to the owner, closing the IDOR where an authorized `ownerId` is
 * paired with a foreign `actionId`. The access check on [param] alone cannot catch this, and the
 * coverage test fails any such handler that leaves [child] undeclared.
 *
 * ```
 * @CheckMissionAccess(param = "ownerId")
 * fun getActions(@PathVariable(name = "ownerId") ownerId: String): List<MissionAction?> { ... }
 *
 * @CheckMissionAccess(param = "inquiryId", resource = MissionAccessResource.INQUIRY)
 * fun update(@PathVariable(name = "inquiryId") inquiryId: String, @RequestBody body: Inquiry): Inquiry? { ... }
 *
 * @CheckMissionAccess(param = "request.missionIds", collection = true)
 * fun exportMissionReports(@Valid @RequestBody request: ExportBodyRequest): MissionExportEntity { ... }
 *
 * @CheckMissionAccess(param = "ownerId", child = "actionId", childResource = MissionChildResource.NAV_ACTION)
 * fun deleteAction(@PathVariable actionId: String, @PathVariable ownerId: String) { ... }
 * ```
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CheckMissionAccess(
    /**
     * Where to read the id from. Either:
     * - the name of a handler parameter (matched against its `@PathVariable`/`@RequestParam` name, or
     *   the Kotlin parameter name), e.g. `"ownerId"`, or
     * - a dotted path `"<param>.<property>"` to reach a field inside a `@RequestBody`, e.g.
     *   `"request.missionIds"`.
     */
    val param: String,
    val resource: MissionAccessResource = MissionAccessResource.MISSION,
    /** true when the resolved value is a `Collection` of ids, each checked individually (e.g. export). */
    val collection: Boolean = false,
    /**
     * Optional containment check. When set, names the handler parameter holding the child id that must
     * belong to the owner resolved from [param] (e.g. `"actionId"`). Blank = no containment check.
     * Requires a non-[MissionChildResource.NONE] [childResource].
     */
    val child: String = "",
    /** Which child resource [child] identifies, selecting the [ChildAccessGuard] that resolves its owner. */
    val childResource: MissionChildResource = MissionChildResource.NONE,
)
