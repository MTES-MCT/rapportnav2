package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control

import com.fasterxml.jackson.annotation.JsonIgnore
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.stringToControlResult
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.toStringOrNull
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionControlModel
import jakarta.persistence.*
import java.time.ZonedDateTime
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

    @Column(name = "amount_of_controls", nullable = false)
    var amountOfControls: Int = 1,

    @Column(name = "unit_should_confirm", nullable = true)
    var unitShouldConfirm: Boolean? = false,

    @Column(name = "unit_has_confirmed", nullable = true)
    var unitHasConfirmed: Boolean? = false,

    @Column(name = "staff_outnumbered", nullable = true)
    var staffOutnumbered: String? = null,

    @Column(name = "up_to_date_medical_check", nullable = true)
    var upToDateMedicalCheck: String? = null,

    @Column(name = "knowledge_of_french_law_and_language", nullable = true)
    var knowledgeOfFrenchLawAndLanguage: String? = null,

    @Column(name = "observations", nullable = true)
    var observations: String? = null,

    @Column(name = "deleted_at")
    var deletedAt: ZonedDateTime? = null,

    @OneToOne
    @JoinColumn(name = "action_control_id", referencedColumnName = "id")
    @JsonIgnore
    var actionControl: ActionControlModel? = null
) {
    fun toControlGensDeMer() = ControlGensDeMerEntity(
        id = id,
        missionId = missionId,
        actionControlId = actionControlId,
        amountOfControls = amountOfControls,
        unitHasConfirmed = unitHasConfirmed,
        unitShouldConfirm = unitShouldConfirm,
        staffOutnumbered = stringToControlResult(staffOutnumbered),
        upToDateMedicalCheck = stringToControlResult(upToDateMedicalCheck),
        knowledgeOfFrenchLawAndLanguage = stringToControlResult(knowledgeOfFrenchLawAndLanguage),
        observations = observations,
        deletedAt = deletedAt,
    )

    companion object {
        fun fromControlGensDeMer(control: ControlGensDeMerEntity, actionControl: ActionControlModel) = ControlGensDeMerModel(
            id = control.id,
            missionId = control.missionId,
            actionControlId = actionControl.id,
            amountOfControls = control.amountOfControls,
            unitHasConfirmed = control.unitHasConfirmed,
            unitShouldConfirm = control.unitShouldConfirm,
            staffOutnumbered = control.staffOutnumbered.toStringOrNull(),
            upToDateMedicalCheck = control.upToDateMedicalCheck.toStringOrNull(),
            knowledgeOfFrenchLawAndLanguage = control.knowledgeOfFrenchLawAndLanguage.toStringOrNull(),
            observations = control.observations,
            deletedAt = control.deletedAt,
            actionControl = actionControl,
        )
    }
}
