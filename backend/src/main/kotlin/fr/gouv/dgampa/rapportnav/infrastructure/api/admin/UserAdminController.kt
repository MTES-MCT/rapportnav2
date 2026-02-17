package fr.gouv.dgampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.*
import fr.gouv.dgampa.rapportnav.infrastructure.api.auth.adapters.inputs.AuthRegisterDataInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.auth.adapters.inputs.UpdateUserInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.auth.adapters.inputs.UpdateUserPasswordInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.auth.adapters.outputs.AdminUser
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2/admin/users")
class UserAdminController(
    private val updateUser: UpdateUser,
    private val disableUser: DisableUser,
    private val enableUser: EnableUser,
    private val getUserList: GetUserList,
    private val createUser: CreateUser,
    private val updatePassword: UpdateUserPassword

) {
    private val logger = LoggerFactory.getLogger(UserAdminController::class.java)

    @GetMapping("")
    @Operation(summary = "Get the list of users")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Get Users", content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = User::class)))
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Did not find any services", content = [Content()])
        ]
    )
    fun getServices(): List<AdminUser> {
        return try {
            getUserList.execute().map { AdminUser.fromUserEntity(it) }
        } catch (e: Exception) {
            logger.error("failed to load users list", e)
            throw Exception(e)
        }
    }

    @PostMapping("")
    @Operation(summary = "Create User")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Create USer", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = User::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Could not create no User", content = [Content()])
        ]
    )
    fun create(@RequestBody body: AuthRegisterDataInput): AdminUser? {
        return try {
            createUser.execute(body)?.let { AdminUser.fromUserEntity(it) }
        } catch (e: Exception) {
            logger.error("Error while creating User : ", e)
            return null
        }
    }

    @PutMapping("")
    @Operation(summary = "Update User")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Create User", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = User::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Could not create no User", content = [Content()])
        ]
    )
    fun update(
        @RequestBody body: UpdateUserInput
    ): AdminUser? {
        return try {
            updateUser.execute(body)?.let { AdminUser.fromUserEntity(it) }
        } catch (e: Exception) {
            logger.error("Error while updating User : ", e)
            return null
        }
    }


    @PostMapping("password/{userId}")
    @Operation(summary = "Update User")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Create User", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = User::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Could not create no User", content = [Content()])
        ]
    )
    fun updatePassword(
        @PathVariable(name = "userId") UserId: Int,
        @RequestBody body: UpdateUserPasswordInput
    ): AdminUser? {
        return try {
            updatePassword.execute(id = UserId, input = body)?.let { AdminUser.fromUserEntity(it) }
        } catch (e: Exception) {
            logger.error("Error while updating User : ", e)
            return null
        }
    }

    @PostMapping("{userId}/disable")
    @Operation(summary = "Disable a User")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "User disabled", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = User::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Could not disable User", content = [Content()])
        ]
    )
    fun disable(
        @PathVariable userId: Int
    ): AdminUser? {
        return try {
            disableUser.execute(id = userId)?.let { AdminUser.fromUserEntity(it) }
        } catch (e: Exception) {
            logger.error("Error while disabling User : ", e)
            null
        }
    }

    @PostMapping("{userId}/enable")
    @Operation(summary = "Enable a User")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "User enabled", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = User::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Could not enable User", content = [Content()])
        ]
    )
    fun enable(
        @PathVariable userId: Int
    ): AdminUser? {
        return try {
            enableUser.execute(id = userId)?.let { AdminUser.fromUserEntity(it) }
        } catch (e: Exception) {
            logger.error("Error while enabling User : ", e)
            null
        }
    }
}
