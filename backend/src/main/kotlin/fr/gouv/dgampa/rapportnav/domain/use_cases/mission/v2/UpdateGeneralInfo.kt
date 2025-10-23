package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavInputEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input.PatchMissionInput
import org.slf4j.LoggerFactory
import java.util.*

@UseCase
class UpdateGeneralInfo(
    private val repository: IMissionGeneralInfoRepository,
    private val patchMissionEnv: PatchMissionEnv,
    private val processMissionCrew: ProcessMissionCrew,
    private val patchNavMission: PatchNavMission,
    private val getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId,
    private val getMissionCrew: GetMissionCrew
) {
    private val logger = LoggerFactory.getLogger(UpdateGeneralInfo::class.java)

    fun execute(missionId: Int, generalInfo: MissionGeneralInfo2): MissionGeneralInfoEntity2? {
        val fromDb = getMissionGeneralInfoByMissionId.execute(missionId = missionId)
        if (fromDb == null || generalInfo.missionId != missionId) throw Exception("No general infos in database or wrong mission id")

        val model = repository.save(generalInfo.toMissionGeneralInfoEntity(missionId = missionId))
        val crew = processMissionCrew.execute(
            missionId = missionId,
            crew = getMissionCrew.execute(
                missionId = missionId,
                generalInfo = generalInfo,
                newServiceId = generalInfo.serviceId,
                oldServiceId = fromDb.serviceId
            )
        )
        // patch MonitorEnv fields through API
        patchMissionEnv.execute(
            missionId = missionId,
            input = PatchMissionInput(
                isUnderJdp = generalInfo.isUnderJdp,
                startDateTimeUtc = generalInfo.startDateTimeUtc,
                endDateTimeUtc = generalInfo.endDateTimeUtc,
                missionTypes = generalInfo.missionTypes,
                observationsByUnit = generalInfo.observations
            ),
            resources = generalInfo.resources?.map { it.toLegacyControlUnitResourceEntity() }
        )
        return getGeneralInfoEntity(crew = crew, generalInfoModel = model)
    }

    fun execute(missionIdUUID: UUID, generalInfo: MissionGeneralInfo2): MissionGeneralInfoEntity2? {
        val fromDb = getMissionGeneralInfoByMissionId.execute(missionIdUUID = missionIdUUID)
        if (fromDb == null || fromDb.missionIdUUID != missionIdUUID) throw Exception("No general infos in database or wrong mission UUID")

        val model = repository.save(generalInfo.toMissionGeneralInfoEntity(missionIdUUID = missionIdUUID))
        val crew = processMissionCrew.execute(
            missionIdUUID = missionIdUUID,
            crew = getMissionCrew.execute(
                generalInfo = generalInfo,
                missionIdUUID = missionIdUUID,
                newServiceId = generalInfo.serviceId,
                oldServiceId = fromDb.serviceId,
            )
        )
        patchNavMission.execute(
            id = missionIdUUID,
            input = MissionNavInputEntity(
                isDeleted = generalInfo.isDeleted ?: false,
                observationsByUnit = generalInfo.observations,
                endDateTimeUtc = generalInfo.endDateTimeUtc,
                startDateTimeUtc = generalInfo.startDateTimeUtc!!
            )

        )
        return getGeneralInfoEntity(crew = crew, generalInfoModel = model)
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
}
