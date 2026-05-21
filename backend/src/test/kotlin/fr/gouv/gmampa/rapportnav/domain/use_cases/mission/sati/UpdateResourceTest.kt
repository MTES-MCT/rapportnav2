package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.sati

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.controlUnitResource.IEnvControlUnitResourceRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.UpdateResource
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.ResourceInput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.inputs.PatchResourceInput
import fr.gouv.gmampa.rapportnav.mocks.mission.env.ControlUnitResourceEnvMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.never
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [UpdateResource::class])
@ContextConfiguration(classes = [UpdateResource::class])
class UpdateResourceTest {

    @Autowired
    private lateinit var updateResource: UpdateResource

    @MockitoBean
    private lateinit var repository: IEnvControlUnitResourceRepository

    @Test
    fun `should update resource when resource belongs to control unit`() {
        val existingResource = ControlUnitResourceEnvMock.create(
            id = 1,
            controlUnitId = 10,
            name = "Old resource name"
        )
        val updatedResource = ControlUnitResourceEnvMock.create(
            id = 1,
            controlUnitId = 10,
            name = "Old resource name"
        )
        val input = ResourceInput(
            id = 1,
            name = "Updated resource name",
            controlUnitId = 10,
            registrationId = "REG-123",
            radioFrequency = "VHF-16"
        )

        `when`(repository.findAll()).thenReturn(listOf(existingResource))
        `when`(repository.patch(id = anyInt(), resource = any())).thenReturn(updatedResource)

        val result = updateResource.execute(input)

        assertThat(result).isEqualTo(updatedResource)
        verify(repository).findAll()
        verify(repository).patch(
            id = eq(1),
            resource = argThat {
                registrationId == "REG-123" &&
                    radioFrequency == "VHF-16"
            }
        )
    }

    @Test
    fun `should update resource with null optional fields`() {
        val existingResource = ControlUnitResourceEnvMock.create(
            id = 1,
            controlUnitId = 10
        )
        val updatedResource = ControlUnitResourceEnvMock.create(
            id = 1,
            controlUnitId = 10
        )
        val input = ResourceInput(
            id = 1,
            name = "Resource name",
            controlUnitId = 10,
            registrationId = null,
            radioFrequency = null
        )

        `when`(repository.findAll()).thenReturn(listOf(existingResource))
        `when`(repository.patch(id = anyInt(), resource = any())).thenReturn(updatedResource)

        val result = updateResource.execute(input)

        assertThat(result).isEqualTo(updatedResource)
        verify(repository).patch(
            id = eq(1),
            resource = argThat {
                registrationId == null && radioFrequency == null
            }
        )
    }

    @Test
    fun `should throw BackendUsageException when resource belongs to another control unit`() {
        val existingResource = ControlUnitResourceEnvMock.create(
            id = 1,
            controlUnitId = 20
        )
        val input = ResourceInput(
            id = 1,
            name = "Resource name",
            controlUnitId = 10,
            registrationId = "REG-123",
            radioFrequency = "VHF-16"
        )

        `when`(repository.findAll()).thenReturn(listOf(existingResource))

        val exception = assertThrows<BackendUsageException> {
            updateResource.execute(input)
        }

        assertThat(exception.message).contains("Action not allowed for the user on the resource: 1")
        verify(repository).findAll()
        verify(repository, never()).patch(any<Int>(), any<PatchResourceInput>())
    }

    @Test
    fun `should throw BackendUsageException when resource does not exist`() {
        val input = ResourceInput(
            id = 999,
            name = "Unknown resource",
            controlUnitId = 10,
            registrationId = "REG-123",
            radioFrequency = "VHF-16"
        )

        `when`(repository.findAll()).thenReturn(emptyList())

        val exception = assertThrows<BackendUsageException> {
            updateResource.execute(input)
        }

        assertThat(exception.message).contains("Action not allowed for the user on the resource: 999")
        verify(repository).findAll()
        verify(repository, never()).patch(any<Int>(), any<PatchResourceInput>())
    }
}
