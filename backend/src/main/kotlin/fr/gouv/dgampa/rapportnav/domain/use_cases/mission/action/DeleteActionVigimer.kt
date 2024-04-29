package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionVigimerRepository
import java.util.*

@UseCase
class DeleteActionVigimer(private val vigimerRepository: INavActionVigimerRepository) {

    fun execute(id: UUID): Boolean {
        return try {
            if (this.vigimerRepository.existsById(id)) {
                this.vigimerRepository.deleteById(id = id)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            // TODO add log
            false
        }

    }
}
