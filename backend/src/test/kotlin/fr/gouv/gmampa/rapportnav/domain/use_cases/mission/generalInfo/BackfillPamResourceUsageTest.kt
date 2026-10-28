package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlUnitResourceType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.PamBackfillSkipReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.PamMetricsMergeResult
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.PamResourceMetrics
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.ResourceUsageEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IResourceUsageRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.BackfillPamResourceUsage
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionByExternalId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.controlUnitResource.GetPamShipResource
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfoEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionModelMock
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.eq
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.UUID

@SpringBootTest(classes = [BackfillPamResourceUsage::class])
class BackfillPamResourceUsageTest {

    @Autowired
    private lateinit var backfill: BackfillPamResourceUsage

    @MockitoBean
    private lateinit var generalInfoRepository: IMissionGeneralInfoRepository

    @MockitoBean
    private lateinit var resourceUsageRepository: IResourceUsageRepository

    @MockitoBean
    private lateinit var getPamShipResource: GetPamShipResource

    @MockitoBean
    private lateinit var getMissionByExternalId: GetMissionByExternalId

    private val pamIris = ServiceEntityMock.create(id = 5, serviceType = ServiceTypeEnum.PAM, controlUnits = listOf(10404))
    private val irisShip = LegacyControlUnitResourceEntity(
        id = 582, controlUnitId = 10404, name = "Iris", type = ControlUnitResourceType.PATROL_BOAT
    )
    @BeforeEach
    fun setup() {
        whenever(getPamShipResource.resolve(pamIris)).thenReturn(irisShip)
        whenever(resourceUsageRepository.mergePamMetrics(any(), any(), any())).thenReturn(PamMetricsMergeResult.CREATED)
    }

    private fun generalInfo(
        id: Int,
        missionIdUUID: UUID? = UUID.randomUUID(),
        missionId: Int? = null,
        service: ServiceEntity? = pamIris,
        distance: Float? = 12.3f
    ): MissionGeneralInfoEntity = MissionGeneralInfoEntityMock.create(
        id = id,
        missionId = missionId,
        missionIdUUID = missionIdUUID,
        service = service,
        distanceInNauticalMiles = distance,
        consumedGOInLiters = 3400f
    )

    private fun givenPages(vararg pages: List<MissionGeneralInfoEntity>, pageSize: Int = 500) {
        val total = pages.sumOf { it.size }.toLong()
        pages.forEachIndexed { index, rows ->
            whenever(generalInfoRepository.findWithPamMetricsPaginated(index, pageSize)).thenReturn(
                PageImpl(rows.map { it.toMissionGeneralInfoModel() }, PageRequest.of(index, pageSize), total)
            )
        }
    }

    @Test
    fun `copies the PAM metrics onto the ship row of a UUID mission`() {
        val row = generalInfo(id = 1)
        givenPages(listOf(row))

        val report = backfill.execute(dryRun = false)

        verify(resourceUsageRepository).mergePamMetrics(
            row.missionIdUUID!!, 582, PamResourceMetrics(distanceInNauticalMiles = 12.3, consumedGOInLiters = 3400.0)
        )
        assertThat(report.dryRun).isFalse()
        assertThat(report.scanned).isEqualTo(1)
        assertThat(report.created).isEqualTo(1)
        assertThat(report.skipped).isEmpty()
    }

    @Test
    fun `resolves the mission UUID of an Int mission from its mirror row`() {
        val local = MissionModelMock.create()
        givenPages(listOf(generalInfo(id = 1, missionIdUUID = null, missionId = 123)))
        whenever(getMissionByExternalId.execute("123")).thenReturn(local)

        backfill.execute(dryRun = false)

        verify(resourceUsageRepository).mergePamMetrics(eq(local.id), eq(582), any())
    }

    @Test
    fun `reports every skip reason without writing those rows`() {
        val duplicated = UUID.randomUUID()
        val gyptis = ServiceEntityMock.create(id = 7, serviceType = ServiceTypeEnum.PAM, controlUnits = listOf(10141))
        givenPages(
            listOf(
                generalInfo(id = 1, service = null),
                generalInfo(id = 2, service = gyptis),
                generalInfo(id = 3, missionIdUUID = null, missionId = 999),
                generalInfo(id = 4, missionIdUUID = duplicated),
                generalInfo(id = 5, missionIdUUID = duplicated),
            )
        )
        whenever(getPamShipResource.resolve(gyptis)).thenReturn(null)
        whenever(getMissionByExternalId.execute("999")).thenReturn(null)

        val report = backfill.execute(dryRun = false)

        assertThat(report.scanned).isEqualTo(5)
        assertThat(report.created).isEqualTo(1)
        assertThat(report.skipped.associate { it.generalInfoId to it.reason }).isEqualTo(
            mapOf(
                1 to PamBackfillSkipReason.NO_SERVICE,
                2 to PamBackfillSkipReason.NO_SHIP,
                3 to PamBackfillSkipReason.NO_MISSION_UUID,
                5 to PamBackfillSkipReason.DUPLICATE_MISSION,
            )
        )
        assertThat(report.skipped.first { it.generalInfoId == 3 }.missionId).isEqualTo(999)
        verify(resourceUsageRepository, times(1)).mergePamMetrics(any(), any(), any())
    }

    @Test
    fun `reports a failed write and carries on with the next rows`() {
        val failing = generalInfo(id = 1)
        val ok = generalInfo(id = 2)
        givenPages(listOf(failing, ok))
        whenever(resourceUsageRepository.mergePamMetrics(eq(failing.missionIdUUID!!), any(), any()))
            .thenThrow(BackendInternalException("FK violation"))

        val report = backfill.execute(dryRun = false)

        assertThat(report.created).isEqualTo(1)
        val skip = report.skipped.single()
        assertThat(skip.generalInfoId).isEqualTo(1)
        assertThat(skip.reason).isEqualTo(PamBackfillSkipReason.WRITE_FAILED)
        assertThat(skip.detail).contains("FK violation")
    }

    @Test
    fun `dry run writes nothing and reports what a real run would do`() {
        val toCreate = generalInfo(id = 1)
        val toUpdate = generalInfo(id = 2)
        val upToDate = generalInfo(id = 3)
        givenPages(listOf(toCreate, toUpdate, upToDate))
        whenever(resourceUsageRepository.findByMissionId(toCreate.missionIdUUID!!)).thenReturn(listOf())
        whenever(resourceUsageRepository.findByMissionId(toUpdate.missionIdUUID!!)).thenReturn(
            listOf(ResourceUsageEntity(missionIdUUID = toUpdate.missionIdUUID!!, resourceId = 582, distanceInNauticalMiles = 1.0))
        )
        whenever(resourceUsageRepository.findByMissionId(upToDate.missionIdUUID!!)).thenReturn(
            listOf(
                ResourceUsageEntity(
                    missionIdUUID = upToDate.missionIdUUID!!,
                    resourceId = 582,
                    nbEngineHours = 4.0,
                    distanceInNauticalMiles = 12.3,
                    consumedGOInLiters = 3400.0
                )
            )
        )

        val report = backfill.execute()

        assertThat(report.dryRun).isTrue()
        assertThat(report.created).isEqualTo(1)
        assertThat(report.updated).isEqualTo(1)
        assertThat(report.unchanged).isEqualTo(1)
        verify(resourceUsageRepository, never()).mergePamMetrics(any(), any(), any())
    }

    @Test
    fun `a rerun counts already migrated rows as unchanged`() {
        givenPages(listOf(generalInfo(id = 1), generalInfo(id = 2)))
        whenever(resourceUsageRepository.mergePamMetrics(any(), any(), any())).thenReturn(PamMetricsMergeResult.UNCHANGED)

        val report = backfill.execute(dryRun = false)

        assertThat(report.created).isZero()
        assertThat(report.updated).isZero()
        assertThat(report.unchanged).isEqualTo(2)
    }

    @Test
    fun `walks every page, resolving each service's ship once`() {
        givenPages(listOf(generalInfo(id = 1), generalInfo(id = 2)), listOf(generalInfo(id = 3)), pageSize = 2)

        val report = backfill.execute(dryRun = false, pageSize = 2)

        assertThat(report.scanned).isEqualTo(3)
        assertThat(report.created).isEqualTo(3)
        verify(getPamShipResource, times(1)).resolve(anyOrNull())
    }

    @Test
    fun `an unresolved ship is also resolved only once per service`() {
        val gyptis = ServiceEntityMock.create(id = 7, serviceType = ServiceTypeEnum.PAM, controlUnits = listOf(10141))
        givenPages(listOf(generalInfo(id = 1, service = gyptis), generalInfo(id = 2, service = gyptis)))
        whenever(getPamShipResource.resolve(gyptis)).thenReturn(null)

        val report = backfill.execute(dryRun = false)

        assertThat(report.skipped.map { it.reason }).containsOnly(PamBackfillSkipReason.NO_SHIP)
        verify(getPamShipResource, times(1)).resolve(anyOrNull())
    }

    @Test
    fun `aborts when MonitorEnv can't be reached instead of reporting rows as NO_SHIP`() {
        givenPages(listOf(generalInfo(id = 1), generalInfo(id = 2)))
        whenever(getPamShipResource.resolve(pamIris)).thenThrow(BackendInternalException("MonitorEnv down"))

        assertThrows<BackendInternalException> { backfill.execute(dryRun = false) }
        verify(resourceUsageRepository, never()).mergePamMetrics(any(), any(), any())
    }
}
