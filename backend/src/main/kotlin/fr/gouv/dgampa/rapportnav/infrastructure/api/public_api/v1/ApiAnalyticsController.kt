package fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.v1

import fr.gouv.dgampa.rapportnav.domain.entities.aem.v2.AEMTableExport2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.NavMissionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetNavMissionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeEnvActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeFishActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeNavActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.v1.adapters.outputs.AEMMetricOutput
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.v1.adapters.outputs.ApiAnalyticsAEMDataOutput
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.v1.adapters.outputs.ApiMissionDataOutput
import io.swagger.v3.oas.annotations.Operation
import jakarta.websocket.server.PathParam
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/analytics")
class ApiAnalyticsController(
    private val getNavMissionById: GetNavMissionById,
    private val getEnvMissionById2: GetEnvMissionById2,
    private val getEnvActionByMissionId: GetComputeEnvActionListByMissionId,
    private val getNavActionByMissionId: GetComputeNavActionListByMissionId,
    private val getFIshListActionByMissionId: GetComputeFishActionListByMissionId,
    private val getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId,
) {
    @GetMapping("aem/{missionId}")
    @Operation(
        summary = "Get AEM data for a specific mission",
    )
    fun getMissionById(
        @PathParam("ID of a Mission")
        @PathVariable(name = "missionId")
        missionId: Int,
    ): ApiAnalyticsAEMDataOutput? {
        val navMission: NavMissionEntity = getNavMissionById.execute(missionId = missionId)
        val envMission = getEnvMissionById2.execute(missionId)
        val envActions = getEnvActionByMissionId.execute(missionId)
        val navActions = getNavActionByMissionId.execute(missionId)
        val fishActions = getFIshListActionByMissionId.execute(missionId)
        val generalInfo = getMissionGeneralInfoByMissionId.execute(missionId)

        val data = AEMTableExport2.fromMissionAction(
            navActions = navActions,
            envActions = envActions,
            fishActions = fishActions,
            missionEndDateTimeUtc = envMission?.endDateTimeUtc,
            nbrOfRecognizedVessel = generalInfo?.nbrOfRecognizedVessel?.toDouble()
        )

        val formattedOutput = listOf<AEMMetricOutput>(
            AEMMetricOutput(
                id = "1.1.1",
                title = "Nombre d'heures de mer",
                value = data.outOfMigrationRescue?.nbrOfHourAtSea ?: 0.0,
            ),
            AEMMetricOutput(
                id = "1.1.3",
                title = "Nombre d'opérations conduites",
                value = data.outOfMigrationRescue?.nbrOfRescuedOperation ?: 0.0,
            ),
            AEMMetricOutput(
                id = "1.1.4",
                title = "Nombre de personnes secourues",
                value = data.outOfMigrationRescue?.nbrPersonsRescued ?: 0.0,
            ),
            AEMMetricOutput(
                id = "1.2.1",
                title = "Nombre d'heures de mer",
                value = data.migrationRescue?.nbrOfHourAtSea ?: 0.0,
            ),
            AEMMetricOutput(
                id = "1.2.3",
                title = "Nombre d'opérations conduites",
                value = data.migrationRescue?.nbrOfOperation ?: 0.0,
            ),
            AEMMetricOutput(
                id = "1.2.4",
                title = "(SAR migrants) Nombre d'embarcations suivies sans nécessité d'intervention",
                value = data.migrationRescue?.nbrOfVesselsTrackedWithoutIntervention ?: 0.0,
            ),
            AEMMetricOutput(
                id = "1.2.5",
                title = "(SAR migrants)Nombre d'embarcations assistées pour un retour à terre",
                value = data.migrationRescue?.nbrAssistedVesselsReturningToShore ?: 0.0,
            ),
            AEMMetricOutput(
                id = "1.2.6",
                title = "(SAR migrants) Nombre d'opérations de sauvetage conduites",
                value = data.migrationRescue?.nbrOfRescuedOperation ?: 0.0,
            ),
            AEMMetricOutput(
                id = "1.2.7",
                title = "(SAR migrants)Nombre de personnes secourues",
                value = data.migrationRescue?.nbrPersonsRescued ?: 0.0,
            ),
            AEMMetricOutput(
                id = "2.1",
                title = "Nombre d'heures de mer",
                value = data.vesselRescue?.nbrOfHourAtSea ?: 0.0,
            ),
            AEMMetricOutput(
                id = "2.3",
                title = "Nombre d'opérations ANED (mise en œuvre de moyens nautique ou aérien)",
                value = data.vesselRescue?.nbrOfRescuedOperation ?: 0.0,
            ),
            AEMMetricOutput(
                id = "2.4",
                title = "Nombre d'intervention faisant suite à une mise en demeure",
                value = data.vesselRescue?.nbrOfNoticedVessel ?: 0.0,
            ),
            AEMMetricOutput(
                id = "2.7",
                title = "Nombre de remorquages",
                value = data.vesselRescue?.nbrOfTowedVessel ?: 0.0,
            ),
            AEMMetricOutput(
                id = "3.3.1",
                title = "Nombre d'heures de mer",
                value = data.envTraffic?.nbrOfHourAtSea ?: 0.0,
            ),
            AEMMetricOutput(
                id = "3.3.3",
                title = "Nombre de navires ou embarcations déroutés ou saisis en mer",
                value = data.envTraffic?.nbrOfRedirectShip ?: 0.0,
            ),
            AEMMetricOutput(
                id = "3.3.4",
                title = "Nombre de saisies",
                value = data.envTraffic?.nbrOfSeizure ?: 0.0,
            ),
            AEMMetricOutput(
                id = "4.1.1",
                title = "Nombre d'heures de mer de surveillance ou de contrôle pour la protection de l'environnement (hors rejets illicites)",
                value = data.notPollutionControlSurveillance?.nbrOfHourAtSea ?: 0.0,
            ),
            AEMMetricOutput(
                id = "4.1.3",
                title = "Nombre d'opérations de surveillance ou de contrôles (hors rejets illicites)",
                value = data.notPollutionControlSurveillance?.nbrOfAction ?: 0.0,
            ),
            AEMMetricOutput(
                id = "4.1.4",
                title = "Nombre d’infractions à la réglementation relative à la protection de l'environnement en mer (hors rejets illicites)",
                value = data.notPollutionControlSurveillance?.nbrOfInfraction ?: 0.0,
            ),
            AEMMetricOutput(
                id = "4.1.5",
                title = "Nombre de Procès-Verbaux dressés en mer (hors rejets illicites)",
                value = data.notPollutionControlSurveillance?.nbrOfInfractionWithNotice ?: 0.0,
            ),
            AEMMetricOutput(
                id = "4.2.1",
                title = "Nombre d'heures de mer (surveillance et lutte)",
                value = data.pollutionControlSurveillance?.nbrOfHourAtSea ?: 0.0,
            ),
            AEMMetricOutput(
                id = "4.2.3",
                title = "Participation à une opération de lutte ANTIPOL en mer (simple brassage)",
                value = data.pollutionControlSurveillance?.nbrOfSimpleBrewingOperation ?: 0.0,
            ),
            AEMMetricOutput(
                id = "4.2.4",
                title = "Déploiement d’un dispositif de lutte anti-pollution en mer (dispersant, barrage, etc…)",
                value = data.pollutionControlSurveillance?.nbrOfAntiPolDeviceDeployed ?: 0.0,
            ),
            AEMMetricOutput(
                id = "4.2.5",
                title = "Nombre d'infractions constatées",
                value = data.pollutionControlSurveillance?.nbrOfInfraction ?: 0.0,
            ),
            AEMMetricOutput(
                id = "4.2.6",
                title = "Nombre de procès-verbaux dressés",
                value = data.pollutionControlSurveillance?.nbrOfInfractionWithNotice ?: 0.0,
            ),
            AEMMetricOutput(
                id = "4.2.7",
                title = "Nombre de déroutements effectués",
                value = data.pollutionControlSurveillance?.nbrOfDiversionCarriedOut ?: 0.0,
            ),
            AEMMetricOutput(
                id = "4.2.8",
                title = "Nombre de pollutions détectées et/ou constatées par un agent habilité",
                value = data.pollutionControlSurveillance?.nbrOfPollutionObservedByAuthorizedAgent ?: 0.0,
            ),
            AEMMetricOutput(
                id = "4.3.1",
                title = "Nombre d’heures de mer (surveillance/police des pêches)",
                value = data.illegalFish?.nbrOfHourAtSea ?: 0.0,
            ),
            AEMMetricOutput(
                id = "4.3.3",
                title = "nombre d'opérations POLPECHE",
                value = data.illegalFish?.nbrOfPolFishAction ?: 0.0,
            ),
            AEMMetricOutput(
                id = "4.3.5",
                title = "Nombre de navires inspectés en mer (montée à bord)",
                value = data.illegalFish?.nbrOfTargetedVessel ?: 0.0,
            ),
            AEMMetricOutput(
                id = "4.3.6",
                title = "Nombre de procès-verbaux dressés en mer (législation pêche)",
                value = data.illegalFish?.nbrOfInfractionWithPV ?: 0.0,
            ),
            AEMMetricOutput(
                id = "4.3.7",
                title = "Nombre d'infractions constatées en mer",
                value = data.illegalFish?.nbrOfInfraction ?: 0.0,
            ),
            AEMMetricOutput(
                id = "4.3.8",
                title = "Nombre de navires accompagnés ou déroutés",
                value = data.illegalFish?.nbrOfSeizureAndDiversionVessel ?: 0.0,
            ),
            AEMMetricOutput(
                id = "4.3.9",
                title = "quantitée de produits de la pêche saisis/rejetés en mer(en kg)",
                value = data.illegalFish?.quantityOfFish ?: 0.0,
            ),
            AEMMetricOutput(
                id = "7.1",
                title = "Nombre d’heures de mer de surveillance générale des approches maritimes (ZEE)",
                value = data.sovereignProtect?.nbrOfHourAtSea ?: 0.0,
            ),
            AEMMetricOutput(
                id = "7.3",
                title = "Nombre total de navires reconnus dans les approches maritimes (ZEE)",
                value = data.sovereignProtect?.nbrOfRecognizedVessel ?: 0.0,
            ),
            AEMMetricOutput(
                id = "7.4",
                title = "Nombre de contrôles en mer de navires (toutes zones)",
                value = data.sovereignProtect?.nbrOfControlledVessel ?: 0.0,
            ),
        )



        val output = ApiAnalyticsAEMDataOutput(
            id = missionId,
            idUUID = generalInfo?.missionIdUUID,
            serviceId = generalInfo?.serviceId,
            startDateTimeUtc = envMission?.startDateTimeUtc,
            endDateTimeUtc = envMission?.endDateTimeUtc,
            missionTypes = envMission?.missionTypes,
            controlUnits = envMission?.controlUnits,
            facade = envMission?.facade,
            isDeleted = envMission?.isDeleted,
            missionSource = envMission?.missionSource,
            data = formattedOutput


        )
        return output
    }

}
