package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.sati

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.ControlResource
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.LogbookMessagePurpose
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.*
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.sati.*
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.sati.SatiMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

class SatiMapperTest {

    private val timestamp = Instant.parse("2026-03-24T09:15:30Z")

    private fun buildFullEntity(): SatiEntity {
        val address = AddressEntity(
            id = 1,
            street = "1 rue de la mer",
            fullAddress = "1 rue de la mer, 75000 Paris",
            zipcode = "75000",
            town = "Paris",
            country = CountryCode.FR,
            lat = 48.856789,
            lng = 2.345678
        )
        val contact = ContactEntity(
            id = 2,
            fullName = "John Doe",
            firstName = "John",
            lastName = "Doe",
            nationality = "FRA",
            email = "john@example.com",
            phone = "+33123456789",
            address = address
        )
        val party = SatiPartyEntity(
            id = 3,
            partyType = "OWNER",
            comments = "some comments",
            signature = true,
            contact = contact
        )
        val jpe = SatiJpeEntity(
            pnoId = "PNO-123",
            portId = "FRLEH",
            portName = "Le Havre",
            tripNumber = "TRIP-001",
            lastStopDate = timestamp,
            pnoType = LogbookMessagePurpose.LAN
        )
        val vessel = SatiVesselEntity(
            id = 10,
            jpe = jpe,
            type = "Trawler",
            name = "Le Marin",
            immat = "CFR-123",
            imo = "IMO1234567",
            length = 25.5,
            extRef = "EXT-001",
            ircs = "FXYZ",
            owner = party,
            flagState = CountryCode.FR,
            charterer = SatiPartyEntity(id = 4, partyType = "CHARTERER"),
            agent = SatiPartyEntity(id = 5, partyType = "AGENT"),
            master = SatiPartyEntity(id = 6, partyType = "MASTER"),
            isMasterOwner = true
        )
        val inspector = SatiInspectorEntity(
            id = 7,
            agentId = 42,
            party = SatiPartyEntity(id = 8, partyType = "INSPECTOR"),
            authorityType = AuthorityType.AECP,
            isOutOfUnit = false
        )
        return SatiEntity(
            id = UUID.randomUUID(),
            module = SatiModuleType.M1,
            actionId = UUID.randomUUID().toString(),
            resource = ControlResourceEntity(id = 99, name = "Vedette A"),
            vessel = vessel,
            startDatetimeUtc = timestamp,
            endDatetimeUtc = timestamp.plusSeconds(3600),
            inspectors = listOf(inspector)
        )
    }

    private fun buildFullSati(): Sati {
        val address = Address(
            id = 1,
            street = "1 rue de la mer",
            fullAddress = "1 rue de la mer, 75000 Paris",
            zipcode = "75000",
            town = "Paris",
            country = CountryCode.FR,
            lat = 48.856789,
            lng = 2.345678
        )
        val contact = Contact(
            id = 2,
            fullName = "John Doe",
            firstName = "John",
            lastName = "Doe",
            nationality = "FRA",
            email = "john@example.com",
            phone = "+33123456789",
            address = address
        )
        val party = SatiParty(
            id = 3,
            partyType = "OWNER",
            comments = "some comments",
            signature = true,
            contact = contact
        )
        val jpe = SatiJpe(
            pnoId = "PNO-123",
            portId = "FRLEH",
            portName = "Le Havre",
            tripNumber = "TRIP-001",
            lastStopDate = timestamp,
            pnoType = LogbookMessagePurpose.LAN
        )
        val vessel = SatiVessel(
            id = 10,
            jpe = jpe,
            type = "Trawler",
            name = "Le Marin",
            immat = "CFR-123",
            imo = "IMO1234567",
            length = 25.5,
            extRef = "EXT-001",
            ircs = "FXYZ",
            owner = party,
            flagState = CountryCode.FR,
            charterer = SatiParty(id = 4, partyType = "CHARTERER"),
            pnoType = "LAN",
            tripNumber = "TRIP-001",
            agent = SatiParty(id = 5, partyType = "AGENT"),
            master = SatiParty(id = 6, partyType = "MASTER"),
            isMasterOwner = true
        )
        val inspector = SatiInspector(
            id = 7,
            agentId = 42,
            party = SatiParty(id = 8, partyType = "INSPECTOR"),
            authorityType = AuthorityType.AECP,
            isOutOfUnit = false
        )
        return Sati(
            id = UUID.randomUUID(),
            actionId = UUID.randomUUID().toString(),
            module = SatiModuleType.M1,
            resource = ControlResource(id = 99, name = "Vedette A"),
            vessel = vessel,
            startDatetimeUtc = timestamp,
            endDatetimeUtc = timestamp.plusSeconds(3600),
            inspectors = listOf(inspector)
        )
    }

    @Nested
    inner class FromEntity {

        @Test
        fun `should map all top-level fields`() {
            val entity = buildFullEntity()
            val result = SatiMapper.fromEntity(entity)

            assertThat(result.id).isEqualTo(entity.id)
            assertThat(result.actionId).isEqualTo(entity.actionId)
            assertThat(result.module).isEqualTo(entity.module)
            assertThat(result.startDatetimeUtc).isEqualTo(entity.startDatetimeUtc)
            assertThat(result.endDatetimeUtc).isEqualTo(entity.endDatetimeUtc)
        }

        @Test
        fun `should map vessel fields`() {
            val entity = buildFullEntity()
            val result = SatiMapper.fromEntity(entity)

            assertThat(result.vessel).isNotNull
            assertThat(result.vessel?.name).isEqualTo("Le Marin")
            assertThat(result.vessel?.immat).isEqualTo("CFR-123")
            assertThat(result.vessel?.imo).isEqualTo("IMO1234567")
            assertThat(result.vessel?.ircs).isEqualTo("FXYZ")
            assertThat(result.vessel?.length).isEqualTo(25.5)
            assertThat(result.vessel?.type).isEqualTo("Trawler")
            assertThat(result.vessel?.extRef).isEqualTo("EXT-001")
            assertThat(result.vessel?.flagState).isEqualTo(CountryCode.FR)
            assertThat(result.vessel?.isMasterOwner).isTrue()
            assertThat(result.vessel?.pnoType).isEqualTo("LAN")
            assertThat(result.vessel?.tripNumber).isEqualTo("TRIP-001")
        }

        @Test
        fun `should map vessel jpe`() {
            val entity = buildFullEntity()
            val result = SatiMapper.fromEntity(entity)

            assertThat(result.vessel?.jpe?.pnoId).isEqualTo("PNO-123")
            assertThat(result.vessel?.jpe?.portId).isEqualTo("FRLEH")
            assertThat(result.vessel?.jpe?.portName).isEqualTo("Le Havre")
            assertThat(result.vessel?.jpe?.tripNumber).isEqualTo("TRIP-001")
            assertThat(result.vessel?.jpe?.pnoType).isEqualTo(LogbookMessagePurpose.LAN)
        }

        @Test
        fun `should map vessel owner with contact and address`() {
            val entity = buildFullEntity()
            val result = SatiMapper.fromEntity(entity)

            val owner = result.vessel?.owner
            assertThat(owner?.partyType).isEqualTo("OWNER")
            assertThat(owner?.comments).isEqualTo("some comments")
            assertThat(owner?.signature).isTrue()
            assertThat(owner?.contact?.fullName).isEqualTo("John Doe")
            assertThat(owner?.contact?.email).isEqualTo("john@example.com")
            assertThat(owner?.contact?.phone).isEqualTo("+33123456789")
            assertThat(owner?.contact?.address?.street).isEqualTo("1 rue de la mer")
            assertThat(owner?.contact?.address?.town).isEqualTo("Paris")
            assertThat(owner?.contact?.address?.country).isEqualTo(CountryCode.FR)
        }

        @Test
        fun `should map resource`() {
            val entity = buildFullEntity()
            val result = SatiMapper.fromEntity(entity)

            assertThat(result.resource?.id).isEqualTo(99)
            assertThat(result.resource?.name).isEqualTo("Vedette A")
        }

        @Test
        fun `should map inspectors`() {
            val entity = buildFullEntity()
            val result = SatiMapper.fromEntity(entity)

            assertThat(result.inspectors).hasSize(1)
            val inspector = result.inspectors?.first()
            assertThat(inspector?.id).isEqualTo(7)
            assertThat(inspector?.agentId).isEqualTo(42)
            assertThat(inspector?.authorityType).isEqualTo(AuthorityType.AECP)
            assertThat(inspector?.isOutOfUnit).isFalse()
            assertThat(inspector?.party?.partyType).isEqualTo("INSPECTOR")
        }

        @Test
        fun `should handle null entity fields gracefully`() {
            val result = SatiMapper.fromEntity(null)

            assertThat(result.id).isNull()
            assertThat(result.vessel).isNull()
            assertThat(result.actionId).isEmpty()
            assertThat(result.module).isEqualTo(SatiModuleType.M1)
            assertThat(result.inspectors).isEmpty()
        }

        @Test
        fun `should default module to M1 when entity module is null`() {
            val entity = buildFullEntity().copy(module = SatiModuleType.M1)
            val result = SatiMapper.fromEntity(entity)
            assertThat(result.module).isEqualTo(SatiModuleType.M1)
        }
    }

    @Nested
    inner class ToEntity {

        @Test
        fun `should map all top-level fields`() {
            val sati = buildFullSati()
            val result = SatiMapper.toEntity(sati)

            assertThat(result.id).isEqualTo(sati.id)
            assertThat(result.actionId).isEqualTo(sati.actionId)
            assertThat(result.module).isEqualTo(sati.module)
            assertThat(result.startDatetimeUtc).isEqualTo(sati.startDatetimeUtc)
            assertThat(result.endDatetimeUtc).isEqualTo(sati.endDatetimeUtc)
        }

        @Test
        fun `should map vessel fields`() {
            val sati = buildFullSati()
            val result = SatiMapper.toEntity(sati)

            assertThat(result.vessel).isNotNull
            assertThat(result.vessel?.name).isEqualTo("Le Marin")
            assertThat(result.vessel?.immat).isEqualTo("CFR-123")
            assertThat(result.vessel?.imo).isEqualTo("IMO1234567")
            assertThat(result.vessel?.ircs).isEqualTo("FXYZ")
            assertThat(result.vessel?.length).isEqualTo(25.5)
            assertThat(result.vessel?.flagState).isEqualTo(CountryCode.FR)
            assertThat(result.vessel?.isMasterOwner).isTrue()
        }

        @Test
        fun `should map vessel parties`() {
            val sati = buildFullSati()
            val result = SatiMapper.toEntity(sati)

            assertThat(result.vessel?.owner?.partyType).isEqualTo("OWNER")
            assertThat(result.vessel?.charterer?.partyType).isEqualTo("CHARTERER")
            assertThat(result.vessel?.agent?.partyType).isEqualTo("AGENT")
            assertThat(result.vessel?.master?.partyType).isEqualTo("MASTER")
        }

        @Test
        fun `should map contact address`() {
            val sati = buildFullSati()
            val result = SatiMapper.toEntity(sati)

            val address = result.vessel?.owner?.contact?.address
            assertThat(address?.street).isEqualTo("1 rue de la mer")
            assertThat(address?.zipcode).isEqualTo("75000")
            assertThat(address?.town).isEqualTo("Paris")
            assertThat(address?.country).isEqualTo(CountryCode.FR)
            assertThat(address?.lat).isEqualTo(48.856789)
            assertThat(address?.lng).isEqualTo(2.345678)
        }

        @Test
        fun `should map inspectors`() {
            val sati = buildFullSati()
            val result = SatiMapper.toEntity(sati)

            assertThat(result.inspectors).hasSize(1)
            val inspector = result.inspectors?.first()
            assertThat(inspector?.agentId).isEqualTo(42)
            assertThat(inspector?.authorityType).isEqualTo(AuthorityType.AECP)
            assertThat(inspector?.isOutOfUnit).isFalse()
        }

        @Test
        fun `should map resource`() {
            val sati = buildFullSati()
            val result = SatiMapper.toEntity(sati)

            assertThat(result.resource?.id).isEqualTo(99)
            assertThat(result.resource?.name).isEqualTo("Vedette A")
        }

        @Test
        fun `should handle null response`() {
            val result = SatiMapper.toEntity(null)

            assertThat(result.id).isNull()
            assertThat(result.vessel).isNull()
            assertThat(result.actionId).isEmpty()
            assertThat(result.module).isEqualTo(SatiModuleType.M1)
            assertThat(result.inspectors).isEmpty()
        }
    }

    @Nested
    inner class RoundTrip {

        @Test
        fun `fromEntity then toEntity should preserve key fields`() {
            val entity = buildFullEntity()
            val roundTripped = SatiMapper.toEntity(SatiMapper.fromEntity(entity))

            assertThat(roundTripped.id).isEqualTo(entity.id)
            assertThat(roundTripped.actionId).isEqualTo(entity.actionId)
            assertThat(roundTripped.module).isEqualTo(entity.module)
            assertThat(roundTripped.vessel?.name).isEqualTo(entity.vessel?.name)
            assertThat(roundTripped.vessel?.immat).isEqualTo(entity.vessel?.immat)
            assertThat(roundTripped.vessel?.owner?.contact?.fullName).isEqualTo(entity.vessel?.owner?.contact?.fullName)
        }
    }
}
