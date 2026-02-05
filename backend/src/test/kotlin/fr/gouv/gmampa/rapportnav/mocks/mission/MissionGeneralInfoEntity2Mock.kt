package fr.gouv.gmampa.rapportnav.mocks.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2

object MissionGeneralInfoEntity2Mock {
    fun create(
        data: MissionGeneralInfoEntity? = null,
        crew: List<MissionCrewEntity>? = null,
        services: List<ServiceEntity>? = null
    ) = MissionGeneralInfoEntity2(
        data = (data ?: MissionGeneralInfoEntity(
            service = ServiceEntity(
                id = 1,
                name = "name",
                serviceType = ServiceTypeEnum.ULAM
            ),
            isResourcesNotUsed = true
        )),
        crew = crew ?: listOf(
            MissionCrewEntity(
                agent = AgentEntity(
                    firstName = "firstName", lastName = "lastName", service = ServiceEntity(
                        id = 1,
                        name = "name",
                        serviceType = ServiceTypeEnum.ULAM
                    )
                )
            )
        ),
        services = services
    )
}
