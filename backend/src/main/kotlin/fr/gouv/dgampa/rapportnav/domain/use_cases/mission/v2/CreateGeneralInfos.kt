package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import java.util.*

@UseCase
class CreateGeneralInfos(
    private val generalInfosRepository: IMissionGeneralInfoRepository
) {
    fun execute(
        missionId: Int? = null,
        missionIdUUID: UUID? = null,
        generalInfo2: MissionGeneralInfo2
    ): MissionGeneralInfoEntity2 {
        val generalInfoModel = generalInfosRepository.save(
            generalInfo2.toMissionGeneralInfoEntity(
                missionId = missionId,
                missionIdUUID = missionIdUUID
            )
        )
        return MissionGeneralInfoEntity2(
            data = MissionGeneralInfoEntity(
                id = generalInfoModel.id,
                missionId = generalInfoModel.missionId,
                missionIdUUID = generalInfoModel.missionIdUUID,
                missionReportType = generalInfo2.missionReportType
            )
        )
    }
}
