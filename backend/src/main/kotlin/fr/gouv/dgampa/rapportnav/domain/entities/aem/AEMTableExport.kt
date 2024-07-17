package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
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

            val rescueActions = actionRescueEntities(navActions)
            val illegalActions = actionIllegalImmigrationEntities(navActions)
            val navAntiPollutionActions = actionAntiPollutionEntities(navActions)

            val vigimerActions = actionVigimerEntities(navActions)
            val nauticalActions = actionNauticalEventEntities(navActions)
            val baaemActions = actionBAAEMPermanenceEntities(navActions)
            val publicActions = actionPublicOrderEntities(navActions)
            val anchoredActions = actionStatusEntities(navActions)
            val navigationActions = actionStatusEntities1(navActions)


            return AEMTableExport(
                sovereignProtect = AEMSovereignProtect(
                    anchoredActions = anchoredActions,
                    navigationActions = navigationActions
                ),
                seaSafety = AEMSeaSafety(
                    baaemActions = baaemActions,
                    publicActions = publicActions,
                    vigimerActions = vigimerActions,
                    nauticalActions = nauticalActions
                ),
                envTraffic = AEMEnvTraffic(envActions),
                illegalFish = AEMIllegalFish(fishActions),
                culturalMaritime = AEMCulturalMaritime(envActions),
                illegalImmigration = AEMIllegalImmigration(illegalActions),
                notPollutionControlSurveillance = AEMNotPollutionControlSurveillance(envActions),
                pollutionControlSurveillance = AEMPollutionControlSurveillance(navAntiPollutionActions, envActions),
                vesselRescue = AEMVesselRescue(rescueActions.filter { it?.isVesselRescue == true }),
                migrationRescue = AEMMigrationRescue(rescueActions.filter { it?.isMigrationRescue == true }),
                outOfMigrationRescue = AEMOutOfMigrationRescue(rescueActions.filter { it?.isMigrationRescue != true }),
            )
        }

        private fun actionStatusEntities1(navActions: List<NavActionEntity>): List<ActionStatusEntity?> {
            return navActions.filter { it.statusAction != null }.map { action -> action.statusAction }
                .filter { it?.status == ActionStatusType.NAVIGATING }
        }

        private fun actionStatusEntities(navActions: List<NavActionEntity>): List<ActionStatusEntity?> {
            return navActions.filter { it.statusAction != null }.map { action -> action.statusAction }
                .filter { it?.status == ActionStatusType.ANCHORED };
        }

        private fun actionPublicOrderEntities(navActions: List<NavActionEntity>): List<ActionPublicOrderEntity?> {
            return navActions.filter { it.publicOrderAction != null }
                .map { action -> action.publicOrderAction }
        }

        private fun actionBAAEMPermanenceEntities(navActions: List<NavActionEntity>): List<ActionBAAEMPermanenceEntity?> {
            return navActions.filter { it.baaemPermanenceAction != null }
                .map { action -> action.baaemPermanenceAction }
        }

        private fun actionNauticalEventEntities(navActions: List<NavActionEntity>): List<ActionNauticalEventEntity?> {
            return navActions.filter { it.nauticalEventAction != null }
                .map { action -> action.nauticalEventAction };
        }

        private fun actionVigimerEntities(navActions: List<NavActionEntity>): List<ActionVigimerEntity?> {
            return navActions.filter { it.vigimerAction != null }
                .map { action -> action.vigimerAction };
        }

        private fun actionAntiPollutionEntities(navActions: List<NavActionEntity>): List<ActionAntiPollutionEntity?> {
            return navActions.filter { it.antiPollutionAction != null }.map { action -> action.antiPollutionAction };
        }

        private fun actionIllegalImmigrationEntities(navActions: List<NavActionEntity>): List<ActionIllegalImmigrationEntity?> {
            return navActions.filter { it.illegalImmigrationAction != null }
                .map { action -> action.illegalImmigrationAction };
        }

        private fun actionRescueEntities(navActions: List<NavActionEntity>): List<ActionRescueEntity?> {
            return navActions.filter { it.rescueAction != null }.map { action -> action.rescueAction };
        }
    }
}
