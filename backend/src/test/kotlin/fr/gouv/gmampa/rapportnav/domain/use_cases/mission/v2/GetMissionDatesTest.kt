package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry.GetInquiryById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetNavMissionById2
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionNavEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.UUID

@SpringBootTest(classes = [GetMissionDates::class])
class GetMissionDatesTest {

    @Autowired
    private lateinit var getMissionDates: GetMissionDates

    @MockitoBean
    private lateinit var getNavMissionById2: GetNavMissionById2

    @MockitoBean
    private lateinit var getEnvMissionById2: GetEnvMissionById2

    @MockitoBean
    private lateinit var getInquiryById: GetInquiryById

    @Test
    fun `returns null when both missionId and ownerId are null`() {
        val result = getMissionDates.execute(null, null)
        assertThat(result).isNull()
    }

    @Test
    fun `returns dates from nav mission when ownerId is provided`() {
        val ownerId = UUID.randomUUID()
        val missionStart = Instant.parse("2024-01-01T00:00:00Z")
        val missionEnd = Instant.parse("2024-01-31T23:59:59Z")

        val navMission = MissionNavEntityMock.create(
            id = ownerId,
            startDateTimeUtc = missionStart,
            endDateTimeUtc = missionEnd
        )

        whenever(getNavMissionById2.execute(ownerId)).thenReturn(navMission)

        val result = getMissionDates.execute(null, ownerId)

        assertThat(result).isNotNull
        assertThat(result?.startDateTimeUtc).isEqualTo(missionStart)
        assertThat(result?.endDateTimeUtc).isEqualTo(missionEnd)
    }

    @Test
    fun `returns dates from env mission when missionId is provided and ownerId is null`() {
        val missionId = 123
        val missionStart = Instant.parse("2024-01-01T00:00:00Z")
        val missionEnd = Instant.parse("2024-01-31T23:59:59Z")

        val envMission = MissionEnvEntity(
            id = missionId,
            startDateTimeUtc = missionStart,
            endDateTimeUtc = missionEnd,
            missionSource = MissionSourceEnum.MONITORENV
        )

        whenever(getEnvMissionById2.execute(missionId)).thenReturn(envMission)

        val result = getMissionDates.execute(missionId, null)

        assertThat(result).isNotNull
        assertThat(result?.startDateTimeUtc).isEqualTo(missionStart)
        assertThat(result?.endDateTimeUtc).isEqualTo(missionEnd)
    }

    @Test
    fun `returns dates from nav mission when both ownerId and missionId are provided`() {
        val ownerId = UUID.randomUUID()
        val missionId = 123
        val navMissionStart = Instant.parse("2024-01-01T00:00:00Z")
        val navMissionEnd = Instant.parse("2024-01-31T23:59:59Z")

        val navMission = MissionNavEntityMock.create(
            id = ownerId,
            startDateTimeUtc = navMissionStart,
            endDateTimeUtc = navMissionEnd
        )

        whenever(getNavMissionById2.execute(ownerId)).thenReturn(navMission)

        val result = getMissionDates.execute(missionId, ownerId)

        assertThat(result).isNotNull
        assertThat(result?.startDateTimeUtc).isEqualTo(navMissionStart)
        assertThat(result?.endDateTimeUtc).isEqualTo(navMissionEnd)
    }

    @Test
    fun `returns null when nav mission not found and missionId is null`() {
        val ownerId = UUID.randomUUID()

        whenever(getNavMissionById2.execute(ownerId)).thenReturn(null)

        val result = getMissionDates.execute(null, ownerId)

        assertThat(result).isNull()
    }

    @Test
    fun `falls back to env mission when nav mission not found`() {
        val ownerId = UUID.randomUUID()
        val missionId = 123
        val envMissionStart = Instant.parse("2024-02-01T00:00:00Z")
        val envMissionEnd = Instant.parse("2024-02-28T23:59:59Z")

        val envMission = MissionEnvEntity(
            id = missionId,
            startDateTimeUtc = envMissionStart,
            endDateTimeUtc = envMissionEnd,
            missionSource = MissionSourceEnum.MONITORENV
        )

        whenever(getNavMissionById2.execute(ownerId)).thenReturn(null)
        whenever(getEnvMissionById2.execute(missionId)).thenReturn(envMission)

        val result = getMissionDates.execute(missionId, ownerId)

        assertThat(result).isNotNull
        assertThat(result?.startDateTimeUtc).isEqualTo(envMissionStart)
        assertThat(result?.endDateTimeUtc).isEqualTo(envMissionEnd)
    }

    @Test
    fun `returns dates from inquiry when inquiryId is provided`() {
        val inquiryId = UUID.randomUUID()
        val inquiryStart = Instant.parse("2024-03-01T00:00:00Z")
        val inquiryEnd = Instant.parse("2024-03-31T23:59:59Z")

        val inquiry = InquiryEntity(
            id = inquiryId,
            startDateTimeUtc = inquiryStart,
            endDateTimeUtc = inquiryEnd
        )

        whenever(getInquiryById.execute(inquiryId)).thenReturn(inquiry)

        val result = getMissionDates.execute(null, null, inquiryId)

        assertThat(result).isNotNull
        assertThat(result?.startDateTimeUtc).isEqualTo(inquiryStart)
        assertThat(result?.endDateTimeUtc).isEqualTo(inquiryEnd)
    }

    @Test
    fun `inquiry takes priority over nav and env missions`() {
        val inquiryId = UUID.randomUUID()
        val ownerId = UUID.randomUUID()
        val missionId = 123
        val inquiryStart = Instant.parse("2024-03-01T00:00:00Z")
        val inquiryEnd = Instant.parse("2024-03-31T23:59:59Z")

        val inquiry = InquiryEntity(
            id = inquiryId,
            startDateTimeUtc = inquiryStart,
            endDateTimeUtc = inquiryEnd
        )

        whenever(getInquiryById.execute(inquiryId)).thenReturn(inquiry)

        val result = getMissionDates.execute(missionId, ownerId, inquiryId)

        assertThat(result).isNotNull
        assertThat(result?.startDateTimeUtc).isEqualTo(inquiryStart)
        assertThat(result?.endDateTimeUtc).isEqualTo(inquiryEnd)
    }

    @Test
    fun `falls back to nav mission when inquiry not found`() {
        val inquiryId = UUID.randomUUID()
        val ownerId = UUID.randomUUID()
        val navMissionStart = Instant.parse("2024-01-01T00:00:00Z")
        val navMissionEnd = Instant.parse("2024-01-31T23:59:59Z")

        val navMission = MissionNavEntityMock.create(
            id = ownerId,
            startDateTimeUtc = navMissionStart,
            endDateTimeUtc = navMissionEnd
        )

        whenever(getInquiryById.execute(inquiryId)).thenReturn(null)
        whenever(getNavMissionById2.execute(ownerId)).thenReturn(navMission)

        val result = getMissionDates.execute(null, ownerId, inquiryId)

        assertThat(result).isNotNull
        assertThat(result?.startDateTimeUtc).isEqualTo(navMissionStart)
        assertThat(result?.endDateTimeUtc).isEqualTo(navMissionEnd)
    }

    @Test
    fun `falls back to env mission when inquiry and nav mission not found`() {
        val inquiryId = UUID.randomUUID()
        val ownerId = UUID.randomUUID()
        val missionId = 123
        val envMissionStart = Instant.parse("2024-02-01T00:00:00Z")
        val envMissionEnd = Instant.parse("2024-02-28T23:59:59Z")

        val envMission = MissionEnvEntity(
            id = missionId,
            startDateTimeUtc = envMissionStart,
            endDateTimeUtc = envMissionEnd,
            missionSource = MissionSourceEnum.MONITORENV
        )

        whenever(getInquiryById.execute(inquiryId)).thenReturn(null)
        whenever(getNavMissionById2.execute(ownerId)).thenReturn(null)
        whenever(getEnvMissionById2.execute(missionId)).thenReturn(envMission)

        val result = getMissionDates.execute(missionId, ownerId, inquiryId)

        assertThat(result).isNotNull
        assertThat(result?.startDateTimeUtc).isEqualTo(envMissionStart)
        assertThat(result?.endDateTimeUtc).isEqualTo(envMissionEnd)
    }
}
