package fr.gouv.dgampa.rapportnav.domain.repositories.rnip

import fr.gouv.dgampa.rapportnav.infrastructure.rnip.inputs.RnipInfractionInput
import fr.gouv.dgampa.rapportnav.infrastructure.rnip.outputs.RnipInfractionResultOutput

interface IRnipInfractionRepository {

    fun send(infractions: List<RnipInfractionInput>): List<RnipInfractionResultOutput>
}
