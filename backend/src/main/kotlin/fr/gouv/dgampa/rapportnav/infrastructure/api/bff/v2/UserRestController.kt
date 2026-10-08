package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.FindById
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserFromToken
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.UserInfos
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2/users")
class UserRestController(
    private val findById: FindById,
    private val getServiceById: GetServiceById,
    private val getUserFromToken: GetUserFromToken
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

        // Cloisonnement: a user may only read their own profile or a colleague from the same service.
        // Admins may read any user.
        val currentUser = getUserFromToken.execute()
            ?: throw BackendUsageException(
                code = BackendUsageErrorCode.USER_NOT_ALLOWED_TO_PERFORM_EXCEPTION,
                message = "UserRestController.getUserById: no authenticated user"
            )
        val isAdmin = currentUser.roles.contains(RoleTypeEnum.ADMIN)
        if (!isAdmin && user.serviceId != currentUser.serviceId) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.USER_NOT_ALLOWED_TO_PERFORM_EXCEPTION,
                message = "UserRestController.getUserById: not allowed to read user id=$userId"
            )
        }

        val service = getServiceById.execute(user.serviceId)
        return UserInfos(
            id = user.id!!,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            serviceId = service?.id,
            serviceName = service?.name,
            controlUnitId = service?.controlUnits?.firstOrNull()
        )
    }
}
