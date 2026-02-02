package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionPassengerEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsCrewByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetServiceByControlUnit
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.CreateGeneralInfos
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetGeneralInfo2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.passenger.GetMissionPassengers
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfoEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*

@SpringBootTest(classes = [GetGeneralInfo2::class])
class GetGeneralInfo2Test {

    @Autowired
    private lateinit var getGeneralInfo2: GetGeneralInfo2

    @MockitoBean
    private lateinit var getServiceById: GetServiceById

    @MockitoBean
    private lateinit var createGeneralInfos: CreateGeneralInfos

    @MockitoBean
    private lateinit var getServiceByControlUnit: GetServiceByControlUnit

    @MockitoBean
    private lateinit var getAgentsCrewByMissionId: GetAgentsCrewByMissionId

    @MockitoBean
    private lateinit var getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId

    @MockitoBean
    private lateinit var getMissionPassengers: GetMissionPassengers

    @Test
    fun `should return general info for missionId when exists`() {
        val missionId = 123
        val entity = MissionGeneralInfoEntityMock.create(id = 1, missionId = missionId)

        whenever(getServiceByControlUnit.execute(anyOrNull())).thenReturn(emptyList())
        whenever(getAgentsCrewByMissionId.execute(missionId)).thenReturn(emptyList())
        whenever(getMissionPassengers.execute(missionId)).thenReturn(emptyList())
        whenever(getMissionGeneralInfoByMissionId.execute(missionId = missionId)).thenReturn(entity)

        val result = getGeneralInfo2.execute(missionId = missionId)

        assertThat(result).isNotNull
        assertThat(result.data?.missionId).isEqualTo(missionId)
    }

    @Test
    fun `should create general info for missionId when not exists`() {
        val missionId = 123
        val service = ServiceEntityMock.create(id = 10)
        val createdEntity = MissionGeneralInfoEntityMock.create(id = 1, missionId = missionId, service = service)

        whenever(getServiceByControlUnit.execute(anyOrNull())).thenReturn(listOf(service))
        whenever(getAgentsCrewByMissionId.execute(missionId)).thenReturn(emptyList())
        whenever(getMissionPassengers.execute(missionId)).thenReturn(emptyList())
        whenever(getMissionGeneralInfoByMissionId.execute(missionId = missionId)).thenReturn(null)
        whenever(getServiceById.execute(10)).thenReturn(service)
        whenever(createGeneralInfos.execute(
            missionId = eq(missionId),
            missionIdUUID = anyOrNull(),
            generalInfo2 = any(),
            service = anyOrNull()
        )).thenReturn(fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2(data = createdEntity))

        val result = getGeneralInfo2.execute(missionId = missionId)

        assertThat(result).isNotNull
        assertThat(result.data?.missionId).isEqualTo(missionId)
    }

    @Test
    fun `should return general info for missionIdUUID when exists`() {
        val missionIdUUID = UUID.randomUUID()
        val entity = MissionGeneralInfoEntityMock.create(id = 1, missionIdUUID = missionIdUUID)

        whenever(getServiceByControlUnit.execute(anyOrNull())).thenReturn(emptyList())
        whenever(getAgentsCrewByMissionId.execute(missionIdUUID = missionIdUUID)).thenReturn(emptyList())
        whenever(getMissionPassengers.execute(missionIdUUID = missionIdUUID)).thenReturn(emptyList())
        whenever(getMissionGeneralInfoByMissionId.execute(missionIdUUID = missionIdUUID)).thenReturn(entity)

        val result = getGeneralInfo2.execute(missionIdUUID = missionIdUUID)

        assertThat(result).isNotNull
        assertThat(result.data?.missionIdUUID).isEqualTo(missionIdUUID)
    }

    @Test
    fun `should include services from control units`() {
        val missionId = 123
        val entity = MissionGeneralInfoEntityMock.create(id = 1, missionId = missionId)
        val services = listOf(
            ServiceEntityMock.create(id = 10, name = "Service 1"),
            ServiceEntityMock.create(id = 20, name = "Service 2")
        )
        val controlUnits = listOf(
            LegacyControlUnitEntity(id = 100, name = "Unit 1", administration = "Admin 1")
        )

        whenever(getServiceByControlUnit.execute(controlUnits)).thenReturn(services)
        whenever(getAgentsCrewByMissionId.execute(missionId)).thenReturn(emptyList())
        whenever(getMissionPassengers.execute(missionId)).thenReturn(emptyList())
        whenever(getMissionGeneralInfoByMissionId.execute(missionId = missionId)).thenReturn(entity)

        val result = getGeneralInfo2.execute(missionId = missionId, controlUnits = controlUnits)

        assertThat(result.services).hasSize(2)
        assertThat(result.services?.get(0)?.name).isEqualTo("Service 1")
    }

    @Test
    fun `should propagate BackendInternalException from dependencies`() {
        val missionId = 123
        val internalException = BackendInternalException(
            message = "Dependency error",
            originalException = RuntimeException("Error")
        )

        whenever(getServiceByControlUnit.execute(anyOrNull())).thenReturn(emptyList())
        whenever(getAgentsCrewByMissionId.execute(missionId)).thenReturn(emptyList())
        whenever(getMissionPassengers.execute(missionId)).thenReturn(emptyList())
        whenever(getMissionGeneralInfoByMissionId.execute(missionId = missionId)).thenAnswer { throw internalException }

        val exception = assertThrows<BackendInternalException> {
            getGeneralInfo2.execute(missionId = missionId)
        }
        assertThat(exception.message).isEqualTo("Dependency error")
    }
}
