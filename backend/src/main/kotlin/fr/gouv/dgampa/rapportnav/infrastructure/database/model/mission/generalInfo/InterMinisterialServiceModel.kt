package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.InterMinisterialServiceEntity
import jakarta.persistence.*

@Entity
@Table(name = "inter_ministerial_service")
class InterMinisterialServiceModel(
    @Id
    @Column(name = "id", unique = true, nullable = false)
    var id: Int,

    @Column(name = "administration_id", nullable = false)
    var administrationId: Int = 0,

    @Column(name = "control_unit_id", nullable = false)
    var controlUnitId: Int = 0,

    @ManyToOne
    @JoinColumn(name = "mission_general_info_id")
    var missionGeneralInfo: MissionGeneralInfoModel? = null
) {

    companion object {
        fun fromInterMinisterialServiceEntity(entity: InterMinisterialServiceEntity): InterMinisterialServiceModel {
            return InterMinisterialServiceModel(
                id = entity.id,
                administrationId = entity.administrationId,
                controlUnitId = entity.controlUnitId,
            )
        }
    }

    fun toInterMinisterialServiceEntity(): InterMinisterialServiceEntity {
        return InterMinisterialServiceEntity(
            id = id,
            administrationId = administrationId,
            controlUnitId = controlUnitId
        )
    }
}
