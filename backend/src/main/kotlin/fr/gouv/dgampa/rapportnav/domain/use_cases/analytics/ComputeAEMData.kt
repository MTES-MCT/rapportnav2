package fr.gouv.dgampa.rapportnav.domain.use_cases.analytics

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMTableExport
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.ExportMissionAEMSingle
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeEnvMission
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.analytics.v1.adapters.output.AEMMetricOutput
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.analytics.v1.adapters.output.ApiAnalyticsAEMDataOutput
import java.time.Instant

@UseCase
class ComputeAEMData(
    private val getComputeEnvMission: GetComputeEnvMission,
    private val exportMissionAEMSingle: ExportMissionAEMSingle,
) {

    fun execute(missionId: Int): ApiAnalyticsAEMDataOutput? {
        val mission = getComputeEnvMission.execute(missionId)
        val data = exportMissionAEMSingle.getAemData(missionId) ?: return null

        val formattedOutput = METRIC_DEFINITIONS.map { def ->
            AEMMetricOutput(
                id = def.id,
                title = def.title,
                value = def.extractor(data) ?: 0.0
            )
        }

        return ApiAnalyticsAEMDataOutput(
            id = missionId,
            idUUID = mission.generalInfos?.data?.missionIdUUID,
            serviceId = mission.generalInfos?.data?.service?.id,
            startDateTimeUtc = mission.data?.startDateTimeUtc,
            endDateTimeUtc = mission.data?.endDateTimeUtc,
            missionTypes = mission.data?.missionTypes,
            controlUnits = mission.data?.controlUnits,
            facade = mission.data?.facade,
            isDeleted = mission.data?.isDeleted,
            missionSource = mission.data?.missionSource,
            completenessForStats = mission.isCompleteForStats(),
            isMissionFinished = mission.data?.endDateTimeUtc?.isBefore(Instant.now()) ?: false,
            data = formattedOutput
        )
    }

    private data class MetricDef(
        val id: String,
        val title: String,
        val extractor: (AEMTableExport) -> Double?
    )

    companion object {
        private val METRIC_DEFINITIONS = listOf(
            // --- 1.1 Out of Migration Rescue ---
            MetricDef("1.1.1", "Nombre d'heures de mer") { it.outOfMigrationRescue?.nbrOfHourAtSea },
            MetricDef("1.1.3", "Nombre d'opérations conduites") { it.outOfMigrationRescue?.nbrOfRescuedOperation },
            MetricDef("1.1.4", "Nombre de personnes secourues") { it.outOfMigrationRescue?.nbrPersonsRescued },

            // --- 1.2 Migration Rescue ---
            MetricDef("1.2.1", "Nombre d'heures de mer") { it.migrationRescue?.nbrOfHourAtSea },
            MetricDef("1.2.3", "Nombre d'opérations conduites") { it.migrationRescue?.nbrOfOperation },
            MetricDef("1.2.4", "(SAR migrants) Nombre d'embarcations suivies sans nécessité d'intervention") {
                it.migrationRescue?.nbrOfVesselsTrackedWithoutIntervention
            },
            MetricDef("1.2.5", "(SAR migrants)Nombre d'embarcations assistées pour un retour à terre") {
                it.migrationRescue?.nbrAssistedVesselsReturningToShore
            },
            MetricDef("1.2.6", "(SAR migrants) Nombre d'opérations de sauvetage conduites") {
                it.migrationRescue?.nbrOfRescuedOperation
            },
            MetricDef("1.2.7", "(SAR migrants)Nombre de personnes secourues") {
                it.migrationRescue?.nbrPersonsRescued
            },

            // --- 2 Vessel Rescue ---
            MetricDef("2.1", "Nombre d'heures de mer") { it.vesselRescue?.nbrOfHourAtSea },
            MetricDef("2.3", "Nombre d'opérations ANED (mise en œuvre de moyens nautique ou aérien)") {
                it.vesselRescue?.nbrOfRescuedOperation
            },
            MetricDef("2.4", "Nombre d'intervention faisant suite à une mise en demeure") {
                it.vesselRescue?.nbrOfNoticedVessel
            },
            MetricDef("2.7", "Nombre de remorquages") { it.vesselRescue?.nbrOfTowedVessel },

            // --- 3.3 Environmental Traffic ---
            MetricDef("3.3.1", "Nombre d'heures de mer") { it.envTraffic?.nbrOfHourAtSea },
            MetricDef("3.3.3", "Nombre de navires ou embarcations déroutés ou saisis en mer") {
                it.envTraffic?.nbrOfRedirectShip
            },
            MetricDef("3.3.4", "Nombre de saisies") { it.envTraffic?.nbrOfSeizure },

            // --- 4.1 Not Pollution Control Surveillance ---
            MetricDef(
                "4.1.1",
                "Nombre d'heures de mer de surveillance ou de contrôle pour la protection de l'environnement (hors rejets illicites)"
            ) { it.notPollutionControlSurveillance?.nbrOfHourAtSea },
            MetricDef(
                "4.1.3",
                "Nombre d'opérations de surveillance ou de contrôles (hors rejets illicites)"
            ) { it.notPollutionControlSurveillance?.nbrOfAction },
            MetricDef(
                "4.1.4",
                "Nombre d’infractions à la réglementation relative à la protection de l'environnement en mer (hors rejets illicites)"
            ) { it.notPollutionControlSurveillance?.nbrOfInfraction },
            MetricDef(
                "4.1.5",
                "Nombre de Procès-Verbaux dressés en mer (hors rejets illicites)"
            ) { it.notPollutionControlSurveillance?.nbrOfInfractionWithNotice },

            // --- 4.2 Pollution Control Surveillance ---
            MetricDef("4.2.1", "Nombre d'heures de mer (surveillance et lutte)") {
                it.pollutionControlSurveillance?.nbrOfHourAtSea
            },
            MetricDef(
                "4.2.3",
                "Participation à une opération de lutte ANTIPOL en mer (simple brassage)"
            ) { it.pollutionControlSurveillance?.nbrOfSimpleBrewingOperation },
            MetricDef(
                "4.2.4",
                "Déploiement d’un dispositif de lutte anti-pollution en mer (dispersant, barrage, etc…)"
            ) { it.pollutionControlSurveillance?.nbrOfAntiPolDeviceDeployed },
            MetricDef("4.2.5", "Nombre d'infractions constatées") {
                it.pollutionControlSurveillance?.nbrOfInfraction
            },
            MetricDef("4.2.6", "Nombre de procès-verbaux dressés") {
                it.pollutionControlSurveillance?.nbrOfInfractionWithNotice
            },
            MetricDef("4.2.7", "Nombre de déroutements effectués") {
                it.pollutionControlSurveillance?.nbrOfDiversionCarriedOut
            },
            MetricDef(
                "4.2.8",
                "Nombre de pollutions détectées et/ou constatées par un agent habilité"
            ) { it.pollutionControlSurveillance?.nbrOfPollutionObservedByAuthorizedAgent },

            // --- 4.3 Illegal Fish ---
            MetricDef("4.3.1", "Nombre d’heures de mer (surveillance/police des pêches)") {
                it.illegalFish?.nbrOfHourAtSea
            },
            MetricDef("4.3.3", "nombre d'opérations POLPECHE") {
                it.illegalFish?.nbrOfPolFishAction
            },
            MetricDef("4.3.5", "Nombre de navires inspectés en mer (montée à bord)") {
                it.illegalFish?.nbrOfTargetedVessel
            },
            MetricDef("4.3.6", "Nombre de procès-verbaux dressés en mer (législation pêche)") {
                it.illegalFish?.nbrOfInfractionWithPV
            },
            MetricDef("4.3.7", "Nombre d'infractions constatées en mer") {
                it.illegalFish?.nbrOfInfraction
            },
            MetricDef("4.3.8", "Nombre de navires accompagnés ou déroutés") {
                it.illegalFish?.nbrOfSeizureAndDiversionVessel
            },
            MetricDef(
                "4.3.9",
                "Quantité de produits de la pêche saisis/rejetés en mer(en kg)"
            ) { it.illegalFish?.quantityOfFish },

            // --- 7 Sovereign Protect ---
            MetricDef(
                "7.1",
                "Nombre d’heures de mer de surveillance générale des approches maritimes (ZEE)"
            ) { it.sovereignProtect?.nbrOfHourAtSea },
            MetricDef(
                "7.3",
                "Nombre total de navires reconnus dans les approches maritimes (ZEE)"
            ) { it.sovereignProtect?.nbrOfRecognizedVessel },
            MetricDef(
                "7.4",
                "Nombre de contrôles en mer de navires (toutes zones)"
            ) { it.sovereignProtect?.nbrOfControlledVessel }
        )
    }
}
