package fr.gouv.gmampa.rapportnav.infrastructure.monitorenv.output.action

import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.output.action.MissionEnvActionDataOutput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime
import java.util.*

class MissionEnvActionDataOutputTest {
    @Test
    fun `toPatchableEnvActionEntity should correctly map properties`() {
        val id = UUID.randomUUID()
        val startTime = ZonedDateTime.parse("2022-01-02T12:00:00Z")
        val endTime = startTime.plusHours(1)
        val observations = "Some observations"

        val actionOutput = MissionEnvActionDataOutput(
            id = id,
            actionStartDateTimeUtc = startTime,
            actionEndDateTimeUtc = endTime,
            observationsByUnit = observations,
            hasDivingDuringOperation = true,
            incidentDuringOperation = false
        )

        val result = actionOutput.toPatchableEnvActionEntity()

        val expectedStart = startTime.toInstant()
        val expectedEnd = endTime.toInstant()

        assertEquals(id, result.id)
        assertEquals(expectedStart, result.actionStartDateTimeUtc)
        assertEquals(expectedEnd, result.actionEndDateTimeUtc)
        assertEquals(observations, result.observationsByUnit)
        assertEquals(true, result.hasDivingDuringOperation)
        assertEquals(false, result.incidentDuringOperation)
    }

    @Test
    fun `toPatchableEnvActionEntity should handle null optional fields`() {
        val id = UUID.randomUUID()

        val actionOutput = MissionEnvActionDataOutput(id = id)

        val result = actionOutput.toPatchableEnvActionEntity()

        assertEquals(id, result.id)
        assertNull(result.actionStartDateTimeUtc)
        assertNull(result.actionEndDateTimeUtc)
        assertNull(result.observationsByUnit)
        assertNull(result.hasDivingDuringOperation)
        assertNull(result.incidentDuringOperation)
    }

    @Test
    fun `toPatchableEnvActionEntity should handle null start with non-null end`() {
        val id = UUID.randomUUID()
        val endTime = ZonedDateTime.parse("2022-01-02T13:00:00Z")

        val actionOutput = MissionEnvActionDataOutput(
            id = id,
            actionEndDateTimeUtc = endTime
        )

        val result = actionOutput.toPatchableEnvActionEntity()

        assertEquals(id, result.id)
        assertNull(result.actionStartDateTimeUtc)
        assertEquals(endTime.toInstant(), result.actionEndDateTimeUtc)
    }

    @Test
    fun `toPatchableEnvActionEntity should handle non-null start with null end`() {
        val id = UUID.randomUUID()
        val startTime = ZonedDateTime.parse("2022-01-02T12:00:00Z")

        val actionOutput = MissionEnvActionDataOutput(
            id = id,
            actionStartDateTimeUtc = startTime
        )

        val result = actionOutput.toPatchableEnvActionEntity()

        assertEquals(id, result.id)
        assertEquals(startTime.toInstant(), result.actionStartDateTimeUtc)
        assertNull(result.actionEndDateTimeUtc)
    }

    @Test
    fun `toPatchableEnvActionEntity should map boolean flags independently`() {
        val id = UUID.randomUUID()

        val output1 = MissionEnvActionDataOutput(id = id, hasDivingDuringOperation = false, incidentDuringOperation = true)
        val result1 = output1.toPatchableEnvActionEntity()
        assertEquals(false, result1.hasDivingDuringOperation)
        assertEquals(true, result1.incidentDuringOperation)

        val output2 = MissionEnvActionDataOutput(id = id, hasDivingDuringOperation = null, incidentDuringOperation = true)
        val result2 = output2.toPatchableEnvActionEntity()
        assertNull(result2.hasDivingDuringOperation)
        assertEquals(true, result2.incidentDuringOperation)
    }
}
