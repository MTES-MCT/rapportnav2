import { renderHook } from '@testing-library/react'
import { MissionListData } from '../../types/mission-types'
import { useMissionList } from '../use-mission-list'

describe('useMissionList', () => {
  it('should return mission ulam name type', () => {
    const { result } = renderHook(() => useMissionList())
    const response = result.current.getMissionListItem({
      startDateTimeUtc: '2024-09-13T15:24:00Z'
    } as MissionListData)
    expect(response.missionNameUlam).toEqual('Mission #2024-09')
  })
})
