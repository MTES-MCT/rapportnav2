package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.CrewModel
import org.springframework.data.repository.CrudRepository

interface IDBCrewRepository : CrudRepository<CrewModel, Int> {
}
