import fr.gouv.dgampa.rapportnav.config.DependentFieldValue
import fr.gouv.dgampa.rapportnav.config.MandatoryForStats
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.utils.EntityCompletenessValidator
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

@SpringBootTest(classes = [EntityCompletenessValidator::class])
class EntityCompletenessValidatorTest {

    private class TestEntity(
        val status: String = "",
        val isFinished: Boolean = false,

        @MandatoryForStats
        val someFieldWithNoCondition: String? = null,

        @MandatoryForStats(
            enableIf = [
                DependentFieldValue(field = "status", value = ["RIGHT_VALUE", "ACCEPTABLE_VALUE"]),
                DependentFieldValue(field = "isFinished", value = ["true"])
            ]
        )
        val someFieldWithConditions: String? = null
    )

    @Test
    fun `should return false when mandatory fields are missing`() {
        val entity = TestEntity()
        val result = EntityCompletenessValidator.isCompleteForStats(entity)
        assertFalse(result)
    }


    @Test
    fun `should return true when field is missing but not considered mandatory`() {
        val entity = TestEntity(
            status = "WRONG_VALUE",
            isFinished = true, // right value
            someFieldWithNoCondition = "whatever",
            someFieldWithConditions = null,
        )
        val result = EntityCompletenessValidator.isCompleteForStats(entity)
        assertTrue(result)
    }

    @Test
    fun `should return false mandatory field is missing, though enabling condition matched`() {
        val entity = TestEntity(
            status = "RIGHT_VALUE",
            isFinished = true, // right value
            someFieldWithNoCondition = "whatever",
            someFieldWithConditions = null, // value is null, making the function return false
        )
        val result = EntityCompletenessValidator.isCompleteForStats(entity)
        assertFalse(result)
    }

    @Test
    fun `should return true when all fields matching the enabling conditions are set`() {
        val entity = TestEntity(
            status = "RIGHT_VALUE",
            isFinished = true, // right value
            someFieldWithNoCondition = "whatever",
            someFieldWithConditions = "whatever",
        )
        val result = EntityCompletenessValidator.isCompleteForStats(entity)
        assertTrue(result)
    }

    @Test
    fun `ActionStatusEntity - should return true - status does not need reason `() {
        val entity = ActionStatusEntity(
            id = UUID.randomUUID(),
            missionId = 10,
            startDateTimeUtc = ZonedDateTime.of(2023, 6, 19, 10, 0, 0, 0, ZoneId.of("Europe/Berlin")),
            status = ActionStatusType.NAVIGATING
        )
        val result = EntityCompletenessValidator.isCompleteForStats(entity)
        assertTrue(result)
    }

    @Test
    fun `ActionStatusEntity - should return true - status come with reason`() {
        val entity = ActionStatusEntity(
            id = UUID.randomUUID(),
            missionId = 10,
            startDateTimeUtc = ZonedDateTime.of(2023, 6, 19, 10, 0, 0, 0, ZoneId.of("Europe/Berlin")),
            status = ActionStatusType.DOCKED,
            reason = ActionStatusReason.WEATHER
        )
        val result = EntityCompletenessValidator.isCompleteForStats(entity)
        assertTrue(result)
    }

    @Test
    fun `ActionStatusEntity - should return false - status is missing reason`() {
        val entity = ActionStatusEntity(
            id = UUID.randomUUID(),
            missionId = 10,
            startDateTimeUtc = ZonedDateTime.of(2023, 6, 19, 10, 0, 0, 0, ZoneId.of("Europe/Berlin")),
            status = ActionStatusType.DOCKED,
        )
        val result = EntityCompletenessValidator.isCompleteForStats(entity)
        assertFalse(result)
    }
}
