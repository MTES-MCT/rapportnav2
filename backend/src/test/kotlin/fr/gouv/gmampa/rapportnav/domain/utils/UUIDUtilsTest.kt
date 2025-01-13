package fr.gouv.gmampa.rapportnav.domain.utils

import fr.gouv.dgampa.rapportnav.domain.utils.isValidUUID
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class UUIDUtilsTest {

    @Test
    fun `test valid UUID returns true`() {
        val validUUID = "123e4567-e89b-12d3-a456-426614174000"
        assertTrue(isValidUUID(validUUID))
    }

    @Test
    fun `test invalid UUID returns false`() {
        val invalidUUID = "invalid-uuid"
        assertFalse(isValidUUID(invalidUUID))
    }

    @Test
    fun `test null value returns false`() {
        assertFalse(isValidUUID(null))
    }

    @Test
    fun `test empty string returns false`() {
        val emptyString = ""
        assertFalse(isValidUUID(emptyString))
    }

    @Test
    fun `test UUID with incorrect format returns false`() {
        val incorrectUUID = "123e4567-e89b-12d3-a456-426614132323232323232400"
        assertFalse(isValidUUID(incorrectUUID))
    }
}
