package fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.NatinfEntity

interface INatinfRepository {
    fun findAll(): List<NatinfEntity>
}
