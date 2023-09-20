package fr.gouv.dgampa.rapportnav.domain.repositories.mission.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlVesselAdministrative


interface IControlVesselAdministrativeRepository {
    fun findAllByMissionId( missionId: Int): List<ControlVesselAdministrative>
}
