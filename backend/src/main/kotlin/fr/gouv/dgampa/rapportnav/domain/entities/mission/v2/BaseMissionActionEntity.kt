package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import java.time.Instant

interface BaseMissionActionEntity {
    val missionId: Int
    val actionType: ActionType
    val isCompleteForStats: Boolean?
    val source: MissionSourceEnum
    var startDateTimeUtc: Instant?
    var endDateTimeUtc: Instant?
    val completenessForStats: CompletenessForStatsEntity?
    val sourcesOfMissingDataForStats: List<MissionSourceEnum>?
    var controlSecurity: ControlSecurityEntity?
    var controlGensDeMer: ControlGensDeMerEntity?
    var controlNavigation: ControlNavigationEntity?
    var controlAdministrative: ControlAdministrativeEntity?
    var summaryTags: List<String>?
    var status: ActionStatusType?
    var controlsToComplete: List<ControlType>?
    val isAdministrativeControl: Boolean?
    val isComplianceWithWaterRegulationsControl: Boolean?
    val isSafetyEquipmentAndStandardsComplianceControl: Boolean?
    val isSeafarersControl: Boolean?

}
