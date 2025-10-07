package fr.gouv.gmampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.config.PasswordValidator
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PasswordValidatorTest {
    @Test
    fun `returns true for a valid strong password`() {
        val password = "StrongPass1!!!!!!"
        assertTrue(PasswordValidator.isStrong(password))
    }

    @Test
    fun `returns false when password is shorter than 10 characters`() {
        val password = "Abc1@def"
        assertFalse(PasswordValidator.isStrong(password))
    }

    @Test
    fun `returns false when password has no uppercase letter`() {
        val password = "weakpass1!"
        assertFalse(PasswordValidator.isStrong(password))
    }

    @Test
    fun `returns false when password has no lowercase letter`() {
        val password = "WEAKPASS1!"
        assertFalse(PasswordValidator.isStrong(password))
    }

    @Test
    fun `returns false when password has no number`() {
        val password = "WeakPassword!"
        assertFalse(PasswordValidator.isStrong(password))
    }

    @Test
    fun `returns false when password has no special character`() {
        val password = "WeakPassword1"
        assertFalse(PasswordValidator.isStrong(password))
    }

    @Test
    fun `returns false when password is empty`() {
        val password = ""
        assertFalse(PasswordValidator.isStrong(password))
    }

    @Test
    fun `returns false for whitespace-only password`() {
        val password = "           "
        assertFalse(PasswordValidator.isStrong(password))
    }

}
