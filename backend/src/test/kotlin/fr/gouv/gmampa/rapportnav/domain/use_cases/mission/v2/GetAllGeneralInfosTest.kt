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
import org.springframework.test.context.bean.override.mockito.MockitoBean

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
}
