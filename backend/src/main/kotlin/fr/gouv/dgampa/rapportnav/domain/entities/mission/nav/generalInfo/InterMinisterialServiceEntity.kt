package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.InterMinisterialServiceModel

data class InterMinisterialServiceEntity(
    var id: Int? = null,
    var administrationId: Int,
    var controlUnitId: Int,
    var missionGeneralInfo: GeneralInfoEntity? = null
) {

    companion object {
        fun fromInterMinisterialServiceModel(model: InterMinisterialServiceModel): InterMinisterialServiceEntity {
            return InterMinisterialServiceEntity(
                id = model.id,
                administrationId = model.administrationId,
                controlUnitId = model.controlUnitId,
            )
        }
    }

    fun toInterMinisterialServiceModel(): InterMinisterialServiceModel {
        return InterMinisterialServiceModel(
            id = id,
            administrationId = administrationId,
            controlUnitId = controlUnitId
        )
    }
}
