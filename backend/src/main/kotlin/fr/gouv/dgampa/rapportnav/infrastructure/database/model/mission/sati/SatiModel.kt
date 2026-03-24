package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati

import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "sati")
data class SatiModel(

    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    val id: UUID? = null,

    @Column(nullable = false, length = 2)
    val module: String,

    @Column(name = "owner_id", unique = true, nullable = false)
    val ownerId: UUID,

    @Column(name = "created_at", nullable = false)
    val createdAt: OffsetDateTime? = null,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: OffsetDateTime? = null,

    @Column(name = "action_taken", length = 100)
    val actionTaken: String? = null,

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "agent_contact_id", referencedColumnName = "id")
    val agentContact: ContactModel? = null,

    @Column(name = "appointing_authority", length = 50)
    val appointingAuthority: String? = null,

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "beneficial_owner_contact_id", referencedColumnName = "id")
    val beneficialOwnerContact: ContactModel? = null,

    @Column(name = "catch_certificate_info")
    val catchCertificateInfo: Boolean? = null,

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "charterer_contact_id", referencedColumnName = "id")
    val chartererContact: ContactModel? = null,

    @Column(name = "common_marketing_standards")
    val commonMarketingStandards: Boolean? = null,

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "driver_contact_id", referencedColumnName = "id")
    val driverContact: ContactModel? = null,

    @Column(name = "eu_fishing_trip_id", length = 50)
    val euFishingTripId: String? = null,

    @Column(name = "fishery_lot_labelling")
    val fisheryLotLabelling: Boolean? = null,

    @Column(name = "fishery_product_documents_inspection")
    val fisheryProductDocumentsInspection: Boolean? = null,

    @Column(name = "fishery_product_storage_mechanism_inspected")
    val fisheryProductStorageMechanismInspected: Boolean? = null,

    @Column(name = "fishery_product_weighed_before_sale")
    val fisheryProductWeighedBeforeSale: Boolean? = null,

    @Column(name = "freshness_categories", columnDefinition = "text")
    val freshnessCategories: String? = null,

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "importer_contact_id", referencedColumnName = "id")
    val importerContact: ContactModel? = null,

    @Column(name = "infringements_observations", columnDefinition = "text")
    val infringementsObservations: String? = null,

    @Column(name = "inspection_end_datetime_utc")
    val inspectionEndDatetimeUtc: OffsetDateTime? = null,

    @Column(name = "inspection_start_datetime_utc", nullable = false)
    val inspectionStartDatetimeUtc: OffsetDateTime,

    @Column(name = "inspection_location_address_id")
    val inspectionLocationAddressId: UUID? = null,

    @Column(name = "inspector_comments", columnDefinition = "text")
    val inspectorComments: String? = null,

    @Column(name = "inspector_name_or_id", length = 255)
    val inspectorNameOrId: String? = null,

    @Column(name = "inspector_signature")
    val inspectorSignature: Boolean? = null,

    @Column(name = "inspector_unique_id", length = 50)
    val inspectorUniqueId: String? = null,

    @Column(name = "landing_declaration_info")
    val landingDeclarationInfo: Boolean? = null,

    @Column(name = "lot_id_species_quantities", columnDefinition = "jsonb")
    val lotIdSpeciesQuantities: String? = null,

    @Column(name = "lots_art58_compliance")
    val lotsArt58Compliance: Boolean? = null,

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "market_owner_contact_id", referencedColumnName = "id")
    val marketOwnerContact: ContactModel? = null,

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "market_owner_representative_contact_id", referencedColumnName = "id")
    val marketOwnerRepresentativeContact: ContactModel? = null,

    @Column(name = "market_premises_address_id")
    val marketPremisesAddressId: UUID? = null,

    @Column(name = "market_premises_location", length = 255)
    val marketPremisesLocation: String? = null,

    @Column(name = "market_premises_name", length = 255)
    val marketPremisesName: String? = null,

    @Column(name = "master_comments", columnDefinition = "text")
    val masterComments: String? = null,

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "master_contact_id", referencedColumnName = "id")
    val masterContact: ContactModel? = null,

    @Column(name = "new_lot_composition_info", columnDefinition = "jsonb")
    val newLotCompositionInfo: String? = null,

    @Column(name = "operator_comments", columnDefinition = "text")
    val operatorComments: String? = null,

    @Column(name = "operator_signature")
    val operatorSignature: Boolean? = null,

    @Column(name = "patrol_vessel_external_marking", length = 50)
    val patrolVesselExternalMarking: String? = null,

    @Column(name = "patrol_vessel_radio_call_sign", length = 20)
    val patrolVesselRadioCallSign: String? = null,

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "registered_buyer_contact_id", referencedColumnName = "id")
    val registeredBuyerContact: ContactModel? = null,

    @Column(name = "responsible_person_signature")
    val responsiblePersonSignature: Boolean? = null,

    @Column(name = "size_grading_categories", columnDefinition = "text")
    val sizeGradingCategories: String? = null,

    @Column(name = "supplier_invoice_info")
    val supplierInvoiceInfo: Boolean? = null,

    @Column(name = "take_over_declaration_info")
    val takeOverDeclarationInfo: Boolean? = null,

    @Column(name = "traceability_recording_system_art58")
    val traceabilityRecordingSystemArt58: Boolean? = null,

    @Column(name = "traceability_supplier_identification_system")
    val traceabilitySupplierIdentificationSystem: Boolean? = null,

    @Column(name = "tractor_registration_number", length = 20)
    val tractorRegistrationNumber: String? = null,

    @Column(name = "trailer_registration_number", length = 20)
    val trailerRegistrationNumber: String? = null,

    @Column(name = "transport_document_info")
    val transportDocumentInfo: Boolean? = null,

    @Column(name = "transporter_comments", columnDefinition = "text")
    val transporterComments: String? = null,

    @Column(name = "transporter_signature")
    val transporterSignature: Boolean? = null,

    @Column(name = "use_of_undersized_fishery_products")
    val useOfUndersizedFisheryProducts: Boolean? = null,

    @Column(name = "vehicle_nationality", length = 3)
    val vehicleNationality: String? = null,

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "vehicle_owner_contact_id", referencedColumnName = "id")
    val vehicleOwnerContact: ContactModel? = null,

    @Column(name = "vehicle_type", length = 50)
    val vehicleType: String? = null,

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "vessel_owner_contact_id", referencedColumnName = "id")
    val vesselOwnerContact: ContactModel? = null
) {
    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as SatiModel
        return id == other.id
    }
}
