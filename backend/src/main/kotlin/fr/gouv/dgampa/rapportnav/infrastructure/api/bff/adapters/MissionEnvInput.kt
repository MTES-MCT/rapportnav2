package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters

data class MissionEnvInput (
    var missionId: Int,
    var observationsByUnit: String?
){
}

