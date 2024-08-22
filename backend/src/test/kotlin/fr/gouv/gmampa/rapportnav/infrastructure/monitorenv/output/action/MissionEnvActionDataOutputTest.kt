package fr.gouv.gmampa.rapportnav.infrastructure.monitorenv.output.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.output.action.MissionEnvActionDataOutput
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime
import java.util.*

class MissionEnvActionDataOutputTest {
    @Test
    fun `toPatchableEnvActionEntity should correctly map properties`() {
        val id = UUID.randomUUID()
        val startTime = ZonedDateTime.now()
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

        assertEquals(id, result.id)
        assertEquals(startTime, result.actionStartDateTimeUtc)
        assertEquals(endTime, result.actionEndDateTimeUtc)
        assertEquals(observations, result.observationsByUnit)
    }
}
