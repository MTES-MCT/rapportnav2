package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.dgampa.rapportnav.infrastructure.api.admin.input.AdminGeneralInfosUpdateInput

@UseCase
class UpdateGeneralInfoAdmin(
    private val repository: IMissionGeneralInfoRepository,
    private val getServiceById: GetServiceById,

) {
    fun execute(generalInfo: AdminGeneralInfosUpdateInput): MissionGeneralInfoEntity? {
        val current: MissionGeneralInfoEntity? = if (generalInfo.missionId != null) {
            MissionGeneralInfoEntity.fromMissionGeneralInfoModel(repository.findByMissionId(generalInfo.missionId).get())
        }
        else if (generalInfo.missionIdUUID != null) {
            MissionGeneralInfoEntity.fromMissionGeneralInfoModel(repository.findByMissionIdUUID(generalInfo.missionIdUUID).get())
        }
        else null

        if (current == null) return null

        val service = getServiceById.execute(id = generalInfo.service?.id)

        val merged = current.copy(
            missionId = generalInfo.missionId,
            missionIdUUID = generalInfo.missionIdUUID,
            service = service
        )

        val saved = repository.save(merged)
        return MissionGeneralInfoEntity.fromMissionGeneralInfoModel(saved)
    }
}
