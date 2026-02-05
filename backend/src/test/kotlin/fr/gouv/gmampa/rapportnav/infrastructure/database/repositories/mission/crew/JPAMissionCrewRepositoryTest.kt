package fr.gouv.gmampa.rapportnav.infrastructure.database.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.MissionCrewModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBAgentRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBAgentRoleRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBMissionCrewRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew.JPAMissionCrewRepository
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [JPAMissionCrewRepository::class])
class JPAMissionCrewRepositoryTest {

    @MockitoBean
    private lateinit var dbMissionCrewRepository: IDBMissionCrewRepository

    @MockitoBean
    private lateinit var dbAgentRepository: IDBAgentRepository

    @MockitoBean
    private lateinit var dbAgentRoleRepository: IDBAgentRoleRepository

    private lateinit var jpaMissionCrewRepository: JPAMissionCrewRepository

    private val service = ServiceEntityMock.create(id = 1, name = "Service 1")

    private val agent = AgentModel(
        id = 1,
        lastName = "Doe",
        firstName = "John",
        role = AgentRoleModel(id = 1, title = "Commander"),
        service = service.toServiceModel()
    )

    private val missionCrewModel = MissionCrewModel(
        id = 1,
        agent = agent,
        missionId = 100,
        role = AgentRoleModel(id = 1, title = "Commander")
    )

    @BeforeEach
    fun setUp() {
        jpaMissionCrewRepository = JPAMissionCrewRepository(
            dbMissionCrewRepository,
            dbAgentRepository,
            dbAgentRoleRepository
        )
    }

    @Test
    fun `findByMissionId should return crew list`() {
        `when`(dbMissionCrewRepository.findByMissionId(100)).thenReturn(listOf(missionCrewModel))

        val result = jpaMissionCrewRepository.findByMissionId(100)

        assertThat(result).hasSize(1)
        assertThat(result[0].missionId).isEqualTo(100)
        verify(dbMissionCrewRepository).findByMissionId(100)
    }

    @Test
    fun `findByMissionId should throw BackendInternalException on error`() {
        `when`(dbMissionCrewRepository.findByMissionId(100)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaMissionCrewRepository.findByMissionId(100)
        }
        assertThat(exception.message).contains("findByMissionId failed")
    }

    @Test
    fun `findByMissionIdUUID should return crew list`() {
        val uuid = UUID.randomUUID()
        `when`(dbMissionCrewRepository.findByMissionIdUUID(uuid)).thenReturn(listOf(missionCrewModel))

        val result = jpaMissionCrewRepository.findByMissionIdUUID(uuid)

        assertThat(result).hasSize(1)
        verify(dbMissionCrewRepository).findByMissionIdUUID(uuid)
    }

    @Test
    fun `findByMissionIdUUID should throw BackendInternalException on error`() {
        val uuid = UUID.randomUUID()
        `when`(dbMissionCrewRepository.findByMissionIdUUID(uuid)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaMissionCrewRepository.findByMissionIdUUID(uuid)
        }
        assertThat(exception.message).contains("findByMissionIdUUID failed")
    }

    @Test
    fun `deleteById should return true when crew exists`() {
        `when`(dbMissionCrewRepository.findById(1)).thenReturn(Optional.of(missionCrewModel))

        val result = jpaMissionCrewRepository.deleteById(1)

        assertThat(result).isTrue()
        verify(dbMissionCrewRepository).deleteById(1)
    }

    @Test
    fun `deleteById should throw BackendUsageException when crew not found`() {
        `when`(dbMissionCrewRepository.findById(999)).thenReturn(Optional.empty())

        val exception = assertThrows<BackendUsageException> {
            jpaMissionCrewRepository.deleteById(999)
        }
        assertThat(exception.message).contains("crew not found")
    }

    @Test
    fun `deleteById should throw BackendInternalException on database error`() {
        `when`(dbMissionCrewRepository.findById(1)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaMissionCrewRepository.deleteById(1)
        }
        assertThat(exception.message).contains("deleteById failed")
    }
}
