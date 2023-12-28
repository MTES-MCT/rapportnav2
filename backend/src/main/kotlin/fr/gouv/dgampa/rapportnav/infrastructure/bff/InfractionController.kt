package fr.gouv.dgampa.rapportnav.infrastructure.bff

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEnvTargetEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.*
import fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.infraction.InfractionInput
import fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.infraction.InfractionWithNewTargetInput
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.infraction.Infraction
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.infraction.Natinf
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.*


@Controller
class InfractionController(
    private val addOrUpdateInfraction: AddOrUpdateInfraction,
    private val deleteInfraction: DeleteInfraction,
    private val getInfractionById: GetInfractionById,
    private val getNatinfs: GetNatinfs,
    private val addOrUpdateInfractionEnvTarget: AddOrUpdateInfractionEnvTarget,
    private val isControlledAllowedForTarget: IsControlledAllowedForTarget,
) {

    private val logger = LoggerFactory.getLogger(InfractionController::class.java)


    @QueryMapping
    fun natinfs(): List<Natinf> {
        return getNatinfs.execute().map { Natinf.fromNatinfEntity(it) }
    }

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
        } catch (exception: Exception) {
//            TODO add logs
            false
        }
    }

    @MutationMapping
    fun addOrUpdateInfractionForEnvTarget(@Argument infraction: InfractionWithNewTargetInput): Infraction? {
        var target: InfractionEnvTargetEntity?


        if (infraction.id == null) {
            // check if there is not already an infraction with the same controlType for this target
            val isAllowed = isControlledAllowedForTarget.execute(
                actionId = infraction.actionId,
                vesselIdentifier = infraction.vesselIdentifier,
                controlType = ControlType.valueOf(infraction.controlType)
            )
            if (!isAllowed) {
                logger.error("addOrUpdateInfractionForEnvTarget - controlType=${infraction.controlType} already exists for target name=${infraction.vesselIdentifier}")
                throw Exception("Ce type de contrôle a déjà été reporté pour cette cible. Veuillez annuler et modifier le contrôle déjà existant.")
            } else {
                target = null
            }
        } else {

            // get the infraction if it already exists and set its target
            val repoInfraction = getInfractionById.execute(id = UUID.fromString(infraction.id))
            target = repoInfraction?.target

            // Check if target has changed or not by crossing fields between InfractionWithNewTargetInput and InfractionEnvTargetEntity
            if (target != null && hasTargetChanged(infraction, target)) {
                // Update the target
                val newTarget = InfractionEnvTargetEntity(
                    id = target.id,
                    missionId = target.missionId,
                    actionId = target.actionId,
                    infractionId = target.infractionId,
                    vesselIdentifier = infraction.vesselIdentifier,
                    vesselSize = VesselSizeEnum.valueOf(infraction.vesselSize),
                    vesselType = VesselTypeEnum.valueOf(infraction.vesselType),
                    identityControlledPerson = infraction.identityControlledPerson
                )
                target = addOrUpdateInfractionEnvTarget.execute(newTarget, infraction.toInfractionEntity(newTarget))
            }
        }

        val input = infraction.toInfractionEntity(target)
        val infractionEntity = addOrUpdateInfraction.execute(input)
        return Infraction.fromInfractionEntity(infractionEntity)
    }

    // Function to check if the target has changed
    private fun hasTargetChanged(input: InfractionWithNewTargetInput, target: InfractionEnvTargetEntity): Boolean {
        return input.vesselType != target.vesselType.name ||
                input.vesselSize != target.vesselSize.name ||
                input.vesselIdentifier != target.vesselIdentifier ||
                input.identityControlledPerson != target.identityControlledPerson
    }


}
