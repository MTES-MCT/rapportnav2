package fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions

enum class ControlOrigin(val value: String) {
    POSEIDON_ENV("POSEIDON_ENV"),
    POSEIDON_FISH("POSEIDON_FISH"),
    MONITORENV("MONITORENV"),
    MONITORFISH("MONITORFISH"),
}
