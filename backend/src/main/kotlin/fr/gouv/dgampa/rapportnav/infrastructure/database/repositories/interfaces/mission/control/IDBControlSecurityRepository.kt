package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlSecurityModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBControlSecurityRepository: JpaRepository<ControlSecurityModel, UUID>
