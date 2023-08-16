package fr.gouv.dgampa.rapportnav.infrastructure.api

import fr.gouv.dgampa.rapportnav.domain.entities.user.UserEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.HashService
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.FindByEmail
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.FindById
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.Save
import fr.gouv.dgampa.rapportnav.infrastructure.api.adapters.ApiException
import fr.gouv.dgampa.rapportnav.infrastructure.api.adapters.inputs.AuthLoginDataInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.adapters.inputs.AuthRegisterDataInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.adapters.outputs.AuthLoginDataOutput
import io.jsonwebtoken.JwsHeader
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v1/auth")
class ApiAuthController(
    private val save: Save,
    private val findById: FindById,
    private val findByEmail: FindByEmail,
    private val hashService: HashService,
    private val tokenService: TokenService,
) {
    @PostMapping("register")
    fun register(@RequestBody body: AuthRegisterDataInput): ResponseEntity<Any> {
        val user = UserEntity(
            id = body.id,
            name = body.name,
            email = body.email,
            password = hashService.hashBcrypt(body.password),
        )

        if (findByEmail.execute(body.email) != null) {
            return ResponseEntity.badRequest().body(ApiException(400, "Email already exists"))
        }

        return ResponseEntity.ok(save.execute(user))

    }

    @PostMapping("login")
    fun login(@RequestBody body: AuthLoginDataInput, response: HttpServletResponse): AuthLoginDataOutput {
        val user = findByEmail.execute(body.email) ?: throw ApiException(400, "Login failed")

        if (!hashService.checkBcrypt(body.password, user.password)) {
            throw ApiException(400, "Login failed")
        }

        return AuthLoginDataOutput(
            token = tokenService.createToken(user),
        )



    }
}
