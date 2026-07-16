package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionPassengerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavInputEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.validation.EntityValidityValidator
import fr.gouv.dgampa.rapportnav.domain.validation.ValidateThrowsBeforeSave
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.passenger.ProcessMissionPassengers
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.MissionEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel
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
    private val entityValidityValidator: EntityValidityValidator
) {
    fun execute(
        missionId: UUID,
        generalInfo: MissionGeneralInfo2,
        controlUnitId: Int? = null
    ): MissionGeneralInfoEntity2 {

        val fromDb = getMissionGeneralInfoByMissionId.execute(missionId = missionId) ?: throw BackendUsageException(
            code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
            message = "UpdateGeneralInfo: general info not found for missionId=$missionId"
        )

        // Validate before save
        val entity = generalInfo.toMissionGeneralInfoEntity(missionId = missionId)
        entityValidityValidator.validateAndThrow(entity, ValidateThrowsBeforeSave::class.java)

        val model = repository.save(entity)
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

        val navMission = patchNavMission.execute(
            id = missionId,
            input = MissionNavInputEntity(
                isDeleted = generalInfo.isDeleted ?: false,
                observationsByUnit = generalInfo.observations,
                endDateTimeUtc = generalInfo.endDateTimeUtc,
                startDateTimeUtc = generalInfo.startDateTimeUtc!!
            )
        )

        val externalId = navMission?.externalId?.toIntOrNull()
        if (externalId != null) {
            patchMissionEnv.execute(
                input = MissionEnvInput(
                    missionId = externalId,
                    controlUnitId = controlUnitId,
                    isUnderJdp = generalInfo.isUnderJdp,
                    startDateTimeUtc = generalInfo.startDateTimeUtc,
                    endDateTimeUtc = generalInfo.endDateTimeUtc,
                    missionTypes = generalInfo.missionTypes,
                    observationsByUnit = generalInfo.observations,
                    resources = generalInfo.resources?.map { it.toLegacyControlUnitResourceEntity() }
                )
            )
        }

        return MissionGeneralInfoEntity2(
            crew = crew,
            passengers = passengers,
            data = MissionGeneralInfoEntity.fromMissionGeneralInfoModel(model = model)
        )

    }


}
