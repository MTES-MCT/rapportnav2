package fr.gouv.gmampa.rapportnav.infrastructure.bff.controllers

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.UpdateGeneralInfo
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2.MissionGeneralInfoRestController
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfoEntityMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import java.util.*

class MissionGeneralInfoRestControllerTest {

    private val updateGeneralInfo: UpdateGeneralInfo = mock()
    private val controller = MissionGeneralInfoRestController(updateGeneralInfo)

    @Test
    fun `update should return updated general info for integer missionId`() {
        val missionId = 123
        val generalInfo = MissionGeneralInfo2(missionId = missionId)
        val entity = MissionGeneralInfoEntityMock.create(missionId = missionId)
        val expectedResult = MissionGeneralInfoEntity2(
            data = entity,
            crew = emptyList(),
            passengers = emptyList()
        )

        `when`(updateGeneralInfo.execute(missionId = eq(missionId), generalInfo = any())).thenReturn(expectedResult)

        val result = controller.update(missionId.toString(), generalInfo)

        assertEquals(expectedResult, result)
        verify(updateGeneralInfo).execute(missionId = eq(missionId), generalInfo = any())
    }

    @Test
    fun `update should return updated general info for UUID missionId`() {
        val missionIdUUID = UUID.randomUUID()
        val generalInfo = MissionGeneralInfo2(missionIdUUID = missionIdUUID)
        val entity = MissionGeneralInfoEntityMock.create(missionIdUUID = missionIdUUID)
        val expectedResult = MissionGeneralInfoEntity2(
            data = entity,
            crew = emptyList(),
            passengers = emptyList()
        )

        `when`(updateGeneralInfo.execute(missionIdUUID = eq(missionIdUUID), generalInfo = any())).thenReturn(expectedResult)

        val result = controller.update(missionIdUUID.toString(), generalInfo)

        assertEquals(expectedResult, result)
        verify(updateGeneralInfo).execute(missionIdUUID = eq(missionIdUUID), generalInfo = any())
    }

    @Test
    fun `update should throw BackendUsageException for invalid missionId format`() {
        val invalidMissionId = "not-a-uuid-or-integer"
        val generalInfo = MissionGeneralInfo2()

        val exception = assertThrows<BackendUsageException> {
            controller.update(invalidMissionId, generalInfo)
        }

        assertEquals(BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION, exception.code)
        assertEquals("Invalid missionId format: must be a valid UUID or integer", exception.message)

        verify(updateGeneralInfo, never()).execute(missionId = any<Int>(), generalInfo = any())
        verify(updateGeneralInfo, never()).execute(missionIdUUID = any<UUID>(), generalInfo = any())
    }
}