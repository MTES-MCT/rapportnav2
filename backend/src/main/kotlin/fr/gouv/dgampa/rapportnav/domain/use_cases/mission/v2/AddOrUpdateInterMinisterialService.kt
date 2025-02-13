package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.InterMinisterialServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.IInterMinisterialServiceRepository

@UseCase
class AddOrUpdateInterMinisterialService(
    private val repository: IInterMinisterialServiceRepository
) {

    fun execute(generalInfoEntity: MissionGeneralInfoEntity): List<InterMinisterialServiceEntity> {
        val interMinisterialServices = mutableListOf<InterMinisterialServiceEntity>()
        generalInfoEntity.interMinisterialServices?.forEach { serviceEntity: InterMinisterialServiceEntity ->
           val result = repository.save(serviceEntity, generalInfoEntity)
            interMinisterialServices.add(result.toInterMinisterialServiceEntity())
        }

        return interMinisterialServices
    }
}
