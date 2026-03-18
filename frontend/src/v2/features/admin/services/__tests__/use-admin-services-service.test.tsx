import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import React from 'react'
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'
import axios from '../../../../../query-client/axios'
import { renderHook, waitFor } from '../../../../../test-utils.tsx'
import useAdminServiceListQuery from '../use-admin-services-service'

vi.mock('../../../../../query-client/axios', () => ({
  default: {
    get: vi.fn()
  }
}))

describe('useAdminServiceListQuery', () => {
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
  })

  it('fetches all services when no options provided', async () => {
    const mockServices = [
      { id: 1, name: 'Service 1', serviceType: 'PAM', controlUnits: [] },
      { id: 2, name: 'Service 2', serviceType: 'ULAM', controlUnits: [], deletedAt: '2024-01-01' }
    ]

    ;(axios.get as vi.Mock).mockResolvedValue({ data: mockServices })

    const { result } = renderHook(() => useAdminServiceListQuery(), { wrapper })

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true)
    })

    expect(axios.get).toHaveBeenCalledWith('admin/services', { params: undefined })
    expect(result.current.data).toEqual(mockServices)
  })

  it('fetches only active services when active=true', async () => {
    const mockServices = [{ id: 1, name: 'Active Service', serviceType: 'PAM', controlUnits: [] }]

    ;(axios.get as vi.Mock).mockResolvedValue({ data: mockServices })

    const { result } = renderHook(() => useAdminServiceListQuery({ active: true }), { wrapper })

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true)
    })

    expect(axios.get).toHaveBeenCalledWith('admin/services', { params: { active: true } })
    expect(result.current.data).toEqual(mockServices)
  })

  it('fetches all services when active=false', async () => {
    const mockServices = [
      { id: 1, name: 'Service 1', serviceType: 'PAM', controlUnits: [] },
      { id: 2, name: 'Service 2', serviceType: 'ULAM', controlUnits: [], deletedAt: '2024-01-01' }
    ]

    ;(axios.get as vi.Mock).mockResolvedValue({ data: mockServices })

    const { result } = renderHook(() => useAdminServiceListQuery({ active: false }), { wrapper })

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true)
    })

    expect(axios.get).toHaveBeenCalledWith('admin/services', { params: { active: false } })
    expect(result.current.data).toEqual(mockServices)
  })

  it('uses different query keys for different active values', async () => {
    ;(axios.get as vi.Mock).mockResolvedValue({ data: [] })

    const { result: resultAll } = renderHook(() => useAdminServiceListQuery(), { wrapper })
    const { result: resultActive } = renderHook(() => useAdminServiceListQuery({ active: true }), { wrapper })

    await waitFor(() => {
      expect(resultAll.current.isSuccess).toBe(true)
      expect(resultActive.current.isSuccess).toBe(true)
    })

    // Both queries should be cached separately
    expect(queryClient.getQueryData(['admin-services', { active: undefined }])).toBeDefined()
    expect(queryClient.getQueryData(['admin-services', { active: true }])).toBeDefined()
  })
})
