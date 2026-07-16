package fr.gouv.gmampa.rapportnav.mocks.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReinforcementTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReportTypeEnum
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.Service
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import java.time.Instant
import java.util.UUID

object MissionGeneralInfo2Mock {
    fun create(
        missionId: UUID? = UUID.randomUUID(),
        startDateTimeUtc: Instant = Instant.now(),
        endDateTimeUtc: Instant? = null,
        missionReportType: MissionReportTypeEnum = MissionReportTypeEnum.FIELD_REPORT,
        missionTypes: List<MissionTypeEnum> = listOf(MissionTypeEnum.SEA),
        reinforcementType: MissionReinforcementTypeEnum? = null,
        nbHourAtSea: Int? = null,
        service: Service? = null,
    ): MissionGeneralInfo2 {
        return MissionGeneralInfo2(
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            missionReportType = missionReportType,
            missionTypes = missionTypes,
            reinforcementType = reinforcementType,
            nbHourAtSea = nbHourAtSea,
            service = service,
        )
    }
}
