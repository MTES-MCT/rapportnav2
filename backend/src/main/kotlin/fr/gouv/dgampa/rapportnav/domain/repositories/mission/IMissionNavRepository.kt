package fr.gouv.dgampa.rapportnav.domain.repositories.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel

interface IMissionNavRepository {

    fun save(entity: MissionNavEntity): MissionModel
}
