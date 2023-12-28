package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.infraction

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction.InfractionEnvTargetModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBInfractionEnvTargetRepository : JpaRepository<InfractionEnvTargetModel, UUID> {
    fun findByActionIdAndVesselIdentifier(actionId: String, vesselIdentifier: String): List<InfractionEnvTargetModel>
}
