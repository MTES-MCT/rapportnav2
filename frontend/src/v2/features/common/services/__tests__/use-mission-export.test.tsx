import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import React from 'react'
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'
import axios from '../../../../../query-client/axios'
import { act, renderHook } from '../../../../../test-utils.tsx'
import useExportMission from '../use-mission-export.tsx'

// --- MOCK axios.post to return mission list ---
vi.mock('../../../../../query-client/axios', () => ({
  default: {
    post: vi.fn()
  }
}))

describe('useMissionExport', () => {
  let queryClient: QueryClient
  let wrapper: React.FC<{ children: React.ReactNode }>

  beforeEach(() => {
    // fresh QueryClient per test
    queryClient = new QueryClient({
      defaultOptions: {
        queries: {
          retry: false
        },
        mutations: {
          retry: false
        }
      }
    })

    wrapper = ({ children }) => <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>

    // Reset mocks
    vi.clearAllMocks()
  })

  afterEach(() => {
    queryClient.clear()
    vi.restoreAllMocks()
  })

  it('calls axios with the expected body', async () => {
    const mockPost = vi.mocked(axios.post)
    mockPost.mockResolvedValue({ data: {} })

    const { result } = renderHook(() => useExportMission(), { wrapper })

    await act(async () => {
      await result.current.mutateAsync({
        data: {
          missionIds: [1],
          exportMode: 1,
          reportType: 2
        }
      })
    })

    expect(mockPost).toHaveBeenCalledWith('missions/export', {
      data: {
        missionIds: [1],
        exportMode: 1,
        reportType: 2
      }
    })
  })
})
