package fr.gouv.gmampa.rapportnav.domain.entities.satiExport

import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiConclusionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiInfractionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.FishInfraction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.NatinfEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiInspectorEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiModuleType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiPartyEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiVesselEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.ControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetEntity
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionFishActionEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class SatiConclusionEntityTest {

    @Test
    fun `should take the infractions and seizures of a fish action`() {
        val infraction = FishInfraction(
            infractionType = InfractionType.WITH_RECORD,
            natinf = 123,
            natinfDescription = "Entrave au contrôle",
            threat = "Obligations déclaratives",
            comments = "Refus d'accès à la cale.",
        )
        val natinfs = listOf(NatinfEntity(infraction = "Entrave", natinfCode = 123, infractionCategory = "Pêche"))
        val action = MissionFishActionEntityMock.create(
            fishInfractions = listOf(infraction),
            hasSomeGearsSeized = true,
            hasSomeSpeciesSeized = true,
            speciesQuantitySeized = 250,
        )

        assertThat(SatiConclusionEntity.fromFishAction(action, natinfs)).isEqualTo(
            SatiConclusionEntity(
                infractions = listOf(
                    SatiInfractionEntity(
                        infractionType = InfractionType.WITH_RECORD,
                        natinfs = listOf("123"),
                        descriptions = listOf("Entrave au contrôle"),
                        // the category of the NATINF, not the MonitorFish threat
                        natinfCategory = "Pêche",
                        comments = "Refus d'accès à la cale.",
                    )
                ),
                hasSomeGearsSeized = true,
                hasSomeSpeciesSeized = true,
                speciesQuantitySeized = 250,
            )
        )
    }

    @Test
    fun `should take the infractions of RapportNav after the ones of MonitorFish`() {
        val action = MissionFishActionEntityMock.create(fishInfractions = listOf(FishInfraction(natinf = 123)))
        action.targets = listOf(
            TargetEntity(
                id = UUID.randomUUID(),
                actionId = "1",
                targetType = TargetType.DEFAULT,
                controls = listOf(
                    ControlEntity(
                        id = UUID.randomUUID(),
                        controlType = ControlType.ADMINISTRATIVE,
                        amountOfControls = 1,
                        infractions = listOf(
                            InfractionEntity(
                                id = UUID.randomUUID(),
                                observations = "Permis périmé.",
                                natinfs = listOf("2593", "99999"),
                                infractionType = InfractionTypeEnum.WAITING,
                            )
                        ),
                    ),
                    ControlEntity(id = UUID.randomUUID(), controlType = ControlType.SECURITY, amountOfControls = 1),
                ),
            )
        )
        val natinfs = listOf(NatinfEntity(infraction = "Défaut de permis d'armement", natinfCode = 2593))

        val conclusion = SatiConclusionEntity.fromFishAction(action, natinfs)

        assertThat(conclusion.infractions.map { it.natinfs }).containsExactly(listOf("123"), listOf("2593", "99999"))
        assertThat(conclusion.infractions.last()).isEqualTo(
            SatiInfractionEntity(
                infractionType = InfractionType.PENDING,
                natinfs = listOf("2593", "99999"),
                // unknown NATINFs have no description
                descriptions = listOf("Défaut de permis d'armement"),
                controlType = ControlType.ADMINISTRATIVE,
                comments = "Permis périmé.",
            )
        )
    }

    @Test
    fun `should look the NATINFs up for MonitorFish or RapportNav infractions only`() {
        val withFishInfraction = MissionFishActionEntityMock.create(fishInfractions = listOf(FishInfraction(natinf = 123)))
        val withRapportNavInfraction = MissionFishActionEntityMock.create()
        withRapportNavInfraction.targets = listOf(
            TargetEntity(
                id = UUID.randomUUID(),
                actionId = "1",
                targetType = TargetType.DEFAULT,
                controls = listOf(
                    ControlEntity(
                        id = UUID.randomUUID(),
                        controlType = ControlType.ADMINISTRATIVE,
                        amountOfControls = 1,
                        infractions = listOf(InfractionEntity(id = UUID.randomUUID(), natinfs = listOf("2593"))),
                    )
                ),
            )
        )

        assertThat(SatiConclusionEntity.hasInfractions(withFishInfraction)).isTrue()
        assertThat(SatiConclusionEntity.hasInfractions(withRapportNavInfraction)).isTrue()
        assertThat(SatiConclusionEntity.hasInfractions(MissionFishActionEntityMock.create())).isFalse()
    }

    @Test
    fun `should take the comments and signatures of the principal inspector and of the master`() {
        val action = MissionFishActionEntityMock.create()
        action.observationsByUnit = "Contrôle sans difficulté."
        action.sati = SatiEntity(
            module = SatiModuleType.M1,
            actionId = "1",
            inspectors = listOf(
                SatiInspectorEntity(isPrincipal = false, party = SatiPartyEntity(signature = false)),
                SatiInspectorEntity(isPrincipal = true, party = SatiPartyEntity(signature = true)),
            ),
            vessel = SatiVesselEntity(master = SatiPartyEntity(comments = "Pas d'accord.", signature = false)),
        )

        val conclusion = SatiConclusionEntity.fromFishAction(action)

        assertThat(conclusion.inspectorComment).isEqualTo("Contrôle sans difficulté.")
        assertThat(conclusion.inspectorSigned).isTrue()
        assertThat(conclusion.controlledPersonComment).isEqualTo("Pas d'accord.")
        assertThat(conclusion.controlledPersonSigned).isFalse()
    }

    @Test
    fun `should not know the signatures without SATI`() {
        val conclusion = SatiConclusionEntity.fromFishAction(MissionFishActionEntityMock.create())

        assertThat(conclusion.inspectorSigned).isNull()
        assertThat(conclusion.controlledPersonSigned).isNull()
    }

    @Test
    fun `should have infractions when at least one is recorded`() {
        assertThat(SatiConclusionEntity(infractions = listOf(SatiInfractionEntity(natinfs = listOf("1")))).hasInfractions).isTrue()
        assertThat(SatiConclusionEntity().hasInfractions).isFalse()
    }

    @Test
    fun `should read unanswered seizures as no seizure`() {
        val action = MissionFishActionEntityMock.create(hasSomeGearsSeized = null, hasSomeSpeciesSeized = null)

        assertThat(SatiConclusionEntity.fromFishAction(action)).isEqualTo(SatiConclusionEntity())
    }
}
