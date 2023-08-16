package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.CrewRoleModel
import org.springframework.data.repository.CrudRepository  
import org.springframework.data.rest.core.annotation.RepositoryRestResource  
  
@RepositoryRestResource(collectionResourceRel = "crew_role", path = "crew_role")
interface CrewRoleRepository : CrudRepository<CrewRoleModel, Int> {
}