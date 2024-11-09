package fr.gouv.gmampa.rapportnav.infrastructure.bff.model

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionFishActionOutput
import fr.gouv.gmampa.rapportnav.mocks.mission.action.FishActionControlMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class MissionFishActionOutputTest {

    @Test
    fun `execute should retrieve output from mission action fish Entity`() {
        val fishAction = FishActionControlMock.create()
        val entity = MissionFishActionEntity.fromFishAction(action = fishAction)
        val output = MissionFishActionOutput.fromMissionActionEntity(entity)

        assertThat(output).isNotNull()
        assertThat(output.id).isEqualTo(entity.id.toString())
        assertThat(output.data.startDateTimeUtc).isEqualTo(entity.actionDatetimeUtc)
        assertThat(output.data.endDateTimeUtc).isEqualTo(entity.actionEndDatetimeUtc)
        assertThat(output.data.vesselId).isEqualTo(entity.vesselId)
        assertThat(output.data.completedBy).isEqualTo(entity.completedBy)
        assertThat(output.data.facade).isEqualTo(entity.facade)
        assertThat(output.data.vesselName).isEqualTo(entity.vesselName)
        assertThat(output.data.fishActionType).isEqualTo(entity.fishActionType)
        assertThat(output.data.isSeafarersControl).isEqualTo(entity.isSeafarersControl)
        assertThat(output.data.isAdministrativeControl).isEqualTo(entity.isAdministrativeControl)
        assertThat(output.data.internalReferenceNumber).isEqualTo(entity.internalReferenceNumber)
        assertThat(output.data.externalReferenceNumber).isEqualTo(entity.externalReferenceNumber)
        assertThat(output.data.observationsByUnit).isEqualTo(entity.observationsByUnit)
        assertThat(output.data.completion).isEqualTo(entity.completion)
        assertThat(output.data.isComplianceWithWaterRegulationsControl).isEqualTo(entity.isComplianceWithWaterRegulationsControl)
        assertThat(output.data.isSafetyEquipmentAndStandardsComplianceControl).isEqualTo(entity.isSafetyEquipmentAndStandardsComplianceControl)
        assertThat(output.data.districtCode).isEqualTo(entity.districtCode)
        assertThat(output.data.faoAreas).isEqualTo(entity.faoAreas)
        assertThat(output.data.emitsVms).isEqualTo(entity.emitsVms)
        assertThat(output.data.emitsAis).isEqualTo(entity.emitsAis)
        assertThat(output.data.logbookMatchesActivity).isEqualTo(entity.logbookMatchesActivity)
        assertThat(output.data.licencesMatchActivity).isEqualTo(entity.licencesMatchActivity)
        assertThat(output.data.speciesWeightControlled).isEqualTo(entity.speciesWeightControlled)
        assertThat(output.data.separateStowageOfPreservedSpecies).isEqualTo(entity.separateStowageOfPreservedSpecies)
        assertThat(output.data.logbookInfractions).isEqualTo(entity.logbookInfractions)
        assertThat(output.data.licencesAndLogbookObservations).isEqualTo(entity.licencesAndLogbookObservations)
        assertThat(output.data.gearInfractions).isEqualTo(entity.gearInfractions)
        assertThat(output.data.speciesInfractions).isEqualTo(entity.speciesInfractions)
        assertThat(output.data.speciesObservations).isEqualTo(entity.speciesObservations)
        assertThat(output.data.seizureAndDiversion).isEqualTo(entity.seizureAndDiversion)
        assertThat(output.data.otherInfractions).isEqualTo(entity.otherInfractions)
        assertThat(output.data.numberOfVesselsFlownOver).isEqualTo(entity.numberOfVesselsFlownOver)
        assertThat(output.data.unitWithoutOmegaGauge).isEqualTo(entity.unitWithoutOmegaGauge)
        assertThat(output.data.controlQualityComments).isEqualTo(entity.controlQualityComments)
        assertThat(output.data.feedbackSheetRequired).isEqualTo(entity.feedbackSheetRequired)
        assertThat(output.data.userTrigram).isEqualTo(entity.userTrigram)
        assertThat(output.data.segments).isEqualTo(entity.segments)
        assertThat(output.data.longitude).isEqualTo(entity.longitude)
        assertThat(output.data.latitude).isEqualTo(entity.latitude)
        assertThat(output.data.portLocode).isEqualTo(entity.portLocode)
        assertThat(output.data.portName).isEqualTo(entity.portName)
        assertThat(output.data.vesselTargeted).isEqualTo(entity.vesselTargeted)
        assertThat(output.data.seizureAndDiversionComments).isEqualTo(entity.seizureAndDiversionComments)
        assertThat(output.data.otherComments).isEqualTo(entity.otherComments)
        assertThat(output.data.gearOnboard).isEqualTo(entity.gearOnboard)
        assertThat(output.data.speciesOnboard).isEqualTo(entity.speciesOnboard)
        assertThat(output.data.isFromPoseidon).isEqualTo(entity.isFromPoseidon)
        assertThat(output.data.isDeleted).isEqualTo(entity.isDeleted)
        assertThat(output.data.hasSomeGearsSeized).isEqualTo(entity.hasSomeGearsSeized)
        assertThat(output.data.hasSomeSpeciesSeized).isEqualTo(entity.hasSomeSpeciesSeized)
        assertThat(output.data.isAdministrativeControl).isEqualTo(entity.isAdministrativeControl)
        assertThat(output.data.isComplianceWithWaterRegulationsControl).isEqualTo(entity.isComplianceWithWaterRegulationsControl)
        assertThat(output.data.isSafetyEquipmentAndStandardsComplianceControl).isEqualTo(entity.isSafetyEquipmentAndStandardsComplianceControl)
        assertThat(output.data.isSeafarersControl).isEqualTo(entity.isSeafarersControl)
        assertThat(output.data.observationsByUnit).isEqualTo(entity.observationsByUnit)
        assertThat(output.data.speciesQuantitySeized).isEqualTo(entity.speciesQuantitySeized)
    }
}
