package fr.gouv.gmampa.rapportnav.infrastructure.cache

import fr.gouv.dgampa.rapportnav.infrastructure.cache.CaffeineConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCache
import java.util.concurrent.TimeUnit

@SpringBootTest(classes = [CaffeineConfiguration::class])
class CaffeineConfigurationTest {

    @Autowired
    private lateinit var cacheManager: CacheManager

    @Autowired
    private lateinit var caffeineConfiguration: CaffeineConfiguration

    @Test
    fun `test cache manager configuration`() {
        val cacheNames = listOf(
            caffeineConfiguration.vessels,
            caffeineConfiguration.natinfs,
            caffeineConfiguration.controlPlans,
            caffeineConfiguration.envMissions,
            caffeineConfiguration.envMission,
            caffeineConfiguration.fishActions,
            caffeineConfiguration.envActionList,
            caffeineConfiguration.fishActionList,
            caffeineConfiguration.envMission2
        )

        cacheNames.forEach { cacheName ->
            val cache = cacheManager.getCache(cacheName)
            assertThat(cache)
                .isNotNull
                .isInstanceOf(CaffeineCache::class.java)
        }

        assertThat(cacheManager.cacheNames.toSet())
            .hasSize(9)
            .isEqualTo(cacheNames.toSet())
    }

    @Test
    fun `verify long-term cache expiration`() {
        val longTermCaches = listOf(
            caffeineConfiguration.natinfs,
            caffeineConfiguration.controlPlans
        )

        longTermCaches.forEach { cacheName ->
            val cache = cacheManager.getCache(cacheName) as CaffeineCache
            val caffeineCache = cache.nativeCache
            val expireAfterWrite = caffeineCache.policy().expireAfterWrite().orElse(null)

            assertThat(expireAfterWrite)
                .isNotNull()

            val duration = expireAfterWrite?.getExpiresAfter(TimeUnit.NANOSECONDS)
            assertThat(duration)
                .isEqualTo(TimeUnit.DAYS.toNanos(7))
        }
    }

    @Test
    fun `verify short-term cache expiration`() {
        val shortTermCaches = listOf(
            caffeineConfiguration.envMissions,
            caffeineConfiguration.envMission,
            caffeineConfiguration.fishActions
        )

        shortTermCaches.forEach { cacheName ->
            val cache = cacheManager.getCache(cacheName) as CaffeineCache
            val caffeineCache = cache.nativeCache
            val expireAfterWrite = caffeineCache.policy().expireAfterWrite().orElse(null)

            assertThat(expireAfterWrite)
                .isNotNull()

            val duration = expireAfterWrite?.getExpiresAfter(TimeUnit.NANOSECONDS)
            assertThat(duration)
                .isEqualTo(TimeUnit.MINUTES.toNanos(5))
        }
    }
}
