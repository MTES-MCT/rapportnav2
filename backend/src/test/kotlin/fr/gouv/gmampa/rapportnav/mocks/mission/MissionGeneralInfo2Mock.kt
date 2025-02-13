package fr.gouv.gmampa.rapportnav.mocks.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReinforcementTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReportTypeEnum
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import java.time.Instant

object MissionGeneralInfo2Mock {
    fun create(
        startDateTimeUtc: Instant = Instant.now(),
        endDateTimeUtc: Instant? = null,
        missionReportType: MissionReportTypeEnum = MissionReportTypeEnum.FIELD_REPORT,
        missionTypes: List<MissionTypeEnum> = listOf(MissionTypeEnum.SEA),
        reinforcementType: MissionReinforcementTypeEnum? = null,
        nbHourAtSea: Int? = null
    ): MissionGeneralInfo2 {
        return MissionGeneralInfo2(
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            missionReportType = missionReportType,
            missionTypes = missionTypes,
            reinforcementType = reinforcementType,
            nbHourAtSea = nbHourAtSea,
            missionId = 0
        )
    }
}
