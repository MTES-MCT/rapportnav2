package fr.gouv.gmampa.rapportnav.domain.entities.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.gmampa.rapportnav.mocks.mission.action.FishActionControlMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class MissionFishActionEntityTest {
    @Test
    fun `execute should retrieve entity from fish action`() {
        val fishAction = getFishAction()
        val entity = MissionFishActionEntity.fromFishAction(action = fishAction)
        assertThat(entity).isNotNull()
        assertThat(entity.id).isEqualTo(fishAction.id)
        assertThat(entity.startDateTimeUtc).isEqualTo(fishAction.actionDatetimeUtc)
        assertThat(entity.endDateTimeUtc).isEqualTo(fishAction.actionEndDatetimeUtc)
        assertThat(entity.vesselId).isEqualTo(fishAction.vesselId)
        assertThat(entity.completedBy).isEqualTo(fishAction.completedBy)
        assertThat(entity.facade).isEqualTo(fishAction.facade)
        assertThat(entity.vesselName).isEqualTo(fishAction.vesselName)
        assertThat(entity.actionType).isEqualTo(fishAction.actionType)
        assertThat(entity.isSeafarersControl).isEqualTo(fishAction.isSeafarersControl)
        assertThat(entity.isAdministrativeControl).isEqualTo(fishAction.isAdministrativeControl)
        assertThat(entity.internalReferenceNumber).isEqualTo(fishAction.internalReferenceNumber)
        assertThat(entity.externalReferenceNumber).isEqualTo(fishAction.externalReferenceNumber)
        assertThat(entity.observationsByUnit).isEqualTo(fishAction.observationsByUnit)
        assertThat(entity.completion).isEqualTo(fishAction.completion)
        assertThat(entity.ircs).isEqualTo(fishAction.ircs)
        assertThat(entity.flagState).isEqualTo(fishAction.flagState)
        assertThat(entity.isComplianceWithWaterRegulationsControl).isEqualTo(fishAction.isComplianceWithWaterRegulationsControl)
        assertThat(entity.isSafetyEquipmentAndStandardsComplianceControl).isEqualTo(fishAction.isSafetyEquipmentAndStandardsComplianceControl)
        assertThat(entity.districtCode).isEqualTo(fishAction.districtCode)
        assertThat(entity.faoAreas).isEqualTo(fishAction.faoAreas)
        assertThat(entity.emitsVms).isEqualTo(fishAction.emitsVms)
        assertThat(entity.emitsAis).isEqualTo(fishAction.emitsAis)
        assertThat(entity.flightGoals).isEqualTo(fishAction.flightGoals)
        assertThat(entity.logbookMatchesActivity).isEqualTo(fishAction.logbookMatchesActivity)
        assertThat(entity.licencesMatchActivity).isEqualTo(fishAction.licencesMatchActivity)
        assertThat(entity.speciesWeightControlled).isEqualTo(fishAction.speciesWeightControlled)
        assertThat(entity.separateStowageOfPreservedSpecies).isEqualTo(fishAction.separateStowageOfPreservedSpecies)
        assertThat(entity.logbookInfractions).isEqualTo(fishAction.logbookInfractions)
        assertThat(entity.licencesAndLogbookObservations).isEqualTo(fishAction.licencesAndLogbookObservations)
        assertThat(entity.gearInfractions).isEqualTo(fishAction.gearInfractions)
        assertThat(entity.speciesInfractions).isEqualTo(fishAction.speciesInfractions)
        assertThat(entity.speciesObservations).isEqualTo(fishAction.speciesObservations)
        assertThat(entity.seizureAndDiversion).isEqualTo(fishAction.seizureAndDiversion)
        assertThat(entity.otherInfractions).isEqualTo(fishAction.otherInfractions)
        assertThat(entity.numberOfVesselsFlownOver).isEqualTo(fishAction.numberOfVesselsFlownOver)
        assertThat(entity.unitWithoutOmegaGauge).isEqualTo(fishAction.unitWithoutOmegaGauge)
        assertThat(entity.controlQualityComments).isEqualTo(fishAction.controlQualityComments)
        assertThat(entity.feedbackSheetRequired).isEqualTo(fishAction.feedbackSheetRequired)
        assertThat(entity.userTrigram).isEqualTo(fishAction.userTrigram)
        assertThat(entity.segments).isEqualTo(fishAction.segments)
        assertThat(entity.longitude).isEqualTo(fishAction.longitude)
        assertThat(entity.latitude).isEqualTo(fishAction.latitude)
        assertThat(entity.portLocode).isEqualTo(fishAction.portLocode)
        assertThat(entity.portName).isEqualTo(fishAction.portName)
        assertThat(entity.vesselTargeted).isEqualTo(fishAction.vesselTargeted)
        assertThat(entity.seizureAndDiversionComments).isEqualTo(fishAction.seizureAndDiversionComments)
        assertThat(entity.otherComments).isEqualTo(fishAction.otherComments)
        assertThat(entity.gearOnboard).isEqualTo(fishAction.gearOnboard)
        assertThat(entity.speciesOnboard).isEqualTo(fishAction.speciesOnboard)
        assertThat(entity.isFromPoseidon).isEqualTo(fishAction.isFromPoseidon)
        assertThat(entity.controlUnits).isEqualTo(fishAction.controlUnits)
        assertThat(entity.isDeleted).isEqualTo(fishAction.isDeleted)
        assertThat(entity.hasSomeGearsSeized).isEqualTo(fishAction.hasSomeGearsSeized)
        assertThat(entity.hasSomeSpeciesSeized).isEqualTo(fishAction.hasSomeSpeciesSeized)
        assertThat(entity.isAdministrativeControl).isEqualTo(fishAction.isAdministrativeControl)
        assertThat(entity.isComplianceWithWaterRegulationsControl).isEqualTo(fishAction.isComplianceWithWaterRegulationsControl)
        assertThat(entity.isSafetyEquipmentAndStandardsComplianceControl).isEqualTo(fishAction.isSafetyEquipmentAndStandardsComplianceControl)
        assertThat(entity.isSeafarersControl).isEqualTo(fishAction.isSeafarersControl)
        assertThat(entity.observationsByUnit).isEqualTo(fishAction.observationsByUnit)
        assertThat(entity.speciesQuantitySeized).isEqualTo(fishAction.speciesQuantitySeized)

    }


    @Test
    fun `execute should retrieve retrieve timeline out put from entity`() {
        val fishAction = getFishAction()
        val entity = MissionFishActionEntity.fromFishAction(action = fishAction)
        val timelineOutput = entity.toMissionActionTimelineOutput()
        assertThat(timelineOutput).isNotNull()
        assertThat(timelineOutput.id).isEqualTo(entity.id.toString())
        assertThat(timelineOutput.startDateTimeUtc).isEqualTo(entity.actionDatetimeUtc)
        assertThat(timelineOutput.endDateTimeUtc).isEqualTo(entity.actionEndDatetimeUtc)
        assertThat(timelineOutput.vesselId).isEqualTo(entity.vesselId)
        assertThat(timelineOutput.vesselName).isEqualTo(entity.vesselName)
    }

    @Test
    fun `execute should be complete for stats env `() {
        val fishAction = getFishAction()
        val entity = MissionFishActionEntity.fromFishAction(action = fishAction)
        entity.computeCompleteness()

        assertThat(entity.isCompleteForStats).isEqualTo(false)
        assertThat(entity.sourcesOfMissingDataForStats).isEqualTo(listOf(MissionSourceEnum.MONITORFISH))
        assertThat(entity.completenessForStats?.sources).isEqualTo(listOf(MissionSourceEnum.MONITORFISH))
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.INCOMPLETE)
    }


    private fun getFishAction(): MissionAction {
        return FishActionControlMock.create()
    }
}
