package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.InterMinisterialServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.PatchEnvMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.AddOrUpdateMissionCrew
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetControlUnitsForUser
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.MissionEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import org.slf4j.LoggerFactory

@UseCase
class CreateOrUpdateGeneralInfo(
    private val repository: IMissionGeneralInfoRepository,
    private val addOrUpdateInterMinisterialService: AddOrUpdateInterMinisterialService,
    private val addOrUpdateMissionCrew: AddOrUpdateMissionCrew,
    private val patchEnvMission: PatchEnvMission
) {

    private val logger = LoggerFactory.getLogger(CreateOrUpdateGeneralInfo::class.java)

    fun execute(missionId: Int, generalInfo: MissionGeneralInfo2): MissionGeneralInfoEntity2? {

        try {
            val entity = generalInfo.toMissionGeneralInfoEntity(missionId)
            val generalInfoModel = repository.save(entity)
            var interMinisterialServices = listOf<InterMinisterialServiceEntity>()

            if (entity.interMinisterialServices?.isNotEmpty() == true) {
                interMinisterialServices = addOrUpdateInterMinisterialService.execute(entity)
            }

            generalInfo.crew?.forEach { crew ->
                addOrUpdateMissionCrew.addOrUpdateMissionCrew(crew.toMissionCrewEntity())
            }

            patchEnvMission.execute(
                MissionEnvInput(
                    startDateTimeUtc = generalInfo.startDateTimeUtc,
                    endDateTimeUtc = generalInfo.endDateTimeUtc,
                    missionId = generalInfo.missionId!!,
                    observationsByUnit = generalInfo.observations
                )
            )

            val generalInfoEntity = generalInfoModel.toMissionGeneralInfoEntity(interMinisterialServices)

            return MissionGeneralInfoEntity2(
                data = generalInfoEntity
            )
        } catch (e: Exception) {
            logger.error("Error while updating general info mission id ${generalInfo.missionId} : ${e.message}")
            return null
        }
    }
}
