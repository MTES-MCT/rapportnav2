package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.ActionCompletionEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action.FormattedEnvActionControlPlan
import org.locationtech.jts.geom.Geometry
import java.util.*

interface BaseMissionEnvAction {
    val id: UUID
    val missionId: Int
    val envActionType: ActionTypeEnum
    val completedBy: String?
    val completion: ActionCompletionEnum?
    val controlPlans: List<EnvActionControlPlanEntity>?
    val geom: Geometry?
    val facade: String?
    val department: String?
    val openBy: String?
    val observations: String?
    val observationsByUnit: String?
    val actionNumberOfControls: Int?
    val actionTargetType: ActionTargetTypeEnum?
    val vehicleType: VehicleTypeEnum?
    val envInfractions: List<InfractionEntity>?
    val navInfractions: List<fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity>?
    val coverMissionZone: Boolean?
    val formattedControlPlans: List<FormattedEnvActionControlPlan>?
    val availableControlTypesForInfraction: List<ControlType>?
}
