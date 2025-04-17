package fr.gouv.gmampa.rapportnav.mocks.mission.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlResult
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import java.util.*

object ControlAdministrativeEntityMock {

    fun create(
        id: UUID = UUID.randomUUID(),
        missionId: String = "1",
        actionControlId: String = "1234",
        amountOfControls: Int = 1,
        unitShouldConfirm: Boolean? = null,
        unitHasConfirmed: Boolean? = null,
        compliantOperatingPermit: ControlResult? = null,
        upToDateNavigationPermit: ControlResult? = null,
        compliantSecurityDocuments: ControlResult? = null,
        observations: String? = null,
        infractions: List<InfractionEntity>? = null
    ): ControlAdministrativeEntity {
        return ControlAdministrativeEntity(
            id = id,
            missionId = missionId,
            actionControlId = actionControlId,
            amountOfControls = amountOfControls,
            unitShouldConfirm = unitShouldConfirm,
            unitHasConfirmed = unitHasConfirmed,
            compliantOperatingPermit = compliantOperatingPermit,
            upToDateNavigationPermit = upToDateNavigationPermit,
            compliantSecurityDocuments = compliantSecurityDocuments,
            observations = observations,
            infractions = infractions,
        )
    }
}
