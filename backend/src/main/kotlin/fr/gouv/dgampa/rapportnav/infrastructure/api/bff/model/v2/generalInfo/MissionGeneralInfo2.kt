package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReinforcementTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReportTypeEnum
import java.time.Instant

data class MissionGeneralInfo2(
    val startDateTimeUtc: Instant,
    val endDateTimeUtc: Instant? = null,
    val missionReportType: MissionReportTypeEnum,
    val missionTypes: List<MissionTypeEnum>,
    val reinforcementType: MissionReinforcementTypeEnum? = null,
    val nbHourAtSea: Int? = null
)
