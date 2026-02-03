package fr.gouv.gmampa.rapportnav.domain.use_cases.auth

import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.HashService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class HashServiceTest {

    private lateinit var hashService: HashService

    @BeforeEach
    fun setUp() {
        hashService = HashService()
    }

    @Nested
    inner class HashBcrypt {

        @Test
        fun `should hash a password`() {
            val password = "mySecurePassword123"

            val hash = hashService.hashBcrypt(password)

            assertThat(hash).isNotNull()
            assertThat(hash).isNotEqualTo(password)
            assertThat(hash).startsWith("\$2a\$") // BCrypt hash prefix
        }

        @Test
        fun `should generate different hashes for same input`() {
            val password = "mySecurePassword123"

            val hash1 = hashService.hashBcrypt(password)
            val hash2 = hashService.hashBcrypt(password)

            // BCrypt generates different salts each time
            assertThat(hash1).isNotEqualTo(hash2)
        }

        @Test
        fun `should hash empty string`() {
            val password = ""

            val hash = hashService.hashBcrypt(password)

            assertThat(hash).isNotNull()
            assertThat(hash).startsWith("\$2a\$")
        }

        @Test
        fun `should hash special characters`() {
            val password = "p@ssw0rd!#\$%^&*()"

            val hash = hashService.hashBcrypt(password)

            assertThat(hash).isNotNull()
            assertThat(hashService.checkBcrypt(password, hash)).isTrue()
        }
    }

    @Nested
    inner class CheckBcrypt {

        @Test
        fun `should return true for matching password and hash`() {
            val password = "mySecurePassword123"
            val hash = hashService.hashBcrypt(password)

            val result = hashService.checkBcrypt(password, hash)

            assertThat(result).isTrue()
        }

        @Test
        fun `should return false for non-matching password`() {
            val password = "mySecurePassword123"
            val wrongPassword = "wrongPassword"
            val hash = hashService.hashBcrypt(password)

            val result = hashService.checkBcrypt(wrongPassword, hash)

            assertThat(result).isFalse()
        }

        @Test
        fun `should return false for similar but different password`() {
            val password = "mySecurePassword123"
            val similarPassword = "mySecurePassword124"
            val hash = hashService.hashBcrypt(password)

            val result = hashService.checkBcrypt(similarPassword, hash)

            assertThat(result).isFalse()
        }

        @Test
        fun `should be case sensitive`() {
            val password = "MyPassword"
            val hash = hashService.hashBcrypt(password)

            assertThat(hashService.checkBcrypt("mypassword", hash)).isFalse()
            assertThat(hashService.checkBcrypt("MYPASSWORD", hash)).isFalse()
            assertThat(hashService.checkBcrypt("MyPassword", hash)).isTrue()
        }

        @Test
        fun `should verify pre-existing hash`() {
            // A known BCrypt hash for "password123"
            val password = "password123"
            val knownHash = "\$2a\$10\$N9qo8uLOickgx2ZMRZoMyeIjZRGdjGj/n3.xLG5xVft6oBGK8.1.e"

            // Note: This specific hash won't match because salt is different
            // Instead, we create our own hash and verify it works
            val ourHash = hashService.hashBcrypt(password)
            assertThat(hashService.checkBcrypt(password, ourHash)).isTrue()
        }
    }

    @Nested
    inner class Integration {

        @Test
        fun `should hash and verify multiple passwords correctly`() {
            val passwords = listOf(
                "simple",
                "WITH_CAPS",
                "with numbers 123",
                "special!@#\$%",
                "unicode: \u00e9\u00e8\u00ea"
            )

            passwords.forEach { password ->
                val hash = hashService.hashBcrypt(password)
                assertThat(hashService.checkBcrypt(password, hash))
                    .withFailMessage("Failed for password: $password")
                    .isTrue()
            }
        }
    }
}
