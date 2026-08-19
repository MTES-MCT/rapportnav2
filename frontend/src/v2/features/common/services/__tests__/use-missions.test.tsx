import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import React from 'react'
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'
import axios from '../../../../../query-client/axios'
import { renderHook, waitFor } from '../../../../../test-utils.tsx'
import useMissionsQuery from '../use-missions'

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

  it('fetches missions and returns them without prefilling per-mission caches', async () => {
    const mockMissions = [
      { id: 11, name: 'Apollo' },
      { id: 22, name: 'Gemini' }
    ]

    ;(axios.get as vi.Mock).mockResolvedValue({ data: mockMissions })
    const endDateTimeUtc = '2025-02-01T00:00:00Z'
    const startDateTimeUtc = '2025-01-01T00:00:00Z'
    const params = new URLSearchParams({ endDateTimeUtc, startDateTimeUtc })
    const { result } = renderHook(() => useMissionsQuery(params), { wrapper })

    // Wait until the query is successful
    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true)
    })

    expect(result.current.data).toEqual(mockMissions)

    // The list now returns a light payload, so it no longer prefills the per-mission (byId) or
    // per-action caches from it — the detail page fetches the full mission via its own query.
    expect(setQueryDataSpy).not.toHaveBeenCalled()
  })

  it('does not run query when startDateTimeUtc is empty', async () => {
    ;(axios.get as vi.Mock).mockResolvedValue({ data: [] })
    const params = new URLSearchParams()

    renderHook(() => useMissionsQuery(params), { wrapper })

    expect(axios.get).not.toHaveBeenCalled()
    expect(setQueryDataSpy).not.toHaveBeenCalled()
  })
})
