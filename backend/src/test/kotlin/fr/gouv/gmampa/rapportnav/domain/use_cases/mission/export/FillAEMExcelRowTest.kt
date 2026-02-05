package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2


import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMEnvTraffic
import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMIllegalFish
import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMMigrationRescue
import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMNotPollutionControlSurveillance
import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMOutOfMigrationRescue
import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMPollutionControlSurveillance
import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMSovereignProtect
import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMTableExport
import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMVesselRescue
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FillAEMExcelRow
import fr.gouv.dgampa.rapportnav.infrastructure.utils.office.ExportExcelFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [FillAEMExcelRow::class])
class FillAEMExcelRowTest {

    @MockitoBean
    private lateinit var exportExcelFile: ExportExcelFile

    @Autowired
    private lateinit var fillAEMExcelRow: FillAEMExcelRow

    @MockitoBean
    private lateinit var tableExport: AEMTableExport

    @MockitoBean
    private lateinit var migrationRescue: AEMMigrationRescue

    @MockitoBean
    private lateinit var outOfMigrationRescue: AEMOutOfMigrationRescue

    @MockitoBean
    private lateinit var vesselRescue: AEMVesselRescue

    @MockitoBean
    private lateinit var envTraffic: AEMEnvTraffic

    @MockitoBean
    private lateinit var notPollControlSurveillance: AEMNotPollutionControlSurveillance

    @MockitoBean
    private lateinit var pollutionControlSurveillance: AEMPollutionControlSurveillance

    @MockitoBean
    private lateinit var illegalFish: AEMIllegalFish

    @MockitoBean
    private lateinit var sovereignProtect: AEMSovereignProtect

    @Test
    fun `test fill method writes outOfMigrationRescue properties to correct cells`() {
        val sheetName = "Synthese"
        val row = 3

        `when`(tableExport.outOfMigrationRescue).thenReturn(outOfMigrationRescue)
        `when`(outOfMigrationRescue.nbrOfHourAtSea).thenReturn(10.0)
        `when`(outOfMigrationRescue.nbrOfRescuedOperation).thenReturn(5.0)
        `when`(outOfMigrationRescue.nbrPersonsRescued).thenReturn(3.0)

        fillAEMExcelRow.fill(tableExport, exportExcelFile, sheetName, row)
        val captor = ArgumentCaptor.forClass(Double::class.java)


        // 1.1.1
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("H$row") ?: "H$row",
            captor.capture()
        )
        assertEquals(10.0, captor.value)

        // 1.1.3
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("J$row") ?: "J$row",
            captor.capture()
        )
        assertEquals(5.0, captor.value)


        // 1.1.4
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("K$row") ?: "K$row",
            captor.capture()
        )
        assertEquals(3.0, captor.value)

    }

    @Test
    fun `test fill method writes migrationRescue properties to correct cells`() {
        val sheetName = "Synthese"
        val row = 3

        `when`(tableExport.migrationRescue).thenReturn(migrationRescue)
        `when`(migrationRescue.nbrOfHourAtSea).thenReturn(10.0)
        `when`(migrationRescue.nbrOfOperation).thenReturn(5.0)
        `when`(migrationRescue.nbrOfVesselsTrackedWithoutIntervention).thenReturn(3.0)
        `when`(migrationRescue.nbrAssistedVesselsReturningToShore).thenReturn(2.0)
        `when`(migrationRescue.nbrOfRescuedOperation).thenReturn(4.0)
        `when`(migrationRescue.nbrPersonsRescued).thenReturn(50.0)

        fillAEMExcelRow.fill(tableExport, exportExcelFile, sheetName, row)
        val captor = ArgumentCaptor.forClass(Double::class.java)


        // 1.2.1
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("M$row") ?: "M$row",
            captor.capture()
        )
        assertEquals(10.0, captor.value)

        // 1.2.3
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("O$row") ?: "O$row",
            captor.capture()
        )
        assertEquals(5.0, captor.value)


        // 1.2.4
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("P$row") ?: "O$row",
            captor.capture()
        )
        assertEquals(3.0, captor.value)


        // 1.2.5
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("Q$row") ?: "O$row",
            captor.capture()
        )
        assertEquals(2.0, captor.value)


        // 1.2.6
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("R$row") ?: "O$row",
            captor.capture()
        )
        assertEquals(4.0, captor.value)

        // 1.2.7
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("S$row") ?: "O$row",
            captor.capture()
        )
        assertEquals(50.0, captor.value)
    }

    @Test
    fun `test fill method writes vesselRescue properties to correct cells`() {
        val sheetName = "Synthese"
        val row = 3

        `when`(tableExport.vesselRescue).thenReturn(vesselRescue)
        `when`(vesselRescue.nbrOfHourAtSea).thenReturn(10.0)
        `when`(vesselRescue.nbrOfRescuedOperation).thenReturn(5.0)
        `when`(vesselRescue.nbrOfNoticedVessel).thenReturn(3.0)
        `when`(vesselRescue.nbrOfTowedVessel).thenReturn(2.0)

        fillAEMExcelRow.fill(tableExport, exportExcelFile, sheetName, row)
        val captor = ArgumentCaptor.forClass(Double::class.java)


        // 2.1
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("U$row") ?: "U$row",
            captor.capture()
        )
        assertEquals(10.0, captor.value)

        // 2.3
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("W$row") ?: "W$row",
            captor.capture()
        )
        assertEquals(5.0, captor.value)


        // 2.4
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("X$row") ?: "X$row",
            captor.capture()
        )
        assertEquals(3.0, captor.value)


        // 2.7
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("AA$row") ?: "AA$row",
            captor.capture()
        )
        assertEquals(2.0, captor.value)


    }

    @Test
    fun `test fill method writes envTraffic properties to correct cells`() {
        val sheetName = "Synthese"
        val row = 3

        `when`(tableExport.envTraffic).thenReturn(envTraffic)
        `when`(envTraffic.nbrOfHourAtSea).thenReturn(10.0)
        `when`(envTraffic.nbrOfRedirectShip).thenReturn(5.0)
        `when`(envTraffic.nbrOfSeizure).thenReturn(3.0)

        fillAEMExcelRow.fill(tableExport, exportExcelFile, sheetName, row)
        val captor = ArgumentCaptor.forClass(Double::class.java)


        // 2.1
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("AV$row") ?: "AV$row",
            captor.capture()
        )
        assertEquals(10.0, captor.value)

        // 2.3
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("AX$row") ?: "AX$row",
            captor.capture()
        )
        assertEquals(5.0, captor.value)


        // 2.4
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("AY$row") ?: "AY$row",
            captor.capture()
        )
        assertEquals(3.0, captor.value)

    }

    @Test
    fun `test fill method writes notPollControlSurveillance properties to correct cells`() {
        val sheetName = "Synthese"
        val row = 3

        `when`(tableExport.notPollutionControlSurveillance).thenReturn(notPollControlSurveillance)
        `when`(notPollControlSurveillance.nbrOfHourAtSea).thenReturn(10.0)
        `when`(notPollControlSurveillance.nbrOfAction).thenReturn(5.0)
        `when`(notPollControlSurveillance.nbrOfInfraction).thenReturn(3.0)
        `when`(notPollControlSurveillance.nbrOfInfractionWithNotice).thenReturn(2.0)

        fillAEMExcelRow.fill(tableExport, exportExcelFile, sheetName, row)
        val captor = ArgumentCaptor.forClass(Double::class.java)


        // 4.1.1
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("BH$row") ?: "BH$row",
            captor.capture()
        )
        assertEquals(10.0, captor.value)

        // 4.1.3
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("BJ$row") ?: "BJ$row",
            captor.capture()
        )
        assertEquals(5.0, captor.value)


        // 4.1.4
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("BK$row") ?: "BK$row",
            captor.capture()
        )
        assertEquals(3.0, captor.value)

        // 4.1.5
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("BL$row") ?: "BL$row",
            captor.capture()
        )
        assertEquals(2.0, captor.value)


    }

    @Test
    fun `test fill method writes pollutionControlSurveillance properties to correct cells`() {
        val sheetName = "Synthese"
        val row = 3

        `when`(tableExport.pollutionControlSurveillance).thenReturn(pollutionControlSurveillance)
        `when`(pollutionControlSurveillance.nbrOfHourAtSea).thenReturn(10.0)
        `when`(pollutionControlSurveillance.nbrOfSimpleBrewingOperation).thenReturn(5.0)
        `when`(pollutionControlSurveillance.nbrOfAntiPolDeviceDeployed).thenReturn(3.0)
        `when`(pollutionControlSurveillance.nbrOfInfraction).thenReturn(2.0)
        `when`(pollutionControlSurveillance.nbrOfInfractionWithNotice).thenReturn(55.0)
        `when`(pollutionControlSurveillance.nbrOfDiversionCarriedOut).thenReturn(8.0)
        `when`(pollutionControlSurveillance.nbrOfPollutionObservedByAuthorizedAgent).thenReturn(47.0)

        fillAEMExcelRow.fill(tableExport, exportExcelFile, sheetName, row)
        val captor = ArgumentCaptor.forClass(Double::class.java)


        // 4.2.1
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("BN$row") ?: "BN$row",
            captor.capture()
        )
        assertEquals(10.0, captor.value)

        // 4.2.3
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("BP$row") ?: "BP$row",
            captor.capture()
        )
        assertEquals(5.0, captor.value)


        // 4.2.4
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("BQ$row") ?: "BQ$row",
            captor.capture()
        )
        assertEquals(3.0, captor.value)

        // 4.2.5
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("BR$row") ?: "BR$row",
            captor.capture()
        )
        assertEquals(2.0, captor.value)

        // 4.2.6
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("BS$row") ?: "BS$row",
            captor.capture()
        )
        assertEquals(55.0, captor.value)

        // 4.2.7
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("BT$row") ?: "BT$row",
            captor.capture()
        )
        assertEquals(8.0, captor.value)

        // 4.2.8
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("BU$row") ?: "BU$row",
            captor.capture()
        )
        assertEquals(47.0, captor.value)


    }

    @Test
    fun `test fill method writes illegalFish properties to correct cells`() {
        val sheetName = "Synthese"
        val row = 3

        `when`(tableExport.illegalFish).thenReturn(illegalFish)
        `when`(illegalFish.nbrOfHourAtSea).thenReturn(10.0)
        `when`(illegalFish.nbrOfPolFishAction).thenReturn(5.0)
        `when`(illegalFish.nbrOfTargetedVessel).thenReturn(3.0)
        `when`(illegalFish.nbrOfInfractionWithPV).thenReturn(2.0)
        `when`(illegalFish.nbrOfInfraction).thenReturn(55.0)
        `when`(illegalFish.nbrOfSeizureAndDiversionVessel).thenReturn(8.0)
        `when`(illegalFish.quantityOfFish).thenReturn(47.0)

        fillAEMExcelRow.fill(tableExport, exportExcelFile, sheetName, row)
        val captor = ArgumentCaptor.forClass(Double::class.java)


        // 4.3.1
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("BW$row") ?: "BN$row",
            captor.capture()
        )
        assertEquals(10.0, captor.value)

        // 4.3.3
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("BY$row") ?: "BY$row",
            captor.capture()
        )
        assertEquals(5.0, captor.value)


        // 4.3.5
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("CA$row") ?: "CA$row",
            captor.capture()
        )
        assertEquals(3.0, captor.value)

        // 4.3.6
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("CB$row") ?: "CB$row",
            captor.capture()
        )
        assertEquals(2.0, captor.value)

        // 4.3.7
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("CC$row") ?: "CC$row",
            captor.capture()
        )
        assertEquals(55.0, captor.value)

        // 4.3.8
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("CD$row") ?: "CD$row",
            captor.capture()
        )
        assertEquals(8.0, captor.value)

        // 4.3.9
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("CE$row") ?: "CE$row",
            captor.capture()
        )
        assertEquals(47.0, captor.value)


    }

    @Test
    fun `test fill method writes sovereignProtect properties to correct cells`() {
        val sheetName = "Synthese"
        val row = 3

        `when`(tableExport.sovereignProtect).thenReturn(sovereignProtect)
        `when`(sovereignProtect.nbrOfHourAtSea).thenReturn(10.0)
        `when`(sovereignProtect.nbrOfRecognizedVessel).thenReturn(5.0)
        `when`(sovereignProtect.nbrOfControlledVessel).thenReturn(3.0)

        fillAEMExcelRow.fill(tableExport, exportExcelFile, sheetName, row)
        val captor = ArgumentCaptor.forClass(Double::class.java)


        // 7.1
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("DA$row") ?: "DA$row",
            captor.capture()
        )
        assertEquals(10.0, captor.value)

        // 7.3
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("DC$row") ?: "DC$row",
            captor.capture()
        )
        assertEquals(5.0, captor.value)


        // 7.4
        verify(exportExcelFile).writeToCell(
            ArgumentMatchers.eq(sheetName) ?: sheetName,
            ArgumentMatchers.eq("DD$row") ?: "DD$row",
            captor.capture()
        )
        assertEquals(3.0, captor.value)
    }

}
