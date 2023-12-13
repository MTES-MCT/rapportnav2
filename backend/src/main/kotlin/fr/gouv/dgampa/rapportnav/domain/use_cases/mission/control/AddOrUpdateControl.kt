package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlAdministrativeRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlGensDeMerRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlNavigationRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlSecurityRepository

@UseCase
class AddOrUpdateControl(
    private val controlAdministrativeRepo: IControlAdministrativeRepository,
    private val controlSecurityRepo: IControlSecurityRepository,
    private val controlNavigationRepo: IControlNavigationRepository,
    private val controlGensDeMerRepo: IControlGensDeMerRepository,
) {
    fun addOrUpdateControlAdministrative(control: ControlAdministrativeEntity): ControlAdministrativeEntity {
        val savedData = controlAdministrativeRepo.save(control).toControlAdministrativeEntity()
        return savedData
    }

    fun addOrUpdateControlSecurity(control: ControlSecurityEntity): ControlSecurityEntity {
        val savedData = controlSecurityRepo.save(control).toControlSecurityEntity()
        return savedData
    }

    fun addOrUpdateControlNavigation(control: ControlNavigationEntity): ControlNavigationEntity {
        val savedData = controlNavigationRepo.save(control).toControlNavigationEntity()
        return savedData
    }

    fun addOrUpdateControlGensDeMer(control: ControlGensDeMerEntity): ControlGensDeMerEntity {
        val savedData = controlGensDeMerRepo.save(control).toControlGensDeMerEntity()
        return savedData
    }
}
