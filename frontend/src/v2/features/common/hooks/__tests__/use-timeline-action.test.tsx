import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { renderHook } from '@testing-library/react'
import React from 'react'
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'
import { ActionType } from '../../types/action-type'
import { Mission2, MissionSourceEnum, MissionStatusEnum } from '../../types/mission-types'
import { useTimelineAction } from '../use-timeline-action'

describe('useTimelineAction', () => {
  let queryClient: QueryClient
  let wrapper: React.FC<{ children: React.ReactNode }>

  beforeEach(() => {
    // Set a fixed date for deterministic tests (after mission dates which are in 2024)
    vi.useFakeTimers()
    vi.setSystemTime(new Date('2025-06-15T10:00:00Z'))

    queryClient = new QueryClient({
      defaultOptions: {
        queries: {
          retry: false
        }
      }
    })

    wrapper = ({ children }) => <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
  })

  afterEach(() => {
    vi.useRealTimers()
    queryClient.clear()
  })

  const createMockMission = (id: number, endDateTimeUtc?: string): Mission2 => ({
    id,
    status: MissionStatusEnum.IN_PROGRESS,
    isCompleteForStats: false,
    generalInfos: {},
    actions: [],
    data: {
      startDateTimeUtc: '2024-01-01T00:00:00Z',
      endDateTimeUtc,
      controlUnits: [],
      facade: 'NAMO' as any,
      missionSource: MissionSourceEnum.RAPPORT_NAV,
      missionTypes: [],
      openBy: 'test'
    }
  })

  describe('getActionInput', () => {
    it('returns action input with missionId when id is numeric', () => {
      const missionId = '123'
      const mockMission = createMockMission(123, '2024-01-31T23:59:59Z')
      queryClient.setQueryData(['missions', missionId], mockMission)

      const { result } = renderHook(() => useTimelineAction(missionId), { wrapper })
      const actionInput = result.current.getActionInput(ActionType.CONTROL)

      expect(actionInput.missionId).toBe(123)
      expect(actionInput.ownerId).toBeUndefined()
    })

    it('returns action input with ownerId when id is non-numeric', () => {
      const ownerId = 'abc-uuid-123'

      const { result } = renderHook(() => useTimelineAction(ownerId), { wrapper })
      const actionInput = result.current.getActionInput(ActionType.CONTROL)

      expect(actionInput.ownerId).toBe('abc-uuid-123')
      expect(actionInput.missionId).toBeUndefined()
    })

    it('sets source to RAPPORT_NAV', () => {
      const missionId = '456'
      const mockMission = createMockMission(456, '2024-01-31T23:59:59Z')
      queryClient.setQueryData(['missions', missionId], mockMission)

      const { result } = renderHook(() => useTimelineAction(missionId), { wrapper })
      const actionInput = result.current.getActionInput(ActionType.CONTROL)

      expect(actionInput.source).toBe(MissionSourceEnum.RAPPORT_NAV)
    })

    it('sets actionType from parameter', () => {
      const missionId = '789'
      const mockMission = createMockMission(789, '2024-01-31T23:59:59Z')
      queryClient.setQueryData(['missions', missionId], mockMission)

      const { result } = renderHook(() => useTimelineAction(missionId), { wrapper })
      const actionInput = result.current.getActionInput(ActionType.SURVEILLANCE)

      expect(actionInput.actionType).toBe(ActionType.SURVEILLANCE)
    })

    it('sets startDateTimeUtc to mission endDateTimeUtc', () => {
      const missionId = '111'
      const mockMission = createMockMission(111, '2024-02-15T12:00:00Z')
      queryClient.setQueryData(['missions', missionId], mockMission)

      const { result } = renderHook(() => useTimelineAction(missionId), { wrapper })
      const actionInput = result.current.getActionInput(ActionType.CONTROL)

      expect(actionInput.data.startDateTimeUtc).toBe('2024-02-15T12:00:00Z')
    })

    it('sets default values for isWithinDepartment and hasDivingDuringOperation', () => {
      const missionId = '222'
      const mockMission = createMockMission(222, '2024-01-31T23:59:59Z')
      queryClient.setQueryData(['missions', missionId], mockMission)

      const { result } = renderHook(() => useTimelineAction(missionId), { wrapper })
      const actionInput = result.current.getActionInput(ActionType.CONTROL)

      expect(actionInput.data.isWithinDepartment).toBe(true)
      expect(actionInput.data.hasDivingDuringOperation).toBe(false)
    })

    it('sets rescue flags for RESCUE action type', () => {
      const missionId = '444'
      const mockMission = createMockMission(444, '2024-01-31T23:59:59Z')
      queryClient.setQueryData(['missions', missionId], mockMission)

      const { result } = renderHook(() => useTimelineAction(missionId), { wrapper })
      const actionInput = result.current.getActionInput(ActionType.RESCUE)

      expect(actionInput.data.isPersonRescue).toBe(false)
      expect(actionInput.data.isVesselRescue).toBe(true)
    })

    it('merges moreData into action input data', () => {
      const missionId = '555'
      const mockMission = createMockMission(555, '2024-01-31T23:59:59Z')
      queryClient.setQueryData(['missions', missionId], mockMission)

      const moreData = {
        observations: 'Test observation',
        latitude: 48.8566,
        longitude: 2.3522
      }

      const { result } = renderHook(() => useTimelineAction(missionId), { wrapper })
      const actionInput = result.current.getActionInput(ActionType.CONTROL, moreData)

      expect(actionInput.data.observations).toBe('Test observation')
      expect(actionInput.data.latitude).toBe(48.8566)
      expect(actionInput.data.longitude).toBe(2.3522)
    })

    it('registry input overrides moreData for same keys', () => {
      const missionId = '666'
      const mockMission = createMockMission(666, '2024-01-31T23:59:59Z')
      queryClient.setQueryData(['missions', missionId], mockMission)

      const moreData = {
        isPersonRescue: true,
        isVesselRescue: false
      }

      const { result } = renderHook(() => useTimelineAction(missionId), { wrapper })
      const actionInput = result.current.getActionInput(ActionType.RESCUE, moreData)

      // Registry values should override moreData
      expect(actionInput.data.isPersonRescue).toBe(false)
      expect(actionInput.data.isVesselRescue).toBe(true)
    })

    it('handles undefined mission endDateTimeUtc', () => {
      const missionId = '777'
      const mockMission = createMockMission(777, undefined)
      queryClient.setQueryData(['missions', missionId], mockMission)

      const { result } = renderHook(() => useTimelineAction(missionId), { wrapper })
      const actionInput = result.current.getActionInput(ActionType.CONTROL)

      // When mission endDateTimeUtc is undefined and we're after mission start,
      // getActionStartDate returns the current date
      expect(actionInput.data.startDateTimeUtc).toBeDefined()
    })

    it('handles action type without registry entry', () => {
      const missionId = '888'
      const mockMission = createMockMission(888, '2024-01-31T23:59:59Z')
      queryClient.setQueryData(['missions', missionId], mockMission)

      const { result } = renderHook(() => useTimelineAction(missionId), { wrapper })
      const actionInput = result.current.getActionInput(ActionType.ANTI_POLLUTION)

      expect(actionInput.actionType).toBe(ActionType.ANTI_POLLUTION)
      expect(actionInput.data.startDateTimeUtc).toBe('2024-01-31T23:59:59Z')
      expect(actionInput.data.isWithinDepartment).toBe(true)
      expect(actionInput.data.hasDivingDuringOperation).toBe(false)
    })
  })
})
