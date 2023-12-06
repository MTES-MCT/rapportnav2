package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface IDBServiceRepository: JpaRepository<ServiceModel, Int>
