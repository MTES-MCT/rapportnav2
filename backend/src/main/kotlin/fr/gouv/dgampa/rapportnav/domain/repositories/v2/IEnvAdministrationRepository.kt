package fr.gouv.dgampa.rapportnav.domain.repositories.v2

import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.outputs.FullAdministrationDataOutput

interface IEnvAdministrationRepository {

    fun findById(administrationId: Int): FullAdministrationDataOutput?

    fun findAll(): List<FullAdministrationDataOutput>
}
