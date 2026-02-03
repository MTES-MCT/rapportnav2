package fr.gouv.gmampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.config.JwtEncodingConfig
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.security.oauth2.jwt.JwsHeader
import java.time.Instant

class JwtEncodingConfigTest {

    private val testJwtKey = "this-is-a-test-secret-key-that-is-long-enough"

    @Test
    fun `should create JwtDecoder bean`() {
        val config = JwtEncodingConfig(testJwtKey)

        val decoder = config.jwtDecoder()

        assertNotNull(decoder)
    }

    @Test
    fun `should create JwtEncoder bean`() {
        val config = JwtEncodingConfig(testJwtKey)

        val encoder = config.jwtEncoder()

        assertNotNull(encoder)
    }

    @Test
    fun `should encode and decode JWT token successfully`() {
        val config = JwtEncodingConfig(testJwtKey)
        val encoder = config.jwtEncoder()
        val decoder = config.jwtDecoder()

        val claims = JwtClaimsSet.builder()
            .subject("test-user")
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .claim("userId", 123)
            .build()

        val jwsHeader = JwsHeader.with { "HS256" }.build()
        val jwt = encoder.encode(JwtEncoderParameters.from(jwsHeader, claims))

        assertNotNull(jwt.tokenValue)

        val decodedJwt = decoder.decode(jwt.tokenValue)
        assertEquals("test-user", decodedJwt.subject)
        assertEquals(123, decodedJwt.getClaim("userId"))
    }

    @Test
    fun `should fail to decode token with wrong key`() {
        val config1 = JwtEncodingConfig(testJwtKey)
        val config2 = JwtEncodingConfig("different-secret-key-that-is-also-long")

        val encoder = config1.jwtEncoder()
        val decoderWithWrongKey = config2.jwtDecoder()

        val claims = JwtClaimsSet.builder()
            .subject("test-user")
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .build()

        val jwsHeader = JwsHeader.with { "HS256" }.build()
        val jwt = encoder.encode(JwtEncoderParameters.from(jwsHeader, claims))

        assertThrows(Exception::class.java) {
            decoderWithWrongKey.decode(jwt.tokenValue)
        }
    }

    @Test
    fun `should preserve custom claims in token`() {
        val config = JwtEncodingConfig(testJwtKey)
        val encoder = config.jwtEncoder()
        val decoder = config.jwtDecoder()

        val claims = JwtClaimsSet.builder()
            .subject("user@example.com")
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .claim("userId", 42)
            .claim("roles", listOf("ADMIN", "USER"))
            .build()

        val jwsHeader = JwsHeader.with { "HS256" }.build()
        val jwt = encoder.encode(JwtEncoderParameters.from(jwsHeader, claims))
        val decodedJwt = decoder.decode(jwt.tokenValue)

        assertEquals("user@example.com", decodedJwt.subject)
        assertEquals(42, decodedJwt.getClaim("userId"))
        @Suppress("UNCHECKED_CAST")
        val roles = decodedJwt.getClaim<List<String>>("roles")
        assertTrue(roles.contains("ADMIN"))
        assertTrue(roles.contains("USER"))
    }
}