package fr.gouv.gmampa.rapportnav.mocks.mission.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlResult
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import java.util.*

object ControlGensDeMerEntityMock {

    fun create(
        id: UUID = UUID.randomUUID(),
        missionId: Int = 1,
        actionControlId: String = "1234",
        amountOfControls: Int = 1,
        unitShouldConfirm: Boolean? = null,
        unitHasConfirmed: Boolean? = null,
        staffOutnumbered: ControlResult? = null,
        upToDateMedicalCheck: ControlResult? = null,
        knowledgeOfFrenchLawAndLanguage: ControlResult? = null,
        observations: String? = null,
        infractions: List<InfractionEntity>? = null
    ): ControlGensDeMerEntity {
        return ControlGensDeMerEntity(
            id = id,
            missionId = missionId,
            actionControlId = actionControlId,
            amountOfControls = amountOfControls,
            unitShouldConfirm = unitShouldConfirm,
            unitHasConfirmed = unitHasConfirmed,
            staffOutnumbered = staffOutnumbered,
            upToDateMedicalCheck = upToDateMedicalCheck,
            knowledgeOfFrenchLawAndLanguage = knowledgeOfFrenchLawAndLanguage,
            observations = observations,
            infractions = infractions,
        )
    }
}
