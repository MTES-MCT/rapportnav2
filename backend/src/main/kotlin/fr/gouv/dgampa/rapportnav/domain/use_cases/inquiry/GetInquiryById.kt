package fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.IInquiryRepository
import java.util.*
import kotlin.jvm.optionals.getOrNull

@UseCase
class GetInquiryById(
    private val inquiryRepo: IInquiryRepository
) {
    fun execute(id: UUID?): InquiryEntity? {
        if(id == null) return null
        return inquiryRepo.findById(id = id).getOrNull()?.let { InquiryEntity.fromInquiryModel(it) }
    }
}
