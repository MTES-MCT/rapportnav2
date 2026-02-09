package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.CrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.PassengerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.GeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavInputEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.passenger.ProcessMissionPassengers
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.MissionEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.GeneralInfoModel
import java.util.*

@UseCase
class UpdateGeneralInfo(
    private val repository: IGeneralInfoRepository,
    private val patchMissionEnv: PatchMissionEnv,
    private val processMissionCrew: ProcessMissionCrew,
    private val processMissionPassengers: ProcessMissionPassengers,
    private val patchNavMission: PatchNavMission,
    private val getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId,
    private val getCrew: GetCrew,
) {

    fun execute(missionId: Int, generalInfo: MissionGeneralInfo): MissionGeneralInfoEntity {
        val fromDb = getMissionGeneralInfoByMissionId.execute(missionId = missionId)
        if (fromDb == null || generalInfo.missionId != missionId) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "UpdateGeneralInfo: general info not found or missionId mismatch for missionId=$missionId"
            )
        }

        val model = repository.save(generalInfo.toGeneralInfoEntity(missionId = missionId).toGeneralInfoModel())
        val crew = processMissionCrew.execute(
            missionId = missionId,
            items = getCrew.execute(
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

    fun execute(missionIdUUID: UUID, generalInfo: MissionGeneralInfo): MissionGeneralInfoEntity {
        val fromDb = getMissionGeneralInfoByMissionId.execute(missionIdUUID = missionIdUUID)
        if (fromDb == null || fromDb.missionIdUUID != missionIdUUID) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "UpdateGeneralInfo: general info not found or missionIdUUID mismatch for missionIdUUID=$missionIdUUID"
            )
        }

        val model = repository.save(generalInfo.toGeneralInfoEntity(missionIdUUID = missionIdUUID).toGeneralInfoModel())
        val crew = processMissionCrew.execute(
            missionId = missionIdUUID,
            items = getCrew.execute(
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
        crew: List<CrewEntity>,
        passengers: List<PassengerEntity>,
        generalInfoModel: GeneralInfoModel
    ): MissionGeneralInfoEntity {
        return MissionGeneralInfoEntity(
            crew = crew,
            passengers = passengers,
            data = GeneralInfoEntity.fromGeneralInfoModel(generalInfoModel)
        )
    }
}
