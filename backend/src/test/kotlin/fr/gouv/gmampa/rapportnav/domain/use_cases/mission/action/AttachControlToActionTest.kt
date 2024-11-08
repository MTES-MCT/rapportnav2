package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action


import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.v2.ActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.AttachControlToAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.GetControlByActionId2
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ControlMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [AttachControlToAction::class])
@ContextConfiguration(classes = [AttachControlToAction::class])
class AttachControlToActionTest {

    @Autowired
    private lateinit var attachControl: AttachControlToAction

    @MockBean
    private lateinit var getControlByActionId2: GetControlByActionId2

    @Test
    fun `test execute attach controls to action`() {
        val actionId = UUID.randomUUID()
        val actionEntity = MissionEnvActionEntity(
            missionId = 761,
            envActionType = ActionTypeEnum.CONTROL,
            id = actionId,
            actionStartDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
            actionEndDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
            observations = "My beautiful observation"
        )

        val mockControl  = ControlMock.createAllControl()

        `when`(getControlByActionId2.getAllControl(actionId.toString())).thenReturn(mockControl)
        val missionActionEntity = attachControl.execute(actionEntity)
        assertThat(missionActionEntity).isNotNull()
        assertThat(missionActionEntity.controlSecurity).isEqualTo(mockControl.controlSecurity)
        assertThat(missionActionEntity.controlGensDeMer).isEqualTo(mockControl.controlGensDeMer)
        assertThat(missionActionEntity.controlNavigation).isEqualTo(mockControl.controlNavigation)
        assertThat(missionActionEntity.controlAdministrative).isEqualTo(mockControl.controlAdministrative)

    }
}
