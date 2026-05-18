import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { renderHook } from '@testing-library/react'
import React from 'react'
import { afterEach, beforeEach, describe, expect, it } from 'vitest'
import { Mission2, MissionSourceEnum, MissionStatusEnum } from '../../types/mission-types'
import { useMissionDates } from '../use-mission-dates'

describe('useMissionDates', () => {
  let queryClient: QueryClient
  let wrapper: React.FC<{ children: React.ReactNode }>

  beforeEach(() => {
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
    queryClient.clear()
  })

  it('returns undefined dates when missionId is undefined', () => {
    const { result } = renderHook(() => useMissionDates(undefined), { wrapper })

    expect(result.current.startDateTimeUtc).toBeUndefined()
    expect(result.current.endDateTimeUtc).toBeUndefined()
  })

  it('returns undefined dates when mission is not in cache', () => {
    const { result } = renderHook(() => useMissionDates('123'), { wrapper })

    expect(result.current.startDateTimeUtc).toBeUndefined()
    expect(result.current.endDateTimeUtc).toBeUndefined()
  })

  it('returns mission dates when mission is in cache', () => {
    const missionId = '456'
    const mockMission: Mission2 = {
      id: 456,
      status: MissionStatusEnum.IN_PROGRESS,
      isCompleteForStats: false,
      generalInfos: {},
      actions: [],
      data: {
        startDateTimeUtc: '2024-01-01T00:00:00Z',
        endDateTimeUtc: '2024-01-31T23:59:59Z',
        controlUnits: [],
        facade: 'NAMO' as any,
        missionSource: MissionSourceEnum.RAPPORT_NAV,
        missionTypes: [],
        openBy: 'test'
      }
    }

    queryClient.setQueryData(['missions', missionId], mockMission)

    const { result } = renderHook(() => useMissionDates(missionId), { wrapper })

    expect(result.current.startDateTimeUtc).toBe('2024-01-01T00:00:00Z')
    expect(result.current.endDateTimeUtc).toBe('2024-01-31T23:59:59Z')
  })

  it('returns undefined endDateTimeUtc when mission has no end date (ongoing mission)', () => {
    const missionId = '789'
    const mockMission: Mission2 = {
      id: 789,
      status: MissionStatusEnum.IN_PROGRESS,
      isCompleteForStats: false,
      generalInfos: {},
      actions: [],
      data: {
        startDateTimeUtc: '2024-01-01T00:00:00Z',
        endDateTimeUtc: undefined,
        controlUnits: [],
        facade: 'NAMO' as any,
        missionSource: MissionSourceEnum.RAPPORT_NAV,
        missionTypes: [],
        openBy: 'test'
      }
    }

    queryClient.setQueryData(['missions', missionId], mockMission)

    const { result } = renderHook(() => useMissionDates(missionId), { wrapper })

    expect(result.current.startDateTimeUtc).toBe('2024-01-01T00:00:00Z')
    expect(result.current.endDateTimeUtc).toBeUndefined()
  })
})
