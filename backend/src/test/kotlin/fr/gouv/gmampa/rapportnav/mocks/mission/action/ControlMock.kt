package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.v2.ActionControlEntity
import fr.gouv.gmampa.rapportnav.mocks.mission.infraction.InfractionEntityMock
import java.util.*

object ControlMock {

    fun createAllControl(): ActionControlEntity {
        return ActionControlEntity(
            controlSecurity = ControlSecurityEntity(
                id = UUID.randomUUID(), missionId = 761,
                actionControlId = "MyActionId",
                amountOfControls = 2
            ),
            controlGensDeMer = ControlGensDeMerEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionControlId = "MyActionId",
                amountOfControls = 2
            ),
            controlNavigation = ControlNavigationEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionControlId = "MyActionId",
                amountOfControls = 2,
                infractions = listOf(InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITHOUT_REPORT))
            ),
            controlAdministrative = ControlAdministrativeEntity(
                id = UUID.randomUUID(),
                missionId = 761,
                actionControlId = "MyActionId",
                amountOfControls = 2,
                infractions = listOf(InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT))
            )
        )
    }
}
