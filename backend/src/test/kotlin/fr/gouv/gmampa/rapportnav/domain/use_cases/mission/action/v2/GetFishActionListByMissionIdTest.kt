package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IFishActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetFishActionListByMissionId
import fr.gouv.gmampa.rapportnav.mocks.mission.action.FishActionControlMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*

@SpringBootTest(classes = [GetFishActionListByMissionId::class])
class GetFishActionListByMissionIdTest {

    @Autowired
    private lateinit var getFishActionListByMissionId: GetFishActionListByMissionId

    @MockitoBean
    private lateinit var fishActionRepo: IFishActionRepository

    @Test
    fun `execute should return fish actions from Fish`() {
        val missionId = 761
        // Given
        val actionId = UUID.randomUUID().hashCode()
        val action = FishActionControlMock.create(
            id = actionId,
        )

        // Mock behavior of findMissionById to return a MissionEntity
        `when`(fishActionRepo.findFishActions(missionId)).thenReturn(listOf(action))

        // When
        val result = getFishActionListByMissionId.execute(missionId)

        // Then
        assertNotNull(result)
    }

    @Test
    fun `should return empty list when missionId is null`() {
        val result = getFishActionListByMissionId.execute(null)
        assertThat(result).isEmpty()
    }

    @Test
    fun `should throw BackendInternalException when repository fails`() {
        val missionId = 761
        `when`(fishActionRepo.findFishActions(missionId)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            getFishActionListByMissionId.execute(missionId)
        }
        assertThat(exception.message).contains("GetFishActionListByMissionId failed for missionId=$missionId")
    }
}
