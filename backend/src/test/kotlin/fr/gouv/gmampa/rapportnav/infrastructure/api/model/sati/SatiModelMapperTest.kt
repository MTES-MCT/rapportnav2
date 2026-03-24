package fr.gouv.dgampa.rapportnav.infrastructure.database.model.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.AddressEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.ContactEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati.AddressModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati.ContactModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati.SatiModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.sati.SatiModelMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.util.UUID

class SatiModelMapperTest {

    @Test
    fun `toEntity should map SatiModel to SatiEntity`() {
        val addressModel = AddressModel(
            id = UUID.randomUUID(),
            street = "1 rue de la mer",
            zipcode = "75000",
            town = "Paris",
            country = "FRA",
            createdAt = OffsetDateTime.parse("2026-03-24T10:15:30+01:00")
        )

        val contactModel = ContactModel(
            id = UUID.randomUUID(),
            fullName = "John Doe",
            nationality = "FRA",
            email = "john.doe@example.com",
            phone = "+33123456789",
            address = addressModel,
            createdAt = OffsetDateTime.parse("2026-03-24T10:15:30+01:00")
        )

        val model = SatiModel(
            id = UUID.randomUUID(),
            module = "AA",
            ownerId = UUID.randomUUID(),
            createdAt = OffsetDateTime.parse("2026-03-24T10:15:30+01:00"),
            updatedAt = OffsetDateTime.parse("2026-03-24T11:15:30+01:00"),
            actionTaken = "Checked",
            agentContact = contactModel,
            appointingAuthority = "Prefecture",
            beneficialOwnerContact = contactModel,
            catchCertificateInfo = true,
            chartererContact = contactModel,
            commonMarketingStandards = false,
            driverContact = contactModel,
            euFishingTripId = "EU-123",
            fisheryLotLabelling = true,
            fisheryProductDocumentsInspection = true,
            fisheryProductStorageMechanismInspected = false,
            fisheryProductWeighedBeforeSale = true,
            freshnessCategories = "A",
            importerContact = contactModel,
            infringementsObservations = "None",
            inspectionEndDatetimeUtc = OffsetDateTime.parse("2026-03-24T12:15:30+01:00"),
            inspectionStartDatetimeUtc = OffsetDateTime.parse("2026-03-24T09:15:30+01:00"),
            inspectionLocationAddressId = UUID.randomUUID(),
            inspectorComments = "OK",
            inspectorNameOrId = "Inspector 1",
            inspectorSignature = true,
            inspectorUniqueId = "INS-1",
            landingDeclarationInfo = false,
            lotIdSpeciesQuantities = """{"a":1}""",
            lotsArt58Compliance = true,
            marketOwnerContact = contactModel,
            marketOwnerRepresentativeContact = contactModel,
            marketPremisesAddressId = UUID.randomUUID(),
            marketPremisesLocation = "Port",
            marketPremisesName = "Market A",
            masterComments = "Master note",
            masterContact = contactModel,
            newLotCompositionInfo = """{"b":2}""",
            operatorComments = "Operator note",
            operatorSignature = false,
            patrolVesselExternalMarking = "EXT-1",
            patrolVesselRadioCallSign = "CALL-1",
            registeredBuyerContact = contactModel,
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
            vehicleNationality = "FRA",
            vehicleOwnerContact = contactModel,
            vehicleType = "Truck",
            vesselOwnerContact = contactModel
        )

        val entity = SatiModelMapper.toEntity(model)

        assertThat(entity.id).isEqualTo(model.id)
        assertThat(entity.module).isEqualTo(model.module)
        assertThat(entity.ownerId).isEqualTo(model.ownerId)
        assertThat(entity.createdAt).isEqualTo(model.createdAt)
        assertThat(entity.updatedAt).isEqualTo(model.updatedAt)
        assertThat(entity.actionTaken).isEqualTo(model.actionTaken)
        assertThat(entity.agentContact?.id).isEqualTo(contactModel.id)
        assertThat(entity.agentContact?.address?.id).isEqualTo(addressModel.id)
        assertThat(entity.appointingAuthority).isEqualTo(model.appointingAuthority)
        assertThat(entity.catchCertificateInfo).isEqualTo(true)
        assertThat(entity.vehicleType).isEqualTo("Truck")
        assertThat(entity.vesselOwnerContact?.email).isEqualTo(contactModel.email)
    }

    @Test
    fun `toModel should map SatiEntity to SatiModel`() {
        val addressEntity = AddressEntity(
            id = UUID.randomUUID(),
            street = "1 rue de la mer",
            zipcode = "75000",
            town = "Paris",
            country = "FRA",
            createdAt = OffsetDateTime.parse("2026-03-24T10:15:30+01:00")
        )

        val contactEntity = ContactEntity(
            id = UUID.randomUUID(),
            fullName = "John Doe",
            nationality = "FRA",
            email = "john.doe@example.com",
            phone = "+33123456789",
            address = addressEntity,
            createdAt = OffsetDateTime.parse("2026-03-24T10:15:30+01:00")
        )

        val entity = SatiEntity(
            id = UUID.randomUUID(),
            module = "AA",
            ownerId = UUID.randomUUID(),
            createdAt = OffsetDateTime.parse("2026-03-24T10:15:30+01:00"),
            updatedAt = OffsetDateTime.parse("2026-03-24T11:15:30+01:00"),
            actionTaken = "Checked",
            agentContact = contactEntity,
            appointingAuthority = "Prefecture",
            beneficialOwnerContact = contactEntity,
            catchCertificateInfo = true,
            chartererContact = contactEntity,
            commonMarketingStandards = false,
            driverContact = contactEntity,
            euFishingTripId = "EU-123",
            fisheryLotLabelling = true,
            fisheryProductDocumentsInspection = true,
            fisheryProductStorageMechanismInspected = false,
            fisheryProductWeighedBeforeSale = true,
            freshnessCategories = "A",
            importerContact = contactEntity,
            infringementsObservations = "None",
            inspectionEndDatetimeUtc = OffsetDateTime.parse("2026-03-24T12:15:30+01:00"),
            inspectionStartDatetimeUtc = OffsetDateTime.parse("2026-03-24T09:15:30+01:00"),
            inspectionLocationAddressId = UUID.randomUUID(),
            inspectorComments = "OK",
            inspectorNameOrId = "Inspector 1",
            inspectorSignature = true,
            inspectorUniqueId = "INS-1",
            landingDeclarationInfo = false,
            lotIdSpeciesQuantities = """{"a":1}""",
            lotsArt58Compliance = true,
            marketOwnerContact = contactEntity,
            marketOwnerRepresentativeContact = contactEntity,
            marketPremisesAddressId = UUID.randomUUID(),
            marketPremisesLocation = "Port",
            marketPremisesName = "Market A",
            masterComments = "Master note",
            masterContact = contactEntity,
            newLotCompositionInfo = """{"b":2}""",
            operatorComments = "Operator note",
            operatorSignature = false,
            patrolVesselExternalMarking = "EXT-1",
            patrolVesselRadioCallSign = "CALL-1",
            registeredBuyerContact = contactEntity,
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
            vehicleNationality = "FRA",
            vehicleOwnerContact = contactEntity,
            vehicleType = "Truck",
            vesselOwnerContact = contactEntity
        )

        val model = SatiModelMapper.toModel(entity)

        assertThat(model.id).isEqualTo(entity.id)
        assertThat(model.module).isEqualTo(entity.module)
        assertThat(model.ownerId).isEqualTo(entity.ownerId)
        assertThat(model.createdAt).isEqualTo(entity.createdAt)
        assertThat(model.updatedAt).isEqualTo(entity.updatedAt)
        assertThat(model.actionTaken).isEqualTo(entity.actionTaken)
        assertThat(model.agentContact?.id).isEqualTo(contactEntity.id)
        assertThat(model.agentContact?.address?.id).isEqualTo(addressEntity.id)
        assertThat(model.appointingAuthority).isEqualTo(entity.appointingAuthority)
        assertThat(model.catchCertificateInfo).isEqualTo(true)
        assertThat(model.vehicleType).isEqualTo("Truck")
        assertThat(model.vesselOwnerContact?.email).isEqualTo(contactEntity.email)
    }
}
