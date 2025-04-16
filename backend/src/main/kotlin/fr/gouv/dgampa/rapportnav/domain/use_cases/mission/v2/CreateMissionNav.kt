package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import java.util.*

@UseCase
class CreateMissionNav(
    private val repository: IMissionNavRepository
) {
    fun execute(generalInfo2: MissionGeneralInfo2, controlUnitIds: List<Int>): MissionNavEntity? {

        val navMission = MissionNavEntity(
            controlUnits = controlUnitIds,
            startDateTimeUtc = generalInfo2.startDateTimeUtc!!,
            endDateTimeUtc = generalInfo2.endDateTimeUtc,
            isDeleted = false,
            controlUnitIdOwner = controlUnitIds.first(),
            navId = UUID.randomUUID()
        )

        val model = repository.save(navMission)

        return MissionNavEntity.fromMissionModel(model)
    }
}
