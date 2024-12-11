package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEnvTargetEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction.IInfractionEnvTargetRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction.IInfractionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.infraction.Infraction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.infraction.InfractionsByVessel
import java.util.*

@UseCase
class ProcessMissionActionInfractionEnvTarget(
    private val infractionRepo: IInfractionRepository,
    private val infractionEnvTargetRepo: IInfractionEnvTargetRepository,
) {

    fun execute(actionId: String, infractionByVessels: List<InfractionsByVessel>?): List<InfractionEntity>? {
        val infractions =
            infractionByVessels?.flatMap { it.infractions }?.filter { it.controlId != null || it.controlType != null }
        val infractionIds = infractions?.map { it.id } ?: listOf()

        val databaseInfractions = infractionRepo
            .findAllByActionId(actionId)
            .map { it.toInfractionEntity() }

        val toDeleteInfractions = databaseInfractions.filter { !infractionIds.contains(it.id.toString()) }

        delete(toDeleteInfractions)
        return save(infractions) //TODO: is not equals save
    }

    fun save(infractions: List<Infraction>?): List<InfractionEntity>? {
        // check target / create or Update target
        // create infractionEntity  -- > save target
        //save infraction
        return infractions?.map {
            // todo check if it has changed
            val target = it.target
            val newTarget = InfractionEnvTargetEntity(
                id = UUID.fromString(target?.id),
                missionId = it.missionId,
                actionId = it.actionId,
                infractionId = UUID.fromString(it.id),
                vesselIdentifier = target?.vesselIdentifier,
                vesselSize = target?.vesselSize,
                vesselType = target?.vesselType,
                identityControlledPerson = target?.identityControlledPerson!!
            )
            infractionEnvTargetRepo.save(infractionTarget = newTarget, infraction = it.toInfractionEntity())
            infractionRepo.save(it.toInfractionEntity()).toInfractionEntity()
        }
    }

    fun delete(infractions: List<InfractionEntity>?) {
        infractions?.forEach { infractionRepo.deleteById(it.id) }
    }
}
