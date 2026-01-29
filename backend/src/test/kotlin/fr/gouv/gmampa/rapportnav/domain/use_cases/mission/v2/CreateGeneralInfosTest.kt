package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.CreateGeneralInfos
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfo2Mock
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfoEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*

@SpringBootTest(classes = [CreateGeneralInfos::class])
class CreateGeneralInfosTest {

    @Autowired
    private lateinit var createGeneralInfos: CreateGeneralInfos

    @MockitoBean
    private lateinit var generalInfosRepository: IMissionGeneralInfoRepository

    @Test
    fun `should create general info with missionId`() {
        val missionId = 123
        val generalInfo2 = MissionGeneralInfo2Mock.create(missionId = missionId)
        val entity = MissionGeneralInfoEntityMock.create(id = 1, missionId = missionId)
        val model = entity.toMissionGeneralInfoModel()

        whenever(generalInfosRepository.save(any())).thenReturn(model)

        val result = createGeneralInfos.execute(
            missionId = missionId,
            generalInfo2 = generalInfo2
        )

        assertThat(result).isNotNull
        assertThat(result.data?.missionId).isEqualTo(missionId)
    }

    @Test
    fun `should create general info with missionIdUUID`() {
        val missionIdUUID = UUID.randomUUID()
        val generalInfo2 = MissionGeneralInfo2Mock.create(missionIdUUID = missionIdUUID)
        val entity = MissionGeneralInfoEntityMock.create(id = 1, missionIdUUID = missionIdUUID)
        val model = entity.toMissionGeneralInfoModel()

        whenever(generalInfosRepository.save(any())).thenReturn(model)

        val result = createGeneralInfos.execute(
            missionIdUUID = missionIdUUID,
            generalInfo2 = generalInfo2
        )

        assertThat(result).isNotNull
        assertThat(result.data?.missionIdUUID).isEqualTo(missionIdUUID)
    }

    @Test
    fun `should create general info with service`() {
        val missionId = 123
        val service = ServiceEntityMock.create(id = 10, name = "Test Service")
        val generalInfo2 = MissionGeneralInfo2Mock.create(missionId = missionId)
        val entity = MissionGeneralInfoEntityMock.create(id = 1, missionId = missionId, service = service)
        val model = entity.toMissionGeneralInfoModel()

        whenever(generalInfosRepository.save(any())).thenReturn(model)

        val result = createGeneralInfos.execute(
            missionId = missionId,
            generalInfo2 = generalInfo2,
            service = service
        )

        assertThat(result).isNotNull
        assertThat(result.data?.service?.id).isEqualTo(10)
    }

    @Test
    fun `should propagate BackendInternalException from repository`() {
        val missionId = 123
        val generalInfo2 = MissionGeneralInfo2Mock.create(missionId = missionId)
        val internalException = BackendInternalException(
            message = "Repository error",
            originalException = RuntimeException("Database error")
        )

        whenever(generalInfosRepository.save(any())).thenAnswer { throw internalException }

        val exception = assertThrows<BackendInternalException> {
            createGeneralInfos.execute(missionId = missionId, generalInfo2 = generalInfo2)
        }
        assertThat(exception.message).isEqualTo("Repository error")
    }
}
