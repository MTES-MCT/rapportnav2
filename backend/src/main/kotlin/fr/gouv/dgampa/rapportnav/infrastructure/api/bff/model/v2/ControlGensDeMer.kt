package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlResult
import java.util.*

data class ControlGensDeMer(
    override var id: UUID? = null,
    override val amountOfControls: Int? = null,
    override val unitShouldConfirm: Boolean? = null,
    override var unitHasConfirmed: Boolean? = null,
    override var infractions: List<Infraction>? = null,
    override val observations: String? = null,
    override val hasBeenDone: Boolean? = null,
    val staffOutnumbered: ControlResult?,
    val upToDateMedicalCheck: ControlResult?,
    val knowledgeOfFrenchLawAndLanguage: ControlResult?,
) : BaseControl() {
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
            hasBeenDone = hasBeenDone,
            infractions = infractions?.map { it.toInfractionEntity() }
        )
    }

    companion object {
        fun fromControlGensDeMerEntity(entity: ControlGensDeMerEntity?): ControlGensDeMer {
            return ControlGensDeMer(
                id = entity?.id,
                amountOfControls = entity?.amountOfControls,
                unitShouldConfirm = entity?.unitShouldConfirm,
                unitHasConfirmed = entity?.unitHasConfirmed,
                observations = entity?.observations,
                hasBeenDone = entity?.hasBeenDone,
                staffOutnumbered = entity?.staffOutnumbered,
                upToDateMedicalCheck = entity?.upToDateMedicalCheck,
                knowledgeOfFrenchLawAndLanguage = entity?.knowledgeOfFrenchLawAndLanguage,
                infractions = entity?.infractions?.map { Infraction.fromInfractionEntity(it) }
            )
        }
    }
}
