import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import React from 'react'
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'
import axios from '../../../../../query-client/axios'
import { renderHook, waitFor } from '../../../../../test-utils.tsx'
import { useOnlineManager } from '../../hooks/use-online-manager.tsx'
import useMissionsQuery, { MISSION_LIST_PAGE_SIZE } from '../use-missions'

// --- MOCK axios + online manager ---
vi.mock('../../../../../query-client/axios', () => ({
  default: {
    get: vi.fn()
  }
}))

vi.mock('../../hooks/use-online-manager.tsx', () => ({
  useOnlineManager: vi.fn()
}))

describe('useMissionsQuery', () => {
  let queryClient: QueryClient
  let wrapper: React.FC<{ children: React.ReactNode }>

  beforeEach(() => {
    queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } })
    wrapper = ({ children }) => <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
    vi.clearAllMocks()
    ;(useOnlineManager as any).mockReturnValue({ isOnline: true })
  })

  afterEach(() => {
    queryClient.clear()
  })

  it('fetches the first page, flattens items and exposes paging metadata', async () => {
    const items = [
      { id: 11, status: 'ENDED' },
      { id: 22, status: 'ENDED' }
    ]
    ;(axios.get as any).mockResolvedValue({ data: { items, hasMore: true, nextOffset: 15 } })

    const params = new URLSearchParams({ statuses: 'ENDED' })
    const { result } = renderHook(() => useMissionsQuery(params), { wrapper })

    await waitFor(() => expect(result.current.missions.length).toBe(2))

    expect(result.current.missions).toEqual(items)
    expect(result.current.hasNextPage).toBe(true)
    // the first page is requested at offset 0 with the default page size
    expect(axios.get).toHaveBeenCalledWith(expect.stringContaining('offset=0'))
    expect(axios.get).toHaveBeenCalledWith(expect.stringContaining(`limit=${MISSION_LIST_PAGE_SIZE}`))
    expect(axios.get).toHaveBeenCalledWith(expect.stringContaining('statuses=ENDED'))
  })

  it('runs even with no date params (dates are optional now)', async () => {
    ;(axios.get as any).mockResolvedValue({ data: { items: [], hasMore: false, nextOffset: 15 } })

    const params = new URLSearchParams()
    const { result } = renderHook(() => useMissionsQuery(params), { wrapper })

    await waitFor(() => expect(axios.get).toHaveBeenCalled())
    expect(result.current.missions).toEqual([])
    expect(result.current.hasNextPage).toBe(false)
  })

  it('does not run when offline', async () => {
    ;(useOnlineManager as any).mockReturnValue({ isOnline: false })
    ;(axios.get as any).mockResolvedValue({ data: { items: [], hasMore: false, nextOffset: 15 } })

    const params = new URLSearchParams()
    renderHook(() => useMissionsQuery(params), { wrapper })

    expect(axios.get).not.toHaveBeenCalled()
  })
})
