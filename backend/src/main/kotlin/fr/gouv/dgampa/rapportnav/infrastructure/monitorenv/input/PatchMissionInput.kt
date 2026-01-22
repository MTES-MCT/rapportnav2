package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import java.time.Instant

@JsonIgnoreProperties(ignoreUnknown = true)
data class PatchMissionInput(
    @param:JsonProperty("missionTypes") val missionTypes: List<MissionTypeEnum>? = null,
    @param:JsonProperty("controlUnits") var controlUnits: List<LegacyControlUnitEntity>? = null,
    @param:JsonProperty("startDateTimeUtc") val startDateTimeUtc: Instant? = null,
    @param:JsonProperty("endDateTimeUtc") val endDateTimeUtc: Instant? = null,
    @param:JsonProperty("isUnderJdp") val isUnderJdp: Boolean? = null,
    @param:JsonProperty("observationsByUnit") val observationsByUnit: String? = null
)
