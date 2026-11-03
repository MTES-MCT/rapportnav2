package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.domain.entities.export.ActionDocumentTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.export.ExportAuditEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.ports.ISatiPdfGenerator
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.export.IExportAuditRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.EnableSati
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetFishActionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.ExportSatiPdf
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserFromToken
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionFishActionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.user.UserMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [ExportSatiPdf::class])
class ExportSatiPdfTest {

    @Autowired
    private lateinit var useCase: ExportSatiPdf

    @MockitoBean
    private lateinit var enableSati: EnableSati

    @MockitoBean
    private lateinit var getUserFromToken: GetUserFromToken

    @MockitoBean
    private lateinit var getFishActionById: GetFishActionById

    @MockitoBean
    private lateinit var satiPdfGenerator: ISatiPdfGenerator

    @MockitoBean
    private lateinit var exportAuditRepository: IExportAuditRepository

    private val pdf = "%PDF-1.7".toByteArray()

    @BeforeEach
    fun setUp() {
        `when`(enableSati.execute()).thenReturn(true)
        `when`(getUserFromToken.execute()).thenReturn(UserMock.create(id = 7))
    }

    private fun givenAction(type: MissionActionType = MissionActionType.SEA_CONTROL, isCompleteForStats: Boolean = true) {
        val action = MissionFishActionEntityMock.create(id = 12345, missionId = 761, fishActionType = type)
        action.isCompleteForStats = isCompleteForStats
        `when`(getFishActionById.execute(761, "12345")).thenReturn(action)
        `when`(satiPdfGenerator.generate(action)).thenReturn(pdf)
    }

    @Test
    fun `should return the pdf and its SATI audit without saving it`() {
        givenAction()

        val result = useCase.execute(ownerId = "761", actionId = "12345")

        assertThat(result.content).isEqualTo(pdf)
        assertThat(result.audit).isEqualTo(
            ExportAuditEntity(ownerId = "761", actionId = "12345", documentType = ActionDocumentTypeEnum.SATI)
        )
        verifyNoInteractions(exportAuditRepository)
    }

    @Test
    fun `should save the audit once the pdf has been downloaded`() {
        givenAction()
        val result = useCase.execute(ownerId = "761", actionId = "12345")

        useCase.markAsDownloaded(result)

        verify(exportAuditRepository).save(result.audit)
    }

    @Test
    fun `should audit the resolved ids rather than the raw path values`() {
        val action = MissionFishActionEntityMock.create(id = 12345, missionId = 761)
        action.isCompleteForStats = true
        `when`(getFishActionById.execute(761, "+012345")).thenReturn(action)
        `when`(satiPdfGenerator.generate(action)).thenReturn(pdf)

        val result = useCase.execute(ownerId = "0761", actionId = "+012345")

        assertThat(result.audit.ownerId).isEqualTo("761")
        assertThat(result.audit.actionId).isEqualTo("12345")
    }

    @Test
    fun `should throw INVALID_PARAMETERS without generating when the action is not complete for stats`() {
        givenAction(isCompleteForStats = false)

        val exception = assertThrows<BackendUsageException> { useCase.execute(ownerId = "761", actionId = "12345") }

        assertThat(exception.code).isEqualTo(BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION)
        verifyNoInteractions(satiPdfGenerator, exportAuditRepository)
    }

    @Test
    fun `should throw USER_NOT_ALLOWED without generating when the user has no id`() {
        givenAction()
        `when`(getUserFromToken.execute()).thenReturn(UserMock.create(id = null))

        val exception = assertThrows<BackendUsageException> { useCase.execute(ownerId = "761", actionId = "12345") }

        assertThat(exception.code).isEqualTo(BackendUsageErrorCode.USER_NOT_ALLOWED_TO_PERFORM_EXCEPTION)
        verifyNoInteractions(getFishActionById, satiPdfGenerator, exportAuditRepository)
    }

    @ParameterizedTest
    @EnumSource(value = MissionActionType::class, names = ["SEA_CONTROL", "LAND_CONTROL"])
    fun `should accept fish controls that have a SATI module`(type: MissionActionType) {
        givenAction(type)

        assertThat(useCase.execute(ownerId = "761", actionId = "12345").content).isEqualTo(pdf)
    }

    @ParameterizedTest
    @EnumSource(value = MissionActionType::class, names = ["AIR_CONTROL", "AIR_SURVEILLANCE", "OBSERVATION"])
    fun `should throw COULD_NOT_FIND for a fish action without a SATI module`(type: MissionActionType) {
        givenAction(type)

        val exception = assertThrows<BackendUsageException> { useCase.execute(ownerId = "761", actionId = "12345") }

        assertThat(exception.code).isEqualTo(BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION)
        verifyNoInteractions(satiPdfGenerator, exportAuditRepository)
    }

    @Test
    fun `should throw USER_NOT_ALLOWED without calling MonitorFish when SATI is disabled for the service`() {
        `when`(enableSati.execute()).thenReturn(false)

        val exception = assertThrows<BackendUsageException> { useCase.execute(ownerId = "761", actionId = "12345") }

        assertThat(exception.code).isEqualTo(BackendUsageErrorCode.USER_NOT_ALLOWED_TO_PERFORM_EXCEPTION)
        verifyNoInteractions(getFishActionById, satiPdfGenerator, exportAuditRepository)
    }

    @Test
    fun `should throw COULD_NOT_FIND when the action does not exist`() {
        `when`(getFishActionById.execute(761, "99999")).thenReturn(null)

        val exception = assertThrows<BackendUsageException> { useCase.execute(ownerId = "761", actionId = "99999") }

        assertThat(exception.code).isEqualTo(BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION)
        verifyNoInteractions(satiPdfGenerator, exportAuditRepository)
    }

    @Test
    fun `should throw COULD_NOT_FIND for a non integer owner id without calling MonitorFish`() {
        val exception = assertThrows<BackendUsageException> {
            useCase.execute(ownerId = "0b6c4f6e-6c39-4a8e-9a54-1f6a1b0c1d2e", actionId = "12345")
        }

        assertThat(exception.code).isEqualTo(BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION)
        verifyNoInteractions(getFishActionById, satiPdfGenerator, exportAuditRepository)
    }

    @Test
    fun `should propagate a pdf generation failure`() {
        givenAction()
        `when`(satiPdfGenerator.generate(any())).thenThrow(IllegalStateException("boom"))

        assertThrows<IllegalStateException> { useCase.execute(ownerId = "761", actionId = "12345") }

        verifyNoInteractions(exportAuditRepository)
    }
}
