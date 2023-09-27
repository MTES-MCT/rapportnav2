package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control

import com.fasterxml.jackson.annotation.JsonIgnore
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMer
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionControlModel
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "control_gens_de_mer")
data class ControlGensDeMerModel(
    @Id
    @Column(name = "id", unique = true, nullable = false)
    var id: UUID,

    @Column(name = "mission_id", nullable = false)
    var missionId: Int,

    @Column(name = "action_control_id", nullable = false, insertable = false, updatable = false)
    var actionControlId: UUID,

    @Column(name = "confirmed", nullable = true)
    var confirmed: Boolean? = false,

    @Column(name = "staff_outnumbered", nullable = true)
    var staffOutnumbered: Boolean? = null,

    @Column(name = "up_to_date_medical_check", nullable = true)
    var upToDateMedicalCheck: Boolean? = null,

    @Column(name = "knowledge_of_french_law_and_language", nullable = true)
    var knowledgeOfFrenchLawAndLanguage: Boolean? = null,

    @Column(name = "observations", nullable = true)
    var observations: String? = null,

    @OneToOne
    @JoinColumn(name = "action_control_id", referencedColumnName = "id")
    @JsonIgnore
    var actionControl: ActionControlModel
) {
    fun toControlGensDeMer() = ControlGensDeMer(
        id = id,
        missionId = missionId,
        actionControlId = actionControl.id,
        confirmed = confirmed,
        staffOutnumbered = staffOutnumbered,
        upToDateMedicalCheck = upToDateMedicalCheck,
        knowledgeOfFrenchLawAndLanguage = knowledgeOfFrenchLawAndLanguage,
        observations = observations
    )
}
