package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry.GetInquiryById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetEnvMissionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetNavMissionById
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [GetMissionDates::class])
class GetMissionDatesTest {

    @Autowired
    private lateinit var getMissionDates: GetMissionDates

    @MockitoBean
    private lateinit var getNavMissionById: GetNavMissionById

    @MockitoBean
    private lateinit var getEnvMissionById: GetEnvMissionById

    @MockitoBean
    private lateinit var getInquiryById: GetInquiryById

    @Test
    fun `should return null when all parameters are null`() {
        val result = getMissionDates.execute(missionId = null, ownerId = null, inquiryId = null)
        assertNull(result)
    }

    @Test
    fun `should return dates from inquiry when inquiryId is provided and inquiry exists`() {
        val inquiryId = UUID.randomUUID()
        val start = Instant.parse("2024-01-01T00:00:00Z")
        val end = Instant.parse("2024-01-02T00:00:00Z")

        val inquiry = InquiryEntity(
            id = inquiryId,
            startDateTimeUtc = start,
            endDateTimeUtc = end
        )

        Mockito.`when`(getInquiryById.execute(id = inquiryId)).thenReturn(inquiry)

        val result = getMissionDates.execute(missionId = 1, ownerId = UUID.randomUUID(), inquiryId = inquiryId)

        assertNotNull(result)
        assertEquals(start, result?.startDateTimeUtc)
        assertEquals(end, result?.endDateTimeUtc)
    }

    @Test
    fun `should fallback to nav mission when inquiryId is null and ownerId is provided`() {
        val ownerId = UUID.randomUUID()
        val start = Instant.parse("2024-02-01T00:00:00Z")
        val end = Instant.parse("2024-02-02T00:00:00Z")

        val navMission = MissionNavEntity(
            id = ownerId,
            startDateTimeUtc = start,
            endDateTimeUtc = end,
            isDeleted = false,
            serviceId = 1,
            missionSource = MissionSourceEnum.RAPPORT_NAV
        )

        Mockito.`when`(getNavMissionById.execute(ownerId)).thenReturn(navMission)

        val result = getMissionDates.execute(missionId = null, ownerId = ownerId)

        assertNotNull(result)
        assertEquals(start, result?.startDateTimeUtc)
        assertEquals(end, result?.endDateTimeUtc)
    }

    @Test
    fun `should fallback to env mission when inquiryId and ownerId are null`() {
        val missionId = 42
        val start = Instant.parse("2024-03-01T00:00:00Z")
        val end = Instant.parse("2024-03-02T00:00:00Z")

        val envMission = MissionEnvEntity(
            id = missionId,
            startDateTimeUtc = start,
            endDateTimeUtc = end,
            isDeleted = false,
            isUnderJdp = false,
            isGeometryComputedFromControls = false,
            missionSource = MissionSourceEnum.RAPPORT_NAV,
            controlUnits = listOf(),
            missionTypes = listOf(MissionTypeEnum.SEA),
            hasMissionOrder = false
        )

        Mockito.`when`(getEnvMissionById.execute(missionId)).thenReturn(envMission)

        val result = getMissionDates.execute(missionId = missionId, ownerId = null)

        assertNotNull(result)
        assertEquals(start, result?.startDateTimeUtc)
        assertEquals(end, result?.endDateTimeUtc)
    }

    @Test
    fun `should fallback to nav mission when inquiry is not found`() {
        val inquiryId = UUID.randomUUID()
        val ownerId = UUID.randomUUID()
        val start = Instant.parse("2024-04-01T00:00:00Z")
        val end = Instant.parse("2024-04-02T00:00:00Z")

        Mockito.`when`(getInquiryById.execute(id = inquiryId)).thenReturn(null)

        val navMission = MissionNavEntity(
            id = ownerId,
            startDateTimeUtc = start,
            endDateTimeUtc = end,
            isDeleted = false,
            serviceId = 1,
            missionSource = MissionSourceEnum.RAPPORT_NAV
        )

        Mockito.`when`(getNavMissionById.execute(ownerId)).thenReturn(navMission)

        val result = getMissionDates.execute(missionId = null, ownerId = ownerId, inquiryId = inquiryId)

        assertNotNull(result)
        assertEquals(start, result?.startDateTimeUtc)
        assertEquals(end, result?.endDateTimeUtc)
    }

    @Test
    fun `should fallback to env mission when inquiry and nav mission are not found`() {
        val inquiryId = UUID.randomUUID()
        val ownerId = UUID.randomUUID()
        val missionId = 99
        val start = Instant.parse("2024-05-01T00:00:00Z")
        val end = Instant.parse("2024-05-02T00:00:00Z")

        Mockito.`when`(getInquiryById.execute(id = inquiryId)).thenReturn(null)
        Mockito.`when`(getNavMissionById.execute(ownerId)).thenReturn(null)

        val envMission = MissionEnvEntity(
            id = missionId,
            startDateTimeUtc = start,
            endDateTimeUtc = end,
            isDeleted = false,
            isUnderJdp = false,
            isGeometryComputedFromControls = false,
            missionSource = MissionSourceEnum.RAPPORT_NAV,
            controlUnits = listOf(),
            missionTypes = listOf(MissionTypeEnum.SEA),
            hasMissionOrder = false
        )

        Mockito.`when`(getEnvMissionById.execute(missionId)).thenReturn(envMission)

        val result = getMissionDates.execute(missionId = missionId, ownerId = ownerId, inquiryId = inquiryId)

        assertNotNull(result)
        assertEquals(start, result?.startDateTimeUtc)
        assertEquals(end, result?.endDateTimeUtc)
    }

    @Test
    fun `should return null when all sources return null`() {
        val inquiryId = UUID.randomUUID()
        val ownerId = UUID.randomUUID()
        val missionId = 100

        Mockito.`when`(getInquiryById.execute(id = inquiryId)).thenReturn(null)
        Mockito.`when`(getNavMissionById.execute(ownerId)).thenReturn(null)
        Mockito.`when`(getEnvMissionById.execute(missionId)).thenReturn(null)

        val result = getMissionDates.execute(missionId = missionId, ownerId = ownerId, inquiryId = inquiryId)

        assertNull(result)
    }

    @Test
    fun `should prioritize inquiry over nav and env missions`() {
        val inquiryId = UUID.randomUUID()
        val ownerId = UUID.randomUUID()
        val missionId = 50

        val inquiryStart = Instant.parse("2024-01-01T00:00:00Z")
        val inquiryEnd = Instant.parse("2024-01-02T00:00:00Z")
        val navStart = Instant.parse("2024-06-01T00:00:00Z")
        val navEnd = Instant.parse("2024-06-02T00:00:00Z")

        val inquiry = InquiryEntity(
            id = inquiryId,
            startDateTimeUtc = inquiryStart,
            endDateTimeUtc = inquiryEnd
        )

        val navMission = MissionNavEntity(
            id = ownerId,
            startDateTimeUtc = navStart,
            endDateTimeUtc = navEnd,
            isDeleted = false,
            serviceId = 1,
            missionSource = MissionSourceEnum.RAPPORT_NAV
        )

        Mockito.`when`(getInquiryById.execute(id = inquiryId)).thenReturn(inquiry)
        Mockito.`when`(getNavMissionById.execute(ownerId)).thenReturn(navMission)

        val result = getMissionDates.execute(missionId = missionId, ownerId = ownerId, inquiryId = inquiryId)

        assertEquals(inquiryStart, result?.startDateTimeUtc)
        assertEquals(inquiryEnd, result?.endDateTimeUtc)
        Mockito.verify(getNavMissionById, Mockito.never()).execute(ownerId)
    }

    @Test
    fun `isMissionFinished should return true when end date is in the past`() {
        val inquiryId = UUID.randomUUID()
        val pastEnd = Instant.parse("2020-01-01T00:00:00Z")

        val inquiry = InquiryEntity(
            id = inquiryId,
            startDateTimeUtc = Instant.parse("2019-12-01T00:00:00Z"),
            endDateTimeUtc = pastEnd
        )

        Mockito.`when`(getInquiryById.execute(id = inquiryId)).thenReturn(inquiry)

        val result = getMissionDates.execute(missionId = null, ownerId = null, inquiryId = inquiryId)

        assertNotNull(result)
        assertTrue(result!!.isMissionFinished())
    }

    @Test
    fun `isMissionFinished should return false when end date is in the future`() {
        val inquiryId = UUID.randomUUID()
        val futureEnd = Instant.now().plusSeconds(86400)

        val inquiry = InquiryEntity(
            id = inquiryId,
            startDateTimeUtc = Instant.now(),
            endDateTimeUtc = futureEnd
        )

        Mockito.`when`(getInquiryById.execute(id = inquiryId)).thenReturn(inquiry)

        val result = getMissionDates.execute(missionId = null, ownerId = null, inquiryId = inquiryId)

        assertNotNull(result)
        assertFalse(result!!.isMissionFinished())
    }

    @Test
    fun `isMissionFinished should return false when end date is null`() {
        val inquiryId = UUID.randomUUID()

        val inquiry = InquiryEntity(
            id = inquiryId,
            startDateTimeUtc = Instant.now(),
            endDateTimeUtc = null
        )

        Mockito.`when`(getInquiryById.execute(id = inquiryId)).thenReturn(inquiry)

        val result = getMissionDates.execute(missionId = null, ownerId = null, inquiryId = inquiryId)

        assertNotNull(result)
        assertFalse(result!!.isMissionFinished())
    }
}
