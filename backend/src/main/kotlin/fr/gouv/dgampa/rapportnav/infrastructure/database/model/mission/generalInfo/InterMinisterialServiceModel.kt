package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo

import com.fasterxml.jackson.annotation.JsonIgnore
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.InterMinisterialServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import jakarta.persistence.*

@Entity
@Table(name = "inter_ministerial_service")
class InterMinisterialServiceModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    var id: Int?,

    @Column(name = "administration_id", nullable = false)
    var administrationId: Int = 0,

    @Column(name = "control_unit_id", nullable = false)
    var controlUnitId: Int = 0,

    @ManyToOne
    @JoinColumn(name = "mission_general_info_id")
    @JsonIgnore
    var missionGeneralInfo: MissionGeneralInfoModel? = null
) {

    companion object {
        fun fromInterMinisterialServiceEntity(entity: InterMinisterialServiceEntity, generalInfo: MissionGeneralInfoEntity? = null): InterMinisterialServiceModel {
            return InterMinisterialServiceModel(
                id = entity.id,
                administrationId = entity.administrationId,
                controlUnitId = entity.controlUnitId,
                missionGeneralInfo = generalInfo?.let { MissionGeneralInfoModel.fromMissionGeneralInfoEntity(it) }
            )
        }
    }

    fun toInterMinisterialServiceEntity(): InterMinisterialServiceEntity {
        return InterMinisterialServiceEntity(
            id = id,
            administrationId = administrationId,
            controlUnitId = controlUnitId,
        )
    }
}
