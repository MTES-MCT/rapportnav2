package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.EstablishmentModel

class EstablishmentEntity(
    var id: Int? = null,
    var name: String? = null,
    var siren: String? = null,
    var siret: String? = null,
    var address: String? = null,
    var city: String? = null,
    var country: String? = null,
    var zipcode: String? = null,
    var isForeign: Boolean? = null,
    var isNotSameAddress: Boolean? = null,
) {

    fun toEstablishmentModel(): EstablishmentModel {
        return EstablishmentModel(
            id = id,
            name = name,
            siren = siren,
            siret = siret,
            address = address,
            city = city,
            country = country,
            zipcode = zipcode,
            isForeign = isForeign,
            isNotSameAddress = isNotSameAddress ?: false
        )
    }

    companion object {
        fun fromEstablishmentModel(model: EstablishmentModel): EstablishmentEntity {
            return EstablishmentEntity(
                id = model.id,
                name = model.name,
                siren = model.siren,
                siret = model.siret,
                address = model.address,
                city = model.city,
                country = model.country,
                zipcode = model.zipcode,
                isForeign = model.isForeign,
                isNotSameAddress = model.isNotSameAddress
            )
        }
    }

}
