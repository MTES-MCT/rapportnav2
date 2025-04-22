package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.*
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

object NavActionControlMock {

    fun create(
        startDateTimeUtc: Instant = Instant.parse("2022-01-02T12:00:00Z"),
        observations: String? = "observations",
        isCompleteForStats: Boolean? = null,
        controlMethod: ControlMethod? = ControlMethod.SEA,
        vesselIdentifier: String? = null,
        vesselType: VesselTypeEnum? = VesselTypeEnum.FISHING,
        vesselSize: VesselSizeEnum? = null,
        identityControlledPerson: String? = null,
        controlAdministrative: ControlAdministrativeEntity? = null,
        controlGensDeMer: ControlGensDeMerEntity? = null,
        controlNavigation: ControlNavigationEntity? = null,
        controlSecurity: ControlSecurityEntity? = null,
    ): ActionControlEntity {
        return ActionControlEntity(
            id = UUID.randomUUID(),
            missionId = "1",
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = startDateTimeUtc.plus(2, ChronoUnit.HOURS),
            controlMethod = controlMethod,
            observations = observations,
            isCompleteForStats = isCompleteForStats,
            vesselIdentifier = vesselIdentifier,
            vesselType = vesselType,
            vesselSize = vesselSize,
            identityControlledPerson = identityControlledPerson,
            controlAdministrative = controlAdministrative,
            controlGensDeMer = controlGensDeMer,
            controlNavigation = controlNavigation,
            controlSecurity = controlSecurity,
        )
    }
}
