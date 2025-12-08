package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity

data class NavMissionEntity(
    val id: Int,
    val actions: List<NavActionEntity> = emptyList(),
    val generalInfo: MissionGeneralInfoEntity? = null,
    val crew: List<MissionCrewEntity>? = null,
    val services: List<ServiceEntity>? = null
)
