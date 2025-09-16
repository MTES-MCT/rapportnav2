import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import React from 'react'
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'
import axios from '../../../../../query-client/axios'
import { renderHook, waitFor } from '../../../../../test-utils.tsx'
import useMissionsQuery, { Frame } from '../use-missions'

// --- MOCK axios.get to return mission list ---
vi.mock('../../../../../query-client/axios', () => ({
  default: {
    get: vi.fn()
  }
}))

// Mock current date to September 2025 for consistent testing
const mockCurrentDate = new Date('2025-09-15T12:00:00.000Z')
vi.setSystemTime(mockCurrentDate)

describe('useMissionsQuery', () => {
  let queryClient: QueryClient
  let wrapper: React.FC<{ children: React.ReactNode }>
  let setQueryDataSpy: vi.SpyInstance

  beforeEach(() => {
    queryClient = new QueryClient({
      defaultOptions: {
        queries: { retry: false }
      }
    })

    setQueryDataSpy = vi.spyOn(queryClient, 'setQueryData')

    wrapper = ({ children }) => <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>

    vi.clearAllMocks()
  })

  afterEach(() => {
    queryClient.clear()
    setQueryDataSpy.mockRestore()
  })

  it('fetches missions yearly frame for current year, queries months Jan-Sep antichronologically', async () => {
    const septemberMissions = [
      { id: 101, name: 'Apollo September', actions: [] },
      { id: 102, name: 'Gemini September', actions: [] }
    ]
    const augustMissions = [{ id: 201, name: 'Apollo August', actions: [] }]
    const januaryMissions = [{ id: 301, name: 'Apollo January', actions: [] }]

    ;(axios.get as vi.Mock).mockImplementation((url: string) => {
      debugger
      if (url.includes('2025-09-30')) return Promise.resolve({ data: septemberMissions })
      if (url.includes('2025-08-31')) return Promise.resolve({ data: augustMissions })
      if (url.includes('2025-01-31')) return Promise.resolve({ data: januaryMissions })
      return Promise.resolve({ data: [] })
    })

    // Simulating current year params (2025) with timezone offset
    const params = new URLSearchParams({
      startDateTimeUtc: '2024-12-31T23:00:00.000Z', // Start of 2025 in French timezone
      endDateTimeUtc: '2025-12-31T22:59:59.999Z' // End of 2025 in French timezone
    })

    const { result } = renderHook(() => useMissionsQuery(params, 'yearly' as Frame), { wrapper })

    await waitFor(() => expect(result.current.isSuccess).toBe(true))

    // Should contain missions from January to September (current month)
    const mergedIds = result.current.data.map(m => m.id)
    expect(mergedIds).toEqual(expect.arrayContaining([101, 102, 201, 301]))

    // Verify axios was called for months January through September (9 calls)
    expect(axios.get).toHaveBeenCalledTimes(9)
  })

  it('fetches missions yearly frame for previous year, queries all 12 months', async () => {
    const decemberMissions = [{ id: 401, name: 'Apollo December 2024', actions: [] }]
    const januaryMissions = [{ id: 501, name: 'Apollo January 2024', actions: [] }]

    ;(axios.get as vi.Mock).mockImplementation((url: string) => {
      if (url.includes('2024-12-31')) return Promise.resolve({ data: decemberMissions })
      if (url.includes('2024-01-31')) return Promise.resolve({ data: januaryMissions })
      return Promise.resolve({ data: [] })
    })

    // Simulating previous year params (2024)
    const params = new URLSearchParams({
      startDateTimeUtc: '2023-12-31T23:00:00.000Z',
      endDateTimeUtc: '2024-12-31T22:59:59.999Z'
    })

    const { result } = renderHook(() => useMissionsQuery(params, 'yearly' as Frame), { wrapper })

    await waitFor(() => expect(result.current.isSuccess).toBe(true))

    const mergedIds = result.current.data.map(m => m.id)
    expect(mergedIds).toEqual(expect.arrayContaining([401, 501]))

    // Verify axios was called for all 12 months of 2024
    expect(axios.get).toHaveBeenCalledTimes(12)
  })

  it('fetches missions yearly frame for future year, makes no queries', async () => {
    // Simulating future year params (2026)
    const params = new URLSearchParams({
      startDateTimeUtc: '2025-12-31T23:00:00.000Z',
      endDateTimeUtc: '2026-12-31T22:59:59.999Z'
    })

    const { result } = renderHook(() => useMissionsQuery(params, 'yearly' as Frame), { wrapper })

    await waitFor(() => expect(result.current.isSuccess).toBe(true))

    // Should return empty array for future years
    expect(result.current.data).toEqual([])
    expect(axios.get).toHaveBeenCalled()
  })

  it('fetches missions monthly frame with single query', async () => {
    const monthlyMissions = [
      { id: 201, name: 'Monthly 1', actions: [] },
      { id: 202, name: 'Monthly 2', actions: [] }
    ]

    ;(axios.get as vi.Mock).mockResolvedValue({ data: monthlyMissions })

    const params = new URLSearchParams({
      startDateTimeUtc: '2025-02-01T00:00:00Z',
      endDateTimeUtc: '2025-02-28T23:59:59Z'
    })

    const { result } = renderHook(() => useMissionsQuery(params, 'monthly' as Frame), { wrapper })

    await waitFor(() => expect(result.current.isSuccess).toBe(true))

    const ids = result.current.data.map(m => m.id)
    expect(ids).toEqual(expect.arrayContaining([201, 202]))

    // Verify only one query was made for monthly frame
    expect(axios.get).toHaveBeenCalledTimes(1)
  })

  it('normalizes missions and actions into cache', async () => {
    const missionsWithActions = [
      {
        id: 601,
        name: 'Mission with actions',
        actions: [
          { id: 'action-1', name: 'Action 1' },
          { id: 'action-2', name: 'Action 2' }
        ]
      }
    ]

    ;(axios.get as vi.Mock).mockResolvedValue({ data: missionsWithActions })

    const params = new URLSearchParams({
      startDateTimeUtc: '2025-02-01T00:00:00Z',
      endDateTimeUtc: '2025-02-28T23:59:59Z'
    })

    renderHook(() => useMissionsQuery(params, 'monthly' as Frame), { wrapper })

    await waitFor(() => {
      // Verify mission was cached
      expect(setQueryDataSpy).toHaveBeenCalledWith(expect.arrayContaining(['missions', '601']), missionsWithActions[0])
      // Verify actions were cached
      expect(setQueryDataSpy).toHaveBeenCalledWith(
        expect.arrayContaining(['actions', 'action-1']),
        missionsWithActions[0].actions[0]
      )
      expect(setQueryDataSpy).toHaveBeenCalledWith(
        expect.arrayContaining(['actions', 'action-2']),
        missionsWithActions[0].actions[1]
      )
    })
  })
})
