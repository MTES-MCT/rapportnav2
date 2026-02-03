package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import org.slf4j.LoggerFactory
import java.time.Instant

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
        fun fromMissionAction(navActions: List<MissionNavActionEntity>,
                              envActions: List<MissionEnvActionEntity>,
                              fishActions: List<MissionFishActionEntity>,
                              missionEndDateTimeUtc: Instant?,
                              nbrOfRecognizedVessel: Double?): AEMTableExport {
            return getAemTableExport2(navActions, envActions, fishActions, missionEndDateTimeUtc, nbrOfRecognizedVessel)
        }

        private fun getAemTableExport2(
            navActions: List<MissionNavActionEntity>,
            envActions: List<MissionEnvActionEntity>,
            fishActions: List<MissionFishActionEntity>,
            missionEndDateTimeUtc: Instant?,
            nbrOfRecognizedVessel: Double?
        ): AEMTableExport {
            val tableExport = AEMTableExport(
                seaSafety = AEMSeaSafety(navActions),
                envTraffic = AEMEnvTraffic(envActions),
                illegalFish = AEMIllegalFish(fishActions, navActions, missionEndDateTimeUtc),
                vesselRescue = AEMVesselRescue(navActions),
                migrationRescue = AEMMigrationRescue(navActions),
                culturalMaritime = AEMCulturalMaritime(envActions),
                illegalImmigration = AEMIllegalImmigration(navActions),
                outOfMigrationRescue = AEMOutOfMigrationRescue(navActions),
                sovereignProtect = AEMSovereignProtect(navActions, envActions, fishActions, missionEndDateTimeUtc),
                notPollutionControlSurveillance = AEMNotPollutionControlSurveillance(envActions),
                pollutionControlSurveillance = AEMPollutionControlSurveillance(navActions, envActions)
            )
            tableExport.sovereignProtect?.nbrOfRecognizedVessel = nbrOfRecognizedVessel
            return tableExport
        }

    }
}
