package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.LogbookMessagePurpose
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.*
import java.util.*

object SatiModelMapper {
    fun toEntity(model: SatiModel): SatiEntity {
        return SatiEntity(
            id = model.id,
            actionId = model.actionId,
            resource = ControlResourceEntity(
                id = model.resourceId
            ),
            vessel = model.vessel?.toEntity(),
            createdAt = model.createdAt,
            updatedAt = model.updatedAt,
            inspectors = model.inspectors.map { it.toEntity() },
            module = model.module.let { SatiModuleType.valueOf(it) },
        )
    }

    fun toModel(entity: SatiEntity): SatiModel {
        return SatiModel(
            id = entity.id ?: UUID.randomUUID(),
            actionId = entity.actionId,
            resourceId = entity.resource?.id,
            vessel = entity.vessel?.toModel(),
            module = entity.module.toString(),
            inspectors = entity.inspectors?.map { it.toModel() }?.toMutableList() ?: mutableListOf()
        )
    }

    private fun AddressModel.toEntity(): AddressEntity {
        return AddressEntity(
            id = id,
            lng = lng,
            lat = lat,
            town = town,
            street = street,
            zipcode = zipcode,
            createdAt = createdAt,
            updatedAt = updatedAt,
            fullAddress = fullAddress,
            country = country?.let { CountryCode.getByAlpha3Code(it) }
        )
    }

    private fun AddressEntity.toModel(): AddressModel {
        return AddressModel(
            id = id,
            lng = lng,
            lat = lat,
            town = town,
            street = street,
            zipcode = zipcode,
            fullAddress = fullAddress,
            country = country?.alpha3
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
            createdAt = createdAt,
            updatedAt = updatedAt,
            nationality = nationality,
            address = address?.toEntity()
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
            address = address?.toModel()
        )
    }

    private fun SatiPartyModel.toEntity(): SatiPartyEntity {
        return SatiPartyEntity(
            id = id,
            partyType = partyType,
            comments = comments,
            signature = signature,
            contact = contact?.toEntity(),
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    private fun SatiPartyEntity.toModel(): SatiPartyModel {
        return SatiPartyModel(
            id = id,
            partyType = partyType,
            comments = comments,
            signature = signature,
            createdAt = createdAt,
            updatedAt = updatedAt,
            contact = contact?.toModel()
        )
    }

    private fun SatiVesselModel.toEntity(): SatiVesselEntity {
        return SatiVesselEntity(
            id = id,
            jpe = SatiJpeEntity(
                tripNumber = tripNumber,
                pnoType = pnoType?.let { LogbookMessagePurpose.valueOf(it) }
            ),
            createdAt = createdAt,
            updatedAt = updatedAt,
            agent = agent?.toEntity(),
            master = master?.toEntity(),
            isMasterOwner = isMasterOwner
        )
    }

    private fun SatiVesselEntity.toModel(): SatiVesselModel {
        return SatiVesselModel(
            id = id,
            agent = agent?.toModel(),
            master = master?.toModel(),
            tripNumber = jpe?.tripNumber,
            isMasterOwner = isMasterOwner,
            pnoType = jpe?.pnoType?.toString()
        )
    }

    private fun SatiInspectorModel.toEntity(): SatiInspectorEntity {
        return SatiInspectorEntity(
            id = id,
            agentId = agentId,
            party = party?.toEntity(),
            isOutOfUnit = isOutOfUnit,
            createdAt = createdAt,
            updatedAt = updatedAt,
            authorityType = authorityType?.let { AuthorityType.valueOf(it) },
        )
    }

    private fun SatiInspectorEntity.toModel(): SatiInspectorModel {
        return SatiInspectorModel(
            id = id,
            agentId = agentId,
            party = party?.toModel(),
            authorityType = authorityType?.toString(),
            isOutOfUnit = isOutOfUnit,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
