package fr.gouv.gmampa.rapportnav.infrastructure.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.FormalNoticeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetExternalDataEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.TargetExternalData
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class TargetExternalDataTest {
    @Test
    fun `execute should convert into entity`() {
        val input = TargetExternalData(
            id = "myExternalData",
            natinfs = listOf("natInf1", "natInf2"),
            toProcess = false,
            vesselType = VesselTypeEnum.SAILING,
            vesselSize = VesselSizeEnum.FROM_12_TO_24m,
            companyName = "myExternalData",
            formalNotice = FormalNoticeEnum.YES,
            observations = "myExternalData",
            relevantCourt = "myExternalData",
            infractionType = InfractionTypeEnum.WITH_REPORT,
            registrationNumber = "myExternalData",
            controlledPersonIdentity = "myExternalData"
        )

        val entity = input.toTargetExternalDataEntity()
        assertThat(entity).isNotNull()
        assertThat(entity.id).isEqualTo(input.id)
        assertThat(entity.natinfs).isEqualTo(input.natinfs)
        assertThat(entity.vesselSize).isEqualTo(input.vesselSize)
        assertThat(entity.vesselType).isEqualTo(input.vesselType)
        assertThat(entity.toProcess).isEqualTo(input.toProcess)
        assertThat(entity.companyName).isEqualTo(input.companyName)
        assertThat(entity.formalNotice).isEqualTo(input.formalNotice)
        assertThat(entity.observations).isEqualTo(input.observations)
        assertThat(entity.relevantCourt).isEqualTo(input.relevantCourt)
        assertThat(entity.infractionType).isEqualTo(input.infractionType)
        assertThat(entity.registrationNumber).isEqualTo(input.registrationNumber)
        assertThat(entity.controlledPersonIdentity).isEqualTo(input.controlledPersonIdentity)
    }


    @Test
    fun `execute should convert from entity`() {
        val entity = TargetExternalDataEntity(
            id = "myExternalData",
            natinfs = listOf("natInf1", "natInf2"),
            toProcess = false,
            vesselType = VesselTypeEnum.SAILING,
            vesselSize = VesselSizeEnum.FROM_12_TO_24m,
            companyName = "myExternalData",
            formalNotice = FormalNoticeEnum.YES,
            observations = "myExternalData",
            relevantCourt = "myExternalData",
            infractionType = InfractionTypeEnum.WITH_REPORT,
            registrationNumber = "myExternalData",
            controlledPersonIdentity = "myExternalData"
        )

        val value = TargetExternalData.fromTargetExternalDataEntity(entity)
        assertThat(value).isNotNull()
        assertThat(value.id).isEqualTo(entity.id)
        assertThat(value.natinfs).isEqualTo(entity.natinfs)
        assertThat(value.vesselSize).isEqualTo(entity.vesselSize)
        assertThat(value.vesselType).isEqualTo(entity.vesselType)
        assertThat(value.toProcess).isEqualTo(entity.toProcess)
        assertThat(value.companyName).isEqualTo(entity.companyName)
        assertThat(value.formalNotice).isEqualTo(entity.formalNotice)
        assertThat(value.observations).isEqualTo(entity.observations)
        assertThat(value.relevantCourt).isEqualTo(entity.relevantCourt)
        assertThat(value.infractionType).isEqualTo(entity.infractionType)
        assertThat(value.registrationNumber).isEqualTo(entity.registrationNumber)
        assertThat(value.controlledPersonIdentity).isEqualTo(entity.controlledPersonIdentity)
    }
}
