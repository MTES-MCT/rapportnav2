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
    fun execute(generalInfo2: MissionGeneralInfo2, serviceId: Int): MissionNavEntity? {
        val entity = MissionNavEntity(
            id = UUID.randomUUID(),
            serviceId = serviceId,
            startDateTimeUtc = generalInfo2.startDateTimeUtc!!,
            endDateTimeUtc = generalInfo2.endDateTimeUtc,
            isDeleted = false
        )
        val model = repository.save(entity.toMissionModel())
        return MissionNavEntity.fromMissionModel(model)
    }
}
