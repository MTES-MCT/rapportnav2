package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.FindById
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.UserInfos
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2/users")
class UserRestController(
    private val findById: FindById,
    private val getServiceById: GetServiceById
) {

    /**
     * Retrieves a specific user by its ID.
     *
     * @param userId The unique identifier of the user to retrieve.
     * @return The user data as a `UserInfos` object.
     * @throws BackendUsageException if user not found.
     */
    @GetMapping("{userId}")
    @Operation(summary = "get user information")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "User information retrieved successfully", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = UserInfos::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "400", description = "User not found", content = [Content()])
        ]
    )
    fun getUserById(
        @PathVariable(name = "userId") userId: Int
    ): UserInfos {
        val user = findById.execute(userId)
            ?: throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "UserRestController.getUserById: user not found for id=$userId"
            )
        return toUserInfos(user)
    }

    /**
     * Returns the currently authenticated user, resolved from the SecurityContext.
     *
     * Works for both authentication methods: the JWT/password path (principal set by
     * CustomAuthenticationFilter) and the ProConnect session path (principal set by
     * ProConnectUserBridgeFilter). Used by the frontend to answer "who am I" without a
     * client-side JWT — required for ProConnect session users.
     */
    @GetMapping("me")
    @Operation(summary = "get the currently authenticated user")
    fun getCurrentUser(): UserInfos {
        val user = SecurityContextHolder.getContext().authentication?.principal as? User
            ?: throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "UserRestController.getCurrentUser: no authenticated user in context"
            )
        return toUserInfos(user)
    }

    private fun toUserInfos(user: User): UserInfos {
        val service = getServiceById.execute(user.serviceId)
        return UserInfos(
            id = user.id!!,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            serviceId = service?.id,
            serviceName = service?.name,
            controlUnitId = service?.controlUnits?.firstOrNull(),
            roles = user.roles
        )
    }
}
