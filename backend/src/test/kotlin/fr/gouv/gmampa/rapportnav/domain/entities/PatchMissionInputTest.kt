package fr.gouv.gmampa.rapportnav.domain.entities

import com.google.gson.Gson
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input.PatchMissionInput
import fr.gouv.dgampa.rapportnav.infrastructure.utils.GsonSerializer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit

class PatchMissionInputTest {

    private val gson: Gson = GsonSerializer().create()

    @Test
    fun `should create PatchMissionInput with given parameters`() {
        val observationsByUnit = "Some observations"
        val startDateTimeUtc = Instant.now()
        val endDateTimeUtc = Instant.now().plus(1, ChronoUnit.DAYS)

        val input = PatchMissionInput(
            observationsByUnit = observationsByUnit,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc
        )

        assertThat(input.observationsByUnit).isEqualTo(observationsByUnit)
        assertThat(input.startDateTimeUtc).isEqualTo(startDateTimeUtc)
        assertThat(input.endDateTimeUtc).isEqualTo(endDateTimeUtc)
    }

    @Test
    fun `should have default values`() {
        val input = PatchMissionInput()

        assertThat(input.observationsByUnit).isNull()
        assertThat(input.startDateTimeUtc).isNull()
        assertThat(input.endDateTimeUtc).isNull()
    }

    @Test
    fun `should have correct equality and hash code`() {
        val input1 = PatchMissionInput(
            observationsByUnit = "Observations",
            startDateTimeUtc = Instant.now(),
            endDateTimeUtc = Instant.now().plus(1, ChronoUnit.DAYS)
        )

        val input2 = PatchMissionInput(
            observationsByUnit = "Observations",
            startDateTimeUtc = input1.startDateTimeUtc,
            endDateTimeUtc = input1.endDateTimeUtc
        )

        assertThat(input1).isEqualTo(input2)
        assertThat(input1.hashCode()).isEqualTo(input2.hashCode())
    }

    @Test
    fun `should serialize and deserialize correctly with Gson`() {
        val input = PatchMissionInput(
            observationsByUnit = "Observations",
            startDateTimeUtc = Instant.now(),
            endDateTimeUtc = Instant.now().plus(1, ChronoUnit.DAYS)
        )

        // Serialize to JSON
        val json = gson.toJson(input)
        println("Serialized JSON: $json")

        // Deserialize from JSON
        val deserializedInput = gson.fromJson(json, PatchMissionInput::class.java)

        // Assert equality
        assertThat(deserializedInput).isEqualTo(input)
    }

}
