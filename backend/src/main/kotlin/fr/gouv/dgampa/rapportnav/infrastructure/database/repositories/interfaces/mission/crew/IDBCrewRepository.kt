package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.CrewModel
import org.springframework.data.repository.CrudRepository

interface IDBCrewRepository : CrudRepository<CrewModel, Int> {
}
