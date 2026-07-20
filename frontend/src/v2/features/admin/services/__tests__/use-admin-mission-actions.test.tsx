import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import React from 'react'
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'
import axios from '../../../../../query-client/axios.ts'
import { renderHook, waitFor } from '../../../../../test-utils.tsx'
import useMissionActionsListQuery from '../use-admin-mission-actions.tsx'

vi.mock('../../../../../query-client/axios.ts', () => ({
  default: {
    get: vi.fn()
  }
}))

describe('useMissionActionsListQuery', () => {
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
    vi.clearAllMocks()
  })

  afterEach(() => {
    queryClient.clear()
    vi.restoreAllMocks()
  })

  it('fetches paginated mission actions and returns the response data', async () => {
    const mockData = {
      items: [{ id: 'a1', actionType: 'CONTROL' }],
      page: 0,
      pageSize: 10,
      totalItems: 1,
      totalPages: 1
    }
    ;(axios.get as unknown as vi.Mock).mockResolvedValue({ data: mockData })

    const { result } = renderHook(() => useMissionActionsListQuery(0, 10), { wrapper })

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true)
    })

    expect(result.current.data).toEqual(mockData)
    expect(axios.get).toHaveBeenCalledWith('admin/mission-actions', {
      params: { page: 0, size: 10, searchId: undefined, searchOwnerId: undefined }
    })
  })

  it('passes searchId and searchOwnerId through as query params', async () => {
    ;(axios.get as unknown as vi.Mock).mockResolvedValue({ data: { items: [] } })

    const { result } = renderHook(
      () => useMissionActionsListQuery(2, 20, 'the-id', 'the-owner'),
      { wrapper }
    )

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true)
    })

    expect(axios.get).toHaveBeenCalledWith('admin/mission-actions', {
      params: { page: 2, size: 20, searchId: 'the-id', searchOwnerId: 'the-owner' }
    })
  })

  it('sends undefined for blank search params', async () => {
    ;(axios.get as unknown as vi.Mock).mockResolvedValue({ data: { items: [] } })

    const { result } = renderHook(() => useMissionActionsListQuery(0, 10, '', ''), { wrapper })

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true)
    })

    expect(axios.get).toHaveBeenCalledWith('admin/mission-actions', {
      params: { page: 0, size: 10, searchId: undefined, searchOwnerId: undefined }
    })
  })
})
