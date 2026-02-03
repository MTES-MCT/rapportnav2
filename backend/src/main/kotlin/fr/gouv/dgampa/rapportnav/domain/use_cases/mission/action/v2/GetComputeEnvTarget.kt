package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetExternalDataEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ITargetRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.v2.ControlModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.target2.v2.TargetModel
import java.time.Instant
import java.util.*

@UseCase
class GetComputeEnvTarget(
    private val targetRepo: ITargetRepository
) {
    fun execute(actionId: String, envInfractions: List<InfractionEnvEntity>?, isControl: Boolean?): List<TargetEntity>? {
        if (isControl != true) return null
        val navTargets = getNavTargets(actionId = actionId)
        val envTargets = getEnvTargets(actionId = actionId, envInfractions = envInfractions)
        return navTargets + envTargets
    }

    fun getNavTargets(actionId: String): List<TargetEntity> {
        var targets = targetRepo.findByActionId(actionId)
            .filter { it.externalId == null }

        if (targets.isEmpty()) {
            val target = save(
                getNewTarget(
                    actionId = actionId,
                    targetType = TargetType.DEFAULT,
                    source = MissionSourceEnum.RAPPORTNAV
                )
            )
            targets = listOf(target)
        }
        return targets.map { TargetEntity.fromTargetModel(it) }
    }

    fun getEnvTargets(actionId: String, envInfractions: List<InfractionEnvEntity>?): List<TargetEntity> {
        return envInfractions?.map {
            var target = targetRepo.findByExternalId(it.id)
            if (target == null) {
                target = save(
                    getNewTarget(
                        actionId = actionId,
                        externalId = it.id,
                        targetType = getTargetType(it),
                        source = MissionSourceEnum.MONITORENV
                    )
                )
            }
            val entity = TargetEntity.fromTargetModel(target)
            entity.externalData = getExternalData(it)
            entity
        }?: listOf()
    }


    fun save(target: TargetModel):TargetModel {
        return targetRepo.save(target)
    }

    private fun getNewTarget(
        actionId: String,
        source: MissionSourceEnum,
        targetType: TargetType,
        externalId: String? = null
    ): TargetModel {
        return TargetModel(
            actionId = actionId,
            id = UUID.randomUUID(),
            targetType = targetType,
            externalId = externalId,
            source = source.toString(),
            controls = getNewControls(),
            startDateTimeUtc = Instant.now(),
            status = TargetStatusType.IN_PROCESS.toString()

        )
    }

    private fun getNewControls(): List<ControlModel> {
        val controlTypes = listOf(
            ControlType.SECURITY,
            ControlType.NAVIGATION,
            ControlType.GENS_DE_MER,
            ControlType.ADMINISTRATIVE
        )
        return controlTypes.map {
            ControlModel(
                controlType = it,
                amountOfControls = 0,
                id = UUID.randomUUID()
            )
        }
    }

    private fun getExternalData(envInfraction: InfractionEnvEntity): TargetExternalDataEntity {
        return TargetExternalDataEntity(
            id = envInfraction.id,
            natinfs = envInfraction.natinf,
            nbTarget = envInfraction.nbTarget,
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

    private fun getTargetType(envInfraction: InfractionEnvEntity): TargetType {
        if(envInfraction.companyName != null) return TargetType.COMPANY
        if(envInfraction.registrationNumber != null) return TargetType.VEHICLE
        if(envInfraction.controlledPersonIdentity != null) return TargetType.INDIVIDUAL
        return TargetType.DEFAULT
    }
}
