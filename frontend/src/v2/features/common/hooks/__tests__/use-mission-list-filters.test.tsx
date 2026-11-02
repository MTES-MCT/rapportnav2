import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { act, renderHook, waitFor } from '@testing-library/react'
import React from 'react'
import { MemoryRouter } from 'react-router'
import { describe, expect, it, vi } from 'vitest'
import { missionsKeys } from '../../services/query-keys'
import { useMissionListFilters } from '../use-mission-list-filters'

// Wrap the hook in the router + query contexts it depends on, seeding the URL with `initialEntry`.
const buildWrapper = (initialEntry: string, queryClient: QueryClient) => {
  return ({ children }: { children: React.ReactNode }) => (
    <QueryClientProvider client={queryClient}>
      <MemoryRouter initialEntries={[initialEntry]}>{children}</MemoryRouter>
    </QueryClientProvider>
  )
}

describe('useMissionListFilters', () => {
  it('exposes the current search params and flags active filters', () => {
    const { result } = renderHook(() => useMissionListFilters(), {
      wrapper: buildWrapper('/?statuses=ENDED', new QueryClient())
    })

    expect(result.current.filtersActive).toBe(true)
    expect(result.current.searchParams.getAll('statuses')).toEqual(['ENDED'])
  })

  it('reports no active filters when none are set', () => {
    const { result } = renderHook(() => useMissionListFilters(), {
      wrapper: buildWrapper('/', new QueryClient())
    })

    expect(result.current.filtersActive).toBe(false)
  })

  it('resetFilters clears every filter param from the URL', async () => {
    const { result } = renderHook(() => useMissionListFilters(), {
      wrapper: buildWrapper(
        '/?statuses=ENDED&completenessStatuses=VALID&dateMode=CURRENT_MONTH&startDateTimeUtc=x&endDateTimeUtc=y',
        new QueryClient()
      )
    })

    expect(result.current.filtersActive).toBe(true)

    act(() => result.current.resetFilters())

    await waitFor(() => expect(result.current.filtersActive).toBe(false))
    expect(result.current.searchParams.toString()).toBe('')
  })

  it('resetFilters invalidates the missions cache so the default list refetches', () => {
    const queryClient = new QueryClient()
    const invalidateSpy = vi.spyOn(queryClient, 'invalidateQueries')

    const { result } = renderHook(() => useMissionListFilters(), {
      wrapper: buildWrapper('/?statuses=ENDED', queryClient)
    })

    act(() => result.current.resetFilters())

    expect(invalidateSpy).toHaveBeenCalledWith({ queryKey: missionsKeys.all() })
  })
})
