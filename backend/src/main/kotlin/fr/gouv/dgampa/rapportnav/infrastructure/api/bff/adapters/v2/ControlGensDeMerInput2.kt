package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlResult
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import java.util.*

data class ControlGensDeMerInput2(
    override var id: UUID? = null,
    override val amountOfControls: Int? = null,
    override val unitShouldConfirm: Boolean? = null,
    override var unitHasConfirmed: Boolean? = null,
    override var infractions: List<InfractionInput2>? = null,
    override val observations: String? = null,
    override val hasBeenDone: Boolean? = null,
    val staffOutnumbered: ControlResult?,
    val upToDateMedicalCheck: ControlResult?,
    val knowledgeOfFrenchLawAndLanguage: ControlResult?,
) : BaseControlInput() {
    override fun toEntity(): ControlGensDeMerEntity {
        return ControlGensDeMerEntity(
            id = id ?: UUID.randomUUID(),
            missionId = missionId!!,
            actionControlId = actionId!!,
            amountOfControls = amountOfControls ?: 0,
            unitShouldConfirm = unitShouldConfirm,
            unitHasConfirmed = unitHasConfirmed,
            staffOutnumbered = staffOutnumbered,
            upToDateMedicalCheck = upToDateMedicalCheck,
            knowledgeOfFrenchLawAndLanguage = knowledgeOfFrenchLawAndLanguage,
            observations = observations,
            hasBeenDone = hasBeenDone
        )
    }
}
