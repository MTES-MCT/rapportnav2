package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlAdministrativeRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlGensDeMerRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlNavigationRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlSecurityRepository
import java.util.*

@UseCase
class DeleteControlByActionId(
    private val controlAdministrativeRepo: IControlAdministrativeRepository,
    private val controlSecurityRepo: IControlSecurityRepository,
    private val controlNavigationRepo: IControlNavigationRepository,
    private val controlGensDeMerRepo: IControlGensDeMerRepository,
) {


    fun deleteAll(actionId: UUID): Boolean {
        return try {
            deleteControlAdministrative(actionId)
            deleteControlSecurity(actionId)
            deleteControlNavigation(actionId)
            deleteControlGensDeMer(actionId)
            true
        } catch (e: Exception) {
            // TODO add log
            false
        }
    }

    fun deleteControlAdministrative(actionId: UUID): Boolean {
        return try {
            controlAdministrativeRepo.deleteByActionControlId(actionControlId = actionId.toString())
            true
        } catch (e: Exception) {
            // TODO add log
            false
        }
    }

    fun deleteControlSecurity(actionId: UUID): Boolean {
        return try {
            controlSecurityRepo.deleteByActionControlId(actionControlId = actionId.toString())
            true
        } catch (e: Exception) {
            // TODO add log
            false
        }
    }

    fun deleteControlNavigation(actionId: UUID): Boolean {
        return try {
            controlNavigationRepo.deleteByActionControlId(actionControlId = actionId.toString())
            true
        } catch (e: Exception) {
            // TODO add log
            false
        }
    }

    fun deleteControlGensDeMer(actionId: UUID): Boolean {
        return try {
            controlGensDeMerRepo.deleteByActionControlId(actionControlId = actionId.toString())
            true
        } catch (e: Exception) {
            // TODO add log
            false
        }
    }
}
