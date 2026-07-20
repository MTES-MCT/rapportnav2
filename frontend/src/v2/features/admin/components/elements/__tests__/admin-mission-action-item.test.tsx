import React from 'react'
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'
import { act, fireEvent, render, screen } from '../../../../../../test-utils.tsx'
import AdminMissionActionItem from '../admin-mission-action-item.tsx'
import useMissionActionsListQuery from '../../../services/use-admin-mission-actions.tsx'

vi.mock('../../../services/use-admin-mission-actions.tsx', () => ({
  default: vi.fn()
}))

const mockedHook = vi.mocked(useMissionActionsListQuery)

const pageWith = (items: any[]) => ({
  data: {
    items,
    page: 0,
    pageSize: 10,
    totalItems: items.length,
    totalPages: items.length > 0 ? 1 : 0
  }
})

describe('AdminMissionActionItem', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    mockedHook.mockReturnValue(pageWith([]) as any)
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  it('renders the title and both search inputs', () => {
    render(<AdminMissionActionItem />)

    expect(screen.getByText('Mission Actions')).toBeInTheDocument()
    expect(screen.getByText('Rechercher par id (UUID)')).toBeInTheDocument()
    expect(screen.getByText('Rechercher par ownerId (UUID)')).toBeInTheDocument()
    expect(screen.getAllByRole('textbox')).toHaveLength(2)
  })

  it('renders a row from the query data', () => {
    mockedHook.mockReturnValue(
      pageWith([
        {
          id: 'action-uuid-1',
          ownerId: 'owner-uuid-1',
          startDateTimeUtc: '2025-01-02T00:00:00Z',
          endDateTimeUtc: '2025-01-02T02:00:00Z',
          actionType: 'CONTROL',
          status: 'ANCHORED',
          isCompleteForStats: true
        }
      ]) as any
    )

    render(<AdminMissionActionItem />)

    expect(screen.getByText('action-uuid-1')).toBeInTheDocument()
    expect(screen.getByText('CONTROL')).toBeInTheDocument()
    expect(screen.getByText('ANCHORED')).toBeInTheDocument()
  })

  it('queries the first page with no search on initial render', () => {
    render(<AdminMissionActionItem />)

    expect(mockedHook).toHaveBeenCalledWith(0, 10, undefined, undefined)
  })

  it('debounces the id search and re-queries with the typed value', () => {
    vi.useFakeTimers()
    try {
      render(<AdminMissionActionItem />)

      const [idInput] = screen.getAllByRole('textbox')
      fireEvent.change(idInput, { target: { value: 'search-id-value' } })

      act(() => {
        vi.advanceTimersByTime(500)
      })

      expect(mockedHook).toHaveBeenLastCalledWith(0, 10, 'search-id-value', undefined)
    } finally {
      vi.useRealTimers()
    }
  })

  it('debounces the ownerId search and re-queries with the typed value', () => {
    vi.useFakeTimers()
    try {
      render(<AdminMissionActionItem />)

      const ownerInput = screen.getAllByRole('textbox')[1]
      fireEvent.change(ownerInput, { target: { value: 'search-owner-value' } })

      act(() => {
        vi.advanceTimersByTime(500)
      })

      expect(mockedHook).toHaveBeenLastCalledWith(0, 10, undefined, 'search-owner-value')
    } finally {
      vi.useRealTimers()
    }
  })
})
