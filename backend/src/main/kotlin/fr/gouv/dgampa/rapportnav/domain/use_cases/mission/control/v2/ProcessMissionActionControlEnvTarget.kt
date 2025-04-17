package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.*
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlAdministrativeRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlGensDeMerRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlNavigationRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlSecurityRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.ActionControl
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Infraction
import java.util.*

@UseCase
class ProcessMissionActionControlEnvTarget(
    private val controlSecurityRepo: IControlSecurityRepository,
    private val controlNavigationRepo: IControlNavigationRepository,
    private val controlGensDeMerRepo: IControlGensDeMerRepository,
    private val controlAdministrativeRepo: IControlAdministrativeRepository
) {

    fun execute(infraction: Infraction, controls: ActionControl): String? {
        val controlType = infraction.controlType
        val id = when (controlType) {
            ControlType.SECURITY -> controls.controlSecurity?.id
                ?: processControlSecurity(
                    actionId = infraction.actionId!!,
                    missionId = infraction.missionId!!
                ).id

            ControlType.GENS_DE_MER -> controls.controlGensDeMer?.id ?: processControlGensDeMer(
                actionId = infraction.actionId!!,
                missionId = infraction.missionId!!
            ).id

            ControlType.NAVIGATION -> controls.controlNavigation?.id ?: processControlNavigation(
                actionId = infraction.actionId!!,
                missionId = infraction.missionId!!
            ).id

            ControlType.ADMINISTRATIVE -> controls.controlAdministrative?.id ?: processControlAdministrative(
                actionId = infraction.actionId!!,
                missionId = infraction.missionId!!
            ).id

            null -> null
        }
        return id?.toString()
    }

    private inline fun <T : BaseControlEntity> processControl(
        missionId: String,
        actionId: String,
        saveControl: (T) -> T,
        findControlByActionId: (String) -> T?,
        createControl: (UUID, String, String) -> T,
    ): T {
        val existingControl = findControlByActionId(actionId)
        if (existingControl?.id != null) return existingControl
        val control = createControl(UUID.randomUUID(), actionId, missionId)
        return saveControl(control)
    }


    private fun processControlSecurity(actionId: String, missionId: String): ControlSecurityEntity {
        return processControl(
            actionId = actionId,
            missionId = missionId,
            createControl = { id: UUID, s: String, i: String ->
                ControlSecurityEntity(
                    id = id,
                    missionId = i,
                    actionControlId = s,
                    amountOfControls = 1
                )
            },
            saveControl = { controlSecurityRepo.save(it).toControlSecurityEntity() },
            findControlByActionId = {
                if (controlSecurityRepo.existsByActionControlId(it)) {
                    controlSecurityRepo.findByActionControlId(it).toControlSecurityEntity()
                } else {
                    null
                }
            },
        )
    }

    private fun processControlGensDeMer(actionId: String, missionId: String): ControlGensDeMerEntity {
        return processControl(
            actionId = actionId,
            missionId = missionId,
            createControl = { id: UUID, s: String, i: String ->
                ControlGensDeMerEntity(
                    id = id,
                    missionId = i,
                    actionControlId = s,
                    amountOfControls = 1
                )
            },
            saveControl = { controlGensDeMerRepo.save(it).toControlGensDeMerEntity() },
            findControlByActionId = {
                if (controlGensDeMerRepo.existsByActionControlId(it)) {
                    controlGensDeMerRepo.findByActionControlId(it).toControlGensDeMerEntity()
                } else {
                    null
                }
            },
        )
    }

    private fun processControlNavigation(actionId: String, missionId: String): ControlNavigationEntity {
        return processControl(
            actionId = actionId,
            missionId = missionId,
            createControl = { id: UUID, s: String, i: String ->
                ControlNavigationEntity(
                    id = id,
                    missionId = i,
                    actionControlId = s,
                    amountOfControls = 1
                )
            },
            saveControl = { controlNavigationRepo.save(it).toControlNavigationEntity() },
            findControlByActionId = {
                if (controlNavigationRepo.existsByActionControlId(it)) {
                    controlNavigationRepo.findByActionControlId(it).toControlNavigationEntity()
                } else {
                    null
                }
            },
        )
    }

    private fun processControlAdministrative(actionId: String, missionId: String): ControlAdministrativeEntity {
        return processControl(
            actionId = actionId,
            missionId = missionId,
            createControl = { id: UUID, s: String, i: String ->
                ControlAdministrativeEntity(
                    id = id,
                    missionId = i,
                    actionControlId = s,
                    amountOfControls = 1
                )
            },
            saveControl = { controlAdministrativeRepo.save(it).toControlAdministrativeEntity() },
            findControlByActionId = {
                if (controlAdministrativeRepo.existsByActionControlId(it)) {
                    controlAdministrativeRepo.findByActionControlId(it).toControlAdministrativeEntity()
                } else {
                    null
                }
            },
        )
    }

}
