package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@EntityListeners(AuditingEntityListener::class)
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
    @JoinColumn(name = "mission_general_info_id", insertable = false, updatable = false)
    @JsonIgnore
    var missionGeneralInfo: MissionGeneralInfoModel? = null,

    @CreatedDate
    @Column(name = "created_at", nullable = true, updatable = false)
    var createdAt: Instant? = null,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    var updatedAt: Instant? = null
)
