package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.InterMinisterialServiceEntity

data class InterMinisterialService(
    val id: Int? = null,
    val administrationId: Int,
    val controlUnitId: Int,
    val missionGeneralInfo: MissionGeneralInfo?
) {

    companion object {
        fun fromInterMinisterialServiceEntity(entity: InterMinisterialServiceEntity): InterMinisterialService {
            return InterMinisterialService(
                id = entity.id,
                administrationId = entity.administrationId,
                controlUnitId = entity.controlUnitId,
                missionGeneralInfo = entity.missionGeneralInfo?.toMissionGeneralInfo()
            )
        }
    }
    fun toInterMinisterialServiceEntity(): InterMinisterialServiceEntity {
        return InterMinisterialServiceEntity(
            id = id,
            administrationId = administrationId,
            controlUnitId = controlUnitId,
            missionGeneralInfo = missionGeneralInfo?.toMissionGeneralInfoEntity()
        )
    }
}
