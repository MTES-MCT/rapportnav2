package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IFishActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetFishActionListByMissionId
import fr.gouv.gmampa.rapportnav.mocks.mission.action.FishActionControlMock
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
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
        val missionId = "761"
        // Given
        val actionId = UUID.randomUUID().hashCode()
        val action = FishActionControlMock.create(
            id = actionId,
        )

        // Mock behavior of findMissionById to return a MissionEntity
        `when`(fishActionRepo.findFishActions(missionId)).thenReturn(listOf(action))
        getFishActionListByMissionId = GetFishActionListByMissionId(fishActionRepo)

        // When
        val result = getFishActionListByMissionId.execute(missionId)

        // Then
        assertNotNull(result)
    }

}
