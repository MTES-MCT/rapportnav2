package fr.gouv.dgampa.rapportnav.infrastructure.database.model

import jakarta.persistence.Table
import jakarta.persistence.Entity
import jakarta.persistence.Basic
import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Id
import fr.gouv.dgampa.rapportnav.domain.entities.crew.CrewEntity
import com.fasterxml.jackson.databind.ObjectMapper



@Entity
@Table(name = "crew")
data class CrewModel(
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "crew_id_seq")
    @SequenceGenerator(name = "crew_id_seq", allocationSize = 1)
    @Column(name = "id", unique = true, nullable = false)
    var id: Int = 0,
    
    @Column(name = "first_name", nullable = false)
    var firstName: String = "",
    
    @Column(name = "last_name", nullable = false)
    var lastName: String = "",
    
    @Column(name = "is_archived", nullable = false)
    var isArchived: Boolean = false,

) {
    fun toCrewEntity(mapper: ObjectMapper) = CrewEntity(
        id = id,
        firstName = firstName,
        lastName = lastName,
        isArchived = isArchived,
    )
}
