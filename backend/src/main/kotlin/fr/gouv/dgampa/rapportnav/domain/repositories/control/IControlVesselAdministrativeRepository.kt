package fr.gouv.dgampa.rapportnav.domain.repositories.control

import fr.gouv.dgampa.rapportnav.domain.entities.control.ControlVesselAdministrative


interface IControlVesselAdministrativeRepository {
    fun findAllByMissionId( missionId: Int): List<ControlVesselAdministrative>
}
