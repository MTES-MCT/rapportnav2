package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetServiceForUser
import org.springframework.beans.factory.annotation.Value

@UseCase
class EnableSati(
    private val getServiceForUser: GetServiceForUser,
    @param:Value("\${sati.enabled-services}") private val serviceIds: List<Int>
) {
    fun execute(): Boolean {
        val serviceId = getServiceForUser.execute()
        return (serviceIds.contains(serviceId?.id))
    }
}
