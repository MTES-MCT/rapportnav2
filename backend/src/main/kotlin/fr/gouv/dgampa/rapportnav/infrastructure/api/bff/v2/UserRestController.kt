package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.FindById
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.UserInfos
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2/users")
class UserRestController(
    private val findById: FindById,
    private val getSeviceById: GetServiceById
) {

    private val logger = LoggerFactory.getLogger(UserRestController::class.java)

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
    fun getUserById(
        @PathVariable(name = "userId") userId: Int
    ): UserInfos? {
        try {
            val user = this.findById.execute(userId) ?: return null
            val service = this.getSeviceById.execute(userId)
            return UserInfos(
                id = user.id!!,
                email = user.email,
                firstName = user.firstName,
                lastName = user.lastName,
                serviceId = service?.id,
                serviceName = service?.name,
            )
        } catch (e: Exception) {
            logger.error("Error while getting user information : ", e)
            return null
        }
    }


}
