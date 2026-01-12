package fr.gouv.gmampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IFishActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetFishActionsByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.AttachControlsToActionControl
import fr.gouv.gmampa.rapportnav.mocks.mission.action.FishActionControlMock
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

@SpringBootTest(classes = [GetFishActionsByMissionId::class])
@ContextConfiguration(classes = [GetFishActionsByMissionIdTest.TestConfig::class])
class GetFishActionsByMissionIdTest {

    @Configuration
    @EnableCaching
    class TestConfig {
        @Bean
        fun cacheManager(): CacheManager {
            return ConcurrentMapCacheManager("fishActions")
        }
    }

    @Autowired
    private lateinit var getFishActionsByMissionId: GetFishActionsByMissionId

    @Autowired
    private lateinit var cacheManager: CacheManager

    @MockitoBean
    private lateinit var fishActionRepo: IFishActionRepository

    @MockitoBean
    private lateinit var attachControlsToActionControl: AttachControlsToActionControl

    private val mockFishMissionActions = listOf(
        FishActionControlMock.create(id = 1),
        FishActionControlMock.create(id = 2),
    )

    val expectedReturn = listOf(
        ExtendedFishActionEntity.fromMissionAction(FishActionControlMock.create(id = 1)),
        ExtendedFishActionEntity.fromMissionAction(FishActionControlMock.create(id = 2)),
    )

    @BeforeEach
    fun setup() {
        cacheManager.getCache("fishActions")?.clear()
        `when`(fishActionRepo.findFishActions(anyInt())).thenReturn(mockFishMissionActions)
        `when`(
            attachControlsToActionControl.toFishAction(
                "1",
                ExtendedFishActionEntity.fromMissionAction(FishActionControlMock.create(id = 1))
            )
        ).thenReturn(
            ExtendedFishActionEntity.fromMissionAction(FishActionControlMock.create(id = 1))
        )
        `when`(
            attachControlsToActionControl.toFishAction(
                "2",
                ExtendedFishActionEntity.fromMissionAction(FishActionControlMock.create(id = 2))
            )
        ).thenReturn(
            ExtendedFishActionEntity.fromMissionAction(FishActionControlMock.create(id = 2))
        )
    }

    @Test
    fun `test execute caches results`() {
        val missionId = 123

        // First call
        val result1 = getFishActionsByMissionId.execute(missionId)
        assertThat(result1).isNotNull
        assertThat(result1).hasSize(2)
        assertThat(result1).isEqualTo(expectedReturn)

        // Verify repository method was called
        verify(fishActionRepo, times(1)).findFishActions(missionId)

        // Second call
        val result2 = getFishActionsByMissionId.execute(missionId)
        assertThat(result2).isEqualTo(result1)

        // Verify repository method was not called again
        verify(fishActionRepo, times(1)).findFishActions(missionId)
    }

    @Test
    fun `test execute returns empty list when repository throws exception`() {
        val missionId = 123

        // Clear the cache and setup repository to throw an exception
        cacheManager.getCache("fishActions")?.clear()
        `when`(fishActionRepo.findFishActions(anyInt())).thenThrow(RuntimeException("API error"))

        // Call execute
        val result = getFishActionsByMissionId.execute(missionId)

        // Verify result is an empty list
        assertThat(result).isEmpty()

        // Verify repository method was called
        verify(fishActionRepo, times(1)).findFishActions(missionId)

        // Verify cache doesn't contain a result
        val cachedResult = cacheManager.getCache("fishActions")?.get(missionId)?.get()
        assertThat(cachedResult).isEqualTo(listOf<ExtendedFishActionEntity>())
    }
}
