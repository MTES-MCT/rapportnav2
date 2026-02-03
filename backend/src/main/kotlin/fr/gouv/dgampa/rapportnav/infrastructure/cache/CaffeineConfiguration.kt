package fr.gouv.dgampa.rapportnav.infrastructure.cache

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.Ticker
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@EnableCaching
@Configuration
class CaffeineConfiguration {

    // long term caches for very static data
    val natinfs = "natinfs"

    // short term caches to reduce API calls
    val envMissions = "envMissions"
    val envMission = "envMission"
    val fishActions = "fishActions"
    val envMission2 = "envMission2"
    val envActionList = "envActionList"
    val fishActionList = "fishActionList"
    val vessels = "vessels"

    @Bean
    fun cacheManager(ticker: Ticker): CacheManager? {

        // long term caches for very static data
        val vesselsCache = buildCache(vessels, ticker, TimeUnit.DAYS, 7)
        val natinfsCache = buildCache(natinfs, ticker, TimeUnit.DAYS, 7)

        // short term caches for Missions and Actions
        val envMissionsCache = buildCache(envMissions, ticker, TimeUnit.MINUTES, 5)
        val envMissionCache = buildCache(envMission, ticker, TimeUnit.MINUTES, 5)
        val fishActionsCache = buildCache(fishActions, ticker, TimeUnit.MINUTES, 5)


        // short term caches for Missions and Actions
        val envMissionCache2 = buildCache(envMission2, ticker, TimeUnit.MINUTES, 5)
        val envActionListCache = buildCache(envActionList, ticker, TimeUnit.MINUTES, 5)
        val fishActionListCache = buildCache(fishActionList, ticker, TimeUnit.MINUTES, 5)


        val manager = SimpleCacheManager()
        manager.setCaches(
            listOf(
                // long term caches
                vesselsCache,
                natinfsCache,

                // short term caches
                envMissionsCache,
                envMissionCache,
                fishActionsCache,
                envMissionCache2,
                envActionListCache,
                fishActionListCache
            ),
        )

        return manager
    }

    private fun buildCache(name: String, ticker: Ticker, timeUnit: TimeUnit, durationInUnit: Int): CaffeineCache {
        return CaffeineCache(
            name,
            Caffeine.newBuilder()
                .expireAfterWrite(durationInUnit.toLong(), timeUnit)
                .recordStats()
                .ticker(ticker)
                .build(),
        )
    }

    @Bean
    fun ticker(): Ticker? {
        return Ticker.systemTicker()
    }
}
