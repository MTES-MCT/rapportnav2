package fr.gouv.dgampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.domain.entities.apikey.ApiKeyEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.CreateApiKey
import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.DisableApiKey
import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.GetAllApiKeys
import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.RotateApiKey
import io.swagger.v3.oas.annotations.Operation
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/api/v1/admin/apikey")
class ApiKeysAdminController(
    private val getAllApiKeys: GetAllApiKeys,
    private val createApiKey: CreateApiKey,
    private val rotateApiKey: RotateApiKey,
    private val disableApiKey: DisableApiKey,
) {

    data class CreateKeyRequest(
        val owner: String
    )

    data class ApiKeyResponse(
        val id: String?,
        val keyValue: String, // Only show prefix

    )

    data class CreateKeyResponse(
        val apiKey: ApiKeyResponse,
        val rawKey: String, // Full key - shown only once!
        val warning: String = "Save this key securely. It will not be shown again."
    )

    @GetMapping
    fun getAllApiKeys(): ResponseEntity<List<ApiKeyEntity>> {
        val keys = getAllApiKeys.execute()
        return ResponseEntity.ok(keys)
    }

    @PostMapping
    fun createApiKeyEndpoint(@RequestBody request: CreateKeyRequest): ResponseEntity<CreateKeyResponse> {
        val (apiKey, rawKey) = createApiKey.execute(owner = "")
//
        return ResponseEntity.ok(
            CreateKeyResponse(
                apiKey = ApiKeyResponse(id=apiKey.publicId,keyValue=apiKey.hashedKey),
                rawKey = rawKey
            )
        )
    }

    @PostMapping("/{keyId}/rotate")
        fun rotateApiKey(@PathVariable keyId: Long): ResponseEntity<CreateKeyResponse> {
//        val (newKey, rawKey) = rotateApiKey.execute(keyId)

        return ResponseEntity.ok(
            CreateKeyResponse(
                apiKey = ApiKeyResponse(id=null,keyValue=""),
                rawKey = "fdfd"
            )
        )
    }

    @PatchMapping("/{keyValue}/disable")
    fun disableApiKey(@PathVariable keyValue: String): ResponseEntity<Map<String, Any>> {
        val success = disableApiKey.execute(keyValue)
        return if (success) {
            ResponseEntity.ok(mapOf("success" to true, "message" to "API key disabled"))
        } else {
            ResponseEntity.notFound().build()
        }
    }

}
