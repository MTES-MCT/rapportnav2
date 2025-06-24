package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavInputEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.MissionEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel
import org.slf4j.LoggerFactory
import java.util.*

@UseCase
class CreateOrUpdateGeneralInfo(
    private val repository: IMissionGeneralInfoRepository,
    private val updateMissionService2: UpdateMissionService2,
    private val getGeneralInfo2: GetGeneralInfo2,
    private val patchMissionEnv: PatchMissionEnv,
    private val processMissionCrew: ProcessMissionCrew,
    private val patchNavMission: PatchNavMission
) {
    private val logger = LoggerFactory.getLogger(CreateOrUpdateGeneralInfo::class.java)

    fun execute(missionId: Int, generalInfo: MissionGeneralInfo2): MissionGeneralInfoEntity2? {

        try {
            val entityToSave = generalInfo.toMissionGeneralInfoEntity(missionId = missionId)
            val entityAlreadySaved = getGeneralInfo2.execute(missionId = missionId)

            // update general info table
            val generalInfoModel = repository.save(entityToSave)

            // regenerate crew if service has changed
            val savedCrew: List<MissionCrewEntity> = if (isServiceHasChanged(entityToSave, entityAlreadySaved)) {
                updateMissionService2.execute(missionId = missionId, serviceId = entityToSave.serviceId!!)
            } else {
                // update crew table
                processMissionCrew.execute(
                    missionId = missionId,
                    crew = generalInfo.crew?.map { it.toMissionCrewEntity() }.orEmpty()
                )
            }

            // patch MonitorEnv fields through API
            patchMissionEnv.execute(
                input = MissionEnvInput(
                    missionId = missionId,
                    startDateTimeUtc = generalInfo.startDateTimeUtc,
                    endDateTimeUtc = generalInfo.endDateTimeUtc,
                    missionTypes = generalInfo.missionTypes,
                    observationsByUnit = generalInfo.observations,
                    resources = generalInfo.resources?.map { it.toLegacyControlUnitResourceEntity() },
                )
            )
            return getGeneralInfoEntity(crew = savedCrew, generalInfoModel = generalInfoModel)
        } catch (e: Exception) {
            logger.error("Error while updating general info mission id ${generalInfo.missionId} : ${e.message}")
            return null
        }
    }

    fun execute(missionIdUUID: UUID, generalInfo: MissionGeneralInfo2): MissionGeneralInfoEntity2? {
        val entityToSave = generalInfo.toMissionGeneralInfoEntity(missionIdUUID = missionIdUUID)
        val entityAlreadySaved = getGeneralInfo2.execute(missionIdUUID = missionIdUUID)
        val generalInfoModel = repository.save(entityToSave)
        val savedCrew: List<MissionCrewEntity> = if (isServiceHasChanged(entityToSave, entityAlreadySaved)) {
            updateMissionService2.execute(missionIdUUID = missionIdUUID, serviceId = entityToSave.serviceId!!)
        } else {
            // update crew table
            processMissionCrew.execute(
                missionIdUUID = missionIdUUID,
                crew = generalInfo.crew?.map { it.toMissionCrewEntity() }.orEmpty()
            )
        }
        patchNavMission.execute(
            id = missionIdUUID,
           input =  MissionNavInputEntity(
                isDeleted = generalInfo.isDeleted?:false,
                observationsByUnit = generalInfo.observations,
                endDateTimeUtc = generalInfo.endDateTimeUtc,
                startDateTimeUtc = generalInfo.startDateTimeUtc!!
            )

        )
        return getGeneralInfoEntity(crew = savedCrew, generalInfoModel = generalInfoModel)
    }

    private fun getGeneralInfoEntity(
        crew: List<MissionCrewEntity>,
        generalInfoModel: MissionGeneralInfoModel
    ): MissionGeneralInfoEntity2 {
        return MissionGeneralInfoEntity2(
            crew = crew,
            data = MissionGeneralInfoEntity.fromMissionGeneralInfoModel(generalInfoModel)
        )
    }

    private fun isServiceHasChanged(
        entityToSave: MissionGeneralInfoEntity,
        entityAlreadySaved: MissionGeneralInfoEntity2
    ) = entityToSave.serviceId != null && entityToSave.serviceId != entityAlreadySaved.data?.serviceId
}
