package fr.gouv.gmampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.ExtendedEnvMissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetEnvMissionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.AttachControlsToActionControl
import fr.gouv.gmampa.rapportnav.mocks.mission.EnvMissionMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.EnvActionControlMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration
import java.util.*

@SpringBootTest(classes = [GetEnvMissionById::class])
@ContextConfiguration(classes = [GetEnvMissionByIdTest.TestConfig::class])
class GetEnvMissionByIdTest {

    @Configuration
    @EnableCaching
    class TestConfig {
        @Bean
        fun cacheManager(): CacheManager {
            return ConcurrentMapCacheManager("envMission")
        }
    }

    @Autowired
    private lateinit var getEnvMissionById: GetEnvMissionById

    @Autowired
    private lateinit var cacheManager: CacheManager

    @MockBean
    private lateinit var monitorEnvApiRepo: IEnvMissionRepository

    @MockBean
    private lateinit var attachControlsToActionControl: AttachControlsToActionControl

    private val envControlActionId: UUID = UUID.randomUUID()
    private val envControlAction: EnvActionControlEntity = EnvActionControlMock.create(id = envControlActionId)
    private val extendedEnvActionEntity: ExtendedEnvActionEntity = ExtendedEnvActionEntity(
        controlAction = ExtendedEnvActionControlEntity.fromEnvActionControlEntity(envControlAction)
    )
    private val mockMissionEntity: MissionEntity = EnvMissionMock.create(id = 1, envActions = listOf(envControlAction))
    private val extendedEnvMissionEntity: ExtendedEnvMissionEntity =
        ExtendedEnvMissionEntity.fromEnvMission(mockMissionEntity)

    @BeforeEach
    fun setup() {
        cacheManager.getCache("envMission")?.clear()

        // Mock the repository and service methods
        `when`(monitorEnvApiRepo.findMissionById(1)).thenReturn(mockMissionEntity)
        `when`(
            attachControlsToActionControl.toEnvAction(envControlActionId.toString(), extendedEnvActionEntity)
        ).thenReturn(
            extendedEnvActionEntity
        )
    }


    @Test
    fun `test execute returns ExtendedEnvMissionEntity`() {
        val missionId = 1

        // First call
        val result = getEnvMissionById.execute(missionId)
        assertThat(result).isEqualTo(extendedEnvMissionEntity)

        // Verify repository method was called
        verify(monitorEnvApiRepo, times(1)).findMissionById(missionId)

        // Verify cache contains the result
        val cachedResult = cacheManager.getCache("envMission")?.get(missionId)?.get()
        assertThat(cachedResult).isEqualTo(extendedEnvMissionEntity)
    }

    @Test
    fun `test execute uses cache`() {
        val missionId = 1

        // First call to cache the result
        getEnvMissionById.execute(missionId)

        // Second call
        val result = getEnvMissionById.execute(missionId)
        assertThat(result).isEqualTo(extendedEnvMissionEntity)

        // Verify repository method was only called once
        verify(monitorEnvApiRepo, times(1)).findMissionById(missionId)
    }

    @Test
    fun `test execute returns null when repository throws exception`() {
        // Clear the cache and setup repository to throw an exception
        cacheManager.getCache("envMission")?.clear()
        `when`(monitorEnvApiRepo.findMissionById(anyInt())).thenThrow(RuntimeException("API error"))

        // Call execute
        val missionId = 1
        val result = getEnvMissionById.execute(missionId)

        // Verify result is null
        assertThat(result).isNull()

        // Verify repository method was called
        verify(monitorEnvApiRepo, times(1)).findMissionById(missionId)

        // Verify cache doesn't contain a result
        val cachedResult = cacheManager.getCache("envMission")?.get(missionId)?.get()
        assertThat(cachedResult).isNull()
    }

}
