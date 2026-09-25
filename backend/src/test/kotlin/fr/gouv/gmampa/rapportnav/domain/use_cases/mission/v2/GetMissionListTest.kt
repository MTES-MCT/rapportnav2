package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReportTypeEnum
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionList
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissions
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.MissionListFilter
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.PaginatedMissions
import fr.gouv.gmampa.rapportnav.mocks.mission.EnvMissionMock
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfoEntity2Mock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionNavActionEntityMock
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.isNull
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.UUID

@SpringBootTest(classes = [GetMissionList::class])
class GetMissionListTest {

    @Autowired
    private lateinit var getMissionList: GetMissionList

    @MockitoBean
    private lateinit var getMissions: GetMissions

    private val start = Instant.parse("2025-01-01T00:00:00Z")

    /**
     * Stub the (mocked) windowed orchestration for any page requested by [GetMissionList.execute]. Filtering now
     * happens *inside* [GetMissions.executePaginated] (via the predicate [GetMissionList] passes down), so the mock
     * returns [missions] verbatim; the filter-behavior tests below capture and exercise that predicate instead.
     */
    private fun stubPage(vararg missions: MissionEntity, hasMore: Boolean = false, nextOffset: Int = 15) {
        // Instant? params use anyOrNull() since `endDateTimeUtc` is null here and any() would not match null.
        whenever(
            getMissions.executePaginated(anyOrNull(), anyOrNull(), any(), any(), any(), any())
        ).thenReturn(PaginatedMissions(missions.toList(), hasMore, nextOffset))
    }

    /** Capture the `hasPostFilter` flag and predicate that [GetMissionList] forwards to [GetMissions]. */
    private fun captureFilterArgs(filter: MissionListFilter?): Pair<Boolean, (MissionEntity) -> Boolean> {
        getMissionList.execute(startDateTimeUtc = start, filter = filter)
        val flag = argumentCaptor<Boolean>()
        val predicate = argumentCaptor<(MissionEntity) -> Boolean>()
        verify(getMissions).executePaginated(anyOrNull(), anyOrNull(), any(), any(), flag.capture(), predicate.capture())
        return flag.firstValue to predicate.firstValue
    }

    @Test
    fun `delegates to GetMissions and projects each mission to a light MissionListItem`() {
        val navUuid = UUID.randomUUID()
        val envMission = MissionEntity(
            id = 1,
            data = EnvMissionMock.create(id = 1),
            actions = listOf(MissionNavActionEntityMock.create()),
            generalInfos = MissionGeneralInfoEntity2Mock.create()
        )
        val navMission = MissionEntity(
            idUUID = navUuid,
            data = EnvMissionMock.create(id = null),
            actions = listOf(MissionNavActionEntityMock.create(), MissionNavActionEntityMock.create()),
            generalInfos = MissionGeneralInfoEntity2Mock.create()
        )

        stubPage(envMission, navMission, hasMore = true, nextOffset = 15)

        val result = getMissionList.execute(startDateTimeUtc = start)

        // it delegates to the windowed, compute-only-the-page orchestration (no filter -> no full scan)
        verify(getMissions).executePaginated(eq(start), isNull(), eq(0), eq(15), eq(false), any())

        // paging metadata is forwarded straight from the windowed page
        assertTrue(result.hasMore)
        assertEquals(15, result.nextOffset)
        assertEquals(2, result.items.size)

        val envItem = result.items.first { it.id == 1 }
        assertEquals(1, envItem.actionCount)
        // completeness is computed on the fly, identical to the full Mission response
        assertEquals(envMission.isCompleteForStats(), envItem.completenessForStats)

        val navItem = result.items.first { it.idUUID == navUuid.toString() }
        assertEquals(2, navItem.actionCount)
        assertEquals(navMission.isCompleteForStats(), navItem.completenessForStats)
    }

    // --- filtering -------------------------------------------------------------------------------------

    // ENDED: both dates in the past; UPCOMING: both dates in the future.
    private fun mission(
        id: Int,
        startDateTimeUtc: Instant,
        endDateTimeUtc: Instant,
        reportType: MissionReportTypeEnum = MissionReportTypeEnum.FIELD_REPORT,
    ) = MissionEntity(
        id = id,
        data = EnvMissionMock.create(id = id, startDateTimeUtc = startDateTimeUtc, endDateTimeUtc = endDateTimeUtc),
        actions = listOf(MissionNavActionEntityMock.create()),
        generalInfos = MissionGeneralInfoEntity2Mock.create(
            data = MissionGeneralInfoEntity(
                service = ServiceEntity(id = 1, name = "name", serviceType = ServiceTypeEnum.ULAM),
                isResourcesNotUsed = true,
                missionReportType = reportType,
            )
        )
    )

    private val ended = mission(
        id = 1,
        startDateTimeUtc = Instant.parse("2022-01-02T00:00:00Z"),
        endDateTimeUtc = Instant.parse("2022-01-03T00:00:00Z"),
        reportType = MissionReportTypeEnum.FIELD_REPORT,
    )
    private val upcoming = mission(
        id = 2,
        startDateTimeUtc = Instant.parse("2999-01-02T00:00:00Z"),
        endDateTimeUtc = Instant.parse("2999-01-03T00:00:00Z"),
        reportType = MissionReportTypeEnum.OFFICE_REPORT,
    )

    @Test
    fun `builds a predicate that filters by mission status`() {
        stubPage(ended, upcoming)

        val (hasPostFilter, predicate) = captureFilterArgs(
            MissionListFilter(statuses = listOf(MissionStatusEnum.ENDED))
        )

        assertTrue(hasPostFilter) // an active dimension triggers the full-range scan
        assertTrue(predicate(ended))
        assertFalse(predicate(upcoming))
    }

    @Test
    fun `builds a predicate that filters by report type`() {
        stubPage(ended, upcoming)

        val (hasPostFilter, predicate) = captureFilterArgs(
            MissionListFilter(reportTypes = listOf(MissionReportTypeEnum.OFFICE_REPORT))
        )

        assertTrue(hasPostFilter)
        assertTrue(predicate(upcoming)) // upcoming is the OFFICE_REPORT one
        assertFalse(predicate(ended))
    }

    @Test
    fun `builds a predicate that filters by completeness status`() {
        stubPage(ended, upcoming)
        // drive the filter from the actually-computed completeness so the test stays robust to rule changes
        val endedCompleteness = ended.isCompleteForStats().status!!

        val (hasPostFilter, predicate) = captureFilterArgs(
            MissionListFilter(completenessStatuses = listOf(endedCompleteness))
        )

        assertTrue(hasPostFilter)
        assertTrue(predicate(ended))
    }

    @Test
    fun `builds a predicate that combines dimensions with AND`() {
        stubPage(ended, upcoming)

        // ENDED matches mission 1, but its report type is FIELD_REPORT — asking for OFFICE_REPORT excludes it
        val (hasPostFilter, predicate) = captureFilterArgs(
            MissionListFilter(
                statuses = listOf(MissionStatusEnum.ENDED),
                reportTypes = listOf(MissionReportTypeEnum.OFFICE_REPORT),
            )
        )

        assertTrue(hasPostFilter)
        assertFalse(predicate(ended))
        assertFalse(predicate(upcoming))
    }

    @Test
    fun `forwards the paging metadata from the filtered page verbatim`() {
        // GetMissions now returns a page already filled to the filtered result; the list just forwards its metadata.
        stubPage(ended, hasMore = true, nextOffset = 7)

        val result = getMissionList.execute(
            startDateTimeUtc = start,
            filter = MissionListFilter(statuses = listOf(MissionStatusEnum.ENDED))
        )

        assertEquals(listOf(1), result.items.map { it.id })
        assertTrue(result.hasMore)
        assertEquals(7, result.nextOffset)
    }

    @Test
    fun `a null filter requests no full scan and matches everything`() {
        stubPage(ended, upcoming)

        val (hasPostFilter, predicate) = captureFilterArgs(null)

        assertFalse(hasPostFilter)
        assertTrue(predicate(ended))
        assertTrue(predicate(upcoming))
    }

    @Test
    fun `empty filter lists request no full scan and match everything`() {
        stubPage(ended, upcoming)

        val (hasPostFilter, predicate) = captureFilterArgs(
            MissionListFilter(statuses = emptyList(), reportTypes = emptyList())
        )

        assertFalse(hasPostFilter) // nothing constrained -> keep the cheap windowed fetch
        assertTrue(predicate(ended))
        assertTrue(predicate(upcoming))
    }
}
