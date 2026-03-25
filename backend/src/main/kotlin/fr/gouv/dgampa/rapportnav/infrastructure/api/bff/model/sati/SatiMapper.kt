package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.AddressEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.ContactEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.sati.Address
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.sati.Contact
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.sati.Sati

object SatiMapper {
    fun fromEntity(entity: SatiEntity): Sati {
        return Sati(
            id = entity.id,
            module = entity.module,
            actionId = entity.actionId,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            actionTaken = entity.actionTaken,
            agentContact = entity.agentContact?.let { fromEntity(it) },
            appointingAuthority = entity.appointingAuthority,
            beneficialOwnerContact = entity.beneficialOwnerContact?.let { fromEntity(it) },
            catchCertificateInfo = entity.catchCertificateInfo,
            chartererContact = entity.chartererContact?.let { fromEntity(it) },
            commonMarketingStandards = entity.commonMarketingStandards,
            driverContact = entity.driverContact?.let { fromEntity(it) },
            euFishingTripId = entity.euFishingTripId,
            fisheryLotLabelling = entity.fisheryLotLabelling,
            fisheryProductDocumentsInspection = entity.fisheryProductDocumentsInspection,
            fisheryProductStorageMechanismInspected = entity.fisheryProductStorageMechanismInspected,
            fisheryProductWeighedBeforeSale = entity.fisheryProductWeighedBeforeSale,
            freshnessCategories = entity.freshnessCategories,
            importerContact = entity.importerContact?.let { fromEntity(it) },
            infringementsObservations = entity.infringementsObservations,
            inspectionEndDatetimeUtc = entity.inspectionEndDatetimeUtc,
            inspectionStartDatetimeUtc = entity.inspectionStartDatetimeUtc,
            inspectionLocationAddressId = entity.inspectionLocationAddressId,
            inspectorComments = entity.inspectorComments,
            inspectorNameOrId = entity.inspectorNameOrId,
            inspectorSignature = entity.inspectorSignature,
            inspectorUniqueId = entity.inspectorUniqueId,
            landingDeclarationInfo = entity.landingDeclarationInfo,
            lotIdSpeciesQuantities = entity.lotIdSpeciesQuantities,
            lotsArt58Compliance = entity.lotsArt58Compliance,
            marketOwnerContact = entity.marketOwnerContact?.let { fromEntity(it) },
            marketOwnerRepresentativeContact = entity.marketOwnerRepresentativeContact?.let { fromEntity(it) },
            marketPremisesAddressId = entity.marketPremisesAddressId,
            marketPremisesLocation = entity.marketPremisesLocation,
            marketPremisesName = entity.marketPremisesName,
            masterComments = entity.masterComments,
            masterContact = entity.masterContact?.let { fromEntity(it) },
            newLotCompositionInfo = entity.newLotCompositionInfo,
            operatorComments = entity.operatorComments,
            operatorSignature = entity.operatorSignature,
            patrolVesselExternalMarking = entity.patrolVesselExternalMarking,
            patrolVesselRadioCallSign = entity.patrolVesselRadioCallSign,
            registeredBuyerContact = entity.registeredBuyerContact?.let { fromEntity(it) },
            responsiblePersonSignature = entity.responsiblePersonSignature,
            sizeGradingCategories = entity.sizeGradingCategories,
            supplierInvoiceInfo = entity.supplierInvoiceInfo,
            takeOverDeclarationInfo = entity.takeOverDeclarationInfo,
            traceabilityRecordingSystemArt58 = entity.traceabilityRecordingSystemArt58,
            traceabilitySupplierIdentificationSystem = entity.traceabilitySupplierIdentificationSystem,
            tractorRegistrationNumber = entity.tractorRegistrationNumber,
            trailerRegistrationNumber = entity.trailerRegistrationNumber,
            transportDocumentInfo = entity.transportDocumentInfo,
            transporterComments = entity.transporterComments,
            transporterSignature = entity.transporterSignature,
            useOfUndersizedFisheryProducts = entity.useOfUndersizedFisheryProducts,
            vehicleNationality = entity.vehicleNationality,
            vehicleOwnerContact = entity.vehicleOwnerContact?.let { fromEntity(it) },
            vehicleType = entity.vehicleType,
            vesselOwnerContact = entity.vesselOwnerContact?.let { fromEntity(it) }
        )
    }

    fun toEntity(response: Sati): SatiEntity {
        return SatiEntity(
            id = response.id,
            module = response.module,
            actionId = response.actionId,
            createdAt = response.createdAt,
            updatedAt = response.updatedAt,
            actionTaken = response.actionTaken,
            agentContact = response.agentContact?.let { toEntity(it) },
            appointingAuthority = response.appointingAuthority,
            beneficialOwnerContact = response.beneficialOwnerContact?.let { toEntity(it) },
            catchCertificateInfo = response.catchCertificateInfo,
            chartererContact = response.chartererContact?.let { toEntity(it) },
            commonMarketingStandards = response.commonMarketingStandards,
            driverContact = response.driverContact?.let { toEntity(it) },
            euFishingTripId = response.euFishingTripId,
            fisheryLotLabelling = response.fisheryLotLabelling,
            fisheryProductDocumentsInspection = response.fisheryProductDocumentsInspection,
            fisheryProductStorageMechanismInspected = response.fisheryProductStorageMechanismInspected,
            fisheryProductWeighedBeforeSale = response.fisheryProductWeighedBeforeSale,
            freshnessCategories = response.freshnessCategories,
            importerContact = response.importerContact?.let { toEntity(it) },
            infringementsObservations = response.infringementsObservations,
            inspectionEndDatetimeUtc = response.inspectionEndDatetimeUtc,
            inspectionStartDatetimeUtc = response.inspectionStartDatetimeUtc,
            inspectionLocationAddressId = response.inspectionLocationAddressId,
            inspectorComments = response.inspectorComments,
            inspectorNameOrId = response.inspectorNameOrId,
            inspectorSignature = response.inspectorSignature,
            inspectorUniqueId = response.inspectorUniqueId,
            landingDeclarationInfo = response.landingDeclarationInfo,
            lotIdSpeciesQuantities = response.lotIdSpeciesQuantities,
            lotsArt58Compliance = response.lotsArt58Compliance,
            marketOwnerContact = response.marketOwnerContact?.let { toEntity(it) },
            marketOwnerRepresentativeContact = response.marketOwnerRepresentativeContact?.let { toEntity(it) },
            marketPremisesAddressId = response.marketPremisesAddressId,
            marketPremisesLocation = response.marketPremisesLocation,
            marketPremisesName = response.marketPremisesName,
            masterComments = response.masterComments,
            masterContact = response.masterContact?.let { toEntity(it) },
            newLotCompositionInfo = response.newLotCompositionInfo,
            operatorComments = response.operatorComments,
            operatorSignature = response.operatorSignature,
            patrolVesselExternalMarking = response.patrolVesselExternalMarking,
            patrolVesselRadioCallSign = response.patrolVesselRadioCallSign,
            registeredBuyerContact = response.registeredBuyerContact?.let { toEntity(it) },
            responsiblePersonSignature = response.responsiblePersonSignature,
            sizeGradingCategories = response.sizeGradingCategories,
            supplierInvoiceInfo = response.supplierInvoiceInfo,
            takeOverDeclarationInfo = response.takeOverDeclarationInfo,
            traceabilityRecordingSystemArt58 = response.traceabilityRecordingSystemArt58,
            traceabilitySupplierIdentificationSystem = response.traceabilitySupplierIdentificationSystem,
            tractorRegistrationNumber = response.tractorRegistrationNumber,
            trailerRegistrationNumber = response.trailerRegistrationNumber,
            transportDocumentInfo = response.transportDocumentInfo,
            transporterComments = response.transporterComments,
            transporterSignature = response.transporterSignature,
            useOfUndersizedFisheryProducts = response.useOfUndersizedFisheryProducts,
            vehicleNationality = response.vehicleNationality,
            vehicleOwnerContact = response.vehicleOwnerContact?.let { toEntity(it) },
            vehicleType = response.vehicleType,
            vesselOwnerContact = response.vesselOwnerContact?.let { toEntity(it) }
        )
    }

    private fun fromEntity(entity: ContactEntity): Contact {
        return Contact(
            id = entity.id,
            fullName = entity.fullName,
            nationality = entity.nationality,
            email = entity.email,
            phone = entity.phone,
            address = entity.address?.let { fromEntity(it) },
            createdAt = entity.createdAt
        )
    }

    private fun toEntity(response: Contact): ContactEntity {
        return ContactEntity(
            id = response.id,
            fullName = response.fullName,
            nationality = response.nationality,
            email = response.email,
            phone = response.phone,
            address = response.address?.let { toEntity(it) },
            createdAt = response.createdAt
        )
    }

    private fun fromEntity(entity: AddressEntity): Address {
        return Address(
            id = entity.id,
            street = entity.street,
            zipcode = entity.zipcode,
            town = entity.town,
            country = entity.country,
            createdAt = entity.createdAt
        )
    }

    private fun toEntity(response: Address): AddressEntity {
        return AddressEntity(
            id = response.id,
            street = response.street,
            zipcode = response.zipcode,
            town = response.town,
            country = response.country,
            createdAt = response.createdAt
        )
    }
}
