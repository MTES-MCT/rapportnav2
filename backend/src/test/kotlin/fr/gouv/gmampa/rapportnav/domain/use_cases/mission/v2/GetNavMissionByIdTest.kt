package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetNavMissionById
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionModelMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.Optional
import java.util.UUID

@SpringBootTest(classes = [GetNavMissionById::class])
class GetNavMissionByIdTest {
    @Autowired
    private lateinit var useCase: GetNavMissionById

    @MockitoBean
    private lateinit var repository: IMissionNavRepository

    @Test
    fun `returns null when mission not found`() {
        val missionId = UUID.randomUUID()

        whenever(repository.findById(missionId)).thenReturn(Optional.empty())

        val result = useCase.execute(missionId)

        assertNull(result)
        verify(repository).findById(missionId)
    }

    @Test
    fun `returns MissionNavEntity when mission is found`() {
        val missionId = UUID.randomUUID()
        val model = MissionModelMock.create(id = missionId)
        val expected = MissionNavEntity.fromMissionModel(model)

        whenever(repository.findById(missionId)).thenReturn(Optional.of(model))

        val result = useCase.execute(missionId)

        assertEquals(expected, result)
        verify(repository).findById(missionId)
    }
}
