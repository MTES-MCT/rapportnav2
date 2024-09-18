package fr.gouv.gmampa.rapportnav.infrastructure.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import fr.gouv.dgampa.rapportnav.infrastructure.utils.InstantAdapter
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.time.format.DateTimeParseException

@SpringBootTest(classes = [InstantAdapterTest::class])
class InstantAdapterTest {

    private lateinit var gson: Gson
    private lateinit var adapter: InstantAdapter

    @BeforeEach
    fun setup() {
        adapter = InstantAdapter()
        gson = GsonBuilder()
            .registerTypeAdapter(Instant::class.java, adapter)
            .create()
    }

    @Test
    fun `should serialize Instant to JSON`() {
        val dateTime = Instant.parse("2024-07-25T15:00:00Z")

        val json = gson.toJson(dateTime)

        // Expected format is ISO-8601 string representation
        assertThat(json).isEqualTo("\"2024-07-25T15:00:00Z\"")
    }

    @Test
    fun `should handle invalid date format during serialization`() {
        val invalidJson = "\"InvalidDateFormat\""

        assertThatThrownBy { gson.toJson(invalidJson, Instant::class.java) }
            .isInstanceOf(ClassCastException::class.java)
    }

    @Test
    fun `should deserialize JSON to Instant`() {
        val json = "\"2024-07-25T15:00:00Z\""

        val dateTime = gson.fromJson(json, Instant::class.java)

        assertThat(dateTime).isEqualTo(Instant.parse("2024-07-25T15:00:00Z"))
    }

    @Test
    fun `should handle invalid date format during deserialization`() {
        val invalidJson = "\"InvalidDateFormat\""

        assertThatThrownBy { gson.fromJson(invalidJson, Instant::class.java) }
            .isInstanceOf(DateTimeParseException::class.java)
    }
}
