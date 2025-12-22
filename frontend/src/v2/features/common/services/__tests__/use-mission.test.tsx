import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import React from 'react'
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'
import axios from '../../../../../query-client/axios'
import { renderHook, waitFor } from '../../../../../test-utils.tsx'
import useGetMissionQuery from '../use-mission.tsx'
import { Mission2 } from '../../types/mission-types.ts'

// --- MOCK axios.get to return mission list ---
vi.mock('../../../../../query-client/axios', () => ({
  default: {
    get: vi.fn()
  }
}))

describe('useMissionsQuery', () => {
  let queryClient: QueryClient
  let wrapper: React.FC<{ children: React.ReactNode }>
  let setQueryDataSpy: vi.SpyInstance

  beforeEach(() => {
    // fresh QueryClient per test
    queryClient = new QueryClient({
      defaultOptions: {
        queries: {
          retry: false
        }
      }
    })

    // Spy on queryClient.setQueryData
    setQueryDataSpy = vi.spyOn(queryClient, 'setQueryData')

    wrapper = ({ children }) => <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>

    // Reset mocks
    vi.clearAllMocks()
  })

  afterEach(() => {
    queryClient.clear()
    setQueryDataSpy.mockRestore()
  })

  it('fetches mission and populates each action in cache using setQueryData', async () => {
    const mockMission: Mission2 = { id: 1, actions: [{ id: '11' }, { id: '12' }] }

    ;(axios.get as vi.Mock).mockResolvedValue({ data: mockMission })

    const { result } = renderHook(() => useGetMissionQuery('123'), { wrapper })

    // Wait until the query is successful
    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true)
    })

    // Should have called setQueryData for each mission
    expect(setQueryDataSpy).toHaveBeenCalledTimes(2)

    expect(setQueryDataSpy).toHaveBeenCalledWith(['actions', '11'], mockMission.actions[0])
    expect(setQueryDataSpy).toHaveBeenCalledWith(['actions', '12'], mockMission.actions[1])
  })

  it('does not run query when missingId falsy', async () => {
    ;(axios.get as vi.Mock).mockResolvedValue({ data: [] })
    renderHook(() => useGetMissionQuery(undefined), { wrapper })

    expect(axios.get).not.toHaveBeenCalled()
    expect(setQueryDataSpy).not.toHaveBeenCalled()
  })
})
