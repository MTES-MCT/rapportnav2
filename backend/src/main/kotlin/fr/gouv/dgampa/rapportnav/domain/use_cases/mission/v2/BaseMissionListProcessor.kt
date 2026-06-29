package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import java.util.UUID

abstract class BaseMissionListProcessor<T, ID>(
    private val loadById: (UUID) -> List<T>,
    private val saveItem: (T) -> T,
    private val deleteItem: (ID) -> Unit,
    private val getId: (T) -> ID?
) {
    fun execute(missionId: UUID, items: List<T>): List<T> {
        val existing = loadById(missionId)
        process(items, existing)
        return loadById(missionId)
    }

    private fun process(items: List<T>, existing: List<T>) {
        val ids = items.mapNotNull(getId)

        val toDelete = existing.filter { getId(it) !in ids }
        val toSave = items.filter { it !in existing }

        toDelete.forEach { getId(it)?.let(deleteItem) }
        toSave.forEach { saveItem(it) }
    }
}
