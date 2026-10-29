package fr.gouv.dgampa.rapportnav.domain.validation

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.SectorEstablishmentSubType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.SectorEstablishmentType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.AddressEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEstablishmentEntity
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
    fun `M1 - missing address zipcode fires at deepest path when fullAddress is also absent`() {
        val brokenAddress = validAddress().copy(fullAddress = null, zipcode = null)
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

    // ---------------------------------------------------------------------
    // M4 - establishment
    // ---------------------------------------------------------------------

    private fun validEstablishment() = SatiEstablishmentEntity(
        id = 1,
        type = SectorEstablishmentType.RESTAURANT,
        owner = SatiPartyEntity(contact = validContact())
    )

    private fun satiM4(establishment: SatiEstablishmentEntity? = validEstablishment()) = SatiEntity(
        module = SatiModuleType.M4,
        actionId = "action-1",
        resource = ControlResourceEntity(id = 42),
        inspectors = listOf(SatiInspectorEntity(agentId = 7)),
        establishment = establishment
    )

    private fun establishmentErrors(sati: SatiEntity) =
        validator.validateCompleteness(fishAction(sati), ValidationPolicies.v1).errors
            .filter { it.field.startsWith("sati.establishment") }.map { it.field }

    @Test
    fun `M4 - fully populated establishment produces no establishment violations`() {
        assertTrue(establishmentErrors(satiM4()).isEmpty())
    }

    @Test
    fun `M4 - missing establishment fires its required fields`() {
        val errors = establishmentErrors(satiM4(establishment = null))
        assertTrue("sati.establishment.id" in errors)
        assertTrue("sati.establishment.type" in errors)
        assertTrue("sati.establishment.owner.contact.fullName" in errors)
    }

    @Test
    fun `M4 - missing id fires`() {
        assertTrue("sati.establishment.id" in establishmentErrors(satiM4(validEstablishment().copy(id = null))))
    }

    @Test
    fun `M4 - missing type fires`() {
        assertTrue("sati.establishment.type" in establishmentErrors(satiM4(validEstablishment().copy(type = null))))
    }

    @Test
    fun `M4 - subType required only when type is GMS`() {
        val gms = validEstablishment().copy(type = SectorEstablishmentType.GMS, subType = null)
        assertTrue("sati.establishment.subType" in establishmentErrors(satiM4(gms)))

        val gmsWithSubType = gms.copy(subType = SectorEstablishmentSubType.GMS_ONLY)
        assertFalse("sati.establishment.subType" in establishmentErrors(satiM4(gmsWithSubType)))

        assertFalse("sati.establishment.subType" in establishmentErrors(satiM4(validEstablishment())))
    }

    @Test
    fun `M4 - owner contact follows the contact rules`() {
        val noOwner = validEstablishment().copy(owner = null)
        assertTrue("sati.establishment.owner.contact.fullName" in establishmentErrors(satiM4(noOwner)))

        val brokenContact = validEstablishment().copy(owner = SatiPartyEntity(contact = validContact().copy(email = null)))
        assertTrue("sati.establishment.owner.contact.email" in establishmentErrors(satiM4(brokenContact)))
    }

    @Test
    fun `M5 - establishment rules skipped when module is not M4`() {
        val sati = satiM4(establishment = null).copy(module = SatiModuleType.M5)
        assertTrue(establishmentErrors(sati).isEmpty())
    }
}
