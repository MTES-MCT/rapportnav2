import { renderHook } from '@testing-library/react'
import { Mission2, MissionEnvData } from '../../types/mission-types'
import { useMissionList } from '../use-mission-list'

describe('useMissionList', () => {
  it('should return mission ulam name type', () => {
    const { result } = renderHook(() => useMissionList())
    const response = result.current.getMissionListItem({
      generalInfos: {},
      envData: { startDateTimeUtc: '2024-09-13T15:24:00Z' } as MissionEnvData
    } as Mission2)
    expect(response.missionNameUlam).toEqual('Mission #2024-09')
  })
})
