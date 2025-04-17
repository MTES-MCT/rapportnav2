package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.BaseAction
import java.time.Instant
import java.util.*
import kotlin.reflect.full.primaryConstructor

/**
 * Mock factory that allows you to boot up a configurable mocked Action for your tests
 *
 * use it like:
 * - ActionMockFactory.create<ActionPublicOrderEntity>()
 * - ActionMockFactory.create<ActionNauticalEventEntity>(
 *      additionalParams = mapOf(
 *          "observations" to "RAS"
 *      )
 *   )
 */
object ActionMockFactory {
    inline fun <reified T : BaseAction> create(
        id: UUID = UUID.randomUUID(),
        missionId: String = "1",
        startDateTimeUtc: Instant = Instant.parse("2022-01-02T12:00:00Z"),
        endDateTimeUtc: Instant? = Instant.parse("2022-01-02T14:00:00Z"),
        observations: String? = null,
        additionalParams: Map<String, Any?> = emptyMap()
    ): T {
        val params = mutableMapOf<String, Any?>(
            "id" to id,
            "missionId" to missionId,
            "startDateTimeUtc" to startDateTimeUtc,
            "endDateTimeUtc" to endDateTimeUtc,
            "observations" to observations
        )

        params.putAll(additionalParams)

        val primaryConstructor = T::class.primaryConstructor
            ?: throw IllegalArgumentException("No primary constructor found for class ${T::class}")

        return primaryConstructor.callBy(
            primaryConstructor.parameters.associateWith { param ->
                params[param.name]
            }
        )
    }
}
