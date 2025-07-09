package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.controlUnit.IEnvControlUnitRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.CreateEnvMission
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [CreateEnvMission::class])
class CreateEnvMissionTest {

    @MockitoBean
    private lateinit var monitorEnvRepo: IEnvMissionRepository

    @MockitoBean
    private lateinit var monitorEnvControlUnitRepo: IEnvControlUnitRepository

    @Autowired
    private lateinit var createEnvMission: CreateEnvMission

    @Test
    fun `should throw exception when control units is null`() {
        Mockito.`when`(monitorEnvControlUnitRepo.findAll()).thenReturn(listOf())
        val exception = assertThrows<Exception> {
            createEnvMission.getControlUnits(controlUnitIds = listOf())
        }
        assertEquals("CreateEnvMission : controlUnits is empty for this user", exception.message)
    }

    @Test
    fun `should remove resources from control units`() {
        val controlUnit  = LegacyControlUnitEntity(
            id = 1,
            isArchived=  true,
            name = "control unit 1",
            resources = mutableListOf(
                LegacyControlUnitResourceEntity(
                    controlUnitId = 1,
                    name = "resource1",
                    id = 11
                )
            )
        )
        Mockito.`when`(monitorEnvControlUnitRepo.findAll()).thenReturn(listOf(controlUnit))
        val response = createEnvMission.getControlUnits(controlUnitIds = listOf(1))

        assertThat(response).isNotNull
        assertThat(response.get(0).id).isEqualTo(controlUnit.id)
        assertThat(response.get(0).name).isEqualTo(controlUnit.name)
        assertThat(response.get(0).isArchived).isEqualTo(controlUnit.isArchived)
        assertThat(response.get(0).resources).isEmpty()

    }
}
