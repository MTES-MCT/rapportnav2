package fr.gouv.gmampa.rapportnav.domain.entities.satiExport

import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiVesselComplianceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.ControlCheck
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SatiVesselComplianceEntityTest {

    @Test
    fun `should take the vessel compliance checks of a fish action`() {
        val action = MissionFishActionEntity(
            id = 1,
            missionId = 2,
            fishActionType = MissionActionType.SEA_CONTROL,
            gangwayPresentAndCompliant = ControlCheck.YES,
            emitsVms = ControlCheck.NO,
            emitsAis = ControlCheck.NOT_APPLICABLE,
            logbookOpenedPriorToControl = ControlCheck.YES,
            logbookMatchesActivity = ControlCheck.NO,
            licencesMatchActivity = ControlCheck.YES,
            europeanFishingLicenceValid = ControlCheck.NOT_APPLICABLE,
            stowagePlanPresent = ControlCheck.YES,
            onboardWeighingPermit = ControlCheck.NO,
            weighingCertificateAndSystemsValid = null,
            licencesAndLogbookObservations = "RAS",
        )

        assertThat(SatiVesselComplianceEntity.fromFishAction(action)).isEqualTo(
            SatiVesselComplianceEntity(
                gangwayPresentAndCompliant = ControlCheck.YES,
                emitsVms = ControlCheck.NO,
                emitsAis = ControlCheck.NOT_APPLICABLE,
                logbookOpenedPriorToControl = ControlCheck.YES,
                logbookMatchesActivity = ControlCheck.NO,
                licencesMatchActivity = ControlCheck.YES,
                europeanFishingLicenceValid = ControlCheck.NOT_APPLICABLE,
                stowagePlanPresent = ControlCheck.YES,
                onboardWeighingPermit = ControlCheck.NO,
                weighingCertificateAndSystemsValid = null,
                licencesAndLogbookObservations = "RAS",
            )
        )
    }
}
