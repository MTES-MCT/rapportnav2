package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity
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

}
