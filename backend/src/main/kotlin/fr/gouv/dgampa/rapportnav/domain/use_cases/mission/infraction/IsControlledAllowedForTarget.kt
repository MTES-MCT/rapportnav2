package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction.IInfractionEnvTargetRepository

@UseCase
class IsControlledAllowedForTarget(
    private val repo: IInfractionEnvTargetRepository,
    private val getInfractionById: GetInfractionById,
) {
    fun execute(actionId: String, vesselIdentifier: String, controlType: ControlType): Boolean {
        val targets = repo.findByActionIdAndVesselIdentifier(actionId = actionId, vesselIdentifier = vesselIdentifier)
        val controlledAllowed = targets.map {
            val infraction = it.infraction?.toInfractionEntity()
            return controlType != infraction?.controlType
        }
        return controlledAllowed.all { true }
    }
}
