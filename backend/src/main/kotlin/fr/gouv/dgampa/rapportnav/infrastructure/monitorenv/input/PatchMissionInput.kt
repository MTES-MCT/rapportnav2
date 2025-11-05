package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import java.time.Instant

@JsonIgnoreProperties(ignoreUnknown = true)
data class PatchMissionInput @JsonCreator(mode = JsonCreator.Mode.PROPERTIES) constructor(
    @JsonProperty("missionTypes") val missionTypes: List<MissionTypeEnum>? = null,
    @JsonProperty("controlUnits") var controlUnits: List<LegacyControlUnitEntity>? = listOf(),
    @JsonProperty("startDateTimeUtc") val startDateTimeUtc: Instant? = null,
    @JsonProperty("endDateTimeUtc") val endDateTimeUtc: Instant? = null,
    @JsonProperty("isUnderJdp") val isUnderJdp: Boolean? = null,
    @JsonProperty("observationsByUnit") val observationsByUnit: String? = null
)
