import { renderHook } from '@testing-library/react'
import { describe, expect, it } from 'vitest'
import { MissionAction } from '../../types/mission-action.ts'
import { Mission2, MissionSourceEnum, MissionStatusEnum } from '../../types/mission-types.ts'
import { useMission } from '../use-mission.tsx'

const mockMission: Mission2 = {
  id: 1,
  status: MissionStatusEnum.ENDED
} as Mission2

describe('useMission', () => {
  it('mission is deletable when source is RAPPORT_NAV', () => {
    const mission = {
      ...mockMission,
      actions: [{ id: 'action-2', source: MissionSourceEnum.RAPPORTNAV } as MissionAction]
    }
    const { result } = renderHook(() => useMission())
    expect(result.current.isMissionNotDeletable(mission)).toBeFalsy()
  })

  it('mission is not deletable when source is MONITORFISH', () => {
    const mission = {
      ...mockMission,
      actions: [{ id: 'action-2', source: MissionSourceEnum.MONITORFISH } as MissionAction]
    }
    const { result } = renderHook(() => useMission())
    expect(result.current.isMissionNotDeletable(mission)).toBeTruthy()
  })

  it('mission is not deletable when source is MONITORENV', () => {
    const mission = {
      ...mockMission,
      actions: [{ id: 'action-2', source: MissionSourceEnum.MONITORENV } as MissionAction]
    }
    const { result } = renderHook(() => useMission())
    expect(result.current.isMissionNotDeletable(mission)).toBeTruthy()
  })
})
