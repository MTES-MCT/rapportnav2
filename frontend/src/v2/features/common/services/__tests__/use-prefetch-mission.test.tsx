import React from 'react'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { usePrefetchMission } from '../use-prefetch-mission'
import { missionsKeys } from '../query-keys'
import { DYNAMIC_DATA_STALE_TIME } from '../../../../../query-client'
import { act, renderHook } from '../../../../../test-utils.tsx'
import { vi } from 'vitest'

vi.mock('../use-mission', () => ({
  fetchMission: vi.fn().mockResolvedValue({ id: 123, name: 'Mock Mission' })
}))

describe('usePrefetchMission', () => {
  let queryClient: QueryClient
  let wrapper: React.FC<{ children: React.ReactNode }>

  beforeEach(() => {
    queryClient = new QueryClient()
    wrapper = ({ children }) => <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
  })

  afterEach(() => {
    vi.restoreAllMocks()
    queryClient.clear()
  })

  it('calls queryClient.prefetchQuery with the right options', async () => {
    // Spy on the QueryClient.prefetchQuery method
    const prefetchSpy = vi.spyOn(queryClient, 'prefetchQuery')

    const { result } = renderHook(() => usePrefetchMission(), { wrapper })

    // Act: prefetch mission #42
    await act(async () => {
      await result.current.prefetchMission(42)
    })

    expect(prefetchSpy).toHaveBeenCalledOnce()
    expect(prefetchSpy).toHaveBeenCalledWith({
      queryKey: missionsKeys.byId(42),
      queryFn: expect.any(Function),
      staleTime: DYNAMIC_DATA_STALE_TIME,
      networkMode: 'offlineFirst'
    })

    // And ensure our fetchMission was wired up
    const fn = prefetchSpy.mock.calls[0][0]!.queryFn!
    await expect(fn()).resolves.toEqual({ id: 123, name: 'Mock Mission' })
  })

  it('logs a warning if prefetchQuery throws', async () => {
    // Make prefetchQuery reject
    vi.spyOn(queryClient, 'prefetchQuery').mockRejectedValueOnce(new Error('network down'))
    const warnSpy = vi.spyOn(console, 'warn').mockImplementation(() => {})

    const { result } = renderHook(() => usePrefetchMission(), { wrapper })

    // Act
    await act(async () => {
      await result.current.prefetchMission(99)
    })

    // It should have caught the error and called console.warn
    expect(warnSpy).toHaveBeenCalledWith('prefetchMission(99) failed', expect.any(Error))
  })
})
