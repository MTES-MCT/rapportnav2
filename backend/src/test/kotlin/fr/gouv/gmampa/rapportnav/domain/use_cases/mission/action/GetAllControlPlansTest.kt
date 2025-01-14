package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ControlPlansEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetAllControlPlans
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

@SpringBootTest(classes = [GetAllControlPlans::class])
@ContextConfiguration(classes = [GetAllControlPlansTest.TestConfig::class])
class GetAllControlPlansTest {

    @Configuration
    @EnableCaching
    class TestConfig {
        @Bean
        fun cacheManager(): CacheManager {
            return ConcurrentMapCacheManager("controlPlans")
        }
    }

    @Autowired
    private lateinit var getAllControlPlans: GetAllControlPlans

    @Autowired
    private lateinit var cacheManager: CacheManager

    @MockitoBean
    private lateinit var envMissionRepository: IEnvMissionRepository

    private val mockControlPlans = ControlPlansEntity(
        themes = listOf(/* ... */),
        subThemes = listOf(/* ... */),
        tags = listOf(/* ... */)
    )

    @BeforeEach
    fun setup() {
        cacheManager.getCache("controlPlans")?.clear()
        `when`(envMissionRepository.findAllControlPlans()).thenReturn(mockControlPlans)
    }

    @Test
    fun `test execute caches results`() {
        // First call
        val result1 = getAllControlPlans.execute()
        assertThat(result1).isNotNull
        assertThat(result1).isEqualTo(mockControlPlans)

        // Verify repository method was called
        verify(envMissionRepository, times(1)).findAllControlPlans()

        // Second call
        val result2 = getAllControlPlans.execute()
        assertThat(result2).isEqualTo(result1)

        // Verify repository method was not called again
        verify(envMissionRepository, times(1)).findAllControlPlans()
    }

    @Test
    fun `test execute returns null when repository throws exception`() {
        // Clear the cache and setup repository to throw an exception
        cacheManager.getCache("controlPlans")?.clear()
        `when`(envMissionRepository.findAllControlPlans()).thenThrow(RuntimeException("API error"))

        // Call execute
        val result = getAllControlPlans.execute()

        // Verify result is null
        assertThat(result).isNull()

        // Verify repository method was called
        verify(envMissionRepository, times(1)).findAllControlPlans()

        // Verify cache doesn't contain a result
        val cachedResult = cacheManager.getCache("controlPlans")?.get("execute")?.get()
        assertThat(cachedResult).isNull()
    }
}
