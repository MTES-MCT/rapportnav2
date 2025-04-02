package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetExternalDataEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ITargetRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.v2.ControlModel2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.target2.v2.TargetModel2
import org.slf4j.LoggerFactory
import java.util.*

@UseCase
class GetComputeEnvTarget(
    private val targetRepo: ITargetRepository
) {
    private val logger = LoggerFactory.getLogger(GetComputeEnvTarget::class.java)
    fun execute(actionId: String, envInfractions: List<InfractionEntity>?, isControl: Boolean?): List<TargetEntity2>? {
        if (isControl != true) return null
        return try {
            val navTargets = getNavTargets(actionId = actionId)
            val envTargets = getEnvTargets(actionId = actionId, envInfractions = envInfractions)
            navTargets + envTargets
        } catch (e: Exception) {
            logger.error("ComputeEnvTarget failed loading targets", e)
            return listOf()
        }
    }

    fun getNavTargets(actionId: String): List<TargetEntity2> {
        return targetRepo.findByActionId(actionId)
            .filter { it.externalId == null }
            .map { TargetEntity2.fromTargetModel(it) }
    }

    fun getEnvTargets(actionId: String, envInfractions: List<InfractionEntity>?): List<TargetEntity2> {
        return envInfractions?.map {
            var target = targetRepo.findByExternalId(it.id)
            if (target == null) {
                target = save(getNewTarget(actionId = actionId, externalId = it.id))
            }
            val entity = TargetEntity2.fromTargetModel(target)
            entity.externalData = getExternalData(it)
            entity
        }?: listOf()
    }


    fun save(target: TargetModel2):TargetModel2 {
        return targetRepo.save(target)
    }

    private fun getNewTarget(actionId: String, externalId: String): TargetModel2 {
        return TargetModel2(
            actionId = actionId,
            id = UUID.randomUUID(),
            externalId = externalId,
            controls = getNewControls(),
            targetType = TargetType.DEFAULT,
            status = TargetStatusType.IN_PROCESS.toString(),
            source = MissionSourceEnum.MONITORENV.toString()
        )
    }

    private fun getNewControls(): List<ControlModel2> {
        val controlTypes = listOf(
            ControlType.SECURITY,
            ControlType.NAVIGATION,
            ControlType.GENS_DE_MER,
            ControlType.ADMINISTRATIVE
        )
        return controlTypes.map {
            ControlModel2(
                controlType = it,
                id = UUID.randomUUID(),
                amountOfControls = 0
            )
        }
    }

    private fun getExternalData(envInfraction: InfractionEntity): TargetExternalDataEntity {
        return TargetExternalDataEntity(
            id = envInfraction.id,
            natinfs = envInfraction.natinf,
            toProcess = envInfraction.toProcess,
            vesselType = envInfraction.vesselType,
            vesselSize = envInfraction.vesselSize,
            companyName = envInfraction.companyName,
            formalNotice = envInfraction.formalNotice,
            observations = envInfraction.observations,
            relevantCourt = envInfraction.relevantCourt,
            infractionType = envInfraction.infractionType,
            registrationNumber = envInfraction.registrationNumber,
            controlledPersonIdentity = envInfraction.controlledPersonIdentity
        )
    }
}
