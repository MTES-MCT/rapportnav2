package fr.gouv.gmampa.rapportnav.mocks.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.CrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.GeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity

object MissionGeneralInfoEntity2Mock {
    fun create(
        data: GeneralInfoEntity? = null,
        crew: List<CrewEntity>? = null,
        services: List<ServiceEntity>? = null
    ) = MissionGeneralInfoEntity(
        data = (data ?: GeneralInfoEntity(
            service = ServiceEntity(
                id = 1,
                name = "name",
                serviceType = ServiceTypeEnum.ULAM
            ),
            isResourcesNotUsed = true
        )),
        crew = crew ?: listOf(
            CrewEntity(
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
