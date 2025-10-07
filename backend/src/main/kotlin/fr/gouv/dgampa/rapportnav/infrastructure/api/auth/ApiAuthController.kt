package fr.gouv.dgampa.rapportnav.infrastructure.api.auth

import fr.gouv.dgampa.rapportnav.config.PasswordValidator
import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode.*
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.HashService
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.FindByEmail
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.Save
import fr.gouv.dgampa.rapportnav.infrastructure.api.auth.adapters.inputs.AuthLoginDataInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.auth.adapters.inputs.AuthRegisterDataInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.auth.adapters.outputs.AuthLoginDataOutput
import fr.gouv.dgampa.rapportnav.infrastructure.exceptions.BackendRequestErrorCode
import fr.gouv.dgampa.rapportnav.infrastructure.exceptions.BackendRequestException
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class ApiAuthController(
    private val save: Save,
    private val findByEmail: FindByEmail,
    private val hashService: HashService,
    private val tokenService: TokenService,
) {
    @PostMapping("register")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    fun register(@RequestBody body: AuthRegisterDataInput): ResponseEntity<Any> {
        val requiredFields = listOf(body.email, body.password, body.firstName, body.lastName)
        if (requiredFields.any { it.isEmpty() }) throw BackendRequestException(
            code = BackendRequestErrorCode.BODY_MISSING_DATA,
            message = "SignUp body does not contain all the required data"
        )

        if (!PasswordValidator.isStrong(body.password)) {
            throw BackendUsageException(
                code = PASSWORD_TOO_WEAK_EXCEPTION,
                message = "Password must be at least 10 characters long and include upper, lower, number, and special character"
            )
        }

        val user = User(
            id = body.id,
            firstName = body.firstName.lowercase().trim(),
            lastName = body.lastName.lowercase().trim(),
            email = body.email.trim(),
            password = hashService.hashBcrypt(body.password),
            serviceId = body.serviceId,
            roles = body.roles ?: listOf(RoleTypeEnum.USER_PAM)
        )

        if (findByEmail.execute(body.email) != null) {
            throw BackendUsageException(
                code = ALREADY_EXISTS_EXCEPTION,
                message = "SignUp failed "
            )
        }

        return ResponseEntity.ok(save.execute(user))
    }

    @PostMapping("login")
    fun login(@RequestBody body: AuthLoginDataInput, response: HttpServletResponse): AuthLoginDataOutput {
        if (body.email.isEmpty() || body.password.isEmpty()) throw BackendRequestException(
            code = BackendRequestErrorCode.BODY_MISSING_DATA,
            message = "Login body does not contain all the required data"
        )

        val user =
            findByEmail.execute(body.email.trim()) ?: throw BackendUsageException(
                code = INCORRECT_USER_IDENTIFIER_EXCEPTION,
                message = "Login failed "
            )

        if (!hashService.checkBcrypt(body.password, user.password)) {
            throw BackendUsageException(
                code = INCORRECT_USER_IDENTIFIER_EXCEPTION,
                message = "Login failed "
            )
        }

        return AuthLoginDataOutput(
            token = tokenService.createToken(user),
        )
    }
}
