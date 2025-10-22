package fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.analytics.v1

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.analytics.ComputeAEMData
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.analytics.ComputePatrolData
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.analytics.v1.adapters.output.ApiAnalyticsAEMDataOutput
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.analytics.v1.adapters.output.ApiAnalyticsPatrolDataOutput
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import kotlinx.serialization.Serializable
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Serializable
data class MissionIdsRequest(
    @field:NotEmpty(message = "missionIds cannot be empty")
    val missionIds: List<String>
)

@Serializable
data class ErrorResponse(
    val status: Int,
    val message: String,
    val details: String? = null
)

@Serializable
data class AnalyticsResponse<T>(
    val count: Int,
    val failedIds: List<String> = emptyList(),
    val results: List<T>
)

private val log = LoggerFactory.getLogger("ApiAnalyticsController")

@RestController
@RequestMapping("/api/analytics/v1")
@Tag(name = "Analytics", description = "Compute and retrieve analytics data for missions")
class ApiAnalyticsController(
    private val computeAEMData: ComputeAEMData,
    private val computePatrolData: ComputePatrolData
) {

    @PostMapping("/aem")
    @Operation(
        summary = "Compute AEM analytics for multiple missions",
        description = "Computes AEM metrics for the provided mission IDs and returns detailed analytics results.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "AEM analytics successfully computed",
                content = [Content(schema = Schema(implementation = AnalyticsResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid mission IDs or input",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Unexpected internal error",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun getAEMData(
        @Valid @RequestBody request: MissionIdsRequest
    ): ResponseEntity<Any> {
        // Partition into parseable ints and invalid strings
        val (maybeInts, invalidIds) = request.missionIds.partition { it.toIntOrNull() != null }
        if (maybeInts.isEmpty()) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse(400, "No valid mission IDs provided", "All missionIds are non-integer"))
        }

        val results = mutableListOf<ApiAnalyticsAEMDataOutput>()
        val computationFailedIds = mutableListOf<String>()

        maybeInts.forEach { id ->
            val missionId = id.toInt()
            runCatching {
                computeAEMData.execute(missionId)
            }.fold(
                onSuccess = { output ->
                    if (output != null) {
                        results.add(output)
                    } else {
                        log.warn("AEM computation returned null for missionId=$missionId")
                        computationFailedIds.add(id)
                    }
                },
                onFailure = { ex ->
                    log.warn("AEM computation failed for missionId=$missionId: ${ex.message}", ex)
                    computationFailedIds.add(id)
                }
            )
        }

        val failedIds = (invalidIds + computationFailedIds)
        val response = AnalyticsResponse(
            count = results.size,
            failedIds = failedIds,
            results = results
        )

        return ResponseEntity.ok(response)
    }

    @PostMapping("/patrol")
    @Operation(
        summary = "Compute Patrol analytics for multiple missions",
        description = "Computes Patrol-related analytics for the provided mission IDs.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Patrol analytics successfully computed",
                content = [Content(schema = Schema(implementation = AnalyticsResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid mission IDs or input",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Unexpected internal error",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun getPatrolData(
        @Valid @RequestBody request: MissionIdsRequest
    ): ResponseEntity<Any> {
        val (maybeInts, invalidIds) = request.missionIds.partition { it.toIntOrNull() != null }
        if (maybeInts.isEmpty()) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse(400, "No valid mission IDs provided", "All missionIds are non-integer"))
        }

        val results = mutableListOf<ApiAnalyticsPatrolDataOutput>()
        val computationFailedIds = mutableListOf<String>()

        maybeInts.forEach { id ->
            val missionId = id.toInt()
            runCatching {
                computePatrolData.execute(missionId)
            }.fold(
                onSuccess = { output ->
                    if (output != null) {
                        results.add(output)
                    } else {
                        log.warn("Patrol computation returned null for missionId=$missionId")
                        computationFailedIds.add(id)
                    }
                },
                onFailure = { ex ->
                    log.warn("Patrol computation failed for missionId=$missionId: ${ex.message}", ex)
                    computationFailedIds.add(id)
                }
            )
        }

        val failedIds = (invalidIds + computationFailedIds)
        val response = AnalyticsResponse(
            count = results.size,
            failedIds = failedIds,
            results = results
        )

        return ResponseEntity.ok(response)
    }
}
