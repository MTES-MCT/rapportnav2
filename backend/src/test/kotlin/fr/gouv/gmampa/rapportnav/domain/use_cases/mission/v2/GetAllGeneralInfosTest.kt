package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetAllGeneralInfos
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfoEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.UUID

@SpringBootTest(classes = [GetAllGeneralInfos::class])
class GetAllGeneralInfosTest {

    @Autowired
    private lateinit var getAllGeneralInfos: GetAllGeneralInfos

    @MockitoBean
    private lateinit var repository: IMissionGeneralInfoRepository

    @Test
    fun `should return all general infos`() {
        val entity1 = MissionGeneralInfoEntityMock.create(id = 1, missionId = 100)
        val entity2 = MissionGeneralInfoEntityMock.create(id = 2, missionId = 200)
        val models = listOf(
            entity1.toMissionGeneralInfoModel(),
            entity2.toMissionGeneralInfoModel()
        )

        whenever(repository.findAll()).thenReturn(models)

        val result = getAllGeneralInfos.execute()

        assertThat(result).hasSize(2)
        assertThat(result[0].missionId).isEqualTo(100)
        assertThat(result[1].missionId).isEqualTo(200)
    }

    @Test
    fun `should return empty list when no general infos`() {
        whenever(repository.findAll()).thenReturn(emptyList())

        val result = getAllGeneralInfos.execute()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should propagate BackendInternalException from repository`() {
        val internalException = BackendInternalException(
            message = "Repository error",
            originalException = RuntimeException("Database error")
        )

        whenever(repository.findAll()).thenAnswer { throw internalException }

        val exception = assertThrows<BackendInternalException> {
            getAllGeneralInfos.execute()
        }
        assertThat(exception.message).isEqualTo("Repository error")
    }

    @Test
    fun `should return paginated general infos`() {
        val entity1 = MissionGeneralInfoEntityMock.create(id = 1, missionId = 100)
        val entity2 = MissionGeneralInfoEntityMock.create(id = 2, missionId = 200)
        val models = listOf(
            entity1.toMissionGeneralInfoModel(),
            entity2.toMissionGeneralInfoModel()
        )
        val pageable = PageRequest.of(0, 10)
        val page = PageImpl(models, pageable, 2)

        whenever(repository.findAllPaginated(0, 10)).thenReturn(page)

        val result = getAllGeneralInfos.execute(0, 10)

        assertThat(result.content).hasSize(2)
        assertThat(result.content[0].missionId).isEqualTo(100)
        assertThat(result.content[1].missionId).isEqualTo(200)
        assertThat(result.totalElements).isEqualTo(2)
    }

    @Test
    fun `should return empty page when no paginated general infos`() {
        val pageable = PageRequest.of(0, 10)
        val page = PageImpl(emptyList<fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel>(), pageable, 0)

        whenever(repository.findAllPaginated(0, 10)).thenReturn(page)

        val result = getAllGeneralInfos.execute(0, 10)

        assertThat(result.content).isEmpty()
        assertThat(result.totalElements).isEqualTo(0)
    }

    @Test
    fun `should propagate BackendInternalException from repository for paginated`() {
        val internalException = BackendInternalException(
            message = "Repository error",
            originalException = RuntimeException("Database error")
        )

        whenever(repository.findAllPaginated(0, 10)).thenAnswer { throw internalException }

        val exception = assertThrows<BackendInternalException> {
            getAllGeneralInfos.execute(0, 10)
        }
        assertThat(exception.message).isEqualTo("Repository error")
    }

    @Test
    fun `should search by missionId when search is a number`() {
        val entity = MissionGeneralInfoEntityMock.create(id = 1, missionId = 100)
        val models = listOf(entity.toMissionGeneralInfoModel())
        val pageable = PageRequest.of(0, 10)
        val page = PageImpl(models, pageable, 1)

        whenever(repository.findByMissionIdPaginated(100, 0, 10)).thenReturn(page)

        val result = getAllGeneralInfos.execute(0, 10, "100")

        assertThat(result.content).hasSize(1)
        assertThat(result.content[0].missionId).isEqualTo(100)
    }

    @Test
    fun `should search by missionIdUUID when search is a UUID`() {
        val uuid = UUID.randomUUID()
        val entity = MissionGeneralInfoEntityMock.create(id = 1, missionId = 100, missionIdUUID = uuid)
        val models = listOf(entity.toMissionGeneralInfoModel())
        val pageable = PageRequest.of(0, 10)
        val page = PageImpl(models, pageable, 1)

        whenever(repository.findByMissionIdUUIDPaginated(uuid, 0, 10)).thenReturn(page)

        val result = getAllGeneralInfos.execute(0, 10, uuid.toString())

        assertThat(result.content).hasSize(1)
        assertThat(result.content[0].missionIdUUID).isEqualTo(uuid)
    }

    @Test
    fun `should return all when search is null`() {
        val entity = MissionGeneralInfoEntityMock.create(id = 1, missionId = 100)
        val models = listOf(entity.toMissionGeneralInfoModel())
        val pageable = PageRequest.of(0, 10)
        val page = PageImpl(models, pageable, 1)

        whenever(repository.findAllPaginated(0, 10)).thenReturn(page)

        val result = getAllGeneralInfos.execute(0, 10, null)

        assertThat(result.content).hasSize(1)
    }

    @Test
    fun `should return all when search is blank`() {
        val entity = MissionGeneralInfoEntityMock.create(id = 1, missionId = 100)
        val models = listOf(entity.toMissionGeneralInfoModel())
        val pageable = PageRequest.of(0, 10)
        val page = PageImpl(models, pageable, 1)

        whenever(repository.findAllPaginated(0, 10)).thenReturn(page)

        val result = getAllGeneralInfos.execute(0, 10, "  ")

        assertThat(result.content).hasSize(1)
    }

    @Test
    fun `should return all when search is invalid format`() {
        val entity = MissionGeneralInfoEntityMock.create(id = 1, missionId = 100)
        val models = listOf(entity.toMissionGeneralInfoModel())
        val pageable = PageRequest.of(0, 10)
        val page = PageImpl(models, pageable, 1)

        whenever(repository.findAllPaginated(0, 10)).thenReturn(page)

        val result = getAllGeneralInfos.execute(0, 10, "invalid-search")

        assertThat(result.content).hasSize(1)
    }
}
