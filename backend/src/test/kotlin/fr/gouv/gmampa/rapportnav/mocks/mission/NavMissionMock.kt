package fr.gouv.gmampa.rapportnav.mocks.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.NavMissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity

object NavMissionMock {
    fun create(
        id: Int = 1,
        actions: List<NavActionEntity> = emptyList(),
        generalInfo: MissionGeneralInfoEntity? = null,
        crew: List<MissionCrewEntity>? = null,
        services: List<ServiceEntity>? = null
    ): NavMissionEntity {
        return NavMissionEntity(
            id = id,
            generalInfo = generalInfo,
            crew = crew,
            actions = actions,
            services = services
        )
    }

}
