package fr.gouv.dgampa.rapportnav.infrastructure.database.model.control

import fr.gouv.dgampa.rapportnav.domain.entities.control.ControlGensDeMer
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "control_gens_de_mer")
data class ControlGensDeMerModel(
    @Id
    @Basic(optional = false)
    @Column(name = "id", unique = true, nullable = false)
    var id: UUID = UUID.randomUUID(),

    @Column(name = "mission_id", nullable = false)
    var missionId: Int,

    @Column(name = "confirmed", nullable = true)
    var confirmed: Boolean? = false,

    @Column(name = "staff_outnumbered", nullable = true)
    var staffOutnumbered: Boolean? = null,

    @Column(name = "up_to_date_medical_check", nullable = true)
    var upToDateMedicalCheck: Boolean? = null,

    @Column(name = "knowledge_of_french_law_and_language", nullable = true)
    var knowledgeOfFrenchLawAndLanguage: Boolean? = null,
) {
    fun toControlGensDeMer() = ControlGensDeMer(
        id = id,
        missionId = missionId,
        confirmed = confirmed,
        staffOutnumbered = staffOutnumbered,
        upToDateMedicalCheck = upToDateMedicalCheck,
        knowledgeOfFrenchLawAndLanguage = knowledgeOfFrenchLawAndLanguage
    )
}
