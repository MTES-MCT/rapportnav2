package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew

import jakarta.persistence.Table
import jakarta.persistence.Entity
import jakarta.persistence.Basic
import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
@Table(name = "crew_role")
data class CrewRoleModel(
    @Id
    @Column(name = "role", unique = true, nullable = false)
    var role: String,

    @Column(name = "is_archived", unique = true, nullable = false)
    var isArchived: Boolean = false,
) {}
