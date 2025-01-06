package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTargetTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VehicleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import org.locationtech.jts.geom.Geometry
import java.time.Instant
import java.util.*


class MissionEnvActionData(
    override val startDateTimeUtc: Instant? = null,
    override val endDateTimeUtc: Instant? = null,
    override val completedBy: String? = null,
    override val geom: Geometry? = null,
    override val facade: String? = null,
    override val department: String? = null,
    override val isAdministrativeControl: Boolean? = null,
    override val isComplianceWithWaterRegulationsControl: Boolean? = null,
    override val isSafetyEquipmentAndStandardsComplianceControl: Boolean? = null,
    override val isSeafarersControl: Boolean? = null,
    override val openBy: String? = null,
    override val observations: String? = null,
    override val observationsByUnit: String? = null,
    override val actionNumberOfControls: Int? = null,
    override val actionTargetType: ActionTargetTypeEnum? = null,
    override val vehicleType: VehicleTypeEnum? = null,
    override val infractions: List<InfractionByTarget>? = listOf(),
    override val coverMissionZone: Boolean? = null,
    override val controlSecurity: ControlSecurity? = null,
    override val controlGensDeMer: ControlGensDeMer? = null,
    override val controlNavigation: ControlNavigation? = null,
    override val controlAdministrative: ControlAdministrative? = null,
    override val formattedControlPlans: Any? = null,
    override val availableControlTypesForInfraction: List<ControlType>? = null
) : MissionActionData(
    startDateTimeUtc = startDateTimeUtc,
    endDateTimeUtc = endDateTimeUtc,
    controlSecurity = controlSecurity,
    controlGensDeMer = controlGensDeMer,
    controlNavigation = controlNavigation,
    controlAdministrative = controlAdministrative
),
    BaseMissionEnvActionData {

    fun getInfractions(missionId: Int, actionId: String): List<Infraction>? {
        val infractions = infractions?.flatMap { it.infractions }
        //?.filter { it.controlId != null || it.controlType != null }
        infractions?.forEach { it.setMissionIdAndActionId(missionId, actionId) }
        return infractions
    }

    companion object {
        fun toMissionEnvActionEntity(input: MissionAction): MissionEnvActionEntity {
            val data = input.data
            val action = MissionEnvActionEntity(
                id = UUID.fromString(input.id),
                missionId = input.missionId,
                endDateTimeUtc = data?.endDateTimeUtc,
                startDateTimeUtc = data?.startDateTimeUtc,
                observationsByUnit = data?.observations,
                envActionType = ActionTypeEnum.valueOf(input.actionType.toString())
            )
            return action
        }
    }
    }











