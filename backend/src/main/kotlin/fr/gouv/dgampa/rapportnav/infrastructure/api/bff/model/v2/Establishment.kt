package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.EstablishmentEntity

class Establishment(
    var id: Int? = null,
    var name: String,
    var siren: String? = null,
    var siret: String? = null,
    var address: String? = null,
    var city: String? = null,
    var country: String? = null,
    var zipcode: String? = null,
    var isForeign: Boolean? = null,
) {

    fun toEstablishmentEntity(): EstablishmentEntity {
        return EstablishmentEntity(
            id = id,
            name = name,
            siren = siren,
            siret = siret,
            address = address,
            city = city,
            country = country,
            zipcode = zipcode,
            isForeign = isForeign
        )
    }

    companion object {
        fun fromEstablishmentEntity(model: EstablishmentEntity): Establishment {
            return Establishment(
                id = model.id,
                name = model.name,
                siren = model.siren,
                siret = model.siret,
                address = model.address,
                city = model.city,
                country = model.country,
                zipcode = model.zipcode,
                isForeign = model.isForeign
            )
        }
    }
}
