package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction.v2

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "infraction_2")
data class InfractionModel2(
    @Id
    @Column(name = "id", unique = true, nullable = false)
    var id: UUID,

    @Column(name = "infraction_type", nullable = false)
    var infractionType: String? = null,

    @Column(name = "observations", nullable = true)
    var observations: String? = null,

    @ElementCollection
    @CollectionTable(
        name = "infraction_natinf_2",
        joinColumns = [JoinColumn(name = "infraction_id")]
    )
    @Column(name = "natinf_code")
    var natinfs: List<String> = mutableListOf()
) {
    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as InfractionModel2
        return id == other.id
    }
}
