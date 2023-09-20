package fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions

class GearControl {
    var gearCode: String? = null
    var gearName: String? = null
    var declaredMesh: Double? = null
    var controlledMesh: Double? = null
    var hasUncontrolledMesh: Boolean = false
    var gearWasControlled: Boolean? = null
    var comments: String? = null
}
