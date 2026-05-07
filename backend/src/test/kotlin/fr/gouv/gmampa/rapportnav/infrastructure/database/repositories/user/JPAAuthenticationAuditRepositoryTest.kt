package fr.gouv.gmampa.rapportnav.infrastructure.database.repositories.user

import fr.gouv.dgampa.rapportnav.domain.entities.user.AuthEventTypeEnum
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.user.AuthenticationAuditModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.user.IDBAuthenticationAuditRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.user.JPAAuthenticationAuditRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [JPAAuthenticationAuditRepository::class])
class JPAAuthenticationAuditRepositoryTest {

    @MockitoBean
    private lateinit var dbRepository: IDBAuthenticationAuditRepository

    private lateinit var jpaRepository: JPAAuthenticationAuditRepository

    private val auditModel = AuthenticationAuditModel(
        id = 1,
        userId = 42,
        email = "test@example.com",
        eventType = AuthEventTypeEnum.LOGIN_SUCCESS,
        ipAddress = "127.0.0.1",
        userAgent = "TestAgent",
        success = true,
        timestamp = Instant.now()
    )

    @BeforeEach
    fun setUp() {
        jpaRepository = JPAAuthenticationAuditRepository(dbRepository)
    }

    @Test
    fun `save should return saved audit`() {
        `when`(dbRepository.save(any<AuthenticationAuditModel>())).thenReturn(auditModel)

        val result = jpaRepository.save(auditModel)

        assertThat(result).isNotNull
        assertThat(result.id).isEqualTo(1)
        assertThat(result.email).isEqualTo("test@example.com")
        assertThat(result.eventType).isEqualTo(AuthEventTypeEnum.LOGIN_SUCCESS)
        verify(dbRepository).save(auditModel)
    }

    @Test
    fun `findAllPaginated should return page of audits`() {
        val pageable = PageRequest.of(0, 10)
        val page = PageImpl(listOf(auditModel), pageable, 1)
        `when`(dbRepository.findAllByOrderByTimestampDesc(pageable)).thenReturn(page)

        val result = jpaRepository.findAllPaginated(0, 10)

        assertThat(result.content).hasSize(1)
        assertThat(result.content[0].email).isEqualTo("test@example.com")
        assertThat(result.totalElements).isEqualTo(1)
        verify(dbRepository).findAllByOrderByTimestampDesc(pageable)
    }

    @Test
    fun `findAllPaginated should return empty page when no audits`() {
        val pageable = PageRequest.of(0, 10)
        val emptyPage = PageImpl<AuthenticationAuditModel>(emptyList(), pageable, 0)
        `when`(dbRepository.findAllByOrderByTimestampDesc(pageable)).thenReturn(emptyPage)

        val result = jpaRepository.findAllPaginated(0, 10)

        assertThat(result.content).isEmpty()
        assertThat(result.totalElements).isEqualTo(0)
    }
}