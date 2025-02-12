package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.InterMinisterialServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.PatchEnvMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.AddOrUpdateMissionCrew
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.MissionEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2

@UseCase
class CreateOrUpdateGeneralInfo(
    private val repository: IMissionGeneralInfoRepository,
    private val addOrUpdateInterMinisterialService: AddOrUpdateInterMinisterialService,
    private val addOrUpdateMissionCrew: AddOrUpdateMissionCrew,
    private val patchEnvMission: PatchEnvMission
) {

    fun execute(generalInfo2: MissionGeneralInfo2): MissionGeneralInfoEntity2 {

        val entity = generalInfo2.toMissionGeneralInfoEntity()
        val generalInfoModel = repository.save(entity)
        var interMinisterialServices = listOf<InterMinisterialServiceEntity>()

        if (entity.interMinisterialServices?.isNotEmpty() == true) {
            interMinisterialServices = addOrUpdateInterMinisterialService.execute(entity)
        }

        generalInfo2.crew?.forEach { crew ->
            addOrUpdateMissionCrew.addOrUpdateMissionCrew(crew.toMissionCrewEntity())
        }

        patchEnvMission.execute(MissionEnvInput(
            startDateTimeUtc = generalInfo2.startDateTimeUtc,
            endDateTimeUtc = generalInfo2.endDateTimeUtc,
            missionId = generalInfo2.missionId,
            observationsByUnit = generalInfo2.observations
        ))

        val generalInfoEntity = generalInfoModel.toMissionGeneralInfoEntity(interMinisterialServices)

        return MissionGeneralInfoEntity2(
            data = generalInfoEntity
        )

    }
}
