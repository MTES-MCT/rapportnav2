package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.ControlResource
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.*
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.sati.Address
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.sati.Contact
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.sati.Sati

object SatiMapper {
    fun fromEntity(entity: SatiEntity?): Sati {
        return Sati(
            id = entity?.id,
            vessel = entity?.vessel?.toOutput(),
            actionId = entity?.actionId.orEmpty(),
            resource = entity?.resource?.toOutput(),
            endDatetimeUtc = entity?.endDatetimeUtc,
            startDatetimeUtc = entity?.startDatetimeUtc,
            module = entity?.module?: SatiModuleType.M1,
            principalInspector = entity?.inspectors?.firstOrNull { it.isPrincipal }?.toOutput() ?: SatiInspector(),
            otherInspectors = entity?.inspectors?.filter { !it.isPrincipal }?.map { it.toOutput() } ?: listOf()
        )
    }

    fun toEntity(response: Sati?): SatiEntity {
        return SatiEntity(
            id = response?.id,
            vessel = response?.vessel?.toEntity(),
            actionId = response?.actionId.orEmpty(),
            resource = response?.resource?.toEntity(),
            endDatetimeUtc = response?.endDatetimeUtc,
            startDatetimeUtc = response?.startDatetimeUtc,
            module = response?.module?: SatiModuleType.M1,
            inspectors = listOfNotNull(
                response?.principalInspector?.toEntity()?.copy(isPrincipal = true)
            ) + (response?.otherInspectors?.map { it.toEntity() } ?: listOf())
        )
    }

    private fun ContactEntity.toOutput(): Contact {
        return Contact(
            id = id,
            email = email,
            phone = phone,
            fullName = fullName,
            lastName = lastName,
            firstName = firstName,
            nationality = nationality,
            address = address?.toOutput()
        )
    }

    private fun Contact.toEntity(): ContactEntity {
        return ContactEntity(
            id = id,
            email = email,
            phone = phone,
            fullName = fullName,
            lastName = lastName,
            firstName = firstName,
            nationality = nationality,
            address = address?.toEntity()
        )
    }

    private fun AddressEntity.toOutput(): Address {
        return Address(
            id = id,
            lng = lng,
            lat = lat,
            town = town,
            zipcode = zipcode,
            country = country,
            fullAddress = fullAddress,
            street = street?: fullAddress
        )
    }

    private fun Address.toEntity(): AddressEntity {
        return AddressEntity(
            id = id,
            lat = lat,
            lng = lng,
            town = town,
            street = street,
            zipcode = zipcode,
            country = country,
            fullAddress = fullAddress
        )
    }

    private fun SatiParty.toEntity(): SatiPartyEntity {
        return SatiPartyEntity(
            id = id,
            partyType = partyType,
            comments = comments,
            signature = signature,
            contact = contact?.toEntity(),
        )
    }

    private fun SatiPartyEntity.toOutput(): SatiParty {
        return SatiParty(
            id = id,
            partyType = partyType,
            comments = comments,
            signature = signature,
            contact = contact?.toOutput()
        )
    }

    private fun SatiVessel.toEntity(): SatiVesselEntity {
        return SatiVesselEntity(
            id = id,
            isMasterOwner = isMasterOwner,
            jpe = jpe?.toEntity(),
            ircs = ircs,
            imo = imo,
            type = type,
            name = name,
            length = length,
            immat = immat,
            extRef = extRef,
            owner = owner?.toEntity(),
            operator = operator?.toEntity(),
            agent = agent?.toEntity(),
            master = master?.toEntity(),
            flagState = flagState
        )
    }

    private fun SatiVesselEntity.toOutput(): SatiVessel {
        return SatiVessel(
            id = id,
            pnoType = jpe?.pnoType?.name,
            tripNumber = jpe?.tripNumber,
            isMasterOwner = isMasterOwner,
            jpe = jpe?.toOutput(),
            ircs = ircs,
            imo = imo,
            type = type,
            name = name,
            length = length,
            immat = immat,
            extRef = extRef,
            owner = owner?.toOutput(),
            operator = operator?.toOutput(),
            agent = agent?.toOutput(),
            master = master?.toOutput(),
            flagState = flagState
        )
    }

    private fun SatiInspector.toEntity(): SatiInspectorEntity {
        return SatiInspectorEntity(
            id = id,
            cardId = cardId,
            agentId = agentId,
            party = party?.toEntity(),
            isOutOfUnit = isOutOfUnit,
            authorityType = authorityType,
        )
    }

    private fun SatiInspectorEntity.toOutput(): SatiInspector {
        return SatiInspector(
            id = id,
            cardId = cardId,
            agentId = agentId,
            isOutOfUnit = isOutOfUnit,
            party = party?.toOutput(),
            authorityType = authorityType
        )
    }

    private fun SatiJpe.toEntity(): SatiJpeEntity {
        return SatiJpeEntity(
            pnoId = pnoId,
            portId = portId,
            pnoType = pnoType,
            portName = portName,
            tripNumber = tripNumber,
            lastStopDate = lastStopDate
        )
    }

    private fun SatiJpeEntity.toOutput(): SatiJpe {
        return SatiJpe(
            pnoId = pnoId,
            portId = portId,
            pnoType = pnoType,
            portName = portName,
            tripNumber = tripNumber,
            lastStopDate = lastStopDate
        )
    }


    private fun ControlResource.toEntity(): ControlResourceEntity {
        return ControlResourceEntity(
            id = id,
            name = name,
            radioFrequency = radioFrequency,
            registrationId = registrationId
        )
    }

    private fun ControlResourceEntity.toOutput(): ControlResource {
        return ControlResource(
            id = id,
            name = name,
            radioFrequency = radioFrequency,
            registrationId = registrationId
        )
    }
}
