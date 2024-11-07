package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTargetTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VehicleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action.FormattedEnvActionControlPlan
import java.time.Instant

data class MissionActionTimeLineOutput(
    val id: String,
    val type: ActionType,
    val missionId: Int,
    val source: MissionSourceEnum,
    var isCompleteForStats: Boolean? = null,
    //val status
    val summaryTags: List<String>? = null,
    val completenessForStats: CompletenessForStatsEntity? = null,
    val startDateTimeUtc: Instant? = null,
    val endDateTimeUtc: Instant? = null,
    var vesselId: String? = null,
    val vesselName: String? = null,
    val actionNumberOfControls: Int? = null,
    val actionTargetType: ActionTargetTypeEnum? = null,
    val vehicleType: VehicleTypeEnum? = null,
    val controlsToComplete: List<ControlType>? = listOf(),
    val formattedControlPlans: FormattedEnvActionControlPlan? = null,
    val observations: String? = null,
    val isPersonRescue: Boolean? = null,
    val isVesselRescue: Boolean? = null,
    val reason: ActionStatusReason? = null,
    val controlMethod: ControlMethod? = null,
    val vesselIdentifier: String? = null,
    val vesselType: VesselTypeEnum? = null,
    val vesselSize: VesselSizeEnum? = null
) {}
