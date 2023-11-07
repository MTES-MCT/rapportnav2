package fr.gouv.dgampa.rapportnav.infrastructure.bff

import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control.GetControlAdministrativeById
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.infraction.AddOrUpdateInfraction
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.infraction.DeleteInfraction
import fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.infraction.InfractionInput
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.infraction.Infraction
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller
import java.util.*


@Controller
class InfractionController(
    private val addOrUpdateInfraction: AddOrUpdateInfraction,
    private val getControlAdministrativeById: GetControlAdministrativeById,
    private val deleteInfraction: DeleteInfraction
) {

    @MutationMapping
    fun addOrUpdateInfraction(@Argument infraction: InfractionInput): Infraction? {
        var input = infraction.toInfraction().toInfractionEntity()
        val associatedControl = getControlAdministrativeById.execute(id = UUID.fromString(infraction.controlId))
        input.controlAdministrative = associatedControl
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

//    @SchemaMapping(typeName = "ControlAdministrative", field = "infraction")
//    fun getInfractionAdministrative(action: ControlAdministrative): Infraction? {
//        return null
//    }

 }
