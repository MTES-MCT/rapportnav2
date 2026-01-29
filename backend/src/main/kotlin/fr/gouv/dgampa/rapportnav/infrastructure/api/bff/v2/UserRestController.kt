package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.FindById
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
    private val getSeviceById: GetServiceById
) {

    /**
     * Retrieves a specific user date by its ID.
     *
     * This endpoint fetches a user identified by the provided `userId` path variable. If the user exists, it
     * returns the user data transformed into the API response format. If the user does not exist or an error occurs,
     * it returns null.
     *
     * @param userId The unique identifier of the user to retrieve.
     * @return The user data as a `UserInfos` object, or null if not found or an error occurs.
     */
    @GetMapping("{userId}")
    @Operation(summary = "get user information ")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "get user informations", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = UserInfos::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Could not get no user information's", content = [Content()])
        ]
    )
    fun getUserById(
        @PathVariable(name = "userId") userId: Int
    ): UserInfos? {
        val user = findById.execute(userId) ?: return null
        val service = getSeviceById.execute(user.serviceId)
        return UserInfos(
            id = user.id!!,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            serviceId = service?.id,
            serviceName = service?.name,
            controlUnitId = service?.controlUnits?.get(0)
        )
    }


}
