package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel
import org.springframework.data.jpa.repository.JpaRepository

interface IDBServiceRepository : JpaRepository<ServiceModel, Int>
