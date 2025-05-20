import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import useMissionsQuery from '../use-missions'
import axios from '../../../../../query-client/axios'
import { waitFor, renderHook } from '../../../../../test-utils.tsx'
import { vi, describe, it, beforeEach, afterEach, expect } from 'vitest'
import React from 'react'

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

    vi.clearAllMocks()
  })

  afterEach(() => {
    queryClient.clear()
    setQueryDataSpy.mockRestore()
  })

  it('fetches missions and populates each mission in cache using setQueryData', async () => {
    const mockMissions = [
      { id: 11, name: 'Apollo' },
      { id: 22, name: 'Gemini' }
    ]

    ;(axios.get as vi.Mock).mockResolvedValue({ data: mockMissions })

    const { result } = renderHook(() => useMissionsQuery({ startDateTimeUtc: '2025-01-01T00:00:00Z' }), { wrapper })

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true)
    })

    // Should have called setQueryData for each mission
    expect(setQueryDataSpy).toHaveBeenCalledTimes(2)

    expect(setQueryDataSpy).toHaveBeenCalledWith(['missions', '11'], mockMissions[0])
    expect(setQueryDataSpy).toHaveBeenCalledWith(['missions', '22'], mockMissions[1])
  })

  it('does not run query when startDateTimeUtc is empty', async () => {
    ;(axios.get as vi.Mock).mockResolvedValue({ data: [] })

    renderHook(() => useMissionsQuery({ startDateTimeUtc: '' }), { wrapper })

    expect(axios.get).not.toHaveBeenCalled()
    expect(setQueryDataSpy).not.toHaveBeenCalled()
  })
})
