import { describe, it, expect, beforeEach } from 'vitest'
import { renderHook } from '@testing-library/react'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { missionsKeys } from '../../services/query-keys.ts'
import { useMissionFinished } from '../use-mission-finished.tsx'
import { Mission2, MissionStatusEnum } from '../../types/mission-types.ts'

const mockMission: Mission2 = {
  id: 1,
  status: MissionStatusEnum.ENDED
} as Mission2

describe('useMissionFinished', () => {
  let queryClient: QueryClient

  beforeEach(() => {
    queryClient = new QueryClient()
  })

  function wrapper({ children }: { children: React.ReactNode }) {
    return <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
  }

  it('returns true if mission status is ENDED', () => {
    queryClient.setQueryData(missionsKeys.byId(1), mockMission)
    const { result } = renderHook(() => useMissionFinished('1'), { wrapper })
    expect(result.current).toBe(true)
  })

  it('returns false if mission status is not ENDED', () => {
    const activeMission = { ...mockMission, status: MissionStatusEnum.IN_PROGRESS }
    queryClient.setQueryData(missionsKeys.byId(1), activeMission)
    const { result } = renderHook(() => useMissionFinished('1'), { wrapper })
    expect(result.current).toBe(false)
  })

  it('returns false if no mission is found', () => {
    const { result } = renderHook(() => useMissionFinished('999'), { wrapper })
    expect(result.current).toBe(false)
  })

  it('returns false if missionId is undefined', () => {
    const { result } = renderHook(() => useMissionFinished(undefined), { wrapper })
    expect(result.current).toBe(false)
  })
})
