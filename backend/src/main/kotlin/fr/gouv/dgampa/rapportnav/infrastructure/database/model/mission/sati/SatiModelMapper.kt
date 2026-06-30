package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.LogbookMessagePurpose
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.*
import java.util.*

object SatiModelMapper {

    fun toEntity(model: SatiModel): SatiEntity {
        return SatiEntity(
            id = model.id,
            actionId = model.actionId,
            resource = ControlResourceEntity(id = model.resourceId),
            vessel = model.vessels.firstOrNull()?.toEntity(),
            inspectors = model.inspectors.map { it.toEntity() },
            module = SatiModuleType.valueOf(model.module),
        )
    }

    fun toModel(entity: SatiEntity): SatiModel {
        return SatiModel(
            id = entity.id ?: UUID.randomUUID(),
            actionId = entity.actionId,
            resourceId = entity.resource?.id,
            module = entity.module.toString(),
            vessels = entity.vessel?.toModel()?.let { mutableListOf(it) } ?: mutableListOf(),
            inspectors = entity.inspectors?.map { it.toModel() }?.toMutableList() ?: mutableListOf()
        )
    }

    private fun SatiVesselModel.toEntity(): SatiVesselEntity {
        return SatiVesselEntity(
            id = id,
            jpe = SatiJpeEntity(
                tripNumber = tripNumber,
                pnoType = pnoType?.let { LogbookMessagePurpose.valueOf(it) }
            ),
            agent = parties.firstOrNull { it.partyType == PartyType.VESSEL_AGENT.name }?.toEntity(),
            master = parties.firstOrNull { it.partyType == PartyType.VESSEL_MASTER.name }?.toEntity(),
            operator = parties.firstOrNull { it.partyType == PartyType.VESSEL_OPERATOR.name }?.toEntity(),
            isMasterOwner = isMasterOwner
        )
    }

    private fun SatiVesselEntity.toModel(): SatiVesselModel {
        return SatiVesselModel(
            id = id,
            tripNumber = jpe?.tripNumber,
            isMasterOwner = isMasterOwner,
            pnoType = jpe?.pnoType?.toString(),
            parties = mutableListOf<SatiPartyModel>().apply {
                agent?.toModel(PartyType.VESSEL_AGENT)?.let { add(it) }
                master?.toModel(PartyType.VESSEL_MASTER)?.let { add(it) }
                operator?.toModel(PartyType.VESSEL_OPERATOR)?.let { add(it) }
            }
        )
    }

    private fun SatiPartyModel.toEntity(): SatiPartyEntity {
        return SatiPartyEntity(
            id = id,
            partyType = partyType,
            comments = comments,
            signature = signature,
            contact = contacts.firstOrNull()?.toEntity()
        )
    }

    private fun SatiPartyEntity.toModel(type: PartyType? = null): SatiPartyModel {
        return SatiPartyModel(
            id = id,
            partyType = type?.name ?: partyType,
            comments = comments,
            signature = signature,
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
            party = parties.firstOrNull()?.toEntity(),
            isOutOfUnit = isOutOfUnit,
            createdAt = createdAt,
            updatedAt = updatedAt,
            authorityType = authorityType?.let { AuthorityType.valueOf(it) }
        )
    }

    private fun SatiInspectorEntity.toModel(): SatiInspectorModel {
        return SatiInspectorModel(
            id = id,
            agentId = agentId,
            parties = party?.toModel()?.let { mutableListOf(it) } ?: mutableListOf(),
            authorityType = authorityType?.toString(),
            isOutOfUnit = isOutOfUnit,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
