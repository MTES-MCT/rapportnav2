import { describe, it, expect, beforeEach } from 'vitest'
import { renderHook } from '@testing-library/react'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { missionsKeys } from '../../services/query-keys.ts'
import { useMissionInterServices } from '../use-mission-interservices.tsx'

describe('useMissionInterServices', () => {
  let queryClient: QueryClient

  beforeEach(() => {
    queryClient = new QueryClient()
  })

  function wrapper({ children }: { children: React.ReactNode }) {
    return <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
  }

  it('returns undefined if mission not found', () => {
    const { result } = renderHook(() => useMissionInterServices('1'), { wrapper })
    expect(result.current).toBe(undefined)
  })

  it('returns false if mission has no controlUnits', () => {
    const missionId = '123'
    queryClient.setQueryData(missionsKeys.byId(missionId), { data: { controlUnits: [] } })
    const { result } = renderHook(() => useMissionInterServices(missionId), { wrapper })
    expect(result.current).toBe(false)
  })

  it('returns false if mission has one non-archived control unit', () => {
    const missionId = '456'
    queryClient.setQueryData(missionsKeys.byId(missionId), {
      data: {
        controlUnits: [{ isArchived: false, administration: 'AdminA' }]
      }
    })
    const { result } = renderHook(() => useMissionInterServices(missionId), { wrapper })
    expect(result.current).toBe(false)
  })

  it('returns false if multiple control units belong to the same administration', () => {
    const missionId = '789'
    queryClient.setQueryData(missionsKeys.byId(missionId), {
      data: {
        controlUnits: [
          { isArchived: false, administration: ' AdminA ' },
          { isArchived: false, administration: 'AdminA' },
          { isArchived: true, administration: 'AdminB' } // archived ignored
        ]
      }
    })
    const { result } = renderHook(() => useMissionInterServices(missionId), { wrapper })
    expect(result.current).toBe(false)
  })

  it('returns true if multiple non-archived control units from different administrations', () => {
    const missionId = '101'
    queryClient.setQueryData(missionsKeys.byId(missionId), {
      data: {
        controlUnits: [
          { isArchived: false, administration: 'AdminA' },
          { isArchived: false, administration: 'AdminB' }
        ]
      }
    })
    const { result } = renderHook(() => useMissionInterServices(missionId), { wrapper })
    expect(result.current).toBe(true)
  })

  it('ignores null or empty administration names', () => {
    const missionId = '202'
    queryClient.setQueryData(missionsKeys.byId(missionId), {
      data: {
        controlUnits: [
          { isArchived: false, administration: null },
          { isArchived: false, administration: '' },
          { isArchived: false, administration: 'AdminA' },
          { isArchived: false, administration: 'AdminB' }
        ]
      }
    })
    const { result } = renderHook(() => useMissionInterServices(missionId), { wrapper })
    expect(result.current).toBe(true)
  })
})
