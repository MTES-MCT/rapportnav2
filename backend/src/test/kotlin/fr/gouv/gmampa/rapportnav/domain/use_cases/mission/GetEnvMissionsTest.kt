package fr.gouv.gmampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetEnvMissions
import fr.gouv.gmampa.rapportnav.mocks.mission.EnvMissionMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [GetEnvMissions::class])
@ContextConfiguration(classes = [GetEnvMissionsTest.TestConfig::class])
class GetEnvMissionsTest {

    @Configuration
    @EnableCaching
    class TestConfig {
        @Bean
        fun cacheManager(): CacheManager {
            return ConcurrentMapCacheManager("envMissions")
        }
    }

    @Autowired
    private lateinit var getEnvMissions: GetEnvMissions

    @Autowired
    private lateinit var cacheManager: CacheManager

    @MockitoBean
    private lateinit var envMissionRepository: IEnvMissionRepository

    private val mockEnvMissions = listOf(
        EnvMissionMock.create(id = 1),
        EnvMissionMock.create(id = 2),
        EnvMissionMock.create(id = 3)
    )

    @BeforeEach
    fun setup() {
        cacheManager.getCache("envMissions")?.clear()
        `when`(
            envMissionRepository.findAllMissions(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        ).thenReturn(mockEnvMissions)
    }

    @Test
    fun `test execute caches results`() {
        // First call
        val result1 = getEnvMissions.execute()
        assertThat(result1).isNotNull
        assertThat(result1).isEqualTo(mockEnvMissions)

        // Verify repository method was called
        verify(envMissionRepository, times(1)).findAllMissions(
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any()
        )

        // Second call
        val result2 = getEnvMissions.execute()
        assertThat(result2).isEqualTo(result1)

        // Verify repository method was not called again
        verify(envMissionRepository, times(1)).findAllMissions(
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any()
        )
    }

    @Test
    fun `test execute returns null when repository throws exception`() {
        // Clear the cache and setup repository to throw an exception
        cacheManager.getCache("envMissions")?.clear()
        `when`(
            envMissionRepository.findAllMissions(
                any(),
                any(),
                any(),
                any(), any(),
                any(),
                any(),
                any(),
                any()
            )
        ).thenThrow(RuntimeException("API error"))

        // Call execute
        val result = getEnvMissions.execute()

        // Verify result is null
        assertThat(result).isNull()

        // Verify repository method was called
        verify(envMissionRepository, times(1)).findAllMissions(
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any(),
            any()
        )

        // Verify cache doesn't contain a result
        val cachedResult = cacheManager.getCache("envMissions")?.get("execute")?.get()
        assertThat(cachedResult).isNull()
    }
}
