package fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.Crew


interface ICrewRepository {
    fun findAllCrew( ): List<Crew>
}
