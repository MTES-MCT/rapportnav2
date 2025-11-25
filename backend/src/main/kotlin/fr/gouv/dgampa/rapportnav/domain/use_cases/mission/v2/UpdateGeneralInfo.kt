package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionPassengerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavInputEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.passenger.ProcessMissionPassengers
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.passenger.UpdateMissionPassenger
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.MissionEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel
import org.slf4j.LoggerFactory
import java.util.*

@UseCase
class UpdateGeneralInfo(
    private val repository: IMissionGeneralInfoRepository,
    private val patchMissionEnv: PatchMissionEnv,
    private val processMissionCrew: ProcessMissionCrew,
    private val processMissionPassengers: ProcessMissionPassengers,
    private val patchNavMission: PatchNavMission,
    private val getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId,
    private val getMissionCrew: GetMissionCrew,
) {
    private val logger = LoggerFactory.getLogger(UpdateGeneralInfo::class.java)

    fun execute(missionId: Int, generalInfo: MissionGeneralInfo2): MissionGeneralInfoEntity2? {
        val fromDb = getMissionGeneralInfoByMissionId.execute(missionId = missionId)
        if (fromDb == null || generalInfo.missionId != missionId) throw Exception("No general infos in database or wrong mission id")

        val model = repository.save(generalInfo.toMissionGeneralInfoEntity(missionId = missionId))
        val crew = processMissionCrew.execute(
            missionId = missionId,
            items = getMissionCrew.execute(
                missionId = missionId,
                generalInfo = generalInfo,
                newServiceId = generalInfo.service?.id,
                oldServiceId = fromDb.service?.id,
            )
        )
        val passengers = processMissionPassengers.execute(
            missionId = missionId,
            items = generalInfo.passengers.orEmpty().map {
                it.toMissionPassengerEntity(missionId = missionId)
            }
        )

        patchMissionEnv.execute(
            input = MissionEnvInput(
                missionId = missionId,
                isUnderJdp = generalInfo.isUnderJdp,
                startDateTimeUtc = generalInfo.startDateTimeUtc,
                endDateTimeUtc = generalInfo.endDateTimeUtc,
                missionTypes = generalInfo.missionTypes,
                observationsByUnit = generalInfo.observations,
                resources = generalInfo.resources?.map { it.toLegacyControlUnitResourceEntity() }
            )
        )
        return getGeneralInfoEntity(
            crew = crew,
            passengers = passengers,
            generalInfoModel = model
        )
    }

    fun execute(missionIdUUID: UUID, generalInfo: MissionGeneralInfo2): MissionGeneralInfoEntity2? {
        val fromDb = getMissionGeneralInfoByMissionId.execute(missionIdUUID = missionIdUUID)
        if (fromDb == null || fromDb.missionIdUUID != missionIdUUID) throw Exception("No general infos in database or wrong mission UUID")

        val model = repository.save(generalInfo.toMissionGeneralInfoEntity(missionIdUUID = missionIdUUID))
        val crew = processMissionCrew.execute(
            missionId = missionIdUUID,
            items = getMissionCrew.execute(
                generalInfo = generalInfo,
                missionIdUUID = missionIdUUID,
                newServiceId = generalInfo.service?.id,
                oldServiceId = fromDb.service?.id,
            )
        )
        val passengers = processMissionPassengers.execute(
            missionId = missionIdUUID,
            items = generalInfo.passengers.orEmpty().map {
                it.toMissionPassengerEntity(missionIdUUID = missionIdUUID)
            }
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

        return getGeneralInfoEntity(
            crew = crew,
            passengers = passengers,
            generalInfoModel = model
        )
    }

    private fun getGeneralInfoEntity(
        crew: List<MissionCrewEntity>,
        passengers: List<MissionPassengerEntity>,
        generalInfoModel: MissionGeneralInfoModel
    ): MissionGeneralInfoEntity2 {
        return MissionGeneralInfoEntity2(
            crew = crew,
            passengers = passengers,
            data = MissionGeneralInfoEntity.fromMissionGeneralInfoModel(generalInfoModel)
        )
    }
}
