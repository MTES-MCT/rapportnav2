package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.GeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.dgampa.rapportnav.infrastructure.api.admin.input.AdminGeneralInfosUpdateInput

@UseCase
class UpdateGeneralInfoAdmin(
    private val repository: IGeneralInfoRepository,
    private val getServiceById: GetServiceById,

    ) {
    fun execute(generalInfo: AdminGeneralInfosUpdateInput): GeneralInfoEntity? {
        val current: GeneralInfoEntity? = if (generalInfo.missionId != null) {
            GeneralInfoEntity.fromGeneralInfoModel(repository.findByMissionId(generalInfo.missionId).get())
        }
        else if (generalInfo.missionIdUUID != null) {
            GeneralInfoEntity.fromGeneralInfoModel(repository.findByMissionIdUUID(generalInfo.missionIdUUID).get())
        }
        else null

        if (current == null) return null

        val service = getServiceById.execute(id = generalInfo.service?.id)

        val merged = current.copy(
            missionId = generalInfo.missionId,
            missionIdUUID = generalInfo.missionIdUUID,
            service = service
        )

        val saved = repository.save(merged.toGeneralInfoModel())
        return GeneralInfoEntity.fromGeneralInfoModel(saved)
    }
}
