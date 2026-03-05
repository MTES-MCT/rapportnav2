import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { renderHook } from '@testing-library/react'
import React from 'react'
import { beforeEach, describe, expect, it } from 'vitest'
import { ActionType } from '../../types/action-type'
import { useTimelineAction } from '../use-timeline-action'

describe('useTimelineAction', () => {
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
  })

  it('should get input from mission action', () => {
    const { result } = renderHook(() => useTimelineAction('2'), { wrapper })
    const response = result.current.getActionInput(ActionType.NOTE, {})

    // When no mission data is in cache, uses current date
    expect(response.missionId).toEqual(2)
    expect(response.ownerId).toBeUndefined()
    expect(response.actionType).toEqual(ActionType.NOTE)
    expect(response.data.startDateTimeUtc).toBeDefined()
  })
})
