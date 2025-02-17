package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.InterMinisterialServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.CreateOrUpdateEnvMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.PatchEnvMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.AddOrUpdateMissionCrew
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetControlUnitsForUser
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.MissionEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2

@UseCase
class CreateOrUpdateGeneralInfo(
    private val repository: IMissionGeneralInfoRepository,
    private val addOrUpdateInterMinisterialService: AddOrUpdateInterMinisterialService,
    private val addOrUpdateMissionCrew: AddOrUpdateMissionCrew,
    private val patchEnvMission: PatchEnvMission,
    private val createOrUpdateEnvMission: CreateOrUpdateEnvMission,
    private val getControlUnitsForUser: GetControlUnitsForUser
) {

    fun execute(generalInfo2: MissionGeneralInfo2, missionId: Int): MissionGeneralInfoEntity2 {

        val entity = generalInfo2.toMissionGeneralInfoEntity(missionId)
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

        val controlUnits = getControlUnitsForUser.execute()

        createOrUpdateEnvMission.execute(generalInfo2, controlUnits) // TODO: to be replaced by patchEnvMission (for missionTypes)

        val generalInfoEntity = generalInfoModel.toMissionGeneralInfoEntity(interMinisterialServices)

        return MissionGeneralInfoEntity2(
            data = generalInfoEntity
        )

    }
}
