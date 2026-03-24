package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.AddressEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.ContactEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati.AddressModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati.ContactModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati.SatiModel

object SatiModelMapper {

    fun toEntity(model: SatiModel): SatiEntity {
        return SatiEntity(
            id = model.id,
            module = model.module,
            ownerId = model.ownerId,
            createdAt = model.createdAt,
            updatedAt = model.updatedAt,
            actionTaken = model.actionTaken,
            agentContact = model.agentContact?.toEntity(),
            appointingAuthority = model.appointingAuthority,
            beneficialOwnerContact = model.beneficialOwnerContact?.toEntity(),
            catchCertificateInfo = model.catchCertificateInfo,
            chartererContact = model.chartererContact?.toEntity(),
            commonMarketingStandards = model.commonMarketingStandards,
            driverContact = model.driverContact?.toEntity(),
            euFishingTripId = model.euFishingTripId,
            fisheryLotLabelling = model.fisheryLotLabelling,
            fisheryProductDocumentsInspection = model.fisheryProductDocumentsInspection,
            fisheryProductStorageMechanismInspected = model.fisheryProductStorageMechanismInspected,
            fisheryProductWeighedBeforeSale = model.fisheryProductWeighedBeforeSale,
            freshnessCategories = model.freshnessCategories,
            importerContact = model.importerContact?.toEntity(),
            infringementsObservations = model.infringementsObservations,
            inspectionEndDatetimeUtc = model.inspectionEndDatetimeUtc,
            inspectionStartDatetimeUtc = model.inspectionStartDatetimeUtc,
            inspectionLocationAddressId = model.inspectionLocationAddressId,
            inspectorComments = model.inspectorComments,
            inspectorNameOrId = model.inspectorNameOrId,
            inspectorSignature = model.inspectorSignature,
            inspectorUniqueId = model.inspectorUniqueId,
            landingDeclarationInfo = model.landingDeclarationInfo,
            lotIdSpeciesQuantities = model.lotIdSpeciesQuantities,
            lotsArt58Compliance = model.lotsArt58Compliance,
            marketOwnerContact = model.marketOwnerContact?.toEntity(),
            marketOwnerRepresentativeContact = model.marketOwnerRepresentativeContact?.toEntity(),
            marketPremisesAddressId = model.marketPremisesAddressId,
            marketPremisesLocation = model.marketPremisesLocation,
            marketPremisesName = model.marketPremisesName,
            masterComments = model.masterComments,
            masterContact = model.masterContact?.toEntity(),
            newLotCompositionInfo = model.newLotCompositionInfo,
            operatorComments = model.operatorComments,
            operatorSignature = model.operatorSignature,
            patrolVesselExternalMarking = model.patrolVesselExternalMarking,
            patrolVesselRadioCallSign = model.patrolVesselRadioCallSign,
            registeredBuyerContact = model.registeredBuyerContact?.toEntity(),
            responsiblePersonSignature = model.responsiblePersonSignature,
            sizeGradingCategories = model.sizeGradingCategories,
            supplierInvoiceInfo = model.supplierInvoiceInfo,
            takeOverDeclarationInfo = model.takeOverDeclarationInfo,
            traceabilityRecordingSystemArt58 = model.traceabilityRecordingSystemArt58,
            traceabilitySupplierIdentificationSystem = model.traceabilitySupplierIdentificationSystem,
            tractorRegistrationNumber = model.tractorRegistrationNumber,
            trailerRegistrationNumber = model.trailerRegistrationNumber,
            transportDocumentInfo = model.transportDocumentInfo,
            transporterComments = model.transporterComments,
            transporterSignature = model.transporterSignature,
            useOfUndersizedFisheryProducts = model.useOfUndersizedFisheryProducts,
            vehicleNationality = model.vehicleNationality,
            vehicleOwnerContact = model.vehicleOwnerContact?.toEntity(),
            vehicleType = model.vehicleType,
            vesselOwnerContact = model.vesselOwnerContact?.toEntity()
        )
    }

    fun toModel(entity: SatiEntity): SatiModel {
        return SatiModel(
            id = entity.id,
            module = entity.module,
            ownerId = entity.ownerId,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            actionTaken = entity.actionTaken,
            agentContact = entity.agentContact?.toModel(),
            appointingAuthority = entity.appointingAuthority,
            beneficialOwnerContact = entity.beneficialOwnerContact?.toModel(),
            catchCertificateInfo = entity.catchCertificateInfo,
            chartererContact = entity.chartererContact?.toModel(),
            commonMarketingStandards = entity.commonMarketingStandards,
            driverContact = entity.driverContact?.toModel(),
            euFishingTripId = entity.euFishingTripId,
            fisheryLotLabelling = entity.fisheryLotLabelling,
            fisheryProductDocumentsInspection = entity.fisheryProductDocumentsInspection,
            fisheryProductStorageMechanismInspected = entity.fisheryProductStorageMechanismInspected,
            fisheryProductWeighedBeforeSale = entity.fisheryProductWeighedBeforeSale,
            freshnessCategories = entity.freshnessCategories,
            importerContact = entity.importerContact?.toModel(),
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
            marketOwnerContact = entity.marketOwnerContact?.toModel(),
            marketOwnerRepresentativeContact = entity.marketOwnerRepresentativeContact?.toModel(),
            marketPremisesAddressId = entity.marketPremisesAddressId,
            marketPremisesLocation = entity.marketPremisesLocation,
            marketPremisesName = entity.marketPremisesName,
            masterComments = entity.masterComments,
            masterContact = entity.masterContact?.toModel(),
            newLotCompositionInfo = entity.newLotCompositionInfo,
            operatorComments = entity.operatorComments,
            operatorSignature = entity.operatorSignature,
            patrolVesselExternalMarking = entity.patrolVesselExternalMarking,
            patrolVesselRadioCallSign = entity.patrolVesselRadioCallSign,
            registeredBuyerContact = entity.registeredBuyerContact?.toModel(),
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
            vehicleOwnerContact = entity.vehicleOwnerContact?.toModel(),
            vehicleType = entity.vehicleType,
            vesselOwnerContact = entity.vesselOwnerContact?.toModel()
        )
    }

    private fun ContactModel.toEntity(): ContactEntity {
        return ContactEntity(
            id = id,
            fullName = fullName,
            nationality = nationality,
            email = email,
            phone = phone,
            address = address?.toEntity(),
            createdAt = createdAt
        )
    }

    private fun ContactEntity.toModel(): ContactModel {
        return ContactModel(
            id = id,
            fullName = fullName,
            nationality = nationality,
            email = email,
            phone = phone,
            address = address?.toModel(),
            createdAt = createdAt
        )
    }

    private fun AddressModel.toEntity(): AddressEntity {
        return AddressEntity(
            id = id,
            street = street,
            zipcode = zipcode,
            town = town,
            country = country,
            createdAt = createdAt
        )
    }

    private fun AddressEntity.toModel(): AddressModel {
        return AddressModel(
            id = id,
            street = street,
            zipcode = zipcode,
            town = town,
            country = country,
            createdAt = createdAt
        )
    }
}
