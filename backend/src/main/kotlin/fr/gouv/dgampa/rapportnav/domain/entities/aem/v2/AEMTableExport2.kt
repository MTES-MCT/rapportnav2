package fr.gouv.dgampa.rapportnav.domain.entities.aem.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import org.slf4j.LoggerFactory
import java.time.Instant

data class AEMTableExport2(
    val outOfMigrationRescue: AEMOutOfMigrationRescue2?, //1.1
    val migrationRescue: AEMMigrationRescue2?, //1.2
    val vesselRescue: AEMVesselRescue2?, // 2
    val envTraffic: AEMEnvTraffic2?, // 3.3
    val illegalImmigration: AEMIllegalImmigration2?, // 3.4
    val notPollutionControlSurveillance: AEMNotPollutionControlSurveillance2?, //4.1
    val pollutionControlSurveillance: AEMPollutionControlSurveillance2?, // 4.2
    val illegalFish: AEMIllegalFish2?, // 4.3
    val culturalMaritime: AEMCulturalMaritime2?, //4.4
    val seaSafety: AEMSeaSafety2?, // 5.
    val sovereignProtect: AEMSovereignProtect2? // 7.
) {
    private val logger = LoggerFactory.getLogger(AEMTableExport2::class.java)

    companion object {
        fun fromMissionAction(navActions: List<MissionNavActionEntity>,
                              envActions: List<MissionEnvActionEntity>,
                              fishActions: List<MissionFishActionEntity>,
                              missionEndDateTimeUtc: Instant?,
                              nbrOfRecognizedVessel: Double?): AEMTableExport2 {
            return getAemTableExport2(navActions, envActions, fishActions, missionEndDateTimeUtc, nbrOfRecognizedVessel)
        }

        private fun getAemTableExport2(
            navActions: List<MissionNavActionEntity>,
            envActions: List<MissionEnvActionEntity>,
            fishActions: List<MissionFishActionEntity>,
            missionEndDateTimeUtc: Instant?,
            nbrOfRecognizedVessel: Double?
        ): AEMTableExport2 {
            val tableExport = AEMTableExport2(
                seaSafety = AEMSeaSafety2(navActions),
                envTraffic = AEMEnvTraffic2(envActions),
                illegalFish = AEMIllegalFish2(fishActions, navActions, missionEndDateTimeUtc),
                vesselRescue = AEMVesselRescue2(navActions),
                migrationRescue = AEMMigrationRescue2(navActions),
                culturalMaritime = AEMCulturalMaritime2(envActions),
                illegalImmigration = AEMIllegalImmigration2(navActions),
                outOfMigrationRescue = AEMOutOfMigrationRescue2(navActions),
                sovereignProtect = AEMSovereignProtect2(navActions, envActions, fishActions, missionEndDateTimeUtc),
                notPollutionControlSurveillance = AEMNotPollutionControlSurveillance2(envActions),
                pollutionControlSurveillance = AEMPollutionControlSurveillance2(navActions, envActions)
            )
            tableExport.sovereignProtect?.nbrOfRecognizedVessel = nbrOfRecognizedVessel
            return tableExport
        }

    }
}
