package fr.gouv.gmampa.rapportnav.domain.entities

import fr.gouv.dgampa.rapportnav.domain.entities.mission.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.ActionCompletionEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.Completion
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentRoleEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.gmampa.rapportnav.mocks.mission.EnvMissionMock
import fr.gouv.gmampa.rapportnav.mocks.mission.NavMissionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.EnvActionControlMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.FishActionControlMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.NavActionStatusMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit


class MissionEntityTests {

    @Nested
    inner class CalculateMissionStatus {
        @Test
        fun `should return ENDED when the endDate is prior than now`() {
            val mission = MissionEntity(
                envMission = ExtendedEnvMissionEntity.fromEnvMission(
                    EnvMissionMock.create(
                        startDateTimeUtc = Instant.now().minus(14, ChronoUnit.DAYS),
                        endDateTimeUtc = Instant.now().minus(2, ChronoUnit.DAYS),
                    )
                ),
                fishMissionActions = listOf(FishActionControlMock.create()).map {
                    ExtendedFishActionEntity.fromMissionAction(
                        it
                    )
                },
                navMission = NavMissionEntityMock.create()
            )
            assertThat(mission.status).isEqualTo(MissionStatusEnum.ENDED)
        }

        @Test
        fun `should return UPCOMING when startDateTimeUtc is after compareDate`() {
            val mission = MissionEntity(
                envMission = ExtendedEnvMissionEntity.fromEnvMission(
                    EnvMissionMock.create(
                        startDateTimeUtc = Instant.now().plus(14, ChronoUnit.DAYS),
                        endDateTimeUtc = Instant.now().plus(26, ChronoUnit.DAYS),
                    )
                ),
                fishMissionActions = listOf(FishActionControlMock.create()).map {
                    ExtendedFishActionEntity.fromMissionAction(
                        it
                    )
                },
                navMission = NavMissionEntityMock.create()
            )
            assertThat(mission.status).isEqualTo(MissionStatusEnum.UPCOMING)
        }

        @Test
        fun `should return ENDED when endDateTimeUtc is before compareDate`() {
            val mission = MissionEntity(
                envMission = ExtendedEnvMissionEntity.fromEnvMission(
                    EnvMissionMock.create(
                        endDateTimeUtc = Instant.now().minus(14, ChronoUnit.DAYS)
                    )
                ),
                fishMissionActions = listOf(FishActionControlMock.create()).map {
                    ExtendedFishActionEntity.fromMissionAction(
                        it
                    )
                },
                navMission = NavMissionEntityMock.create()
            )
            assertThat(mission.status).isEqualTo(MissionStatusEnum.ENDED)
        }

        @Test
        fun `should return IN_PROGRESS when after startDateTimeUtc but before endDateTimeUtc`() {
            val mission = MissionEntity(
                envMission = ExtendedEnvMissionEntity.fromEnvMission(
                    EnvMissionMock.create(
                        startDateTimeUtc = Instant.now().minus(30, ChronoUnit.DAYS),
                        endDateTimeUtc = Instant.now().plus(14, ChronoUnit.DAYS)
                    )
                ),
                fishMissionActions = listOf(FishActionControlMock.create()).map {
                    ExtendedFishActionEntity.fromMissionAction(
                        it
                    )
                },
                navMission = NavMissionEntityMock.create()
            )
            assertThat(mission.status).isEqualTo(MissionStatusEnum.IN_PROGRESS)
        }


        @Test
        fun `should return UNAVAILABLE when endDateTimeUtc is null`() {
            val mission = MissionEntity(
                envMission = ExtendedEnvMissionEntity.fromEnvMission(
                    EnvMissionMock.create(
                        startDateTimeUtc = Instant.now().minus(30, ChronoUnit.DAYS),
                        endDateTimeUtc = null
                    )
                ),
                fishMissionActions = listOf(FishActionControlMock.create()).map {
                    ExtendedFishActionEntity.fromMissionAction(
                        it
                    )
                },
                navMission = NavMissionEntityMock.create()
            )
            assertThat(mission.status).isEqualTo(MissionStatusEnum.UNAVAILABLE)
        }
    }


    @Nested
    inner class CalculateCompletenessForStats {

        private val validMissionGeneralInfoEntity = MissionGeneralInfoEntity(
            id = 1,
            missionId = 1,
            consumedFuelInLiters = 1F,
            consumedGOInLiters = 1F,
            distanceInNauticalMiles = 1F
        )
        private val validCrewEntity = listOf(
            MissionCrewEntity(
                id = 1,
                missionId = 1,
                agent = AgentEntity(
                    firstName = "name",
                    lastName = "name",
                ),
                role = AgentRoleEntity(title = "role"),
            )
        )

        @Test
        fun `should return incomplete and RapportNav when no crew`() {
            val mission = MissionEntity(
                envMission = ExtendedEnvMissionEntity.fromEnvMission(
                    EnvMissionMock.create(envActions = null)
                ),
                fishMissionActions = listOf(),
                navMission = NavMissionEntityMock.create(
                    crew = null,
                    generalInfo = validMissionGeneralInfoEntity,
                    actions = listOf()
                )
            )
            val expected = CompletenessForStatsEntity(
                status = CompletenessForStatsStatusEnum.INCOMPLETE,
                sources = listOf(MissionSourceEnum.RAPPORTNAV)
            )
            assertThat(mission.completenessForStats).isEqualTo(expected)
        }

        @Test
        fun `should return incomplete and RapportNav when no generalInfo`() {
            val mission = MissionEntity(
                envMission = ExtendedEnvMissionEntity.fromEnvMission(
                    EnvMissionMock.create(envActions = null)
                ),
                fishMissionActions = listOf(),
                navMission = NavMissionEntityMock.create(
                    crew = validCrewEntity,
                    generalInfo = null,
                    actions = listOf()
                )
            )
            val expected = CompletenessForStatsEntity(
                status = CompletenessForStatsStatusEnum.INCOMPLETE,
                sources = listOf(MissionSourceEnum.RAPPORTNAV)
            )
            assertThat(mission.completenessForStats).isEqualTo(expected)
        }


        @Test
        fun `should return incomplete and all sources when actions are incomplete`() {
            val mission = MissionEntity(
                envMission = ExtendedEnvMissionEntity.fromEnvMission(
                    EnvMissionMock.create(
                        envActions = listOf(EnvActionControlMock.create(completion = ActionCompletionEnum.TO_COMPLETE))
                    )
                ),
                fishMissionActions = listOf(FishActionControlMock.create(completion = Completion.TO_COMPLETE)).map {
                    ExtendedFishActionEntity.fromMissionAction(
                        it
                    )
                },
                navMission = NavMissionEntityMock.create(
                    crew = validCrewEntity,
                    generalInfo = validMissionGeneralInfoEntity,
                    actions = listOf(
                        NavActionStatusMock.create(isCompleteForStats = false).toNavActionEntity()
                    )
                )
            )
            val expected = CompletenessForStatsEntity(
                status = CompletenessForStatsStatusEnum.INCOMPLETE,
                sources = listOf(
                    MissionSourceEnum.MONITORENV,
                    MissionSourceEnum.MONITORFISH,
                    MissionSourceEnum.RAPPORTNAV,
                )
            )
            assertThat(mission.completenessForStats).isEqualTo(expected)
        }


        @Test
        fun `should return have list of services`() {
            val expected = listOf(
                ServiceEntity(
                    id = 3,
                    name = "firstService",
                    controlUnits = listOf(1, 3)
                ), ServiceEntity(
                    id = 4,
                    name = "SecondService",
                    controlUnits = listOf(3, 4)
                )
            );
            val mission = MissionEntity(
                envMission = ExtendedEnvMissionEntity.fromEnvMission(
                    EnvMissionMock.create()
                ),
                fishMissionActions = listOf(),
                navMission = NavMissionEntityMock.create(
                    services = expected
                )
            )
            assertThat(mission.services).isNotNull()
            assertThat(mission.services).isEqualTo(expected);
        }

        @Test
        fun `should return observationsByUnit`() {
            val expected = "MyBeautifulObservations";
            val mission = MissionEntity(
                envMission = ExtendedEnvMissionEntity.fromEnvMission(
                    EnvMissionMock.create(observationsByUnit = expected)
                ),
                fishMissionActions = listOf(),
                navMission = NavMissionEntityMock.create()
            )
            assertThat(mission.observationsByUnit).isNotNull()
            assertThat(mission.observationsByUnit).isEqualTo(expected);
        }
    }
}

