package fr.gouv.gmampa.rapportnav.infrastructure.database.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBAgentRoleRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew.JPAAgentRoleRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.assertArg
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [JPAAgentRoleRepository::class])
class JPAAgentRoleRepositoryTest {

    @MockitoBean
    private lateinit var dbAgentRoleRepository: IDBAgentRoleRepository

    private lateinit var jpaAgentRoleRepository: JPAAgentRoleRepository

    private val agentRoleModel = AgentRoleModel(
        id = 1,
        title = "Commandant",
        priority = 1
    )

    @BeforeEach
    fun setUp() {
        jpaAgentRoleRepository = JPAAgentRoleRepository(dbAgentRoleRepository)
    }

    @Test
    fun `findById should return role when found`() {
        `when`(dbAgentRoleRepository.findById(1)).thenReturn(Optional.of(agentRoleModel))

        val result = jpaAgentRoleRepository.findById(1)

        assertThat(result).isNotNull
        assertThat(result?.id).isEqualTo(1)
        assertThat(result?.title).isEqualTo("Commandant")
        verify(dbAgentRoleRepository).findById(1)
    }

    @Test
    fun `findById should return null when not found`() {
        `when`(dbAgentRoleRepository.findById(999)).thenReturn(Optional.empty())

        val result = jpaAgentRoleRepository.findById(999)

        assertThat(result).isNull()
        verify(dbAgentRoleRepository).findById(999)
    }

    @Test
    fun `findById should throw BackendInternalException on error`() {
        `when`(dbAgentRoleRepository.findById(1)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaAgentRoleRepository.findById(1)
        }
        assertThat(exception.message).contains("findById failed")
    }

    @Test
    fun `findAll should return list of roles`() {
        val roles = listOf(
            agentRoleModel,
            AgentRoleModel(id = 2, title = "Second capitaine", priority = 2)
        )
        `when`(dbAgentRoleRepository.findAll()).thenReturn(roles)

        val result = jpaAgentRoleRepository.findAll()

        assertThat(result).hasSize(2)
        assertThat(result[0].title).isEqualTo("Commandant")
        assertThat(result[1].title).isEqualTo("Second capitaine")
        verify(dbAgentRoleRepository).findAll()
    }

    @Test
    fun `findAll should return empty list when no roles`() {
        `when`(dbAgentRoleRepository.findAll()).thenReturn(emptyList())

        val result = jpaAgentRoleRepository.findAll()

        assertThat(result).isEmpty()
        verify(dbAgentRoleRepository).findAll()
    }

    @Test
    fun `findAll should throw BackendInternalException on error`() {
        `when`(dbAgentRoleRepository.findAll()).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaAgentRoleRepository.findAll()
        }
        assertThat(exception.message).contains("findAll failed")
    }

    @Test
    fun `save should return saved role`() {
        `when`(dbAgentRoleRepository.save(any<AgentRoleModel>())).thenReturn(agentRoleModel)

        val result = jpaAgentRoleRepository.save(agentRoleModel)

        assertThat(result).isNotNull
        assertThat(result.title).isEqualTo("Commandant")
        verify(dbAgentRoleRepository).save(agentRoleModel)
    }

    @Test
    fun `save should throw BackendUsageException on invalid data`() {
        `when`(dbAgentRoleRepository.save(any<AgentRoleModel>())).thenThrow(InvalidDataAccessApiUsageException("Invalid data"))

        val exception = assertThrows<BackendUsageException> {
            jpaAgentRoleRepository.save(agentRoleModel)
        }
        assertThat(exception.message).contains("save failed")
    }

    @Test
    fun `save should throw BackendInternalException on database error`() {
        `when`(dbAgentRoleRepository.save(any<AgentRoleModel>())).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaAgentRoleRepository.save(agentRoleModel)
        }
        assertThat(exception.message).contains("save failed")
    }

    @Test
    fun `deleteById should soft delete role by setting deletedAt`() {
        `when`(dbAgentRoleRepository.findById(1)).thenReturn(Optional.of(agentRoleModel))
        `when`(dbAgentRoleRepository.save(any<AgentRoleModel>())).thenReturn(agentRoleModel)

        jpaAgentRoleRepository.deleteById(1)

        verify(dbAgentRoleRepository).findById(1)
        verify(dbAgentRoleRepository).save(assertArg { role ->
            assertThat(role.deletedAt).isNotNull()
        })
    }

    @Test
    fun `deleteById should do nothing when role not found`() {
        `when`(dbAgentRoleRepository.findById(999)).thenReturn(Optional.empty())

        jpaAgentRoleRepository.deleteById(999)

        verify(dbAgentRoleRepository).findById(999)
    }

    @Test
    fun `deleteById should throw BackendUsageException on invalid data`() {
        `when`(dbAgentRoleRepository.findById(1)).thenReturn(Optional.of(agentRoleModel))
        `when`(dbAgentRoleRepository.save(any<AgentRoleModel>())).thenThrow(InvalidDataAccessApiUsageException("Invalid data"))

        val exception = assertThrows<BackendUsageException> {
            jpaAgentRoleRepository.deleteById(1)
        }
        assertThat(exception.message).contains("deleteById failed")
    }

    @Test
    fun `deleteById should throw BackendInternalException on database error`() {
        `when`(dbAgentRoleRepository.findById(1)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaAgentRoleRepository.deleteById(1)
        }
        assertThat(exception.message).contains("deleteById failed")
    }
}
