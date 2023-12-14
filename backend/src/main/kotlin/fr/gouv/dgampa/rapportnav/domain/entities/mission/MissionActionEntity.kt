package fr.gouv.dgampa.rapportnav.domain.entities.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavActionEntity
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = MissionActionEntity.EnvAction::class, name = "env"),
    JsonSubTypes.Type(value = MissionActionEntity.FishAction::class, name = "fish"),
    JsonSubTypes.Type(value = MissionActionEntity.NavAction::class, name = "nav")
)
sealed class MissionActionEntity {
    @JsonTypeName("env")
    data class EnvAction(val envAction: ExtendedEnvActionEntity?) : MissionActionEntity()

    @JsonTypeName("fish")
    data class FishAction(val fishAction: ExtendedFishActionEntity) : MissionActionEntity()

    @JsonTypeName("nav")
    data class NavAction(val navAction: NavActionEntity) : MissionActionEntity()
}

