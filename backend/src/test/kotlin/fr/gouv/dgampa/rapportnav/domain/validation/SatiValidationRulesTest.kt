package fr.gouv.dgampa.rapportnav.domain.validation

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.AddressEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.ContactEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiInspectorEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiJpeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiModuleType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiPartyEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiVesselEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Instant

class SatiValidationRulesTest {

    private val validator = EntityValidityValidator.createDefault()

    private fun validAddress() = AddressEntity(
        id = 1, street = "1 rue de la Paix", fullAddress = "1 rue de la Paix, 75000 Paris",
        zipcode = "75000", town = "Paris", country = "FR", lng = 2.0, lat = 48.0
    )

    private fun validContact() = ContactEntity(
        id = 1, fullName = "Jean Martin", firstName = "Jean", lastName = "Martin",
        nationality = "FR", email = "jean@example.com", phone = "0102030405",
        address = validAddress()
    )

    private fun validJpe() = SatiJpeEntity(
        pnoId = "PNO-1", tripNumber = "TRIP-1", portId = "FRPAR",
        lastStopDate = Instant.parse("2026-07-01T00:00:00Z"),
        lastPortIsNotSame = false
    )

    private fun validVessel(jpe: SatiJpeEntity? = validJpe()) = SatiVesselEntity(
        id = 1, name = "SS Test", immat = "IMM-1",
        master = SatiPartyEntity(contact = validContact()),
        jpe = jpe
    )

    private fun satiM1(
        vessel: SatiVesselEntity? = validVessel(),
        resourceId: Int? = 42,
        inspectorAgentId: Int? = 7
    ) = SatiEntity(
        module = SatiModuleType.M1,
        actionId = "action-1",
        resource = ControlResourceEntity(id = resourceId),
        vessel = vessel,
        inspectors = listOf(SatiInspectorEntity(agentId = inspectorAgentId))
    )

    private fun fishAction(sati: SatiEntity? = satiM1()) = MissionFishActionEntity(
        id = 1,
        missionId = 100,
        fishActionType = MissionActionType.SEA_CONTROL,
        actionDatetimeUtc = Instant.parse("2026-07-01T10:00:00Z"),
        actionEndDatetimeUtc = Instant.parse("2026-07-01T12:00:00Z"),
        sati = sati
    ).apply {
        startDateTimeUtc = actionDatetimeUtc
        endDateTimeUtc = actionEndDatetimeUtc
    }

    // ---------------------------------------------------------------------
    // Tests
    // ---------------------------------------------------------------------

    @Test
    fun `happy path — fully populated SATI M1 produces no violations`() {
        val result = validator.validateCompleteness(fishAction(), ValidationPolicies.v1)
        assertTrue(result.isComplete, "unexpected errors: ${result.errors}")
    }

    @Test
    fun `SATI missing entirely — no SATI-scoped violations`() {
        val result = validator.validateCompleteness(fishAction(sati = null), ValidationPolicies.v1)
        val satiErrs = result.errors.filter { it.field.startsWith("sati.") }
        assertTrue(satiErrs.isEmpty(), "unexpected SATI errors: $satiErrs")
    }

    @Test
    fun `M1 - missing resource id fires`() {
        val entity = fishAction(sati = satiM1(resourceId = null))
        val result = validator.validateCompleteness(entity, ValidationPolicies.v1)
        assertTrue(result.errors.any { it.field == "sati.resource.id" })
    }

    @Test
    fun `M1 - missing principal inspector agentId fires`() {
        val entity = fishAction(sati = satiM1(inspectorAgentId = null))
        val result = validator.validateCompleteness(entity, ValidationPolicies.v1)
        assertTrue(result.errors.any { it.field == "sati.inspectors[0].agentId" })
    }

    @Test
    fun `M1 - missing captain email fires at nested path`() {
        val brokenContact = validContact().copy(email = null)
        val vessel = validVessel().copy(master = SatiPartyEntity(contact = brokenContact))
        val entity = fishAction(sati = satiM1(vessel = vessel))
        val result = validator.validateCompleteness(entity, ValidationPolicies.v1)
        assertTrue(result.errors.any { it.field == "sati.vessel.master.contact.email" })
    }

    @Test
    fun `M1 - missing address zipcode fires at deepest path`() {
        val brokenAddress = validAddress().copy(zipcode = null)
        val brokenContact = validContact().copy(address = brokenAddress)
        val vessel = validVessel().copy(master = SatiPartyEntity(contact = brokenContact))
        val entity = fishAction(sati = satiM1(vessel = vessel))
        val result = validator.validateCompleteness(entity, ValidationPolicies.v1)
        assertTrue(result.errors.any { it.field == "sati.vessel.master.contact.address.zipcode" })
    }

    @Test
    fun `JPE - tripNumber required when pnoId is null`() {
        val jpe = validJpe().copy(pnoId = null, tripNumber = null)
        val vessel = validVessel(jpe = jpe)
        val entity = fishAction(sati = satiM1(vessel = vessel))
        val result = validator.validateCompleteness(entity, ValidationPolicies.v1)
        assertTrue(result.errors.any { it.field == "sati.vessel.jpe.tripNumber" })
    }

    @Test
    fun `JPE - tripNumber NOT required when pnoId is present`() {
        val jpe = validJpe().copy(pnoId = "PNO-1", tripNumber = null)
        val vessel = validVessel(jpe = jpe)
        val entity = fishAction(sati = satiM1(vessel = vessel))
        val result = validator.validateCompleteness(entity, ValidationPolicies.v1)
        assertFalse(result.errors.any { it.field == "sati.vessel.jpe.tripNumber" })
    }

    @Test
    fun `JPE - lastStopDate and portId required when lastPortIsNotSame is true`() {
        val jpe = validJpe().copy(lastPortIsNotSame = true, lastStopDate = null, portId = null)
        val vessel = validVessel(jpe = jpe)
        val entity = fishAction(sati = satiM1(vessel = vessel))
        val result = validator.validateCompleteness(entity, ValidationPolicies.v1)
        assertTrue(result.errors.any { it.field == "sati.vessel.jpe.lastStopDate" })
        assertTrue(result.errors.any { it.field == "sati.vessel.jpe.portId" })
    }

    @Test
    fun `M5 - vessel rules skipped when module is neither M1 nor M3`() {
        val brokenContact = validContact().copy(email = null)
        val vessel = validVessel().copy(master = SatiPartyEntity(contact = brokenContact))
        val sati = SatiEntity(
            module = SatiModuleType.M5,
            actionId = "action-1",
            vessel = vessel,
            resource = ControlResourceEntity(id = 42),
            inspectors = listOf(SatiInspectorEntity(agentId = 7))
        )
        val result = validator.validateCompleteness(fishAction(sati), ValidationPolicies.v1)
        assertFalse(result.errors.any { it.field.startsWith("sati.vessel.") })
    }
}
