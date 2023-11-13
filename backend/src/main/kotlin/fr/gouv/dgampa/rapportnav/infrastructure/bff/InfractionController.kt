package fr.gouv.dgampa.rapportnav.infrastructure.bff

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEnvTargetEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.infraction.AddOrUpdateInfraction
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.infraction.DeleteInfraction
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.infraction.GetInfractionById
import fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.infraction.InfractionInput
import fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.infraction.InfractionWithNewTargetInput
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.infraction.Infraction
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller
import java.util.*


@Controller
class InfractionController(
    private val addOrUpdateInfraction: AddOrUpdateInfraction,
    private val deleteInfraction: DeleteInfraction,
    private val getInfractionById: GetInfractionById
) {

    @MutationMapping
    fun addOrUpdateInfraction(@Argument infraction: InfractionInput): Infraction? {
        var input = infraction.toInfraction().toInfractionEntity()
        val infractionEntity = addOrUpdateInfraction.execute(input)
        return Infraction.fromInfractionEntity(infractionEntity)
    }

    @MutationMapping
    fun deleteInfraction(@Argument id: String): Boolean? {
        return try {
            deleteInfraction.execute(id = UUID.fromString(id))
            true
        }
        catch (exception: Exception) {
//            TODO add logs
            false
        }
    }

    @MutationMapping
    fun addOrUpdateInfractionForEnvTarget(@Argument infraction: InfractionWithNewTargetInput): Infraction? {
        val target: InfractionEnvTargetEntity?

        // get the infraction if it already exists and set its target
        if (infraction.id != null) {
            val repoInfraction = getInfractionById.execute(id = UUID.fromString(infraction.id))
            target = repoInfraction?.target
        } else {
            target = null
        }

        // TODO must also verify that there is not already an infraction for this target and this controlType

        val input = infraction.toInfractionEntity(target)
        val infractionEntity = addOrUpdateInfraction.execute(input)
        return Infraction.fromInfractionEntity(infractionEntity)
    }




}
