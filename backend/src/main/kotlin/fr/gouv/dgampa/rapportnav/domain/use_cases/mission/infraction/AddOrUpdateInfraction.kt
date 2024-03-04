package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlAdministrativeRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlGensDeMerRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlNavigationRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlSecurityRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction.IInfractionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.GetControlByActionId
import java.util.*

@UseCase
class AddOrUpdateInfraction(
    private val infractionRepo: IInfractionRepository,
    private val controlAdministrativeRepo: IControlAdministrativeRepository,
    private val controlSecurityRepo: IControlSecurityRepository,
    private val controlNavigationRepo: IControlNavigationRepository,
    private val controlGensDeMerRepo: IControlGensDeMerRepository,
    private val getControlByActionId: GetControlByActionId
) {
    @Suppress("IMPLICIT_CAST_TO_ANY")
    fun execute(infraction: InfractionEntity): InfractionEntity {
        // create control if it doesn't exist
        // users might add infractions without having checked a control
        return if (infraction.controlId != null) {
            val savedData = infractionRepo.save(infraction).toInfractionEntity()
            savedData
        } else {
            // Check if any of the controls already exist
            val existingControl =
                getControlByActionId.getControlForControlType(infraction.actionId, controlType = infraction.controlType)
            if (existingControl != null) {
                // Use the existing control
                // TODO make this better, it shouldn't be if getAnyControl would not return any
                val property = existingControl.javaClass.getDeclaredField("id")
                property.isAccessible = true
                val existingControlId = property.get(existingControl) as UUID
                infraction.controlId = existingControlId
            } else {
                val newControlId = UUID.randomUUID()
                val newControl = when (infraction.controlType) {
                    ControlType.ADMINISTRATIVE -> ControlAdministrativeEntity(
                        id = newControlId,
                        missionId = infraction.missionId,
                        actionControlId = infraction.actionId,
                        amountOfControls = 1
                    )

                    ControlType.SECURITY -> ControlSecurityEntity(
                        id = newControlId,
                        missionId = infraction.missionId,
                        actionControlId = infraction.actionId,
                        amountOfControls = 1
                    )

                    ControlType.NAVIGATION -> ControlNavigationEntity(
                        id = newControlId,
                        missionId = infraction.missionId,
                        actionControlId = infraction.actionId,
                        amountOfControls = 1
                    )

                    ControlType.GENS_DE_MER -> ControlGensDeMerEntity(
                        id = newControlId,
                        missionId = infraction.missionId,
                        actionControlId = infraction.actionId,
                        amountOfControls = 1
                    )

                    null -> null
                }

                if (newControl != null) {
                    // save the control
                    when (infraction.controlType) {
                        ControlType.ADMINISTRATIVE -> controlAdministrativeRepo.save(newControl as ControlAdministrativeEntity)
                        ControlType.SECURITY -> controlSecurityRepo.save(newControl as ControlSecurityEntity)
                        ControlType.NAVIGATION -> controlNavigationRepo.save(newControl as ControlNavigationEntity)
                        ControlType.GENS_DE_MER -> controlGensDeMerRepo.save(newControl as ControlGensDeMerEntity)
                        null -> {}
                    }
                    infraction.controlId = newControlId
                }


            }


            val savedData = infractionRepo.save(infraction).toInfractionEntity()
            savedData
        }

    }
}
