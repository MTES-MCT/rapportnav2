package fr.gouv.dgampa.rapportnav.domain.entities.mission.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.sati.Sati
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.sati.SatiModelMapper.toModel

object SatiEntityMapper {
    fun merge(sati: SatiEntity?, action: MissionAction): SatiEntity? {
        return sati
    }

    fun merge(sati: SatiEntity?, input: Sati): SatiEntity? {
        return sati
    }

    fun isEquals(fromDb: SatiEntity?, entity: SatiEntity?): Boolean {
        if (fromDb == null || entity == null) return false
        return toModel(fromDb) == toModel(entity)
    }
}
