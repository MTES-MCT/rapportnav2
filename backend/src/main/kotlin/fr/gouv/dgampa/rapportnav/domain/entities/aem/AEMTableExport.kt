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
        fun fromMissionAction(actions: List<MissionActionEntity>): AEMTableExport {
            val navActions = actions.filterIsInstance<MissionActionEntity.NavAction>().map { it.navAction };
            val envActions = actions.filterIsInstance<MissionActionEntity.EnvAction>().map { it.envAction };
            val fishActions = actions.filterIsInstance<MissionActionEntity.FishAction>().map { it.fishAction };

            return AEMTableExport(
                migrationRescue = AEMTableExport.getMigrationRescue(navActions), //actionRescues
                outOfMigrationRescue = AEMTableExport.getOutOfMigrationRescue(navActions), //actionRescues
                vesselRescue = AEMTableExport.getVesselRescue(navActions), //actionRescues
                envTraffic = AEMTableExport.getAgainstTraffic(envActions), //envActions
                illegalImmigration = AEMTableExport.getIllegalImmigration(navActions), //illegalImmigrationActions
                notPollutionControlSurveillance = AEMTableExport.getNotPollutionControlSurveillance(envActions), //envActions
                pollutionControlSurveillance = AEMTableExport.getPollutionControlSurveillance(
                    navActions,
                    envActions
                ), //antiPollutionActions
                illegalFish = AEMTableExport.getIllegalFish(fishActions), //fishActions
                culturalMaritime = AEMTableExport.getCulturalMaritime(envActions), //envActions
                seaSafety = AEMTableExport.getSeaSafety(navActions), //vigimerActions, publicOrderActions, nauticalActions, baaempActions
                sovereignProtect = AEMTableExport.getSovereignProtect(navActions)
            )
        }

        private fun getMigrationRescue(navActions: List<NavActionEntity>): AEMMigrationRescue {
            val actions = navActions.filter { it.rescueAction != null && it.rescueAction.isMigrationRescue == true }
                .map { action -> action.rescueAction };
            return AEMMigrationRescue(actions);
        }

        private fun getOutOfMigrationRescue(navActions: List<NavActionEntity>): AEMOutOfMigrationRescue {
            val actions = navActions.filter { it.rescueAction != null && it.rescueAction.isMigrationRescue != true }
                .map { action -> action.rescueAction };
            return AEMOutOfMigrationRescue(actions);
        }

        private fun getVesselRescue(navActions: List<NavActionEntity>): AEMVesselRescue {
            val actions = navActions.filter { it.rescueAction != null && it.rescueAction.isVesselRescue == true }
                .map { action -> action.rescueAction };
            return AEMVesselRescue(actions);
        }

        private fun getAgainstTraffic(actions: List<ExtendedEnvActionEntity?>): AEMEnvTraffic {
            return AEMEnvTraffic();
            /**
             *
             * 3.3.1  Nombre d’heures de mer	Contrôle + surveillance thématique "espèces protégées" - importé de MonitorEnv	Durée (date et heure de début et fin)
             * 3.3.3 Nombre de navires ou embarcations déroutés ou saisis en mer	Contrôle + surveillance thématique "espèces protégées" - importé de MonitorEnv	Nombre de navires déroutés
             * 3.3.4 Nombre de saisies	Contrôle + surveillance thématique "espèces protégées" - importé de MonitorEnv	Nb infractions ?
             */
            /**
             *
             * 3.3.1  Nombre d’heures de mer	Contrôle + surveillance thématique "espèces protégées" - importé de MonitorEnv	Durée (date et heure de début et fin)
             * 3.3.3 Nombre de navires ou embarcations déroutés ou saisis en mer	Contrôle + surveillance thématique "espèces protégées" - importé de MonitorEnv	Nombre de navires déroutés
             * 3.3.4 Nombre de saisies	Contrôle + surveillance thématique "espèces protégées" - importé de MonitorEnv	Nb infractions ?
             */
        }

        private fun getIllegalImmigration(navActions: List<NavActionEntity>): AEMIllegalImmigration {
            val actions = navActions.filter { it.illegalImmigrationAction != null }
                .map { action -> action.illegalImmigrationAction };
            return AEMIllegalImmigration(); //AEMVesselRescue(actionRescues.filter { it.isVesselRescue == true });
            /**
             *
             * 3.4.1 Nombre d’heures de mer	Autre activité de mission + Opération de lutte contre l'immigration illégale 	Durée (date et heure de début et fin)
             * 3.4.3 Nombre de navires/embarcations interceptés 	Autre activité de mission + Opération de lutte contre l'immigration illégale 	Champ nombre "Nb de navires / embarcations interceptés"
             * 3.4.4 Nombre de migrants interceptés 	Autre activité de mission + Opération de lutte contre l'immigration illégale 	Champ nombre "Nb de migrants interceptés"
             * 3.4.5 Nombre de passeurs présumés interceptés	Autre activité de mission + Opération de lutte contre l'immigration illégale 	Champ nombre "Nb de passeurs présumés"
             */
            /**
             *
             * 3.4.1 Nombre d’heures de mer	Autre activité de mission + Opération de lutte contre l'immigration illégale 	Durée (date et heure de début et fin)
             * 3.4.3 Nombre de navires/embarcations interceptés 	Autre activité de mission + Opération de lutte contre l'immigration illégale 	Champ nombre "Nb de navires / embarcations interceptés"
             * 3.4.4 Nombre de migrants interceptés 	Autre activité de mission + Opération de lutte contre l'immigration illégale 	Champ nombre "Nb de migrants interceptés"
             * 3.4.5 Nombre de passeurs présumés interceptés	Autre activité de mission + Opération de lutte contre l'immigration illégale 	Champ nombre "Nb de passeurs présumés"
             */
        }

        private fun getNotPollutionControlSurveillance(envActions: List<ExtendedEnvActionEntity?>): AEMNotPollutionControlSurveillance {
            /**
             * 4.1.1 Nombre d'heures de mer de surveillance ou de contrôle pour la protection de l'environnement (hors rejets illicites)	Contrôle + surveillance hors thématique "Rejets illicites" - importé de MonitorEnv	Durée (date et heure de début et fin)
             * 4.1.3 Nombre d'opérations de surveillance ou de contrôles (hors rejets illicites)	Contrôle + surveillance hors thématique "Rejets illicites" - importé de MonitorEnv	Nb Action
             * 4.1.4 Nombre d’infractions à la réglementation relative à la protection de l'environnement en mer (hors rejets illicites)	Contrôle  hors thématique "Rejets illicites" - importé de MonitorEnv	Champ infraction - issu des contrôles MonitorEnv
             * 4.1.5 Nombre de Procès-Verbaux dressés en mer (hors rejets illicites)	Contrôle  hors thématique "Rejets illicites" - importé de MonitorEnv	Champ infraction + bouton radio "avec PV"
             */
            /**
             * 4.1.1 Nombre d'heures de mer de surveillance ou de contrôle pour la protection de l'environnement (hors rejets illicites)	Contrôle + surveillance hors thématique "Rejets illicites" - importé de MonitorEnv	Durée (date et heure de début et fin)
             * 4.1.3 Nombre d'opérations de surveillance ou de contrôles (hors rejets illicites)	Contrôle + surveillance hors thématique "Rejets illicites" - importé de MonitorEnv	Nb Action
             * 4.1.4 Nombre d’infractions à la réglementation relative à la protection de l'environnement en mer (hors rejets illicites)	Contrôle  hors thématique "Rejets illicites" - importé de MonitorEnv	Champ infraction - issu des contrôles MonitorEnv
             * 4.1.5 Nombre de Procès-Verbaux dressés en mer (hors rejets illicites)	Contrôle  hors thématique "Rejets illicites" - importé de MonitorEnv	Champ infraction + bouton radio "avec PV"
             */
            return AEMNotPollutionControlSurveillance();
        }

        private fun getPollutionControlSurveillance(
            navActions: List<NavActionEntity>,
            envActions: List<ExtendedEnvActionEntity?>
        ): AEMPollutionControlSurveillance {
            val actions =
                navActions.filter { it.antiPollutionAction != null }.map { action -> action.antiPollutionAction };
            return AEMPollutionControlSurveillance(); //AEMVesselRescue(actionRescues.filter { it.isVesselRescue == true });
            /**
             * 4.2.1 Nombre d'heures de mer (surveillance et lutte)	Contrôle + surveillance thématique "Rejets illicites" - importé de MonitorEnv + autre activité de mission / opération de lutte anti-pollution	Durée (date et heure de début et fin)
             * 4.2.3 Participation à une opération de lutte ANTIPOL en mer (simple brassage)	Autre activité de mission / opération de lutte anti-pollution	Toggle simple brassage
             * 4.2.4 Déploiement d’un dispositif de lutte anti-pollution en mer (dispersant, barrage, etc…)	Autre activité de mission / opération de lutte anti-pollution	Toggle déploiement de dispositif
             * 4.2.5 Nombre d’infractions constatées	Contrôle + surveillance thématique "Rejets illicites" - importé de MonitorEnv	Champ infraction - issu des contrôles MonitorEnv
             * 4.2.6 Nombre de procès-verbaux dressés	Contrôle + surveillance thématique "Rejets illicites" - importé de MonitorEnv	Champ infraction + bouton radio "avec PV"
             * 4.2.7 Nombre de déroutements effectués	Contrôle + surveillance thématique "Rejets illicites" - importé de MonitorEnv + autre activité de mission / opération de lutte anti-pollution	à ajouter dans MonitorEnv / Rapp Nav case à cocher "déroutement effectué"
             * 4.2.8 Nombre de pollutions détectées et/ou constatées par un agent habilité	Contrôle + surveillance thématique "Rejets illicites" - importé de MonitorEnv + autre activité de mission / opération de lutte anti-pollution	à ajouter dans MonitorEnv / Rapp Nav case à cocher "Pollution constatée par un agent habilité"
             */
            /**
             * 4.2.1 Nombre d'heures de mer (surveillance et lutte)	Contrôle + surveillance thématique "Rejets illicites" - importé de MonitorEnv + autre activité de mission / opération de lutte anti-pollution	Durée (date et heure de début et fin)
             * 4.2.3 Participation à une opération de lutte ANTIPOL en mer (simple brassage)	Autre activité de mission / opération de lutte anti-pollution	Toggle simple brassage
             * 4.2.4 Déploiement d’un dispositif de lutte anti-pollution en mer (dispersant, barrage, etc…)	Autre activité de mission / opération de lutte anti-pollution	Toggle déploiement de dispositif
             * 4.2.5 Nombre d’infractions constatées	Contrôle + surveillance thématique "Rejets illicites" - importé de MonitorEnv	Champ infraction - issu des contrôles MonitorEnv
             * 4.2.6 Nombre de procès-verbaux dressés	Contrôle + surveillance thématique "Rejets illicites" - importé de MonitorEnv	Champ infraction + bouton radio "avec PV"
             * 4.2.7 Nombre de déroutements effectués	Contrôle + surveillance thématique "Rejets illicites" - importé de MonitorEnv + autre activité de mission / opération de lutte anti-pollution	à ajouter dans MonitorEnv / Rapp Nav case à cocher "déroutement effectué"
             * 4.2.8 Nombre de pollutions détectées et/ou constatées par un agent habilité	Contrôle + surveillance thématique "Rejets illicites" - importé de MonitorEnv + autre activité de mission / opération de lutte anti-pollution	à ajouter dans MonitorEnv / Rapp Nav case à cocher "Pollution constatée par un agent habilité"
             */
        }

        private fun getIllegalFish(fishActions: List<ExtendedFishActionEntity>): AEMIllegalFish {
            return AEMIllegalFish(); //AEMVesselRescue(actionRescues.filter { it.isVesselRescue == true });
            /**
             *
             * 4.3.1 Nombre d’heures de mer (surveillance/police des pêches) :	formulaire importé de MonitorFish	Durée (date et heure de début et fin)
             * 4.3.3 nombre d'opérations POLPECHE	formulaire importé de MonitorFish	Nb action
             * 4.3.5 Nombre de navires inspectés en mer (montée à bord)	formulaire importé de MonitorFish	Nb action
             * 4.3.6 Nombre de procès-verbaux dressés en mer (législation pêche)	formulaire importé de MonitorFish	Champ infraction + champ PV
             * 4.3.7 Nombre d'infractions constatées en mer	formulaire importé de MonitorFish	Champ infraction
             * 4.3.8 Nombre de navires accompagnés ou déroutés	formulaire importé de MonitorFish	Case à cocher "Appréhension et déroutement"
             * 4.3.9 Quantité de produits de la pêche saisis en mer(en kg)	formulaire importé de MonitorFish	Somme des champs quantitées déclarées rubrique "espèces à bord" du formulaire Fish
             */
            /**
             *
             * 4.3.1 Nombre d’heures de mer (surveillance/police des pêches) :	formulaire importé de MonitorFish	Durée (date et heure de début et fin)
             * 4.3.3 nombre d'opérations POLPECHE	formulaire importé de MonitorFish	Nb action
             * 4.3.5 Nombre de navires inspectés en mer (montée à bord)	formulaire importé de MonitorFish	Nb action
             * 4.3.6 Nombre de procès-verbaux dressés en mer (législation pêche)	formulaire importé de MonitorFish	Champ infraction + champ PV
             * 4.3.7 Nombre d'infractions constatées en mer	formulaire importé de MonitorFish	Champ infraction
             * 4.3.8 Nombre de navires accompagnés ou déroutés	formulaire importé de MonitorFish	Case à cocher "Appréhension et déroutement"
             * 4.3.9 Quantité de produits de la pêche saisis en mer(en kg)	formulaire importé de MonitorFish	Somme des champs quantitées déclarées rubrique "espèces à bord" du formulaire Fish
             */
        }

        fun getCulturalMaritime(envActions: List<ExtendedEnvActionEntity?>): AEMCulturalMaritime {
            return AEMCulturalMaritime(); //AEMVesselRescue(actionRescues.filter { it.isVesselRescue == true });
            /**
             * 4.4.1 Nombre d’heures de mer	Contrôle + surveillance thématique "Bien culturel maritime" - importé de MonitorEnv	Durée (date et heure de début et fin)
             * 4.4.2 Nombre d’opérations scientifiques	Contrôle + surveillance thématique "Autre" / SS thém. "Campagne scientifique" - importé de MonitorEnv	Nb action
             * 4.4.3 Nombre d’opération de police des BCM	Contrôle + surveillance thématique "Bien culturel maritime" - importé de MonitorEnv	Nb action
             * */
            /**
             * 4.4.1 Nombre d’heures de mer	Contrôle + surveillance thématique "Bien culturel maritime" - importé de MonitorEnv	Durée (date et heure de début et fin)
             * 4.4.2 Nombre d’opérations scientifiques	Contrôle + surveillance thématique "Autre" / SS thém. "Campagne scientifique" - importé de MonitorEnv	Nb action
             * 4.4.3 Nombre d’opération de police des BCM	Contrôle + surveillance thématique "Bien culturel maritime" - importé de MonitorEnv	Nb action
             * */
        }

        private fun getSeaSafety(navActions: List<NavActionEntity>): AEMSeaSafety {
            val vigimerActions =
                navActions.filter { it.vigimerAction != null }.map { action -> action.vigimerAction };
            val nauticalActions =
                navActions.filter { it.nauticalEventAction != null }.map { action -> action.nauticalEventAction };
            val baaemActions =
                navActions.filter { it.baaemPermanenceAction != null }.map { action -> action.baaemPermanenceAction };
            val publicActions =
                navActions.filter { it.publicOrderAction != null }.map { action -> action.publicOrderAction };
            return AEMSeaSafety(
                baaemActions = baaemActions,
                publicActions = publicActions,
                vigimerActions = vigimerActions,
                nauticalActions = nauticalActions,
            );

            /**
             * 5.1  Nombre d'heures de mer sureté maritime (y compris Vigipirate-mer)  	Autre activité de mission + Permanence Vigimer / Surveillance manifestations nautiques / Maintien de l'ordre public en mer / BAAEM	Durée (date et heure de début et fin)
             * 5.3 Nombre d'heures de mer de maintien de l'ordre public en mer	Autre activité de mission + Maintien de l'ordre public en mer 	Durée (date et heure de début et fin)
             * 5.4 Nombre d'opérations de maintien de l'ordre public en mer	Autre activité de mission + Maintien de l'ordre public en mer 	Nb action
             * */

            /**
             * 5.1  Nombre d'heures de mer sureté maritime (y compris Vigipirate-mer)  	Autre activité de mission + Permanence Vigimer / Surveillance manifestations nautiques / Maintien de l'ordre public en mer / BAAEM	Durée (date et heure de début et fin)
             * 5.3 Nombre d'heures de mer de maintien de l'ordre public en mer	Autre activité de mission + Maintien de l'ordre public en mer 	Durée (date et heure de début et fin)
             * 5.4 Nombre d'opérations de maintien de l'ordre public en mer	Autre activité de mission + Maintien de l'ordre public en mer 	Nb action
             * */
        }

        private fun getSovereignProtect(navActions: List<NavActionEntity>): AEMSovereignProtect {
            val anchoredActions =
                navActions.filter { it.statusAction != null }.map { action -> action.statusAction }
                    .filter { it?.status == ActionStatusType.ANCHORED };
            val navigationActions =
                navActions.filter { it.statusAction != null }.map { action -> action.statusAction }
                    .filter { it?.status == ActionStatusType.NAVIGATING };
            return AEMSovereignProtect(anchoredActions = anchoredActions, navigationActions = navigationActions);
            /**
             *
             * 7.1 Nombre d’heures de mer de surveillance générale des approches maritimes (ZEE)	Statuts navire	Totalité de la durée des statuts navigation et mouillage sur la mission
             * 7.3 Nombre total de navires reconnus dans les approches maritimes (ZEE)	Volet de gauche rapport	Champ "compteur" : Nombre total de navires reconnus dans les approches maritimes (ZEE)*
             * 7.4 Nombre de contrôles en mer de navires (toutes zones)	Ensemble des contrôles sur navires	Totalité des navires contrôlés : Fish + contrôles unités RPN + contrôles Env avec type de cible navire
             */
            /**
             *
             * 7.1 Nombre d’heures de mer de surveillance générale des approches maritimes (ZEE)	Statuts navire	Totalité de la durée des statuts navigation et mouillage sur la mission
             * 7.3 Nombre total de navires reconnus dans les approches maritimes (ZEE)	Volet de gauche rapport	Champ "compteur" : Nombre total de navires reconnus dans les approches maritimes (ZEE)*
             * 7.4 Nombre de contrôles en mer de navires (toutes zones)	Ensemble des contrôles sur navires	Totalité des navires contrôlés : Fish + contrôles unités RPN + contrôles Env avec type de cible navire
             */
        }


    }
}
