package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.ActionDocumentTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.ExportAuditEntity
import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiDocumentEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.ControlCheck
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.GearControl
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.FishInfraction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.NatinfEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.AuthorityType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.ContactEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiInspectorEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiModuleType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiPartyEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiVesselEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.ControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.ports.ISatiPdfGenerator
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.export.IExportAuditRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.EnableSati
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetFishActionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.ExportSatiPdf
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.GetNatinfs
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
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
import org.mockito.kotlin.argumentCaptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.UUID

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
    private lateinit var getServiceById: GetServiceById

    @MockitoBean
    private lateinit var getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId

    @MockitoBean
    private lateinit var agentRepository: IAgentRepository

    @MockitoBean
    private lateinit var satiPdfGenerator: ISatiPdfGenerator

    @MockitoBean
    private lateinit var exportAuditRepository: IExportAuditRepository

    @MockitoBean
    private lateinit var getNatinfs: GetNatinfs

    private val pdf = "%PDF-1.7".toByteArray()

    @BeforeEach
    fun setUp() {
        `when`(enableSati.execute()).thenReturn(true)
        `when`(getUserFromToken.execute()).thenReturn(UserMock.create(id = 7, serviceId = 3))
        `when`(getServiceById.execute(3)).thenReturn(ServiceEntityMock.create(id = 3, name = "ULAM 56"))
        `when`(getMissionGeneralInfoByMissionId.execute(761)).thenReturn(
            MissionGeneralInfoEntity(missionId = 761, service = ServiceEntityMock.create(id = 5, name = "ULAM 29"))
        )
        `when`(satiPdfGenerator.generate(any())).thenReturn(pdf)
    }

    private fun givenAction(
        type: MissionActionType = MissionActionType.SEA_CONTROL,
        isCompleteForStats: Boolean = true,
    ): MissionFishActionEntity {
        val action = MissionFishActionEntityMock.create(
            id = 12345,
            missionId = 761,
            fishActionType = type,
            vesselName = "FLEUR DE LYS",
            actionDatetimeUtc = Instant.parse("2026-12-31T08:15:00Z"),
            actionEndDatetimeUtc = Instant.parse("2026-12-31T10:45:00Z"),
            latitude = 47.8,
            longitude = -3.9,
        )
        action.isCompleteForStats = isCompleteForStats
        `when`(getFishActionById.execute(761, "12345")).thenReturn(action)
        return action
    }

    private fun givenSati(action: MissionFishActionEntity, inspector: SatiInspectorEntity) {
        action.sati = SatiEntity(
            module = SatiModuleType.M1,
            actionId = "12345",
            vessel = SatiVesselEntity(name = "FLEUR DE LYS II"),
            inspectors = listOf(SatiInspectorEntity(isPrincipal = false, agentId = 99, authorityType = AuthorityType.OTHERS), inspector),
        )
    }

    private fun generatedDocument(): SatiDocumentEntity {
        val captor = argumentCaptor<SatiDocumentEntity>()
        verify(satiPdfGenerator).generate(captor.capture())
        return captor.firstValue
    }

    @Test
    fun `should render the SATI vessel, the mission's service, the principal agent, the dates and the position`() {
        givenSati(givenAction(), SatiInspectorEntity(isPrincipal = true, agentId = 42, authorityType = AuthorityType.AECP))
        `when`(agentRepository.findById(42)).thenReturn(
            AgentModel(
                id = 42,
                firstName = "Jean",
                lastName = "Dupont",
                role = AgentRoleModel(id = 1, title = "Chef"),
                service = ServiceEntityMock.create(id = 3).toServiceModel(),
            )
        )

        useCase.execute(ownerId = "761", actionId = "12345")

        assertThat(generatedDocument()).isEqualTo(
            SatiDocumentEntity(
                module = SatiModuleType.M1,
                vesselName = "FLEUR DE LYS II",
                serviceName = "ULAM 29",
                inspectorFirstName = "Jean",
                inspectorLastName = "Dupont",
                inspectorAuthorityType = AuthorityType.AECP,
                startDatetimeUtc = Instant.parse("2026-12-31T08:15:00Z"),
                endDatetimeUtc = Instant.parse("2026-12-31T10:45:00Z"),
                latitude = 47.8,
                longitude = -3.9,
                vessel = SatiVesselEntity(name = "FLEUR DE LYS II"),
            )
        )
    }

    @Test
    fun `should fall back to the exporting user's service when the mission has none`() {
        givenAction()
        `when`(getMissionGeneralInfoByMissionId.execute(761)).thenReturn(MissionGeneralInfoEntity(missionId = 761))

        useCase.execute(ownerId = "761", actionId = "12345")

        assertThat(generatedDocument().serviceName).isEqualTo("ULAM 56")
    }

    @Test
    fun `should render the vessel compliance checks of the action`() {
        val action = MissionFishActionEntityMock.create(id = 12345, missionId = 761, emitsVms = ControlCheck.NO)
        action.isCompleteForStats = true
        `when`(getFishActionById.execute(761, "12345")).thenReturn(action)

        useCase.execute(ownerId = "761", actionId = "12345")

        assertThat(generatedDocument().compliance.emitsVms).isEqualTo(ControlCheck.NO)
    }

    @Test
    fun `should render whether the unit boarded the vessel`() {
        val action = MissionFishActionEntity(
            id = 12345,
            missionId = 761,
            fishActionType = MissionActionType.SEA_CONTROL,
            isUnitBoarded = false,
        )
        action.isCompleteForStats = true
        `when`(getFishActionById.execute(761, "12345")).thenReturn(action)

        useCase.execute(ownerId = "761", actionId = "12345")

        assertThat(generatedDocument().isUnitBoarded).isFalse()
    }

    @Test
    fun `should render the port of the control`() {
        val action = MissionFishActionEntity(
            id = 12345,
            missionId = 761,
            fishActionType = MissionActionType.SEA_CONTROL,
            portName = "Lorient",
            portLocode = "FRLRT",
        )
        action.isCompleteForStats = true
        `when`(getFishActionById.execute(761, "12345")).thenReturn(action)

        useCase.execute(ownerId = "761", actionId = "12345")

        assertThat(generatedDocument().portName).isEqualTo("Lorient")
        assertThat(generatedDocument().portLocode).isEqualTo("FRLRT")
    }

    private fun actionWithRapportNavInfraction(): MissionFishActionEntity {
        val action = MissionFishActionEntityMock.create(id = 12345, missionId = 761)
        action.isCompleteForStats = true
        action.targets = listOf(
            TargetEntity(
                id = UUID.randomUUID(),
                actionId = "12345",
                targetType = TargetType.DEFAULT,
                controls = listOf(
                    ControlEntity(
                        id = UUID.randomUUID(),
                        controlType = ControlType.ADMINISTRATIVE,
                        amountOfControls = 1,
                        infractions = listOf(
                            InfractionEntity(
                                id = UUID.randomUUID(),
                                natinfs = listOf("2593"),
                                infractionType = InfractionTypeEnum.WITH_REPORT,
                            )
                        ),
                    )
                ),
            )
        )
        return action
    }

    @Test
    fun `should describe the NATINFs of the RapportNav infractions`() {
        `when`(getFishActionById.execute(761, "12345")).thenReturn(actionWithRapportNavInfraction())
        `when`(getNatinfs.execute()).thenReturn(listOf(NatinfEntity(infraction = "Défaut de permis d'armement", natinfCode = 2593)))

        useCase.execute(ownerId = "761", actionId = "12345")

        assertThat(generatedDocument().conclusion.infractions.single().descriptions).containsExactly("Défaut de permis d'armement")
    }

    @Test
    fun `should still render the RapportNav infractions when their NATINFs cannot be described`() {
        `when`(getFishActionById.execute(761, "12345")).thenReturn(actionWithRapportNavInfraction())
        `when`(getNatinfs.execute()).thenThrow(RuntimeException("MonitorEnv is down"))

        useCase.execute(ownerId = "761", actionId = "12345")

        val infraction = generatedDocument().conclusion.infractions.single()
        assertThat(infraction.natinfs).containsExactly("2593")
        assertThat(infraction.descriptions).isEmpty()
    }

    @Test
    fun `should categorize the MonitorFish infractions with their NATINF`() {
        val action = MissionFishActionEntityMock.create(id = 12345, missionId = 761, fishInfractions = listOf(FishInfraction(natinf = 7059)))
        action.isCompleteForStats = true
        `when`(getFishActionById.execute(761, "12345")).thenReturn(action)
        `when`(getNatinfs.execute()).thenReturn(listOf(NatinfEntity(infraction = "Pêche sans autorisation", natinfCode = 7059, infractionCategory = "Pêche")))

        useCase.execute(ownerId = "761", actionId = "12345")

        assertThat(generatedDocument().conclusion.infractions.single().natinfCategory).isEqualTo("Pêche")
    }

    @Test
    fun `should not look the NATINFs up without infraction`() {
        val action = MissionFishActionEntityMock.create(id = 12345, missionId = 761)
        action.isCompleteForStats = true
        `when`(getFishActionById.execute(761, "12345")).thenReturn(action)

        useCase.execute(ownerId = "761", actionId = "12345")

        verifyNoInteractions(getNatinfs)
    }

    @Test
    fun `should render the gears on board`() {
        val gear = GearControl().apply { gearCode = "OTB"; gearWasControlled = true }
        val action = MissionFishActionEntityMock.create(id = 12345, missionId = 761, gearOnboard = listOf(gear))
        action.isCompleteForStats = true
        `when`(getFishActionById.execute(761, "12345")).thenReturn(action)

        useCase.execute(ownerId = "761", actionId = "12345")

        assertThat(generatedDocument().gearInspection.gears).containsExactly(gear)
    }

    @Test
    fun `should render the contact names of a principal inspector from outside the unit`() {
        givenSati(
            givenAction(),
            SatiInspectorEntity(
                isPrincipal = true,
                isOutOfUnit = true,
                party = SatiPartyEntity(
                    contact = ContactEntity(fullName = "Marie Martin", firstName = "Marie", lastName = "Martin")
                ),
            ),
        )

        useCase.execute(ownerId = "761", actionId = "12345")

        assertThat(generatedDocument().inspectorFirstName).isEqualTo("Marie")
        assertThat(generatedDocument().inspectorLastName).isEqualTo("Martin")
    }

    @Test
    fun `should render the full name as last name of an outside inspector without split names`() {
        givenSati(
            givenAction(),
            SatiInspectorEntity(
                isPrincipal = true,
                isOutOfUnit = true,
                party = SatiPartyEntity(contact = ContactEntity(fullName = "Marie Martin")),
            ),
        )

        useCase.execute(ownerId = "761", actionId = "12345")

        assertThat(generatedDocument().inspectorFirstName).isNull()
        assertThat(generatedDocument().inspectorLastName).isEqualTo("Marie Martin")
    }

    @Test
    fun `should fall back to the action vessel name and no inspector without SATI data`() {
        givenAction()

        useCase.execute(ownerId = "761", actionId = "12345")

        assertThat(generatedDocument().vesselName).isEqualTo("FLEUR DE LYS")
        assertThat(generatedDocument().inspectorName).isNull()
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

    @Test
    fun `should accept a fish sea control (SATI module M1)`() {
        givenAction(MissionActionType.SEA_CONTROL)

        assertThat(useCase.execute(ownerId = "761", actionId = "12345").content).isEqualTo(pdf)
        assertThat(generatedDocument().module).isEqualTo(SatiModuleType.M1)
    }

    @ParameterizedTest
    @EnumSource(value = MissionActionType::class, names = ["LAND_CONTROL", "AIR_CONTROL", "AIR_SURVEILLANCE", "OBSERVATION"])
    fun `should throw COULD_NOT_FIND for a fish action other than a sea control`(type: MissionActionType) {
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
