package fr.gouv.gmampa.rapportnav.domain.utils

import fr.gouv.dgampa.rapportnav.config.DependentFieldValue
import fr.gouv.dgampa.rapportnav.config.MandatoryForStats
import fr.gouv.dgampa.rapportnav.domain.utils.EntityCompletenessValidator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class EntityCompletenessValidatorTest {

    // Test entity with no conditions - field is always required
    class EntityWithUnconditionalField(
        @MandatoryForStats
        var requiredField: String? = null
    )

    // Test entity with single condition
    class EntityWithSingleCondition(
        var type: String? = null,

        @MandatoryForStats(
            enableIf = [DependentFieldValue(field = "type", value = ["ACTIVE"])]
        )
        var conditionalField: String? = null
    )

    // Test entity with multiple conditions (AND logic)
    class EntityWithMultipleConditions(
        var actionType: String? = null,
        var sectorType: String? = null,
        var establishmentType: String? = null,

        @MandatoryForStats(
            enableIf = [
                DependentFieldValue(field = "actionType", value = ["CONTROL_SECTOR"]),
                DependentFieldValue(field = "sectorType", value = ["FISHING"]),
                DependentFieldValue(field = "establishmentType", value = ["LANDING_SITE", "FISH_AUCTION"])
            ]
        )
        var locationDescription: String? = null,

        @MandatoryForStats(
            enableIf = [
                DependentFieldValue(field = "actionType", value = ["CONTROL_SECTOR"]),
                DependentFieldValue(field = "establishmentType", value = ["GMS", "RESTAURANT"])
            ]
        )
        var establishment: String? = null
    )

    // Test entity with multiple values in single condition
    class EntityWithMultipleValuesCondition(
        var status: String? = null,

        @MandatoryForStats(
            enableIf = [DependentFieldValue(field = "status", value = ["DOCKED", "UNAVAILABLE"])]
        )
        var reason: String? = null
    )

    // ==================== Tests for unconditional fields ====================

    @Test
    fun `unconditional field - should return false when required field is null`() {
        val entity = EntityWithUnconditionalField(requiredField = null)
        assertThat(EntityCompletenessValidator.isCompleteForStats(entity)).isFalse()
    }

    @Test
    fun `unconditional field - should return false when required field is empty string`() {
        val entity = EntityWithUnconditionalField(requiredField = "")
        assertThat(EntityCompletenessValidator.isCompleteForStats(entity)).isFalse()
    }

    @Test
    fun `unconditional field - should return true when required field has value`() {
        val entity = EntityWithUnconditionalField(requiredField = "some value")
        assertThat(EntityCompletenessValidator.isCompleteForStats(entity)).isTrue()
    }

    // ==================== Tests for single condition ====================

    @Test
    fun `single condition - should return false when condition matches and field is null`() {
        val entity = EntityWithSingleCondition(
            type = "ACTIVE",
            conditionalField = null
        )
        assertThat(EntityCompletenessValidator.isCompleteForStats(entity)).isFalse()
    }

    @Test
    fun `single condition - should return true when condition matches and field has value`() {
        val entity = EntityWithSingleCondition(
            type = "ACTIVE",
            conditionalField = "some value"
        )
        assertThat(EntityCompletenessValidator.isCompleteForStats(entity)).isTrue()
    }

    @Test
    fun `single condition - should return true when condition does not match and field is null`() {
        val entity = EntityWithSingleCondition(
            type = "INACTIVE",
            conditionalField = null
        )
        assertThat(EntityCompletenessValidator.isCompleteForStats(entity)).isTrue()
    }

    @Test
    fun `single condition - should return true when condition field is null and conditional field is null`() {
        val entity = EntityWithSingleCondition(
            type = null,
            conditionalField = null
        )
        assertThat(EntityCompletenessValidator.isCompleteForStats(entity)).isTrue()
    }

    // ==================== Tests for multiple conditions (AND logic) ====================

    @Test
    fun `multiple conditions - should return false when ALL conditions match and field is null`() {
        val entity = EntityWithMultipleConditions(
            actionType = "CONTROL_SECTOR",
            sectorType = "FISHING",
            establishmentType = "LANDING_SITE",
            locationDescription = null
        )
        assertThat(EntityCompletenessValidator.isCompleteForStats(entity)).isFalse()
    }

    @Test
    fun `multiple conditions - should return true when ALL conditions match and field has value`() {
        val entity = EntityWithMultipleConditions(
            actionType = "CONTROL_SECTOR",
            sectorType = "FISHING",
            establishmentType = "LANDING_SITE",
            locationDescription = "Brest (29200)"
        )
        assertThat(EntityCompletenessValidator.isCompleteForStats(entity)).isTrue()
    }

    @Test
    fun `multiple conditions - should return true when first condition does not match and field is null`() {
        val entity = EntityWithMultipleConditions(
            actionType = "OTHER_ACTION",  // Does not match
            sectorType = "FISHING",
            establishmentType = "LANDING_SITE",
            locationDescription = null
        )
        assertThat(EntityCompletenessValidator.isCompleteForStats(entity)).isTrue()
    }

    @Test
    fun `multiple conditions - should return true when second condition does not match and field is null`() {
        val entity = EntityWithMultipleConditions(
            actionType = "CONTROL_SECTOR",
            sectorType = "PLEASURE",  // Does not match
            establishmentType = "LANDING_SITE",
            locationDescription = null
        )
        assertThat(EntityCompletenessValidator.isCompleteForStats(entity)).isTrue()
    }

    @Test
    fun `multiple conditions - should return true when third condition does not match and field is null`() {
        val entity = EntityWithMultipleConditions(
            actionType = "CONTROL_SECTOR",
            sectorType = "FISHING",
            establishmentType = "GMS",  // Does not match LANDING_SITE or FISH_AUCTION for locationDescription
            locationDescription = null,
            establishment = "Some Establishment"  // Required because establishmentType is GMS
        )
        assertThat(EntityCompletenessValidator.isCompleteForStats(entity)).isTrue()
    }

    @Test
    fun `multiple conditions - FISH_AUCTION should also trigger locationDescription requirement`() {
        val entity = EntityWithMultipleConditions(
            actionType = "CONTROL_SECTOR",
            sectorType = "FISHING",
            establishmentType = "FISH_AUCTION",
            locationDescription = null
        )
        assertThat(EntityCompletenessValidator.isCompleteForStats(entity)).isFalse()
    }

    // ==================== Tests for establishment field (different conditions) ====================

    @Test
    fun `establishment field - should return false when conditions match and field is null`() {
        val entity = EntityWithMultipleConditions(
            actionType = "CONTROL_SECTOR",
            sectorType = "FISHING",
            establishmentType = "GMS",
            establishment = null,
            locationDescription = null  // Not required because establishmentType is GMS
        )
        assertThat(EntityCompletenessValidator.isCompleteForStats(entity)).isFalse()
    }

    @Test
    fun `establishment field - should return true when conditions match and field has value`() {
        val entity = EntityWithMultipleConditions(
            actionType = "CONTROL_SECTOR",
            sectorType = "FISHING",
            establishmentType = "GMS",
            establishment = "Some Establishment",
            locationDescription = null  // Not required because establishmentType is GMS
        )
        assertThat(EntityCompletenessValidator.isCompleteForStats(entity)).isTrue()
    }

    @Test
    fun `establishment field - should return true when actionType does not match`() {
        val entity = EntityWithMultipleConditions(
            actionType = "OTHER",
            sectorType = "FISHING",
            establishmentType = "GMS",
            establishment = null,
            locationDescription = null
        )
        assertThat(EntityCompletenessValidator.isCompleteForStats(entity)).isTrue()
    }

    // ==================== Tests for multiple values in single condition ====================

    @Test
    fun `multiple values - should return false when status is DOCKED and reason is null`() {
        val entity = EntityWithMultipleValuesCondition(
            status = "DOCKED",
            reason = null
        )
        assertThat(EntityCompletenessValidator.isCompleteForStats(entity)).isFalse()
    }

    @Test
    fun `multiple values - should return false when status is UNAVAILABLE and reason is null`() {
        val entity = EntityWithMultipleValuesCondition(
            status = "UNAVAILABLE",
            reason = null
        )
        assertThat(EntityCompletenessValidator.isCompleteForStats(entity)).isFalse()
    }

    @Test
    fun `multiple values - should return true when status is ACTIVE and reason is null`() {
        val entity = EntityWithMultipleValuesCondition(
            status = "ACTIVE",
            reason = null
        )
        assertThat(EntityCompletenessValidator.isCompleteForStats(entity)).isTrue()
    }

    @Test
    fun `multiple values - should return true when status matches and reason has value`() {
        val entity = EntityWithMultipleValuesCondition(
            status = "DOCKED",
            reason = "Maintenance"
        )
        assertThat(EntityCompletenessValidator.isCompleteForStats(entity)).isTrue()
    }

    // ==================== Tests for real-world scenario ====================

    @Test
    fun `real scenario - CONTROL_SECTOR FISHING LANDING_SITE requires locationDescription not establishment`() {
        // This test validates the core fix - locationDescription is required, establishment is not
        val entity = EntityWithMultipleConditions(
            actionType = "CONTROL_SECTOR",
            sectorType = "FISHING",
            establishmentType = "LANDING_SITE",
            locationDescription = "Brest (29200)",
            establishment = null  // Should NOT be required for LANDING_SITE
        )
        assertThat(EntityCompletenessValidator.isCompleteForStats(entity)).isTrue()
    }

    @Test
    fun `real scenario - CONTROL_SECTOR FISHING GMS requires establishment not locationDescription`() {
        // This test validates the core fix - establishment is required, locationDescription is not
        val entity = EntityWithMultipleConditions(
            actionType = "CONTROL_SECTOR",
            sectorType = "FISHING",
            establishmentType = "GMS",
            locationDescription = null,  // Should NOT be required for GMS
            establishment = "Some Establishment"
        )
        assertThat(EntityCompletenessValidator.isCompleteForStats(entity)).isTrue()
    }
}
