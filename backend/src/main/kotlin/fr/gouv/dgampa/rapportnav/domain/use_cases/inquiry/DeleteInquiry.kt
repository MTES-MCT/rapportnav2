package fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.IInquiryRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.DeleteNavAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetNavActionListByOwnerId
import java.util.*
import kotlin.jvm.optionals.getOrNull

@UseCase
class DeleteInquiry(
    private val inquiryRepo: IInquiryRepository,
    private val deleteNavAction: DeleteNavAction,
    private val getNavActionListByOwnerId: GetNavActionListByOwnerId
) {
    fun execute(id: UUID?) {
        if (id == null) return
        val inquiry = inquiryRepo.findById(id).getOrNull() ?: return
        deleteActions(ownerId = inquiry.id)
        return inquiryRepo.deleteById(inquiry.id)
    }

    private fun deleteActions(ownerId: UUID): List<Unit> {
        return getNavActionListByOwnerId.execute(ownerId = ownerId).map { deleteNavAction.execute(id = it.id) }
    }
}
