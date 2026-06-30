package fr.gouv.dgampa.rapportnav.domain.entities.mission.sati

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

class SatiEntityMapperTest {

    private val timestamp = Instant.parse("2026-03-24T09:15:30Z")

    private fun buildSatiEntity(
        vessel: SatiVesselEntity? = SatiVesselEntity(
            id = 10,
            agent = SatiPartyEntity(id = 5, partyType = "AGENT"),
            master = SatiPartyEntity(id = 6, partyType = "MASTER"),
            jpe = SatiJpeEntity(tripNumber = "EXISTING-TRIP")
        ),
        inspectors: List<SatiInspectorEntity> = listOf(
            SatiInspectorEntity(id = 7, agentId = 42, authorityType = AuthorityType.AECP)
        ),
        resource: ControlResourceEntity? = ControlResourceEntity(id = 99, name = "Vedette A")
    ): SatiEntity {
        return SatiEntity(
            id = UUID.randomUUID(),
            module = SatiModuleType.M1,
            actionId = "old-action-id",
            vessel = vessel,
            resource = resource,
            inspectors = inspectors
        )
    }

    private fun buildMissionAction(
        id: Int = 123,
        actionType: MissionActionType = MissionActionType.SEA_CONTROL
    ): MissionAction {
        return MissionAction(
            id = id,
            missionId = 1,
            actionType = actionType,
            actionDatetimeUtc = timestamp,
            actionEndDatetimeUtc = timestamp.plusSeconds(3600),
            userTrigram = "ABC",
            isFromPoseidon = false,
            isDeleted = false,
            hasSomeGearsSeized = false,
            hasSomeSpeciesSeized = false,
            completion = Completion.TO_COMPLETE,
            vesselName = "Le Marin",
            internalReferenceNumber = "CFR-123",
            externalReferenceNumber = "EXT-001",
            ircs = "FXYZ",
            imo = "IMO1234567",
            vesselLength = 25.5,
            vesselType = "Trawler",
            flagState = CountryCode.FR,
            proprietorName = "Jean Dupont",
            proprietorEmails = listOf("jean@example.com", "jean2@example.com"),
            proprietorPhones = listOf("+33612345678"),
            proprietorNationality = "FRA",
            proprietorAddress = "1 rue de la mer, 75000 Paris",
            tripNumber = "TRIP-001",
            pnoReportId = "PNO-123",
            pnoPurpose = LogbookMessagePurpose.LAN,
            lastDeparturePortLocode = "FRLEH",
            lastDeparturePortName = "Le Havre",
            lastDepartureDateTime = timestamp
        )
    }

    @Nested
    inner class MergeWithMissionAction {

        @Test
        fun `should preserve sati id and module`() {
            val sati = buildSatiEntity()
            val action = buildMissionAction()
            val result = SatiEntityMapper.merge(sati, action)

            assertThat(result.id).isEqualTo(sati.id)
            assertThat(result.module).isEqualTo(sati.module)
        }

        @Test
        fun `should use action id as actionId`() {
            val sati = buildSatiEntity()
            val action = buildMissionAction(id = 456)
            val result = SatiEntityMapper.merge(sati, action)

            assertThat(result.actionId).isEqualTo("456")
        }


        @Test
        fun `should map action datetimes to sati datetimes`() {
            val sati = buildSatiEntity()
            val action = buildMissionAction()
            val result = SatiEntityMapper.merge(sati, action)

            assertThat(result.startDatetimeUtc).isEqualTo(action.actionDatetimeUtc)
            assertThat(result.endDatetimeUtc).isEqualTo(action.actionEndDatetimeUtc)
        }

        @Test
        fun `should map vessel identification from action`() {
            val sati = buildSatiEntity()
            val action = buildMissionAction()
            val result = SatiEntityMapper.merge(sati, action)

            assertThat(result.vessel?.name).isEqualTo("Le Marin")
            assertThat(result.vessel?.immat).isEqualTo("CFR-123")
            assertThat(result.vessel?.extRef).isEqualTo("EXT-001")
            assertThat(result.vessel?.ircs).isEqualTo("FXYZ")
            assertThat(result.vessel?.imo).isEqualTo("IMO1234567")
            assertThat(result.vessel?.length).isEqualTo(25.5)
            assertThat(result.vessel?.type).isEqualTo("Trawler")
            assertThat(result.vessel?.flagState).isEqualTo(CountryCode.FR)
        }

        @Test
        fun `should map proprietor to owner contact`() {
            val sati = buildSatiEntity()
            val action = buildMissionAction()
            val result = SatiEntityMapper.merge(sati, action)

            val owner = result.vessel?.owner
            assertThat(owner?.contact?.fullName).isEqualTo("Jean Dupont")
            assertThat(owner?.contact?.email).isEqualTo("jean@example.com")
            assertThat(owner?.contact?.phone).isEqualTo("+33612345678")
            assertThat(owner?.contact?.nationality).isEqualTo("FRA")
            assertThat(owner?.contact?.address?.fullAddress).isEqualTo("1 rue de la mer, 75000 Paris")
        }

        @Test
        fun `should map jpe fields from action`() {
            val sati = buildSatiEntity()
            val action = buildMissionAction()
            val result = SatiEntityMapper.merge(sati, action)

            val jpe = result.vessel?.jpe
            assertThat(jpe?.pnoId).isEqualTo("PNO-123")
            assertThat(jpe?.portId).isEqualTo("FRLEH")
            assertThat(jpe?.portName).isEqualTo("Le Havre")
            assertThat(jpe?.lastStopDate).isEqualTo(timestamp)
            assertThat(jpe?.tripNumber).isEqualTo("TRIP-001")
            assertThat(jpe?.pnoType).isEqualTo(LogbookMessagePurpose.LAN)
        }

        @Test
        fun `should fallback to sati tripNumber when action tripNumber is null`() {
            val sati = buildSatiEntity()
            val action = buildMissionAction().copy(tripNumber = null)
            val result = SatiEntityMapper.merge(sati, action)

            assertThat(result.vessel?.jpe?.tripNumber).isEqualTo("EXISTING-TRIP")
        }

        @Test
        fun `should preserve sati resource`() {
            val sati = buildSatiEntity()
            val action = buildMissionAction()
            val result = SatiEntityMapper.merge(sati, action)

            assertThat(result.resource?.id).isEqualTo(99)
            assertThat(result.resource?.name).isEqualTo("Vedette A")
        }

        @Test
        fun `should preserve sati inspectors`() {
            val sati = buildSatiEntity()
            val action = buildMissionAction()
            val result = SatiEntityMapper.merge(sati, action)

            assertThat(result.inspectors).hasSize(1)
            assertThat(result.inspectors?.first()?.agentId).isEqualTo(42)
        }

        @Test
        fun `should preserve agent and master from sati vessel`() {
            val sati = buildSatiEntity()
            val action = buildMissionAction()
            val result = SatiEntityMapper.merge(sati, action)

            assertThat(result.vessel?.agent?.partyType).isEqualTo("AGENT")
            assertThat(result.vessel?.master?.partyType).isEqualTo("MASTER")
        }

        @Test
        fun `should build operator from action data`() {
            val sati = buildSatiEntity()
            val action = buildMissionAction()
            val result = SatiEntityMapper.merge(sati, action)

            assertThat(result.vessel?.operator).isNotNull
            assertThat(result.vessel?.operator?.contact?.fullName).isNull()
        }

        @Test
        fun `should handle null action id`() {
            val sati = buildSatiEntity()
            val action = buildMissionAction().copy(id = null)
            val result = SatiEntityMapper.merge(sati, action)

            assertThat(result.actionId).isEmpty()
        }

        @Test
        fun `should handle null proprietor fields`() {
            val sati = buildSatiEntity()
            val action = buildMissionAction().copy(
                proprietorName = null,
                proprietorEmails = null,
                proprietorPhones = null,
                proprietorNationality = null,
                proprietorAddress = null
            )
            val result = SatiEntityMapper.merge(sati, action)

            val owner = result.vessel?.owner
            assertThat(owner?.contact?.fullName).isNull()
            assertThat(owner?.contact?.email).isNull()
            assertThat(owner?.contact?.phone).isNull()
            assertThat(owner?.contact?.nationality).isNull()
            assertThat(owner?.contact?.address?.fullAddress).isNull()
        }
    }

    @Nested
    inner class IsEquals {

        @Test
        fun `should return true for equal entities`() {
            val entity = buildSatiEntity()
            val copy = entity.copy()
            assertThat(SatiEntityMapper.isEquals(entity, copy)).isTrue()
        }

        @Test
        fun `should return false when fromDb is null`() {
            val entity = buildSatiEntity()
            assertThat(SatiEntityMapper.isEquals(null, entity)).isFalse()
        }

        @Test
        fun `should return false for entities with different ids`() {
            val entity1 = buildSatiEntity()
            val entity2 = entity1.copy(id = UUID.randomUUID())
            assertThat(SatiEntityMapper.isEquals(entity1, entity2)).isFalse()
        }
    }

    @Nested
    inner class MergeWithSatiInput {

        @Test
        fun `should return existing sati unchanged`() {
            val sati = buildSatiEntity()
            val input = fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.sati.Sati(
                actionId = "new-action",
                module = SatiModuleType.M3
            )
            val result = SatiEntityMapper.merge(sati, input)
            assertThat(result).isEqualTo(sati)
        }

        @Test
        fun `should return null when sati is null`() {
            val input = fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.sati.Sati(
                actionId = "new-action",
                module = SatiModuleType.M3
            )
            val result = SatiEntityMapper.merge(null, input)
            assertThat(result).isNull()
        }
    }
}
