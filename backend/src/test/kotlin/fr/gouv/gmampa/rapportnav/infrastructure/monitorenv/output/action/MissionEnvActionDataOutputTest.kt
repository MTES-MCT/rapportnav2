package fr.gouv.gmampa.rapportnav.infrastructure.monitorenv.output.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.output.action.MissionEnvActionDataOutput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime
import java.util.*

class MissionEnvActionDataOutputTest {
    @Test
    fun `toPatchableEnvActionEntity should correctly map properties`() {
        val id = UUID.randomUUID()
        val startTime = ZonedDateTime.parse("2022-01-02T12:00:00Z")
        val endTime = startTime.plusHours(1)
        val actionType = ActionTypeEnum.CONTROL
        val observations = "Some observations"

        val actionOutput = MissionEnvActionDataOutput(
            id = id,
            actionStartDateTimeUtc = startTime,
            actionEndDateTimeUtc = endTime,
            actionType = actionType,
            observationsByUnit = observations
        )

        val result = actionOutput.toPatchableEnvActionEntity()

        val expectedStart = startTime.toInstant()
        val expectedEnd = endTime.toInstant()

        assertEquals(id, result.id)
        assertEquals(expectedStart, result.actionStartDateTimeUtc)
        assertEquals(expectedEnd, result.actionEndDateTimeUtc)
        assertEquals(observations, result.observationsByUnit)
    }
}
