package fr.gouv.dgampa.rapportnav.domain.repositories.mission.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrative


interface IControlAdministrativeRepository {
    fun findAllByMissionId( missionId: Int): List<ControlAdministrative>
}
