package fr.gouv.gmampa.rapportnav.domain.entities.satiExport

import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiSpeciesInspectionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.ControlCheck
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.DiscardReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.DiscardedSpeciesControl
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.SpeciesOnboardControl
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SatiSpeciesInspectionEntityTest {

    @Test
    fun `should take the species inspection of a fish action`() {
        val species = SpeciesOnboardControl().apply { speciesCode = "HKE" }
        val discard = DiscardedSpeciesControl(speciesCode = "NEP", rejectedWeight = 5.0, discardReason = DiscardReason.DIM)
        val action = MissionFishActionEntity(
            id = 1,
            missionId = 2,
            fishActionType = MissionActionType.SEA_CONTROL,
            speciesWeightControlled = ControlCheck.YES,
            speciesSizeControlled = ControlCheck.NO,
            separateStowageOfPreservedSpecies = ControlCheck.NOT_APPLICABLE,
            underSizedSeparateStowage = ControlCheck.YES,
            underSizedSeparateRecording = null,
            speciesOnboard = listOf(species),
            speciesObservations = "RAS",
            discardedSpecies = listOf(discard),
        )

        assertThat(SatiSpeciesInspectionEntity.fromFishAction(action)).isEqualTo(
            SatiSpeciesInspectionEntity(
                speciesWeightControlled = ControlCheck.YES,
                speciesSizeControlled = ControlCheck.NO,
                separateStowageOfPreservedSpecies = ControlCheck.NOT_APPLICABLE,
                underSizedSeparateStowage = ControlCheck.YES,
                underSizedSeparateRecording = null,
                species = listOf(species),
                speciesObservations = "RAS",
                discards = listOf(discard),
            )
        )
    }

    @Test
    fun `discarded species name should be the one sent by MonitorFish`() {
        val discard = DiscardedSpeciesControl(speciesCode = "BIB", speciesName = "TACAUD COMMUN")

        assertThat(SatiSpeciesInspectionEntity().speciesName(discard)).isEqualTo("TACAUD COMMUN")
    }

    @Test
    fun `discarded species name should fall back on the name of the species on board with that code`() {
        val inspection = SatiSpeciesInspectionEntity(
            species = listOf(SpeciesOnboardControl().apply { speciesCode = "HKE"; speciesName = "Merlu européen" })
        )

        assertThat(inspection.speciesName(DiscardedSpeciesControl(speciesCode = "HKE"))).isEqualTo("Merlu européen")
        assertThat(inspection.speciesName(DiscardedSpeciesControl(speciesCode = "BIB"))).isNull()
    }
}
