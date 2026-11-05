package fr.gouv.dgampa.rapportnav.domain.entities.satiExport

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.ControlCheck
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity

/**
 * Vessel compliance checks of a sea control, as filled in MonitorFish. A null check was not answered.
 */
data class SatiVesselComplianceEntity(
    val gangwayPresentAndCompliant: ControlCheck? = null,
    val emitsVms: ControlCheck? = null,
    val emitsAis: ControlCheck? = null,
    val logbookOpenedPriorToControl: ControlCheck? = null,
    val logbookMatchesActivity: ControlCheck? = null,
    val licencesMatchActivity: ControlCheck? = null,
    val europeanFishingLicenceValid: ControlCheck? = null,
    val stowagePlanPresent: ControlCheck? = null,
    val onboardWeighingPermit: ControlCheck? = null,
    val weighingCertificateAndSystemsValid: ControlCheck? = null,
    val licencesAndLogbookObservations: String? = null,
) {
    companion object {
        fun fromFishAction(action: MissionFishActionEntity) = SatiVesselComplianceEntity(
            gangwayPresentAndCompliant = action.gangwayPresentAndCompliant,
            emitsVms = action.emitsVms,
            emitsAis = action.emitsAis,
            logbookOpenedPriorToControl = action.logbookOpenedPriorToControl,
            logbookMatchesActivity = action.logbookMatchesActivity,
            licencesMatchActivity = action.licencesMatchActivity,
            europeanFishingLicenceValid = action.europeanFishingLicenceValid,
            stowagePlanPresent = action.stowagePlanPresent,
            onboardWeighingPermit = action.onboardWeighingPermit,
            weighingCertificateAndSystemsValid = action.weighingCertificateAndSystemsValid,
            licencesAndLogbookObservations = action.licencesAndLogbookObservations,
        )
    }
}
