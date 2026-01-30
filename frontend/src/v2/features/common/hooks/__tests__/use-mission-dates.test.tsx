import { describe, it, expect, beforeEach } from 'vitest'
import { renderHook } from '@testing-library/react'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { missionsKeys } from '../../services/query-keys.ts'
import { useMissionDates } from '../use-mission-dates.tsx'
import { Mission2, MissionStatusEnum } from '../../types/mission-types.ts'

const mockMission: Mission2 = {
  id: 1,
  status: MissionStatusEnum.IN_PROGRESS,
  data: {
    startDateTimeUtc: '2024-01-15T08:00:00Z',
    endDateTimeUtc: '2024-01-15T18:00:00Z'
  }
} as Mission2

describe('useMissionDates', () => {
  let queryClient: QueryClient

  beforeEach(() => {
    queryClient = new QueryClient()
  })

  function wrapper({ children }: { children: React.ReactNode }) {
    return <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
  }

  it('returns start and end dates when mission exists', () => {
    queryClient.setQueryData(missionsKeys.byId('1'), mockMission)
    const { result } = renderHook(() => useMissionDates('1'), { wrapper })
    expect(result.current).toEqual(['2024-01-15T08:00:00Z', '2024-01-15T18:00:00Z'])
  })

  it('returns startDateTimeUtc as first element', () => {
    queryClient.setQueryData(missionsKeys.byId('1'), mockMission)
    const { result } = renderHook(() => useMissionDates('1'), { wrapper })
    expect(result.current[0]).toBe('2024-01-15T08:00:00Z')
  })

  it('returns endDateTimeUtc as second element', () => {
    queryClient.setQueryData(missionsKeys.byId('1'), mockMission)
    const { result } = renderHook(() => useMissionDates('1'), { wrapper })
    expect(result.current[1]).toBe('2024-01-15T18:00:00Z')
  })

  it('returns undefined values when mission is not found', () => {
    const { result } = renderHook(() => useMissionDates('999'), { wrapper })
    expect(result.current).toEqual([undefined, undefined])
  })

  it('returns undefined values when missionId is undefined', () => {
    const { result } = renderHook(() => useMissionDates(undefined), { wrapper })
    expect(result.current).toEqual([undefined, undefined])
  })

  it('returns undefined values when mission has no data', () => {
    const missionWithoutData = { id: 1, status: MissionStatusEnum.IN_PROGRESS } as Mission2
    queryClient.setQueryData(missionsKeys.byId('1'), missionWithoutData)
    const { result } = renderHook(() => useMissionDates('1'), { wrapper })
    expect(result.current).toEqual([undefined, undefined])
  })
})
