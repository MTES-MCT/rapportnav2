package fr.gouv.gmampa.rapportnav.mocks.mission.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlResult
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import java.util.*

object ControlSecurityEntityMock {

    fun create(
        id: UUID = UUID.randomUUID(),
        missionId: Int = 1,
        actionControlId: String = "1234",
        amountOfControls: Int = 1,
        unitShouldConfirm: Boolean? = null,
        unitHasConfirmed: Boolean? = null,
        observations: String? = null,
        infractions: List<InfractionEntity>? = null
    ): ControlSecurityEntity {
        return ControlSecurityEntity(
            id = id,
            missionId = missionId,
            actionControlId = actionControlId,
            amountOfControls = amountOfControls,
            unitShouldConfirm = unitShouldConfirm,
            unitHasConfirmed = unitHasConfirmed,
            observations = observations,
            infractions = infractions,
        )
    }
}
