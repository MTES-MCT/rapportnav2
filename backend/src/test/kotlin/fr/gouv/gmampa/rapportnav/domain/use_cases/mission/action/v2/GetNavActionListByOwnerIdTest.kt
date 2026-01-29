package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetNavActionListByOwnerId
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [GetNavActionListByOwnerId::class])
@ContextConfiguration(classes = [GetNavActionListByOwnerId::class])
class GetNavActionListByOwnerIdTest {

    @Autowired
    private lateinit var getNavActionListByOwnerId: GetNavActionListByOwnerId

    @MockitoBean
    private lateinit var navMissionActionRepository: INavMissionActionRepository

    @Test
    fun `should return empty list if input is null`() {
        assertThat(getNavActionListByOwnerId.execute(ownerId = null)).isEmpty()
        assertThat(getNavActionListByOwnerId.execute(missionId = null)).isEmpty()
    }

    @Test
    fun `should call repository with ownerId and missionId`() {
        val missionId = 761
        val ownerId = UUID.randomUUID()

        val action = MissionActionModel(
            id = UUID.randomUUID(),
            ownerId = ownerId,
            missionId = missionId,
            startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
            endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
            observations = "My beautiful observation",
            isAntiPolDeviceDeployed = true,
            isSimpleBrewingOperationDone = true,
            diversionCarriedOut = true,
            actionType = ActionType.CONTROL,
        )

        `when`(navMissionActionRepository.findByOwnerId(ownerId = ownerId)).thenReturn(listOf(action))
        `when`(navMissionActionRepository.findByMissionId(missionId = missionId)).thenReturn(listOf(action))


        getNavActionListByOwnerId.execute(ownerId = ownerId)
        verify(navMissionActionRepository, times(1)).findByOwnerId(ownerId = ownerId)

        getNavActionListByOwnerId.execute(missionId = missionId)
        verify(navMissionActionRepository, times(1)).findByMissionId(missionId = missionId)
    }

    @Test
    fun `should throw BackendInternalException when repository fails for missionId`() {
        val missionId = 761
        `when`(navMissionActionRepository.findByMissionId(missionId = missionId))
            .thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            getNavActionListByOwnerId.execute(missionId = missionId)
        }
        assertThat(exception.message).contains("GetNavActionListByOwnerId failed for missionId=$missionId")
    }

    @Test
    fun `should throw BackendInternalException when repository fails for ownerId`() {
        val ownerId = UUID.randomUUID()
        `when`(navMissionActionRepository.findByOwnerId(ownerId = ownerId))
            .thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            getNavActionListByOwnerId.execute(ownerId = ownerId)
        }
        assertThat(exception.message).contains("GetNavActionListByOwnerId failed for ownerId=$ownerId")
    }
}
