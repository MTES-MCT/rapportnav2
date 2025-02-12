package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.InterMinisterialServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2

@UseCase
class CreateOrUpdateGeneralInfo(
    private val repository: IMissionGeneralInfoRepository,
    private val addOrUpdateInterMinisterialService: AddOrUpdateInterMinisterialService
) {

    fun execute(generalInfo2: MissionGeneralInfo2): MissionGeneralInfoEntity2 {

        val entity = generalInfo2.toMissionGeneralInfoEntity()
        val generalInfoModel = repository.save(entity)
        var interMinisterialServices = listOf<InterMinisterialServiceEntity>()

        if (entity.interMinisterialServices?.isNotEmpty() == true) {
            interMinisterialServices = addOrUpdateInterMinisterialService.execute(entity)
        }

        val generalInfoEntity = generalInfoModel.toMissionGeneralInfoEntity(interMinisterialServices)

        return MissionGeneralInfoEntity2(
            data = generalInfoEntity
        )

    }
}
