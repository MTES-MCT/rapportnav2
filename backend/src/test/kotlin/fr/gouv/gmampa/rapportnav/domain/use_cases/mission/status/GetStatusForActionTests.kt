package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.status

import fr.gouv.dgampa.rapportnav.RapportNavApplication
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.action.GetStatusForAction
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*


@SpringBootTest(classes = [RapportNavApplication::class])
class GetStatusForActionTests {

    private var missionId: Int = 1

    @MockBean
    private lateinit var statusActionsRepository: INavActionStatusRepository

    @Autowired
    private lateinit var getStatusForAction: GetStatusForAction

    @Test
    fun `execute Should return Unknown when action is null for a mission`() {
        given(this.statusActionsRepository.findAllByMissionId(missionId)).willReturn(null);
        val statusForAction = getStatusForAction.execute(missionId=missionId, actionStartDateTimeUtc=null);
        assertThat(statusForAction).isEqualTo(ActionStatusType.UNKNOWN);
    }
    @Test
    fun `execute Should return Unknown when action is empty list for a mission`() {
        given(this.statusActionsRepository.findAllByMissionId(missionId = 1)).willReturn(listOf<ActionStatusEntity>());
        val statusForAction = getStatusForAction.execute(missionId=missionId, actionStartDateTimeUtc=null);
        assertThat(statusForAction).isEqualTo(ActionStatusType.UNKNOWN);
    }
    @Test
    fun `execute Should return Unknown if only finishing actions`() {
        val startDatetime = ZonedDateTime.of(2023, 6, 19, 10, 0, 0, 0, ZoneId.of("Europe/Berlin"))
        val finishingAction = ActionStatusEntity(
            id = UUID.randomUUID(),
            missionId = missionId,
            startDateTimeUtc = startDatetime,
            isStart = false,
            status = ActionStatusType.UNAVAILABLE,
        )
        val actions = listOf(finishingAction)
        given(this.statusActionsRepository.findAllByMissionId(missionId = 1)).willReturn(actions);
        val statusForAction = getStatusForAction.execute(missionId=missionId, actionStartDateTimeUtc=startDatetime);
        assertThat(statusForAction).isEqualTo(ActionStatusType.UNKNOWN);
    }
    @Test
    fun `execute Should return the last action status if the last action is a starting status`() {
        val startDatetime = ZonedDateTime.of(2023, 6, 19, 10, 0, 0, 0, ZoneId.of("Europe/Berlin"))
        val startingAction = ActionStatusEntity(
            id = UUID.randomUUID(),
            missionId = missionId,
            startDateTimeUtc = startDatetime,
            isStart = true,
            status = ActionStatusType.UNAVAILABLE,
        )
        val actions = listOf(startingAction)
        given(this.statusActionsRepository.findAllByMissionId(missionId = 1)).willReturn(actions);
        val statusForAction = getStatusForAction.execute(missionId=missionId, actionStartDateTimeUtc=startDatetime);
        assertThat(statusForAction).isEqualTo(ActionStatusType.UNAVAILABLE);
    }
    @Test
    fun `execute Should return the last started action status if last action is ending but at the same timestamp as a starting status action`() {
        val startDatetime = ZonedDateTime.of(2023, 6, 19, 10, 0, 0, 0, ZoneId.of("Europe/Berlin"))
        val startingAction = ActionStatusEntity(
            id = UUID.randomUUID(),
            missionId = missionId,
            startDateTimeUtc = startDatetime,
            isStart = true,
            status = ActionStatusType.UNAVAILABLE,
        )
        val finishingAction = ActionStatusEntity(
            id = UUID.randomUUID(),
            missionId = missionId,
            startDateTimeUtc = startDatetime,
            isStart = false,
            status = ActionStatusType.DOCKED,
        )
        val actions = listOf(finishingAction, startingAction)
        given(this.statusActionsRepository.findAllByMissionId(missionId = 1)).willReturn(actions);
        val statusForAction = getStatusForAction.execute(missionId=missionId, actionStartDateTimeUtc=startDatetime);
        assertThat(statusForAction).isEqualTo(startingAction.status);
    }
}
