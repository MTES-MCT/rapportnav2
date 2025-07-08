package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryStatusType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.IInquiryRepository
import java.util.*
import kotlin.jvm.optionals.getOrNull

@UseCase
class DeleteInquiry(
    private val inquiryRepo: IInquiryRepository
) {
    fun execute(id: UUID?, actionType: ActionType?) {
        if (id == null || actionType !== ActionType.INQUIRY) return
        val crossControl = inquiryRepo.findById(id).getOrNull()
        if (crossControl?.status != InquiryStatusType.NEW.toString()) return
        return inquiryRepo.deleteById(crossControl.id)
    }
}
