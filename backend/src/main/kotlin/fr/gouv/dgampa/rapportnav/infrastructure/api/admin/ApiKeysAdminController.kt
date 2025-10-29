package fr.gouv.dgampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.CreateApiKey
import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.DisableApiKey
import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.GetAllApiKeys
import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.RotateApiKey
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2/admin/apikey")
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
        val publicId: String?, // Only show prefix

    )

    data class CreateKeyResponse(
        val apiKey: ApiKeyResponse,
        val rawKey: String, // Full key - shown only once!
        val warning: String = "Save this key securely. It will not be shown again."
    )

    @GetMapping
    fun getAllApiKeysEndpoint(): ResponseEntity<List<ApiKey>> {
        val keys = getAllApiKeys.execute()
        return ResponseEntity.ok(keys.map { ApiKey.fromApiKeyEntity(it) })
    }

    @PostMapping
    fun createApiKeyEndpoint(@RequestBody request: CreateKeyRequest): ResponseEntity<CreateKeyResponse> {
        val (apiKey, rawKey) = createApiKey.execute(owner = request.owner, id = null)

        return ResponseEntity.ok(
            CreateKeyResponse(
                apiKey = ApiKeyResponse(id = apiKey?.id.toString(), publicId = apiKey?.publicId),
                rawKey = rawKey
            )
        )
    }

    @PostMapping("/{publicId}/rotate")
    fun rotateApiKeyEndpoint(@PathVariable publicId: String): ResponseEntity<CreateKeyResponse> {
        val (newKey, rawKey) = rotateApiKey.execute(publicId = publicId)

        return ResponseEntity.ok(
            CreateKeyResponse(
                apiKey = ApiKeyResponse(id = newKey?.id.toString(), publicId = newKey?.publicId),
                rawKey = rawKey
            )
        )
    }

    @PatchMapping("/{publicId}/disable")
    fun disableApiKeyEndpoint(@PathVariable publicId: String): ResponseEntity<Map<String, Any>> {
        val success = disableApiKey.execute(publicId = publicId)
        return if (success) {
            ResponseEntity.ok(mapOf("success" to true, "message" to "API key disabled"))
        } else {
            ResponseEntity.notFound().build()
        }
    }

}
