package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.dgampa.rapportnav.config.JtsGeometryDeserializer
import fr.gouv.dgampa.rapportnav.config.JtsGeometrySerializer
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTargetTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VehicleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.tags.TagEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.themes.ThemeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import org.locationtech.jts.geom.Geometry
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.time.Instant
import java.util.*


class MissionEnvActionData(
    override val startDateTimeUtc: Instant? = null,
    override val endDateTimeUtc: Instant? = null,
    override val completedBy: String? = null,
    @param:JsonProperty("geom")
    @param:JsonDeserialize(using = JtsGeometryDeserializer::class)
    @get:JsonSerialize(using = JtsGeometrySerializer::class)
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
    override val coverMissionZone: Boolean? = null,
    override val formattedControlPlans: Any? = null,
    override val availableControlTypesForInfraction: List<ControlType>? = null,
    override val targets: List<Target>? = null,
    override val tags: List<TagEntity>? = listOf(),
    override var themes: List<ThemeEntity>? = listOf(),
) : MissionActionData(
    startDateTimeUtc = startDateTimeUtc,
    endDateTimeUtc = endDateTimeUtc,
    targets = targets
),
    BaseMissionEnvActionData {
    companion object {
        fun toMissionEnvActionEntity(input: MissionAction): MissionEnvActionEntity {
            val data = input.data as MissionEnvActionData
            val action = MissionEnvActionEntity(
                id = UUID.fromString(input.id),
                missionId = input.missionId,
                endDateTimeUtc = data.endDateTimeUtc,
                startDateTimeUtc = data.startDateTimeUtc,
                observationsByUnit = data.observationsByUnit,
                envActionType = ActionTypeEnum.valueOf(input.actionType.toString()),
                tags = data.tags,
                themes = data.themes.orEmpty()
            )
            return action
        }
    }
    }











