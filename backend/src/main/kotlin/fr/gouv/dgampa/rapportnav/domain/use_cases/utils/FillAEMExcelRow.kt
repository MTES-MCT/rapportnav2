package fr.gouv.dgampa.rapportnav.domain.use_cases.utils

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMTableExport
import fr.gouv.dgampa.rapportnav.infrastructure.utils.office.ExportExcelFile

@UseCase
class FillAEMExcelRow {

    fun fill(tableExport: AEMTableExport, excelFile: ExportExcelFile, sheetName: String, row: Int) {

        //1.1  Sauvegarde de la vie humaine hors cadre d'un phénomène migratoire
        tableExport.outOfMigrationRescue?.nbrOfHourAtSea?.let { excelFile.writeToCell(sheetName, "H$row", it) }
        tableExport.outOfMigrationRescue?.nbrOfRescuedOperation?.let { excelFile.writeToCell(sheetName, "J$row", it) }
        tableExport.outOfMigrationRescue?.nbrPersonsRescued?.let { excelFile.writeToCell(sheetName, "K$row", it) }

        //1.2 Sauvegarde de la vie humaine dans le cadre d'un phénomène migratoire
        tableExport.migrationRescue?.nbrOfHourAtSea?.let { excelFile.writeToCell(sheetName, "M$row", it) }
        tableExport.migrationRescue?.nbrOfOperation?.let { excelFile.writeToCell(sheetName, "O$row", it) }
        tableExport.migrationRescue?.nbrOfVesselsTrackedWithoutIntervention?.let {
            excelFile.writeToCell(
                sheetName,
                "P$row",
                it
            )
        }
        tableExport.migrationRescue?.nbrAssistedVesselsReturningToShore?.let {
            excelFile.writeToCell(
                sheetName,
                "Q$row",
                it
            )
        }
        tableExport.migrationRescue?.nbrOfRescuedOperation?.let { excelFile.writeToCell(sheetName, "R$row", it) }
        tableExport.migrationRescue?.nbrPersonsRescued?.let { excelFile.writeToCell(sheetName, "S$row", it) }


        // 2 Assistance aux navires en difficulté et sécurité maritime
        tableExport.vesselRescue?.nbrOfHourAtSea?.let { excelFile.writeToCell(sheetName, "U$row", it) }
        tableExport.vesselRescue?.nbrOfRescuedOperation?.let { excelFile.writeToCell(sheetName, "W$row", it) }
        tableExport.vesselRescue?.nbrOfNoticedVessel?.let { excelFile.writeToCell(sheetName, "X$row", it) }
        tableExport.vesselRescue?.nbrOfTowedVessel?.let { excelFile.writeToCell(sheetName, "AA$row", it) }

        // 3.3) Lutte contre le trafic en mer d’espèces protégées
        tableExport.envTraffic?.nbrOfHourAtSea?.let { excelFile.writeToCell(sheetName, "AV$row", it) }
        tableExport.envTraffic?.nbrOfRedirectShip?.let { excelFile.writeToCell(sheetName, "AX$row", it) }
        tableExport.envTraffic?.nbrOfSeizure?.let { excelFile.writeToCell(sheetName, "AY$row", it) }

        // 4.1) Surveillance et contrôles pour la protection de l'environnement (hors rejets illicites)
        tableExport.notPollutionControlSurveillance?.nbrOfHourAtSea?.let {
            excelFile.writeToCell(
                sheetName,
                "BH$row",
                it
            )
        }
        tableExport.notPollutionControlSurveillance?.nbrOfAction?.let { excelFile.writeToCell(sheetName, "BJ$row", it) }
        tableExport.notPollutionControlSurveillance?.nbrOfInfraction?.let {
            excelFile.writeToCell(
                sheetName,
                "BK$row",
                it
            )
        }
        tableExport.notPollutionControlSurveillance?.nbrOfInfractionWithNotice?.let {
            excelFile.writeToCell(
                sheetName,
                "BL$row",
                it
            )
        }

        // 4.2) Répression contre les rejets illicites, lutte contre les pollutions
        tableExport.pollutionControlSurveillance?.nbrOfHourAtSea?.let { excelFile.writeToCell(sheetName, "BN$row", it) }
        tableExport.pollutionControlSurveillance?.nbrOfSimpleBrewingOperation?.let {
            excelFile.writeToCell(
                sheetName,
                "BP$row",
                it
            )
        }
        tableExport.pollutionControlSurveillance?.nbrOfAntiPolDeviceDeployed?.let {
            excelFile.writeToCell(
                sheetName,
                "BQ$row",
                it
            )
        }
        tableExport.pollutionControlSurveillance?.nbrOfInfraction?.let {
            excelFile.writeToCell(
                sheetName,
                "BR$row",
                it
            )
        }
        tableExport.pollutionControlSurveillance?.nbrOfInfractionWithNotice?.let {
            excelFile.writeToCell(
                sheetName,
                "BS$row",
                it
            )
        }
        tableExport.pollutionControlSurveillance?.nbrOfDiversionCarriedOut?.let {
            excelFile.writeToCell(
                sheetName,
                "BT$row",
                it
            )
        }
        tableExport.pollutionControlSurveillance?.nbrOfPollutionObservedByAuthorizedAgent?.let {
            excelFile.writeToCell(
                sheetName,
                "BU$row",
                it
            )
        }

        // 4.3) Lutte contre les activités de pêche illégale
        tableExport.illegalFish?.nbrOfHourAtSea?.let { excelFile.writeToCell(sheetName, "BW$row", it) }
        tableExport.illegalFish?.nbrOfPolFishAction?.let { excelFile.writeToCell(sheetName, "BY$row", it) }
        tableExport.illegalFish?.nbrOfTargetedVessel?.let { excelFile.writeToCell(sheetName, "CA$row", it) }
        tableExport.illegalFish?.nbrOfInfractionWithPV?.let { excelFile.writeToCell(sheetName, "CB$row", it) }
        tableExport.illegalFish?.nbrOfInfraction?.let { excelFile.writeToCell(sheetName, "CC$row", it) }
        tableExport.illegalFish?.nbrOfSeizureAndDiversionVessel?.let { excelFile.writeToCell(sheetName, "CD$row", it) }
        tableExport.illegalFish?.quantityOfFish?.let { excelFile.writeToCell(sheetName, "CE$row", it) }

        // 4.4) Protection des biens culturels maritimes

        // 7) Souveraineté et protection des intérêts nationaux
        tableExport.sovereignProtect?.nbrOfHourAtSea?.let { excelFile.writeToCell(sheetName, "DA$row", it) }
        tableExport.sovereignProtect?.nbrOfRecognizedVessel?.let { excelFile.writeToCell(sheetName, "DC$row", it) }
        tableExport.sovereignProtect?.nbrOfControlledVessel?.let { excelFile.writeToCell(sheetName, "DD$row", it) }
    }
}
