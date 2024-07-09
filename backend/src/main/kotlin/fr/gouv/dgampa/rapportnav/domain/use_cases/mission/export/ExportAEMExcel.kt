package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMTableExport
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetMissionById
import fr.gouv.dgampa.rapportnav.infrastructure.excel.ExportExcelFile
import fr.gouv.dgampa.rapportnav.infrastructure.factories.ExportExcelFactory

@UseCase
class ExportAEMExcel(
    private val getMissionById: GetMissionById
) {

    fun execute(missionId: Int) {
        val excelFile: ExportExcelFile = ExportExcelFactory.create("/src/main/resources/synthese_AEM_PAM.xlsx")
        val sheetName = "Synthese"
        val mission: MissionEntity? = getMissionById.execute(missionId)

        if (mission !== null && mission.actions !== null) {

            val tableExport = AEMTableExport.fromMissionAction(mission.actions!!)

            //1.1  Sauvegarde de la vie humaine hors cadre d'un phénomène migratoire
            tableExport.outOfMigrationRescue?.nbrOfHourInSea?.let { excelFile.writeToCell(sheetName, "H3", it) }
            tableExport.outOfMigrationRescue?.nbrOfRescuedOperation?.let { excelFile.writeToCell(sheetName, "J3", it) }
            tableExport.outOfMigrationRescue?.nbrPersonsRescued?.let { excelFile.writeToCell(sheetName, "K3", it) }

            //1.2 Sauvegarde de la vie humaine dans le cadre d'un phénomène migratoire
            tableExport.migrationRescue?.nbrOfHourInSea?.let { excelFile.writeToCell(sheetName, "M3", it) }
            tableExport.migrationRescue?.nbrOfOperation?.let { excelFile.writeToCell(sheetName, "03", it) }
            tableExport.migrationRescue?.nbrOfVesselsTrackedWithoutIntervention?.let { excelFile.writeToCell(sheetName, "P3", it) }
            tableExport.migrationRescue?.nbrAssistedVesselsReturningToShore?.let { excelFile.writeToCell(sheetName, "Q3", it) }

            // 2 Assistance aux navires en difficulté et sécurité maritime
            tableExport.vesselRescue?.nbrOfHourInSea?.let { excelFile.writeToCell(sheetName, "U3", it) }
            tableExport.vesselRescue?.nbrOfRescuedOperation?.let { excelFile.writeToCell(sheetName, "W3", it) }
            tableExport.vesselRescue?.nbrOfTowedVessel?.let { excelFile.writeToCell(sheetName, "X3", it) }
            tableExport.vesselRescue?.nbrOfNoticedVessel?.let { excelFile.writeToCell(sheetName, "Y3", it) }

            // 3.3) Lutte contre le trafic en mer d’espèces protégées
            tableExport.envTraffic?.nbrOfHourInSea?.let { excelFile.writeToCell(sheetName, "AV3", it) }
            tableExport.envTraffic?.nbrOfRedirectShip?.let { excelFile.writeToCell(sheetName, "AX3", it) }
            tableExport.envTraffic?.nbrOfSeizure?.let { excelFile.writeToCell(sheetName, "AY3", it) }

            // 4.1) Surveillance et contrôles pour la protection de l'environnement (hors rejets illicites)
            tableExport.notPollutionControlSurveillance?.nbrOfHourInSea?.let { excelFile.writeToCell(sheetName, "BH3", it) }
            tableExport.notPollutionControlSurveillance?.nbrOfAction?.let { excelFile.writeToCell(sheetName, "BJ3", it) }
            tableExport.notPollutionControlSurveillance?.nbrOfInfraction?.let { excelFile.writeToCell(sheetName, "BK3", it) }
            tableExport.notPollutionControlSurveillance?.nbrOfInfractionWithPV?.let { excelFile.writeToCell(sheetName, "BL3", it) }

            // 4.2) Répression contre les rejets illicites, lutte contre les pollutions
            tableExport.pollutionControlSurveillance?.nbrOfHourInSea?.let { excelFile.writeToCell(sheetName, "BN3", it) }
            tableExport.pollutionControlSurveillance?.nbrOfSimpleBrassage?.let { excelFile.writeToCell(sheetName, "BP3", it) }
            tableExport.pollutionControlSurveillance?.nbrOfDeploymentAction?.let { excelFile.writeToCell(sheetName, "BQ3", it) }
            tableExport.pollutionControlSurveillance?.nbrOfDetectedPollution?.let { excelFile.writeToCell(sheetName, "BR3", it) }
            tableExport.pollutionControlSurveillance?.nbrOfInfractionWithPV?.let { excelFile.writeToCell(sheetName, "BS3", it) }
            tableExport.pollutionControlSurveillance?.nbrOfDiversionCarriedOut?.let { excelFile.writeToCell(sheetName, "BT3", it) }
            tableExport.pollutionControlSurveillance?.nbrOfPollutionObservedByAuthorizedAgent?.let { excelFile.writeToCell(sheetName, "BU3", it) }

            // 4.3) Lutte contre les activités de pêche illégale
            tableExport.illegalFish?.nbrOfHourInSea?.let { excelFile.writeToCell(sheetName, "BW3", it) }
            tableExport.illegalFish?.nbrOfPolFishAction?.let { excelFile.writeToCell(sheetName, "BY3", it) }
            tableExport.illegalFish?.nbrOfTargetedVessel?.let { excelFile.writeToCell(sheetName, "BZ3", it) }
            tableExport.illegalFish?.nbrOfInfraction?.let { excelFile.writeToCell(sheetName, "CA3", it) }
            tableExport.illegalFish?.nbrOfInfractionWithPV?.let { excelFile.writeToCell(sheetName, "CB3", it) }
            tableExport.illegalFish?.nbrOfTowedVessel?.let { excelFile.writeToCell(sheetName, "CD3", it) }
            tableExport.illegalFish?.quantityOfFish?.let { excelFile.writeToCell(sheetName, "CE3", it) }

            // 4.4) Protection des biens culturels maritimes

            // 7) Souveraineté et protection des intérêts nationaux
            tableExport.sovereignProtect?.nbrOfHourInSea?.let { excelFile.writeToCell(sheetName, "DA3", it) }
            tableExport.sovereignProtect?.nbrOfReconizedVessel?.let { excelFile.writeToCell(sheetName, "DC3", it) }
            tableExport.sovereignProtect?.nbrOfControlledVessel?.let { excelFile.writeToCell(sheetName, "DD3", it) }
        }

        excelFile.save()
    }


}
