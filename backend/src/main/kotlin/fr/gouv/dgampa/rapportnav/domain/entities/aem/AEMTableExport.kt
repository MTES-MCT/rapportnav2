package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionActionEntity
import org.slf4j.LoggerFactory

data class AEMTableExport(
    val outOfMigrationRescue: AEMOutOfMigrationRescue?, //1.1
    val migrationRescue: AEMMigrationRescue?, //1.2
    val vesselRescue: AEMVesselRescue?, // 2
    val envTraffic: AEMEnvTraffic?, // 3.3
    val illegalImmigration: AEMIllegalImmigration?, // 3.4
    val notPollutionControlSurveillance: AEMNotPollutionControlSurveillance?, //4.1
    val pollutionControlSurveillance: AEMPollutionControlSurveillance?, // 4.2
    val illegalFish: AEMIllegalFish?, // 4.3
    val culturalMaritime: AEMCulturalMaritime?, //4.4
    val seaSafety: AEMSeaSafety?, // 5.
    val sovereignProtect: AEMSovereignProtect? // 7.
) {
    private val logger = LoggerFactory.getLogger(AEMTableExport::class.java)


    companion object {

        fun fromMissionAction(actions: List<MissionActionEntity?>): AEMTableExport {
            val navActions = actions.filterIsInstance<MissionActionEntity.NavAction>().map { it.navAction };
            val envActions = actions.filterIsInstance<MissionActionEntity.EnvAction>().map { it.envAction };
            val fishActions = actions.filterIsInstance<MissionActionEntity.FishAction>().map { it.fishAction };

            return AEMTableExport(
                seaSafety = AEMSeaSafety(navActions),
                envTraffic = AEMEnvTraffic(envActions),
                illegalFish = AEMIllegalFish(fishActions),
                vesselRescue = AEMVesselRescue(navActions),
                migrationRescue = AEMMigrationRescue(navActions),
                culturalMaritime = AEMCulturalMaritime(envActions),
                illegalImmigration = AEMIllegalImmigration(navActions),
                outOfMigrationRescue = AEMOutOfMigrationRescue(navActions),
                sovereignProtect = AEMSovereignProtect(navActions, envActions, fishActions),
                notPollutionControlSurveillance = AEMNotPollutionControlSurveillance(envActions),
                pollutionControlSurveillance = AEMPollutionControlSurveillance(navActions, envActions)
            )
        }

    }
}
