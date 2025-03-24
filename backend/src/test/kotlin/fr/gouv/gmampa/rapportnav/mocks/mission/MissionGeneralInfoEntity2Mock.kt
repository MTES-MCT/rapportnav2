package fr.gouv.gmampa.rapportnav.mocks.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2

object MissionGeneralInfoEntity2Mock {
    fun create(
        data: MissionGeneralInfoEntity? = null,
        crew: List<MissionCrewEntity>? = null,
        services: List<ServiceEntity>? = null
    ) = MissionGeneralInfoEntity2(
        data = data,
        crew = crew,
        services = services
    )
}
