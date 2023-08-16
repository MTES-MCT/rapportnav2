package fr.gouv.dgampa.rapportnav.domain.repositories

import fr.gouv.dgampa.rapportnav.domain.entities.crew.CrewEntity


interface ICrewRepository {
    fun findAllCrew( ): List<CrewEntity>
}
