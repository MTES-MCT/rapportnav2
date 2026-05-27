package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import jakarta.annotation.PostConstruct
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.beans.factory.annotation.Value
import java.util.Date

@RestController
@RequestMapping("/api/v2/metabase")
class MetabaseEmbedController(
    private val getServiceById: GetServiceById,
    @param:Value("\${metabase.secret-key}") private val metabaseSecretKey: String,
    @param:Value("\${metabase.site-url}") private val metabaseSiteUrl: String
) {

    @PostConstruct
    fun validateSecretKey() {
        if (metabaseSecretKey.isNotBlank()) {
            require(metabaseSecretKey.toByteArray(Charsets.UTF_8).size >= 32) {
                "METABASE_SECRET_KEY must be at least 256 bits (32 bytes) for HS256 signing"
            }
        }
    }

    companion object {
        private const val ULAM_ACTIVITY_DASHBOARD_ID = 491
        private const val PAM_ACTIVITY_DASHBOARD_ID = 436
        private const val TOKEN_EXPIRATION_MINUTES = 10

        private val serviceIdToShortName: Map<Int, String> = mapOf(
            // PAM
            1 to "PAM Jeanne Barret",
            2 to "PAM Jeanne Barret",
            3 to "PAM Themis",
            4 to "PAM Themis",
            5 to "PAM Iris",
            6 to "PAM Iris",
            7 to "PAM Gyptis",
            8 to "PAM Gyptis",

            // ULAM
            9 to "33",
            10 to "971",
            11 to "35",
            12 to "974",
            13 to "44",
            14 to "17",
            15 to "971",
            16 to "976",
            17 to "34/30",
            18 to "62/80",
            19 to "50",
            20 to "06",
            21 to "13",
            22 to "14",
            23 to "22",
            24 to "29 Br",
            25 to "29 Dz",
            26 to "2A",
            27 to "2B",
            28 to "64/40",
            29 to "66/11",
            30 to "76",
            31 to "83",
            32 to "85",
            33 to "972",
            34 to "973",
            35 to "975",
            36 to "56",
        )
    }

    @GetMapping("/embed-url")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    fun getEmbedUrl(): ResponseEntity<Map<String, String>> {
        val user = SecurityContextHolder.getContext().authentication?.principal as? User
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        val service = getServiceById.execute(user.serviceId)
        val dashboardId = when (service?.serviceType) {
            ServiceTypeEnum.PAM -> PAM_ACTIVITY_DASHBOARD_ID
            ServiceTypeEnum.ULAM -> ULAM_ACTIVITY_DASHBOARD_ID
            else -> ULAM_ACTIVITY_DASHBOARD_ID
        }
        val unitName = user.serviceId?.let { serviceIdToShortName[it] }

        val now = Date()
        val exp = Date(now.time + TOKEN_EXPIRATION_MINUTES * 60 * 1000)

        val params = mutableMapOf<String, Any?>()
        params["unit%C3%A9"] = unitName
        params["filtre_de_date"] = null

        val claimsSet = JWTClaimsSet.Builder()
            .claim("resource", mapOf("dashboard" to dashboardId))
            .claim("params", params)
            .expirationTime(exp)
            .issueTime(now)
            .build()

        val signedJWT = SignedJWT(JWSHeader(JWSAlgorithm.HS256), claimsSet)
        signedJWT.sign(MACSigner(metabaseSecretKey.toByteArray(Charsets.UTF_8)))

        val token = signedJWT.serialize()
        val iframeUrl = "$metabaseSiteUrl/embed/dashboard/$token#bordered=true&titled=true&locale=fr"

        return ResponseEntity.ok(mapOf("iframeUrl" to iframeUrl))
    }
}
