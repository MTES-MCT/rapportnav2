package fr.gouv.gmampa.rapportnav.domain.entities.satiExport

import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiGearInspectionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.ControlCheck
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.GearControl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SatiGearInspectionEntityTest {

    private fun gear(code: String, controlled: Boolean?, marking: ControlCheck?) = GearControl().apply {
        gearCode = code
        gearWasControlled = controlled
        gearMarkingIsCompliant = marking
    }

    @Test
    fun `controlled gears should only keep the gears that were controlled`() {
        val inspection = SatiGearInspectionEntity(
            listOf(gear("OTB", true, null), gear("GNS", false, null), gear("MIS", null, null))
        )

        assertThat(inspection.controlledGears.map { it.gearCode }).containsExactly("OTB")
    }

    @Test
    fun `gear controlled should be YES when at least one gear was controlled`() {
        assertThat(SatiGearInspectionEntity(listOf(gear("OTB", false, null), gear("GNS", true, null))).gearControlled)
            .isEqualTo(ControlCheck.YES)
        assertThat(SatiGearInspectionEntity(listOf(gear("OTB", false, null))).gearControlled).isEqualTo(ControlCheck.NO)
        assertThat(SatiGearInspectionEntity().gearControlled).isEqualTo(ControlCheck.NO)
    }

    @Test
    fun `gear marking should be NO when a controlled gear is not compliant`() {
        val inspection = SatiGearInspectionEntity(
            listOf(gear("OTB", true, ControlCheck.YES), gear("GNS", true, ControlCheck.NO))
        )

        assertThat(inspection.gearMarkingCompliant).isEqualTo(ControlCheck.NO)
    }

    @Test
    fun `gear marking should ignore the gears that were not controlled`() {
        val inspection = SatiGearInspectionEntity(
            listOf(gear("OTB", true, ControlCheck.YES), gear("GNS", false, ControlCheck.NO))
        )

        assertThat(inspection.gearMarkingCompliant).isEqualTo(ControlCheck.YES)
    }

    @Test
    fun `gear marking should be N_A when not all controlled gears are known to be compliant`() {
        assertThat(
            SatiGearInspectionEntity(listOf(gear("OTB", true, ControlCheck.YES), gear("GNS", true, null))).gearMarkingCompliant
        ).isEqualTo(ControlCheck.NOT_APPLICABLE)
        assertThat(SatiGearInspectionEntity().gearMarkingCompliant).isEqualTo(ControlCheck.NOT_APPLICABLE)
    }
}
