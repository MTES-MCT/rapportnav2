package fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources

data class LegacyControlUnitEntity(
    val id: Int,
    val administration: String,
    val isArchived: Boolean,
    val name: String,
    val resources: MutableList<LegacyControlUnitResourceEntity>,
    val contact: String? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LegacyControlUnitEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id
    }
}
