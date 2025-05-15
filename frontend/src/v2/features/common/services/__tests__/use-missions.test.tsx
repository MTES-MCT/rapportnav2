import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import useMissionsQuery from '../use-missions'
import axios from '../../../../../query-client/axios'
import { waitFor, renderHook } from '../../../../../test-utils.tsx'
import { vi } from 'vitest'
import React from 'react'

// 1) Mock axios.get to return a fake mission list
vi.mock('../../../../../query-client/axios', () => ({
  default: {
    get: vi.fn()
  }
}))

// 2) Spy on usePrefetchMission so we can inspect calls
const prefetchSpy = vi.fn()
vi.mock('../use-prefetch-mission.tsx', () => ({
  usePrefetchMission: () => ({ prefetchMission: prefetchSpy })
}))

describe('useMissionsQuery', () => {
  let queryClient: QueryClient
  let wrapper: React.FC<{ children: React.ReactNode }>

  beforeEach(() => {
    // fresh QueryClient per test
    queryClient = new QueryClient({
      defaultOptions: {
        queries: {
          retry: false // fail fast
        }
      }
    })
    wrapper = ({ children }) => <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>

    // Reset mocks
    vi.clearAllMocks()
  })

  afterEach(() => {
    queryClient.clear()
  })

  it('fetches missions and prefetches each mission ID', async () => {
    // Arrange: have axios.get resolve with two missions
    const mockMissions = [
      { id: 11, name: 'Apollo' },
      { id: 22, name: 'Gemini' }
    ]
    ;(axios.get as vi.Mock).mockResolvedValue({ data: mockMissions })

    // Act: render with a valid startDateTimeUtc
    const { result } = renderHook(() => useMissionsQuery({ startDateTimeUtc: '2025-01-01T00:00:00Z' }), { wrapper })

    // Wait until the query is successful
    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true)
    })

    // Assert: data matches
    expect(result.current.data).toEqual(mockMissions)

    // And prefetchMission was called for each mission id
    expect(prefetchSpy).toHaveBeenCalledTimes(2)
    expect(prefetchSpy).toHaveBeenCalledWith(11)
    expect(prefetchSpy).toHaveBeenCalledWith(22)
  })

  it('does not run query or prefetch when startDateTimeUtc is empty', async () => {
    // Arrange: even if axios.get were to resolve, it should not be called
    ;(axios.get as vi.Mock).mockResolvedValue({ data: [] })

    // Act: render with an empty startDateTimeUtc
    renderHook(() => useMissionsQuery({ startDateTimeUtc: '' }), { wrapper })

    expect(axios.get).not.toHaveBeenCalled()
    expect(prefetchSpy).not.toHaveBeenCalled()
  })
})
