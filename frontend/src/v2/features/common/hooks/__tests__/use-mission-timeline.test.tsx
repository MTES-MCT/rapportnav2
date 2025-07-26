import { renderHook } from '@testing-library/react'
import { ActionType } from '../../types/action-type'
import { useTimelineAction } from '../use-timeline-action'

describe('useMissionTimeline', () => {
  it('should get input from mission action', () => {
    const { result } = renderHook(() => useTimelineAction('2'))
    const response = result.current.getActionInput(ActionType.NOTE, {})

    expect(response.missionId).toEqual(2)
    expect(response.ownerId).toBeUndefined()
    expect(response.actionType).toEqual(ActionType.NOTE)
  })
})
