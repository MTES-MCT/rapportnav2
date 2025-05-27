package fr.gouv.dgampa.rapportnav.domain.entities.user

enum class RoleTypeEnum {
    ADMIN,
    USER_PAM,
    USER_ULAM,
}

enum class AuthoritiesEnum {
    ROLE_ADMIN,
    ROLE_USER_PAM,
    ROLE_USER_ULAM,
}

fun RoleTypeEnum.toAuthority(): AuthoritiesEnum = when (this) {
    RoleTypeEnum.ADMIN -> AuthoritiesEnum.ROLE_ADMIN
    RoleTypeEnum.USER_PAM -> AuthoritiesEnum.ROLE_USER_PAM
    RoleTypeEnum.USER_ULAM -> AuthoritiesEnum.ROLE_USER_ULAM
}
