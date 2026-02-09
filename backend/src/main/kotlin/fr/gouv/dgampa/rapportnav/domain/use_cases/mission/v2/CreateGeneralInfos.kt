package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.GeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo
import java.util.*

@UseCase
class CreateGeneralInfos(
    private val generalInfosRepository: IGeneralInfoRepository
) {
    fun execute(
        missionId: Int? = null,
        missionIdUUID: UUID? = null,
        generalInfo2: MissionGeneralInfo,
        service: ServiceEntity? = null
    ): MissionGeneralInfoEntity {
        val generalInfoModel = generalInfosRepository.save(
            generalInfo2.toGeneralInfoEntity(
                missionId = missionId,
                missionIdUUID = missionIdUUID,
                inputService = service
            ).toGeneralInfoModel()
        )
        return MissionGeneralInfoEntity(
            data = GeneralInfoEntity(
                id = generalInfoModel.id,
                missionId = generalInfoModel.missionId,
                missionIdUUID = generalInfoModel.missionIdUUID,
                missionReportType = generalInfo2.missionReportType,
                service = service
            )
        )
    }
}
