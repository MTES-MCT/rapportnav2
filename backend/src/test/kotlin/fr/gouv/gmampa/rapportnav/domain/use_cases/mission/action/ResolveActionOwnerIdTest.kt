package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.ResolveActionOwnerId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionByExternalId
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.UUID

@SpringBootTest(classes = [ResolveActionOwnerId::class])
class ResolveActionOwnerIdTest {

    @Autowired
    private lateinit var resolveActionOwnerId: ResolveActionOwnerId

    @MockitoBean
    private lateinit var getMissionByExternalId: GetMissionByExternalId

    @Test
    fun `returns the UUID directly when the owner is already a UUID`() {
        val ownerId = UUID.randomUUID()

        val result = resolveActionOwnerId.execute(ownerId.toString())

        assertThat(result).isEqualTo(ownerId)
        verify(getMissionByExternalId, never()).execute(ownerId.toString())
    }

    @Test
    fun `trims the value before parsing a UUID`() {
        val ownerId = UUID.randomUUID()

        val result = resolveActionOwnerId.execute("  $ownerId  ")

        assertThat(result).isEqualTo(ownerId)
    }

    @Test
    fun `resolves the local mission UUID from a numeric external id`() {
        val missionUuid = UUID.randomUUID()
        val mission = MissionModel(id = missionUuid, serviceId = 1, startDateTimeUtc = Instant.now())
        whenever(getMissionByExternalId.execute("761")).thenReturn(mission)

        val result = resolveActionOwnerId.execute("761")

        assertThat(result).isEqualTo(missionUuid)
        verify(getMissionByExternalId).execute("761")
    }

    @Test
    fun `returns null when no local mission exists for the external id`() {
        whenever(getMissionByExternalId.execute("761")).thenReturn(null)

        val result = resolveActionOwnerId.execute("761")

        assertThat(result).isNull()
    }

    @Test
    fun `returns null when the owner is null or blank`() {
        assertThat(resolveActionOwnerId.execute(null)).isNull()
        assertThat(resolveActionOwnerId.execute("   ")).isNull()
        verify(getMissionByExternalId, never()).execute("   ")
    }
}
