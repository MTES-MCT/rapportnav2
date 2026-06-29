package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetActionByOwnerId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvActionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetFishActionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetNavActionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionExternalId
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionEnvActionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionFishActionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionNavActionEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [GetActionByOwnerId::class])
@ContextConfiguration(classes = [GetActionByOwnerId::class])
class GetActionByOwnerIdTest {

    @Autowired
    private lateinit var getActionByOwnerId: GetActionByOwnerId

    @MockitoBean
    private lateinit var getNavActionById: GetNavActionById

    @MockitoBean
    private lateinit var getEnvActionById: GetEnvActionById

    @MockitoBean
    private lateinit var getFishActionById: GetFishActionById

    @MockitoBean
    private lateinit var getStatusForAction: GetStatusForAction

    @MockitoBean
    private lateinit var getMissionExternalId: GetMissionExternalId

    @Test
    fun `should return the nav action with its computed status when found`() {
        val ownerId = UUID.randomUUID()
        val actionId = UUID.randomUUID().toString()
        val navAction = MissionNavActionEntityMock.create(
            ownerId = ownerId,
            actionType = ActionType.CONTROL,
            startDateTimeUtc = Instant.parse("2024-01-01T10:00:00Z"),
        )

        `when`(getNavActionById.execute(actionId)).thenReturn(navAction)
        `when`(
            getStatusForAction.execute(
                ownerId = ownerId,
                actionStartDateTimeUtc = navAction.startDateTimeUtc,
            )
        ).thenReturn(ActionStatusType.NAVIGATING)

        val result = getActionByOwnerId.execute(ownerId = ownerId.toString(), actionId = actionId)

        assertThat(result).isSameAs(navAction)
        assertThat(result?.status).isEqualTo(ActionStatusType.NAVIGATING)
        // env/fish resolution must not be attempted once a nav action is found
        verify(getMissionExternalId, never()).execute(anyOrNull())
        verify(getFishActionById, never()).execute(anyOrNull(), anyOrNull())
        verify(getEnvActionById, never()).execute(anyOrNull(), anyOrNull())
    }

    @Test
    fun `should return the fish action when ownerId is an external Int id`() {
        val actionId = "fish-action-id"
        val fishAction = MissionFishActionEntityMock.create(ownerId = UUID.randomUUID())

        `when`(getNavActionById.execute(actionId)).thenReturn(null)
        `when`(getFishActionById.execute(42, actionId)).thenReturn(fishAction)

        val result = getActionByOwnerId.execute(ownerId = "42", actionId = actionId)

        assertThat(result).isSameAs(fishAction)
        // ownerId was not a UUID, so no mission lookup is needed
        verify(getMissionExternalId, never()).execute(anyOrNull())
        verify(getEnvActionById, never()).execute(anyOrNull(), anyOrNull())
    }

    @Test
    fun `should resolve externalId from mission then return the env action when ownerId is a UUID`() {
        val ownerId = UUID.randomUUID()
        val actionId = "env-action-id"
        val envAction = MissionEnvActionEntityMock.create(ownerId = ownerId)

        `when`(getNavActionById.execute(actionId)).thenReturn(null)
        `when`(getMissionExternalId.execute(ownerId)).thenReturn(7)
        `when`(getFishActionById.execute(7, actionId)).thenReturn(null)
        `when`(getEnvActionById.execute(7, actionId)).thenReturn(envAction)

        val result = getActionByOwnerId.execute(ownerId = ownerId.toString(), actionId = actionId)

        assertThat(result).isSameAs(envAction)
    }

    @Test
    fun `should return null when no action matches`() {
        val actionId = "missing-action-id"

        `when`(getNavActionById.execute(actionId)).thenReturn(null)
        `when`(getFishActionById.execute(99, actionId)).thenReturn(null)
        `when`(getEnvActionById.execute(99, actionId)).thenReturn(null)

        val result = getActionByOwnerId.execute(ownerId = "99", actionId = actionId)

        assertThat(result).isNull()
    }

    @Test
    fun `should return null without looking up actions when the UUID mission has no externalId`() {
        val ownerId = UUID.randomUUID()
        val actionId = "some-action-id"

        `when`(getNavActionById.execute(actionId)).thenReturn(null)
        `when`(getMissionExternalId.execute(ownerId)).thenReturn(null)

        val result = getActionByOwnerId.execute(ownerId = ownerId.toString(), actionId = actionId)

        assertThat(result).isNull()
        verify(getFishActionById, never()).execute(anyOrNull(), anyOrNull())
        verify(getEnvActionById, never()).execute(anyOrNull(), anyOrNull())
    }
}
