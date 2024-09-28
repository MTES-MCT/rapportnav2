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


    fun deleteAll(actionId: String): Boolean {
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

    fun deleteControlAdministrative(actionId: String): Boolean {
        return try {
            controlAdministrativeRepo.deleteByActionControlId(actionControlId = actionId)
            true
        } catch (e: Exception) {
            // TODO add log
            false
        }
    }

    fun deleteControlSecurity(actionId: String): Boolean {
        return try {
            controlSecurityRepo.deleteByActionControlId(actionControlId = actionId)
            true
        } catch (e: Exception) {
            // TODO add log
            false
        }
    }

    fun deleteControlNavigation(actionId: String): Boolean {
        return try {
            controlNavigationRepo.deleteByActionControlId(actionControlId = actionId)
            true
        } catch (e: Exception) {
            // TODO add log
            false
        }
    }

    fun deleteControlGensDeMer(actionId: String): Boolean {
        return try {
            controlGensDeMerRepo.deleteByActionControlId(actionControlId = actionId)
            true
        } catch (e: Exception) {
            // TODO add log
            false
        }
    }
}
