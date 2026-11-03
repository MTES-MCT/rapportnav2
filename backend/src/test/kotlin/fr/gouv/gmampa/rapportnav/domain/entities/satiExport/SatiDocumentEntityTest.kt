package fr.gouv.gmampa.rapportnav.domain.entities.satiExport

import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiDocumentEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.ContactEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiModuleType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiPartyEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiVesselEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate

class SatiDocumentEntityTest {

    private val document = SatiDocumentEntity(
        module = SatiModuleType.M1,
        vesselName = "Fleur de lys",
        serviceName = " ULAM 56 ",
        inspectorFirstName = "Jean",
        inspectorLastName = "Dupont",
        startDatetimeUtc = Instant.parse("2026-12-31T10:00:00Z"),
        endDatetimeUtc = null,
        latitude = null,
        longitude = null,
        vessel = null,
    )

    @Test
    fun `reference should join module, service, vessel and date in upper snake case`() {
        assertThat(document.reference).isEqualTo("M1_ULAM_56_FLEUR_DE_LYS_2026-12-31")
    }

    @Test
    fun `reference should flag missing values`() {
        val empty = document.copy(vesselName = null, serviceName = null, startDatetimeUtc = null)

        assertThat(empty.reference).isEqualTo("M1_INCONNU_INCONNU_INCONNU")
    }

    @Test
    fun `control date should be the day in Paris`() {
        val lateEvening = document.copy(startDatetimeUtc = Instant.parse("2026-12-31T23:30:00Z"))

        assertThat(lateEvening.controlDate).isEqualTo(LocalDate.of(2027, 1, 1))
    }

    @Test
    fun `operator and agent should be absent unless their identity is filled in`() {
        // the operator is always built from MonitorFish data, possibly without any name
        val vessel = SatiVesselEntity(
            operator = SatiPartyEntity(contact = ContactEntity(fullName = " ")),
            agent = null,
        )

        assertThat(document.copy(vessel = vessel).operator).isNull()
        assertThat(document.copy(vessel = vessel).agent).isNull()
    }

    @Test
    fun `operator and agent should be the contacts of their party when filled in`() {
        val operator = ContactEntity(fullName = "Armement Breton")
        val agent = ContactEntity(fullName = "Paul Agent")
        val vessel = SatiVesselEntity(
            operator = SatiPartyEntity(contact = operator),
            agent = SatiPartyEntity(contact = agent),
        )

        assertThat(document.copy(vessel = vessel).operator).isEqualTo(operator)
        assertThat(document.copy(vessel = vessel).agent).isEqualTo(agent)
    }

    @Test
    fun `inspector name should join the known names`() {
        assertThat(document.inspectorName).isEqualTo("Jean Dupont")
        assertThat(document.copy(inspectorFirstName = null).inspectorName).isEqualTo("Dupont")
        assertThat(document.copy(inspectorFirstName = null, inspectorLastName = null).inspectorName).isNull()
    }
}
