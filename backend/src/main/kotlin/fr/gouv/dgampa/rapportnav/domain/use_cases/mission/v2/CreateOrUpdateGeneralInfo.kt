package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.PatchEnvMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.AddOrUpdateMissionCrew
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.MissionEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.MissionCrew
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import org.slf4j.LoggerFactory

@UseCase
class CreateOrUpdateGeneralInfo(
    private val repository: IMissionGeneralInfoRepository,
    private val updateMissionService2: UpdateMissionService2,
    private val getGeneralInfo2: GetGeneralInfo2,
    private val updateMissionEnv: UpdateMissionEnv,
    private val processMissionCrew: ProcessMissionCrew
) {
    private val logger = LoggerFactory.getLogger(CreateOrUpdateGeneralInfo::class.java)

    fun execute(missionId: Int, generalInfo: MissionGeneralInfo2): MissionGeneralInfoEntity2? {

        try {
            val entityToSave = generalInfo.toMissionGeneralInfoEntity(missionId)
            val entityAlreadySaved = getGeneralInfo2.execute(missionId)

            // update general info table
            val generalInfoModel = repository.save(entityToSave)

            // regenerate crew if service has changed
            if (entityToSave.serviceId != null && entityToSave.serviceId != entityAlreadySaved.data?.serviceId) {
                updateMissionService2.execute(missionId=missionId, serviceId = entityToSave.serviceId!!)
            }
            else {
                // update crew table
                // TODO this could be improved and only update what's needed when needed, instead of running every time
                processMissionCrew.execute(
                    missionId=missionId,
                    crew = generalInfo.crew.orEmpty()
                )

                // patch MonitorEnv fields through API
                updateMissionEnv.execute(
                    input = MissionEnvInput(
                        missionId = missionId,
                        startDateTimeUtc = generalInfo.startDateTimeUtc,
                        endDateTimeUtc = generalInfo.endDateTimeUtc,
                        missionTypes = generalInfo.missionTypes,
                        observationsByUnit = generalInfo.observations,
                        resources = generalInfo.resources?.map { it.toLegacyControlUnitResourceEntity() },
                    )
                )
            }

            val generalInfoEntity = MissionGeneralInfoEntity.fromMissionGeneralInfoModel(generalInfoModel)
            return MissionGeneralInfoEntity2(data = generalInfoEntity)
        } catch (e: Exception) {
            logger.error("Error while updating general info mission id ${generalInfo.missionId} : ${e.message}")
            return null
        }
    }
}
