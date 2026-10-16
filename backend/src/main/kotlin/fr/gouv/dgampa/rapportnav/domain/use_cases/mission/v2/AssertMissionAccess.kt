package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry.GetInquiryById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetEnvMissionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserFromToken
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserServiceScope
import fr.gouv.dgampa.rapportnav.domain.utils.isValidUUID
import java.util.*

/**
 * Enforces access segregation (cloisonnement) on a mission (or mission-scoped resource): the
 * authenticated user may only access data that belongs to their service — or a linked service
 * (see [GetUserServiceScope]).
 *
 * Mirrors the filtering already applied by [GetMissions] on the list endpoint:
 * - nav missions (local, UUID id, RAPPORT_NAV) are owned by a `serviceId`
 * - env missions (MonitorEnv/Fish, Int external id) are owned by `controlUnits`
 * - inquiries (UUID id) are owned by a `serviceId`, like nav missions
 *
 * [ROLE_ADMIN][RoleTypeEnum.ADMIN] users bypass the check. Call this at the start of any by-id /
 * by-owner handler before returning or mutating mission-scoped data.
 */
@UseCase
class AssertMissionAccess(
    private val getUserFromToken: GetUserFromToken,
    private val getNavMissionById: GetNavMissionById,
    private val getEnvMissionById: GetEnvMissionById,
    private val getUserServiceScope: GetUserServiceScope,
    private val getInquiryById: GetInquiryById,
) {
    /**
     * @param missionId the mission (or action owner) id as received on the route: a UUID for nav
     * missions, an integer string for env missions.
     * @throws BackendUsageException with [BackendUsageErrorCode.USER_NOT_ALLOWED_TO_PERFORM_EXCEPTION]
     * when the user's service does not own the mission, or [BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION]
     * when the mission does not exist.
     */
    fun execute(missionId: String) {
        val user = getUserFromToken.execute() ?: throw notAllowed("no authenticated user")

        if (user.roles.contains(RoleTypeEnum.ADMIN)) return

        val scope = getUserServiceScope.execute()

        if (isValidUUID(missionId)) {
            val mission = getNavMissionById.execute(UUID.fromString(missionId))
                ?: throw notFound(missionId)

            // Env-mirror nav rows carry an external id; they are owned through control units, not serviceId.
            val externalId = mission.externalId?.toIntOrNull()
            if (externalId != null) {
                assertEnvAccess(externalId, scope.controlUnitIds)
            } else {
                assertServiceMatch(mission.serviceId, scope.serviceIds, "nav mission $missionId")
            }
        } else {
            val externalId = missionId.toIntOrNull()
                ?: throw BackendUsageException(
                    code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                    message = "AssertMissionAccess: missionId must be a UUID or an integer, got '$missionId'"
                )
            assertEnvAccess(externalId, scope.controlUnitIds)
        }
    }

    /**
     * Same cloisonnement as [execute] but for an inquiry, which is owned by a `serviceId` exactly like a
     * nav mission (see [GetInquiryByServiceIdBetweenDates], which filters the list endpoint on `serviceId`).
     *
     * @param inquiryId the inquiry UUID as received on the route.
     */
    fun executeForInquiry(inquiryId: String) {
        val user = getUserFromToken.execute() ?: throw notAllowed("no authenticated user")

        if (user.roles.contains(RoleTypeEnum.ADMIN)) return

        if (!isValidUUID(inquiryId)) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "AssertMissionAccess: inquiryId must be a UUID, got '$inquiryId'"
            )
        }
        val inquiry = getInquiryById.execute(UUID.fromString(inquiryId)) ?: throw notFound(inquiryId)
        assertServiceMatch(inquiry.serviceId, getUserServiceScope.execute().serviceIds, "inquiry $inquiryId")
    }

    /**
     * Fail closed: a null owner service, or one outside the user's scope (own + linked services),
     * is denied.
     */
    private fun assertServiceMatch(ownerServiceId: Int?, allowedServiceIds: Set<Int>, label: String) {
        if (ownerServiceId == null || ownerServiceId !in allowedServiceIds) {
            throw notAllowed("$label does not belong to the user's service")
        }
    }

    private fun assertEnvAccess(externalId: Int, allowedControlUnits: Set<Int>) {
        val mission = getEnvMissionById.execute(externalId) ?: throw notFound(externalId.toString())
        val missionControlUnits = mission.controlUnits.map { it.id }
        if (missionControlUnits.none { it in allowedControlUnits }) {
            throw notAllowed("env mission $externalId is not in the user's control units")
        }
    }

    private fun notAllowed(reason: String) = BackendUsageException(
        code = BackendUsageErrorCode.USER_NOT_ALLOWED_TO_PERFORM_EXCEPTION,
        message = "AssertMissionAccess: $reason"
    )

    private fun notFound(missionId: String) = BackendUsageException(
        code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
        message = "AssertMissionAccess: mission not found for id=$missionId"
    )
}
