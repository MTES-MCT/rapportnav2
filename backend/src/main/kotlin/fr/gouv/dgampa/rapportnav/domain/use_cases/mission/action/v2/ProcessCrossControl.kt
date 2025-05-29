package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CrossControlEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetVessels
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.CrossControlModel

@UseCase
class ProcessCrossControl(
    private val getVessels: GetVessels,
    private val missionActionRepository: INavMissionActionRepository
) {
    fun execute(model: CrossControlModel): CrossControlEntity {
        val entity = CrossControlEntity.fromCrossControlModel(model)
        val vessel = getVessels.execute().find {vessel -> vessel.vesselId == entity.vesselId   }
        entity.withVessel(vessel)
        return entity
    }
}

