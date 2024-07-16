package fr.gouv.dgampa.rapportnav.domain.use_cases.utils

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMTableExport
import fr.gouv.dgampa.rapportnav.infrastructure.excel.ExportExcelFile

@UseCase
class FillAEMExcelRow {

    fun fill(tableExport: AEMTableExport, excelFile: ExportExcelFile, sheetName: String, row: Int) {

        //1.1  Sauvegarde de la vie humaine hors cadre d'un phénomène migratoire
        tableExport.outOfMigrationRescue?.nbrOfHourInSea?.let { excelFile.writeToCell(sheetName, "H$row", it) }
        tableExport.outOfMigrationRescue?.nbrOfRescuedOperation?.let { excelFile.writeToCell(sheetName, "J$row", it) }
        tableExport.outOfMigrationRescue?.nbrPersonsRescued?.let { excelFile.writeToCell(sheetName, "K$row", it) }

        //1.2 Sauvegarde de la vie humaine dans le cadre d'un phénomène migratoire
        tableExport.migrationRescue?.nbrOfHourInSea?.let { excelFile.writeToCell(sheetName, "M$row", it) }
        tableExport.migrationRescue?.nbrOfOperation?.let { excelFile.writeToCell(sheetName, "O$row", it) }
        tableExport.migrationRescue?.nbrOfVesselsTrackedWithoutIntervention?.let { excelFile.writeToCell(sheetName, "P$row", it) }
        tableExport.migrationRescue?.nbrAssistedVesselsReturningToShore?.let { excelFile.writeToCell(sheetName, "Q$row", it) }

        // 2 Assistance aux navires en difficulté et sécurité maritime
        tableExport.vesselRescue?.nbrOfHourInSea?.let { excelFile.writeToCell(sheetName, "U$row", it) }
        tableExport.vesselRescue?.nbrOfRescuedOperation?.let { excelFile.writeToCell(sheetName, "W$row", it) }
        tableExport.vesselRescue?.nbrOfTowedVessel?.let { excelFile.writeToCell(sheetName, "X$row", it) }
        tableExport.vesselRescue?.nbrOfNoticedVessel?.let { excelFile.writeToCell(sheetName, "Y$row", it) }

        // $row.$row) Lutte contre le trafic en mer d’espèces protégées
        tableExport.envTraffic?.nbrOfHourInSea?.let { excelFile.writeToCell(sheetName, "AV$row", it) }
        tableExport.envTraffic?.nbrOfRedirectShip?.let { excelFile.writeToCell(sheetName, "AX$row", it) }
        tableExport.envTraffic?.nbrOfSeizure?.let { excelFile.writeToCell(sheetName, "AY$row", it) }

        // 4.1) Surveillance et contrôles pour la protection de l'environnement (hors rejets illicites)
        tableExport.notPollutionControlSurveillance?.nbrOfHourInSea?.let { excelFile.writeToCell(sheetName, "BH$row", it) }
        tableExport.notPollutionControlSurveillance?.nbrOfAction?.let { excelFile.writeToCell(sheetName, "BJ$row", it) }
        tableExport.notPollutionControlSurveillance?.nbrOfInfraction?.let { excelFile.writeToCell(sheetName, "BK$row", it) }
        tableExport.notPollutionControlSurveillance?.nbrOfInfractionWithPV?.let { excelFile.writeToCell(sheetName, "BL$row", it) }

        // 4.2) Répression contre les rejets illicites, lutte contre les pollutions
        tableExport.pollutionControlSurveillance?.nbrOfHourInSea?.let { excelFile.writeToCell(sheetName, "BN$row", it) }
        tableExport.pollutionControlSurveillance?.nbrOfSimpleBrassage?.let { excelFile.writeToCell(sheetName, "BP$row", it) }
        tableExport.pollutionControlSurveillance?.nbrOfDeploymentAction?.let { excelFile.writeToCell(sheetName, "BQ$row", it) }
        tableExport.pollutionControlSurveillance?.nbrOfDetectedPollution?.let { excelFile.writeToCell(sheetName, "BR$row", it) }
        tableExport.pollutionControlSurveillance?.nbrOfInfractionWithPV?.let { excelFile.writeToCell(sheetName, "BS$row", it) }
        tableExport.pollutionControlSurveillance?.nbrOfDiversionCarriedOut?.let { excelFile.writeToCell(sheetName, "BT$row", it) }
        tableExport.pollutionControlSurveillance?.nbrOfPollutionObservedByAuthorizedAgent?.let { excelFile.writeToCell(sheetName, "BU$row", it) }

        // 4.$row) Lutte contre les activités de pêche illégale
        tableExport.illegalFish?.nbrOfHourInSea?.let { excelFile.writeToCell(sheetName, "BW$row", it) }
        tableExport.illegalFish?.nbrOfPolFishAction?.let { excelFile.writeToCell(sheetName, "BY$row", it) }
        tableExport.illegalFish?.nbrOfTargetedVessel?.let { excelFile.writeToCell(sheetName, "BZ$row", it) }
        tableExport.illegalFish?.nbrOfInfraction?.let { excelFile.writeToCell(sheetName, "CA$row", it) }
        tableExport.illegalFish?.nbrOfInfractionWithPV?.let { excelFile.writeToCell(sheetName, "CB$row", it) }
        tableExport.illegalFish?.nbrOfTowedVessel?.let { excelFile.writeToCell(sheetName, "CD$row", it) }
        tableExport.illegalFish?.quantityOfFish?.let { excelFile.writeToCell(sheetName, "CE$row", it) }

        // 4.4) Protection des biens culturels maritimes

        // 7) Souveraineté et protection des intérêts nationaux
        tableExport.sovereignProtect?.nbrOfHourInSea?.let { excelFile.writeToCell(sheetName, "DA$row", it) }
        tableExport.sovereignProtect?.nbrOfReconizedVessel?.let { excelFile.writeToCell(sheetName, "DC$row", it) }
        tableExport.sovereignProtect?.nbrOfControlledVessel?.let { excelFile.writeToCell(sheetName, "DD$row", it) }
    }
}
