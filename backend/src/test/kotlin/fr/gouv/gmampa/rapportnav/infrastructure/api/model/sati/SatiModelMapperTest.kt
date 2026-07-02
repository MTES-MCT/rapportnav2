package fr.gouv.dgampa.rapportnav.infrastructure.database.model.sati

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.LogbookMessagePurpose
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.*
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

class SatiModelMapperTest {

    private val timestamp = Instant.parse("2026-03-24T09:15:30Z")

    private fun buildAddressModel(): AddressModel {
        return AddressModel(
            id = 1,
            street = "1 rue de la mer",
            fullAddress = "1 rue de la mer, 75000 Paris",
            zipcode = "75000",
            town = "Paris",
            country = "FRA",
            lng = 2.345678,
            lat = 48.856789,
            createdAt = timestamp,
            updatedAt = timestamp
        )
    }

    private fun buildContactModel(address: AddressModel? = null): ContactModel {
        return ContactModel(
            id = 2,
            fullName = "John Doe",
            firstName = "John",
            lastName = "Doe",
            nationality = "FRA",
            email = "john@example.com",
            phone = "+33123456789",
            addresses = if (address != null) mutableListOf(address) else mutableListOf(),
            createdAt = timestamp,
            updatedAt = timestamp
        )
    }

    private fun buildPartyModel(type: String = "OWNER", contact: ContactModel? = null): SatiPartyModel {
        return SatiPartyModel(
            id = 3,
            partyType = type,
            comments = "some comments",
            signature = true,
            contacts = if (contact != null) mutableListOf(contact) else mutableListOf(),
            createdAt = timestamp,
            updatedAt = timestamp
        )
    }

    private fun buildInspectorModel(party: SatiPartyModel? = null): SatiInspectorModel {
        return SatiInspectorModel(
            id = 7,
            agentId = 42,
            cardId = "FRD455643",
            parties = if (party != null) mutableListOf(party) else mutableListOf(),
            authorityType = "AECP",
            isOutOfUnit = false,
            createdAt = timestamp,
            updatedAt = timestamp
        )
    }

    private fun buildVesselModel(
        agent: SatiPartyModel? = null,
        master: SatiPartyModel? = null
    ): SatiVesselModel {
        return SatiVesselModel(
            id = 10,
            pnoType = "LAN",
            tripNumber = "TRIP-001",
            parties = listOfNotNull(agent, master).toMutableList(),
            isMasterOwner = true,
            createdAt = timestamp,
            updatedAt = timestamp
        )
    }

    private fun buildFullModel(): SatiModel {
        val address = buildAddressModel()
        val contact = buildContactModel(address)
        val agentParty = buildPartyModel(PartyType.VESSEL_AGENT.name, contact)
        val masterParty = buildPartyModel(PartyType.VESSEL_MASTER.name)
        val vessel = buildVesselModel(agent = agentParty, master = masterParty)
        val inspectorParty = buildPartyModel(PartyType.INSPECTOR.name)
        val inspector = buildInspectorModel(inspectorParty)

        return SatiModel(
            id = UUID.randomUUID(),
            module = "M1",
            actionId = UUID.randomUUID().toString(),
            resourceId = 99,
            vessels = mutableListOf(vessel),
            inspectors = mutableListOf(inspector),
            createdAt = timestamp,
            updatedAt = timestamp
        )
    }

    private fun buildFullEntity(): SatiEntity {
        val address = AddressEntity(
            id = 1,
            street = "1 rue de la mer",
            fullAddress = "1 rue de la mer, 75000 Paris",
            zipcode = "75000",
            town = "Paris",
            country = CountryCode.FR.toString(),
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
        val agentParty = SatiPartyEntity(
            id = 3,
            partyType = "AGENT",
            comments = "some comments",
            signature = true,
            contact = contact
        )
        val masterParty = SatiPartyEntity(id = 6, partyType = "MASTER")
        val vessel = SatiVesselEntity(
            id = 10,
            jpe = SatiJpeEntity(pnoType = LogbookMessagePurpose.LAN, tripNumber = "TRIP-001"),
            agent = agentParty,
            master = masterParty,
            isMasterOwner = true
        )
        val inspector = SatiInspectorEntity(
            id = 7,
            agentId = 42,
            cardId = "FRD455643",
            party = SatiPartyEntity(id = 8, partyType = "INSPECTOR"),
            authorityType = AuthorityType.AECP,
            isOutOfUnit = false
        )
        return SatiEntity(
            id = UUID.randomUUID(),
            module = SatiModuleType.M1,
            actionId = UUID.randomUUID().toString(),
            resource = ControlResourceEntity(id = 99),
            vessel = vessel,
            inspectors = listOf(inspector)
        )
    }

    @Nested
    inner class ToEntity {

        @Test
        fun `should map top-level fields`() {
            val model = buildFullModel()
            val entity = SatiModelMapper.toEntity(model)

            assertThat(entity.id).isEqualTo(model.id)
            assertThat(entity.actionId).isEqualTo(model.actionId)
            assertThat(entity.module).isEqualTo(SatiModuleType.M1)
        }

        @Test
        fun `should map resource id`() {
            val model = buildFullModel()
            val entity = SatiModelMapper.toEntity(model)

            assertThat(entity.resource?.id).isEqualTo(99)
        }

        @Test
        fun `should map vessel fields`() {
            val model = buildFullModel()
            val entity = SatiModelMapper.toEntity(model)

            assertThat(entity.vessel).isNotNull
            assertThat(entity.vessel?.jpe?.pnoType).isEqualTo(LogbookMessagePurpose.LAN)
            assertThat(entity.vessel?.jpe?.tripNumber).isEqualTo("TRIP-001")
            assertThat(entity.vessel?.isMasterOwner).isTrue()
        }

        @Test
        fun `should map vessel agent party with nested contact and address`() {
            val model = buildFullModel()
            val entity = SatiModelMapper.toEntity(model)

            val agent = entity.vessel?.agent
            assertThat(agent?.partyType).isEqualTo(PartyType.VESSEL_AGENT.name)
            assertThat(agent?.comments).isEqualTo("some comments")
            assertThat(agent?.signature).isTrue()
            assertThat(agent?.contact?.fullName).isEqualTo("John Doe")
            assertThat(agent?.contact?.email).isEqualTo("john@example.com")
            assertThat(agent?.contact?.address?.street).isEqualTo("1 rue de la mer")
            assertThat(agent?.contact?.address?.town).isEqualTo("Paris")
            assertThat(agent?.contact?.address?.country).isEqualTo("FRA")
        }

        @Test
        fun `should map inspectors with authority type`() {
            val model = buildFullModel()
            val entity = SatiModelMapper.toEntity(model)

            assertThat(entity.inspectors).hasSize(1)
            val inspector = entity.inspectors?.first()
            assertThat(inspector?.id).isEqualTo(7)
            assertThat(inspector?.agentId).isEqualTo(42)
            assertThat(inspector?.cardId).isEqualTo("FRD455643")
            assertThat(inspector?.authorityType).isEqualTo(AuthorityType.AECP)
            assertThat(inspector?.isOutOfUnit).isFalse()
        }

        @Test
        fun `should handle empty vessels`() {
            val model = SatiModel(
                module = "M1",
                actionId = "action-1",
                vessels = mutableListOf()
            )
            val entity = SatiModelMapper.toEntity(model)
            assertThat(entity.vessel).isNull()
        }

        @Test
        fun `should handle empty inspectors`() {
            val model = SatiModel(
                module = "M1",
                actionId = "action-1",
                inspectors = mutableListOf()
            )
            val entity = SatiModelMapper.toEntity(model)
            assertThat(entity.inspectors).isEmpty()
        }
    }

    @Nested
    inner class ToModel {

        @Test
        fun `should map top-level fields`() {
            val entity = buildFullEntity()
            val model = SatiModelMapper.toModel(entity)

            assertThat(model.id).isEqualTo(entity.id)
            assertThat(model.actionId).isEqualTo(entity.actionId)
            assertThat(model.module).isEqualTo("M1")
        }

        @Test
        fun `should map resource id from entity`() {
            val entity = buildFullEntity()
            val model = SatiModelMapper.toModel(entity)

            assertThat(model.resourceId).isEqualTo(99)
        }

        @Test
        fun `should map vessel fields`() {
            val entity = buildFullEntity()
            val model = SatiModelMapper.toModel(entity)

            val vessel = model.vessels.firstOrNull()
            assertThat(vessel).isNotNull
            assertThat(vessel?.pnoType).isEqualTo("LAN")
            assertThat(vessel?.tripNumber).isEqualTo("TRIP-001")
            assertThat(vessel?.isMasterOwner).isTrue()
        }

        @Test
        fun `should map vessel agent party with nested contact and address`() {
            val entity = buildFullEntity()
            val model = SatiModelMapper.toModel(entity)

            val agent = model.vessels.firstOrNull()?.parties?.firstOrNull { it.partyType == PartyType.VESSEL_AGENT.name }
            assertThat(agent?.partyType).isEqualTo(PartyType.VESSEL_AGENT.name)
            assertThat(agent?.comments).isEqualTo("some comments")
            assertThat(agent?.signature).isTrue()
            val contact = agent?.contacts?.firstOrNull()
            assertThat(contact?.fullName).isEqualTo("John Doe")
            assertThat(contact?.email).isEqualTo("john@example.com")
            val address = contact?.addresses?.firstOrNull()
            assertThat(address?.street).isEqualTo("1 rue de la mer")
            assertThat(address?.town).isEqualTo("Paris")
            assertThat(address?.country).isEqualTo("FR")
        }

        @Test
        fun `should map inspectors with authority type as string`() {
            val entity = buildFullEntity()
            val model = SatiModelMapper.toModel(entity)

            assertThat(model.inspectors).hasSize(1)
            val inspector = model.inspectors.first()
            assertThat(inspector.id).isEqualTo(7)
            assertThat(inspector.agentId).isEqualTo(42)
            assertThat(inspector.cardId).isEqualTo("FRD455643")
            assertThat(inspector.authorityType).isEqualTo("AECP")
            assertThat(inspector.isOutOfUnit).isFalse()
        }

        @Test
        fun `should handle null vessel`() {
            val entity = SatiEntity(
                module = SatiModuleType.M1,
                actionId = "action-1",
                vessel = null
            )
            val model = SatiModelMapper.toModel(entity)
            assertThat(model.vessels).isEmpty()
        }

        @Test
        fun `should handle null inspectors`() {
            val entity = SatiEntity(
                module = SatiModuleType.M1,
                actionId = "action-1",
                inspectors = null
            )
            val model = SatiModelMapper.toModel(entity)
            assertThat(model.inspectors).isEmpty()
        }

        @Test
        fun `should handle null resource`() {
            val entity = SatiEntity(
                module = SatiModuleType.M1,
                actionId = "action-1",
                resource = null
            )
            val model = SatiModelMapper.toModel(entity)
            assertThat(model.resourceId).isNull()
        }
    }

    @Nested
    inner class RoundTrip {

        @Test
        fun `toModel then toEntity should preserve key fields`() {
            val entity = buildFullEntity()
            val roundTripped = SatiModelMapper.toEntity(SatiModelMapper.toModel(entity))

            assertThat(roundTripped.id).isEqualTo(entity.id)
            assertThat(roundTripped.actionId).isEqualTo(entity.actionId)
            assertThat(roundTripped.module).isEqualTo(entity.module)
            assertThat(roundTripped.vessel?.jpe?.pnoType).isEqualTo(entity.vessel?.jpe?.pnoType)
            assertThat(roundTripped.vessel?.jpe?.tripNumber).isEqualTo(entity.vessel?.jpe?.tripNumber)
            assertThat(roundTripped.vessel?.isMasterOwner).isEqualTo(entity.vessel?.isMasterOwner)
        }
    }
}
