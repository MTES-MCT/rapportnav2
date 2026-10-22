package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.LogbookMessagePurpose
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.SectorEstablishmentSubType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.SectorEstablishmentType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.*

object SatiModelMapper {

    fun toEntity(model: SatiModel): SatiEntity {
        return SatiEntity(
            id = model.id,
            actionId = model.actionId,
            resource = ControlResourceEntity(id = model.resourceId),
            vessel = model.vessels.firstOrNull()?.toEntity(),
            establishment = model.establishments.firstOrNull()?.toEntity(),
            inspectors = model.inspectors.map { it.toEntity() },
            module = SatiModuleType.valueOf(model.module),
        )
    }

    fun toModel(entity: SatiEntity): SatiModel {
        return SatiModel(
            id = entity.id,
            actionId = entity.actionId,
            resourceId = entity.resource?.id,
            module = entity.module.toString(),
            vessels = entity.vessel?.toModel()?.let { mutableListOf(it) } ?: mutableListOf(),
            establishments = entity.establishment?.toModel()?.let { mutableListOf(it) } ?: mutableListOf(),
            inspectors = entity.inspectors?.map { it.toModel() }?.toMutableList() ?: mutableListOf()
        )
    }

    private fun SatiVesselModel.toEntity(): SatiVesselEntity {
        return SatiVesselEntity(
            id = id,
            jpe = SatiJpeEntity(
                portId = portId,
                tripNumber = tripNumber,
                lastStopDate = lastStopDate,
                lastPortIsNotSame = lastPortIsNotSame,
                pnoType = pnoType?.let { LogbookMessagePurpose.valueOf(it) }
            ),
            agent = parties.firstOrNull { it.partyType == SatiPartyType.VESSEL_AGENT.name }?.toEntity(),
            master = parties.firstOrNull { it.partyType == SatiPartyType.VESSEL_MASTER.name }?.toEntity(),
            beneficiary = parties.firstOrNull { it.partyType == SatiPartyType.VESSEL_BENEFICIARY.name }?.toEntity(),
            isMasterOwner = isMasterOwner
        )
    }

    private fun SatiVesselEntity.toModel(): SatiVesselModel {
        return SatiVesselModel(
            id = id,
            portId = jpe?.portId,
            tripNumber = jpe?.tripNumber,
            isMasterOwner = isMasterOwner,
            lastStopDate = jpe?.lastStopDate,
            pnoType = jpe?.pnoType?.toString(),
            lastPortIsNotSame = jpe?.lastPortIsNotSame,
            parties = mutableListOf<SatiPartyModel>().apply {
                agent?.toModel(SatiPartyType.VESSEL_AGENT)?.let { add(it) }
                master?.toModel(SatiPartyType.VESSEL_MASTER)?.let { add(it) }
            }
        )
    }

    private fun SatiEstablishmentModel.toEntity(): SatiEstablishmentEntity {
        return SatiEstablishmentEntity(
            id = id,
            type = type?.let { SectorEstablishmentType.valueOf(it) },
            subType = subType?.let { SectorEstablishmentSubType.valueOf(it) },
            owner = parties.firstOrNull { it.partyType == SatiPartyType.ESTABLISHMENT_OWNER.name }?.toEntity(),
            operator = parties.firstOrNull { it.partyType == SatiPartyType.ESTABLISHMENT_OPERATOR.name }?.toEntity()
        )
    }

    private fun SatiEstablishmentEntity.toModel(): SatiEstablishmentModel {
        return SatiEstablishmentModel(
            id = id,
            type = type?.name,
            subType = subType?.name,
            establishmentId = data?.id,
            parties = mutableListOf<SatiPartyModel>().apply {
                owner?.toModel(SatiPartyType.ESTABLISHMENT_OWNER)?.let { add(it) }
                operator?.toModel(SatiPartyType.ESTABLISHMENT_OPERATOR)?.let { add(it) }
            }
        )
    }

    private const val ADDRESS_STREET = "street"
    private const val ADDRESS_FULL_ADDRESS = "fullAddress"
    private const val ADDRESS_ZIPCODE = "zipcode"
    private const val ADDRESS_TOWN = "town"
    private const val ADDRESS_COUNTRY = "country"
    private const val ADDRESS_LNG = "lng"
    private const val ADDRESS_LAT = "lat"

    private fun Map<String, Any?>.toAddressEntity(): AddressEntity {
        return AddressEntity(
            street = this[ADDRESS_STREET] as? String,
            fullAddress = this[ADDRESS_FULL_ADDRESS] as? String,
            zipcode = this[ADDRESS_ZIPCODE] as? String,
            town = this[ADDRESS_TOWN] as? String,
            country = this[ADDRESS_COUNTRY] as? String,
            lng = (this[ADDRESS_LNG] as? Number)?.toDouble(),
            lat = (this[ADDRESS_LAT] as? Number)?.toDouble()
        )
    }

    private fun AddressEntity.toAddressMap(): Map<String, Any?>? {
        return mapOf(
            ADDRESS_STREET to street,
            ADDRESS_FULL_ADDRESS to fullAddress,
            ADDRESS_ZIPCODE to zipcode,
            ADDRESS_TOWN to town,
            ADDRESS_COUNTRY to country,
            ADDRESS_LNG to lng,
            ADDRESS_LAT to lat
        ).filterValues { it != null }.ifEmpty { null }
    }

    private fun SatiPartyModel.toEntity(): SatiPartyEntity {
        return SatiPartyEntity(
            id = id,
            comments = comments,
            signature = signature,
            contact = contacts.firstOrNull()?.toEntity(),
            partyType = partyType?.let { SatiPartyType.valueOf(it) }
        )
    }

    private fun SatiPartyEntity.toModel(type: SatiPartyType? = null): SatiPartyModel {
        return SatiPartyModel(
            id = id,
            comments = comments,
            signature = signature,
            partyType = type?.name ?: partyType?.name,
            contacts = contact?.toModel()?.let { mutableListOf(it) } ?: mutableListOf()
        )
    }

    private fun ContactModel.toEntity(): ContactEntity {
        return ContactEntity(
            id = id,
            email = email,
            phone = phone,
            fullName = fullName,
            lastName = lastName,
            firstName = firstName,
            nationality = nationality,
            address = addresses.firstOrNull()?.toEntity()
        )
    }

    private fun ContactEntity.toModel(): ContactModel {
        return ContactModel(
            id = id,
            email = email,
            phone = phone,
            fullName = fullName,
            lastName = lastName,
            firstName = firstName,
            nationality = nationality,
            addresses = address?.toModel()?.let { mutableListOf(it) } ?: mutableListOf()
        )
    }

    private fun AddressModel.toEntity(): AddressEntity {
        return AddressEntity(
            id = id, lng = lng, lat = lat, town = town,
            street = street, zipcode = zipcode, fullAddress = fullAddress, country = country
        )
    }

    private fun AddressEntity.toModel(): AddressModel {
        return AddressModel(
            id = id, lng = lng, lat = lat, town = town,
            street = street, zipcode = zipcode, fullAddress = fullAddress, country = country
        )
    }

    private fun SatiInspectorModel.toEntity(): SatiInspectorEntity {
        return SatiInspectorEntity(
            id = id,
            agentId = agentId,
            cardId = cardId,
            isPrincipal = isPrincipal,
            isOutOfUnit = isOutOfUnit,
            party = parties.firstOrNull()?.toEntity(),
            authorityType = authorityType?.let { AuthorityType.valueOf(it) }
        )
    }

    private fun SatiInspectorEntity.toModel(): SatiInspectorModel {
        return SatiInspectorModel(
            id = id,
            cardId = cardId,
            agentId = agentId,
            isPrincipal = isPrincipal,
            parties = party?.toModel()?.let { mutableListOf(it) } ?: mutableListOf(),
            authorityType = authorityType?.toString(),
            isOutOfUnit = isOutOfUnit
        )
    }
}
