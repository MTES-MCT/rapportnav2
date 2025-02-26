package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.AddOrUpdateMissionCrew
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.MissionEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import org.slf4j.LoggerFactory

@UseCase
class CreateOrUpdateGeneralInfo(
    private val repository: IMissionGeneralInfoRepository,
    private val addOrUpdateMissionCrew: AddOrUpdateMissionCrew,
    private val updateMissionEnv: UpdateMissionEnv
) {
    private val logger = LoggerFactory.getLogger(CreateOrUpdateGeneralInfo::class.java)

    fun execute(missionId: Int, generalInfo: MissionGeneralInfo2): MissionGeneralInfoEntity2? {

        try {
            val entity = generalInfo.toMissionGeneralInfoEntity(missionId)
            val generalInfoModel = repository.save(entity)
            generalInfo.crew?.map { addOrUpdateMissionCrew.addOrUpdateMissionCrew(it.toMissionCrewEntity())}

            updateMissionEnv.execute(
                input = MissionEnvInput(
                    missionId = missionId,
                    startDateTimeUtc = generalInfo.startDateTimeUtc,
                    endDateTimeUtc = generalInfo.endDateTimeUtc,
                    missionTypes = generalInfo.missionTypes,
                    observationsByUnit = generalInfo.observations
                )
            )

            val generalInfoEntity = MissionGeneralInfoEntity.fromMissionGeneralInfoModel(generalInfoModel)
            return MissionGeneralInfoEntity2(data = generalInfoEntity)
        } catch (e: Exception) {
            logger.error("Error while updating general info mission id ${generalInfo.missionId} : ${e.message}")
            return null
        }
    }
}
