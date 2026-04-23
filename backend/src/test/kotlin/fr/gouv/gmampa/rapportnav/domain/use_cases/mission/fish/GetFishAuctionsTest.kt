package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.fish

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.FacadeTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.FishAuctionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import java.time.Instant
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.fish.IFishAuctionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.fish.GetFishAuctions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [GetFishAuctions::class])
class GetFishAuctionsTest {

    @Autowired
    private lateinit var getFishAuctions: GetFishAuctions

    @MockitoBean
    private lateinit var fishAuctionRepository: IFishAuctionRepository

    @Test
    fun `should return all fish auctions`() {
        val auctions = listOf(
            FishAuctionEntity(id = 1, name = "Boulogne-sur-Mer", facade = FacadeTypeEnum.MEMN),
            FishAuctionEntity(id = 2, name = "Saint-Malo", facade = FacadeTypeEnum.NAMO),
            FishAuctionEntity(id = 3, name = "La Rochelle", facade = FacadeTypeEnum.SA)
        )

        `when`(fishAuctionRepository.findAll()).thenReturn(auctions)

        val result = getFishAuctions.execute()

        assertThat(result).hasSize(3)
        assertThat(result[0].id).isEqualTo(1)
        assertThat(result[0].name).isEqualTo("Boulogne-sur-Mer")
        assertThat(result[0].facade).isEqualTo(FacadeTypeEnum.MEMN)
        assertThat(result[1].facade).isEqualTo(FacadeTypeEnum.NAMO)
        assertThat(result[2].facade).isEqualTo(FacadeTypeEnum.SA)
    }

    @Test
    fun `should return empty list when no auctions`() {
        `when`(fishAuctionRepository.findAll()).thenReturn(emptyList())

        val result = getFishAuctions.execute()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should exclude deleted auctions by default`() {
        val auctions = listOf(
            FishAuctionEntity(id = 1, name = "Boulogne-sur-Mer", facade = FacadeTypeEnum.MEMN),
            FishAuctionEntity(id = 2, name = "Dunkerque", facade = FacadeTypeEnum.MEMN, deletedAt = Instant.now()),
            FishAuctionEntity(id = 3, name = "Saint-Malo", facade = FacadeTypeEnum.NAMO)
        )

        `when`(fishAuctionRepository.findAll()).thenReturn(auctions)

        val result = getFishAuctions.execute()

        assertThat(result).hasSize(2)
        assertThat(result.map { it.name }).containsExactly("Boulogne-sur-Mer", "Saint-Malo")
    }

    @Test
    fun `should include deleted auctions when includeDeleted is true`() {
        val auctions = listOf(
            FishAuctionEntity(id = 1, name = "Boulogne-sur-Mer", facade = FacadeTypeEnum.MEMN),
            FishAuctionEntity(id = 2, name = "Dunkerque", facade = FacadeTypeEnum.MEMN, deletedAt = Instant.now()),
            FishAuctionEntity(id = 3, name = "Saint-Malo", facade = FacadeTypeEnum.NAMO)
        )

        `when`(fishAuctionRepository.findAll()).thenReturn(auctions)

        val result = getFishAuctions.execute(includeDeleted = true)

        assertThat(result).hasSize(3)
    }

    @Test
    fun `should propagate BackendInternalException from repository`() {
        val internalException = BackendInternalException(
            message = "Repository error",
            originalException = RuntimeException("Database error")
        )

        `when`(fishAuctionRepository.findAll()).thenAnswer { throw internalException }

        val exception = assertThrows<BackendInternalException> {
            getFishAuctions.execute()
        }
        assertThat(exception.message).isEqualTo("Repository error")
    }
}
