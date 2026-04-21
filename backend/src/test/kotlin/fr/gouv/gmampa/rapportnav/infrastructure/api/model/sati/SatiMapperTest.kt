package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.sati

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.AddressEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.ContactEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.sati.SatiMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class)
class SatiMapperTest {

    @Test
    fun `fromEntity should map SatiEntity to SatiResponse`() {
        val address = AddressEntity(
            id = UUID.randomUUID(),
            street = "1 rue de la mer",
            zipcode = "75000",
            town = "Paris",
            country = CountryCode.FR,
            createdAt = Instant.parse("2026-03-24T10:15:30+01:00")
        )

        val contact = ContactEntity(
            id = UUID.randomUUID(),
            fullName = "John Doe",
            nationality = CountryCode.FR,
            email = "john.doe@example.com",
            phone = "+33123456789",
            address = address,
            createdAt = Instant.parse("2026-03-24T10:15:30+01:00")
        )

        val sati = SatiEntity(
            id = UUID.randomUUID(),
            module = "AA",
            actionId = UUID.randomUUID().toString(),
            createdAt = Instant.parse("2026-03-24T10:15:30+01:00"),
            updatedAt = Instant.parse("2026-03-24T11:15:30+01:00"),
            actionTaken = "Checked",
            agentContact = contact,
            appointingAuthority = "Prefecture",
            beneficialOwnerContact = contact,
            catchCertificateInfo = true,
            chartererContact = contact,
            commonMarketingStandards = false,
            driverContact = contact,
            euFishingTripId = "EU-123",
            fisheryLotLabelling = true,
            fisheryProductDocumentsInspection = true,
            fisheryProductStorageMechanismInspected = false,
            fisheryProductWeighedBeforeSale = true,
            freshnessCategories = "A",
            importerContact = contact,
            infringementsObservations = "None",
            inspectionEndDatetimeUtc = Instant.parse("2026-03-24T12:15:30+01:00"),
            inspectionStartDatetimeUtc = Instant.parse("2026-03-24T09:15:30+01:00"),
            inspectionLocationAddressId = UUID.randomUUID().toString(),
            inspectorComments = "OK",
            inspectorNameOrId = "Inspector 1",
            inspectorSignature = true,
            inspectorUniqueId = "INS-1",
            landingDeclarationInfo = false,
            lotIdSpeciesQuantities = """{"a":1}""",
            lotsArt58Compliance = true,
            marketOwnerContact = contact,
            marketOwnerRepresentativeContact = contact,
            marketPremisesAddressId = UUID.randomUUID().toString(),
            marketPremisesLocation = "Port",
            marketPremisesName = "Market A",
            masterComments = "Master note",
            masterContact = contact,
            newLotCompositionInfo = """{"b":2}""",
            operatorComments = "Operator note",
            operatorSignature = false,
            patrolVesselExternalMarking = "EXT-1",
            patrolVesselRadioCallSign = "CALL-1",
            registeredBuyerContact = contact,
            responsiblePersonSignature = true,
            sizeGradingCategories = "S",
            supplierInvoiceInfo = true,
            takeOverDeclarationInfo = false,
            traceabilityRecordingSystemArt58 = true,
            traceabilitySupplierIdentificationSystem = false,
            tractorRegistrationNumber = "TR-1",
            trailerRegistrationNumber = "TL-1",
            transportDocumentInfo = true,
            transporterComments = "Transport note",
            transporterSignature = false,
            useOfUndersizedFisheryProducts = true,
            vehicleNationality = CountryCode.FR,
            vehicleOwnerContact = contact,
            vehicleType = "Truck",
            vesselOwnerContact = contact
        )

        val response = SatiMapper.fromEntity(sati)

        assertThat(response.id).isEqualTo(sati.id)
        assertThat(response.module).isEqualTo(sati.module)
        assertThat(response.actionId).isEqualTo(sati.actionId)
        assertThat(response.createdAt).isEqualTo(sati.createdAt)
        assertThat(response.updatedAt).isEqualTo(sati.updatedAt)
        assertThat(response.actionTaken).isEqualTo(sati.actionTaken)
        assertThat(response.agentContact?.id).isEqualTo(contact.id)
        assertThat(response.agentContact?.address?.id).isEqualTo(address.id)
        assertThat(response.appointingAuthority).isEqualTo(sati.appointingAuthority)
        assertThat(response.catchCertificateInfo).isEqualTo(true)
        assertThat(response.vehicleType).isEqualTo("Truck")
        assertThat(response.vesselOwnerContact?.email).isEqualTo(contact.email)
    }

    @Test
    fun `toEntity should map SatiResponse to SatiEntity`() {
        val address = Address(
            id = UUID.randomUUID(),
            street = "1 rue de la mer",
            zipcode = "75000",
            town = "Paris",
            country = CountryCode.FR,
            createdAt = Instant.parse("2026-03-24T10:15:30+01:00")
        )

        val contact = Contact(
            id = UUID.randomUUID(),
            fullName = "John Doe",
            nationality = CountryCode.FR,
            email = "john.doe@example.com",
            phone = "+33123456789",
            address = address,
            createdAt = Instant.parse("2026-03-24T10:15:30+01:00")
        )

        val response = Sati(
            id = UUID.randomUUID(),
            module = "AA",
            actionId = UUID.randomUUID().toString(),
            createdAt = Instant.parse("2026-03-24T10:15:30+01:00"),
            updatedAt = Instant.parse("2026-03-24T11:15:30+01:00"),
            actionTaken = "Checked",
            agentContact = contact,
            appointingAuthority = "Prefecture",
            beneficialOwnerContact = contact,
            catchCertificateInfo = true,
            chartererContact = contact,
            commonMarketingStandards = false,
            driverContact = contact,
            euFishingTripId = "EU-123",
            fisheryLotLabelling = true,
            fisheryProductDocumentsInspection = true,
            fisheryProductStorageMechanismInspected = false,
            fisheryProductWeighedBeforeSale = true,
            freshnessCategories = "A",
            importerContact = contact,
            infringementsObservations = "None",
            inspectionEndDatetimeUtc = Instant.parse("2026-03-24T12:15:30+01:00"),
            inspectionStartDatetimeUtc = Instant.parse("2026-03-24T09:15:30+01:00"),
            inspectionLocationAddressId = UUID.randomUUID().toString(),
            inspectorComments = "OK",
            inspectorNameOrId = "Inspector 1",
            inspectorSignature = true,
            inspectorUniqueId = "INS-1",
            landingDeclarationInfo = false,
            lotIdSpeciesQuantities = """{"a":1}""",
            lotsArt58Compliance = true,
            marketOwnerContact = contact,
            marketOwnerRepresentativeContact = contact,
            marketPremisesAddressId = UUID.randomUUID().toString(),
            marketPremisesLocation = "Port",
            marketPremisesName = "Market A",
            masterComments = "Master note",
            masterContact = contact,
            newLotCompositionInfo = """{"b":2}""",
            operatorComments = "Operator note",
            operatorSignature = false,
            patrolVesselExternalMarking = "EXT-1",
            patrolVesselRadioCallSign = "CALL-1",
            registeredBuyerContact = contact,
            responsiblePersonSignature = true,
            sizeGradingCategories = "S",
            supplierInvoiceInfo = true,
            takeOverDeclarationInfo = false,
            traceabilityRecordingSystemArt58 = true,
            traceabilitySupplierIdentificationSystem = false,
            tractorRegistrationNumber = "TR-1",
            trailerRegistrationNumber = "TL-1",
            transportDocumentInfo = true,
            transporterComments = "Transport note",
            transporterSignature = false,
            useOfUndersizedFisheryProducts = true,
            vehicleNationality = CountryCode.FR,
            vehicleOwnerContact = contact,
            vehicleType = "Truck",
            vesselOwnerContact = contact
        )

        val entity = SatiMapper.toEntity(response)

        assertThat(entity.id).isEqualTo(response.id)
        assertThat(entity.module).isEqualTo(response.module)
        assertThat(entity.actionId).isEqualTo(response.actionId)
        assertThat(entity.createdAt).isEqualTo(response.createdAt)
        assertThat(entity.updatedAt).isEqualTo(response.updatedAt)
        assertThat(entity.actionTaken).isEqualTo(response.actionTaken)
        assertThat(entity.agentContact?.id).isEqualTo(contact.id)
        assertThat(entity.agentContact?.address?.id).isEqualTo(address.id)
        assertThat(entity.appointingAuthority).isEqualTo(response.appointingAuthority)
        assertThat(entity.catchCertificateInfo).isEqualTo(true)
        assertThat(entity.vehicleType).isEqualTo("Truck")
        assertThat(entity.vesselOwnerContact?.email).isEqualTo(contact.email)
    }
}
