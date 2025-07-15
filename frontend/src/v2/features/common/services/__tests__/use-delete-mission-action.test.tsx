import { renderHook, waitFor } from '../../../../../test-utils.tsx'
import useDeleteActionMutation, { offlineDeleteActionMutationDefaults } from '../use-delete-mission-action.tsx'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import queryClient from '../../../../../query-client'
import React from 'react'
import { afterEach, beforeEach, vi } from 'vitest'
import axios from '../../../../../query-client/axios.ts'
import { actionsKeys, missionsKeys } from '../query-keys.ts'
import { Mission2 } from '../../types/mission-types.ts'
import { MissionAction } from '../../types/mission-action.ts'

// Mock axios
vi.mock('../../../../../query-client/axios.ts')
const mockedAxios = vi.mocked(axios)

describe('Hook useDeleteMissionAction', () => {
  let setQueryDataSpy: vi.SpyInstance
  let wrapper: React.FC<{ children: React.ReactNode }>
  const missionId = 'mission-1'
  const actionId = 'action-1'
  const mockMission: Mission2 = {
    id: missionId,
    actions: [{ id: 'action-1' } as MissionAction, { id: 'action-2' } as MissionAction]
  }

  // Test utility to properly set up queries
  const setupMissionQuery = async (queryClient: QueryClient, missionId: string, data: Mission2) => {
    await queryClient.prefetchQuery({
      queryKey: missionsKeys.byId(missionId),
      queryFn: () => Promise.resolve(data),
      staleTime: Infinity
    })
  }

  beforeEach(() => {
    setQueryDataSpy = vi.spyOn(queryClient, 'setQueryData')
    wrapper = ({ children }) => <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
    // Reset mocks
    vi.clearAllMocks()
  })

  afterEach(() => {
    queryClient.clear()
    setQueryDataSpy.mockRestore()
  })

  describe('hook behavior', () => {
    it('should call deleteAction with correct parameters', async () => {
      mockedAxios.delete.mockResolvedValue({ data: undefined })

      const { result } = renderHook(() => useDeleteActionMutation(missionId), { wrapper })

      result.current.mutate({ missionId, actionId })

      await waitFor(() => {
        expect(mockedAxios.delete).toHaveBeenCalledWith('missions/mission-1/actions/action-1')
      })
    })

    it('should handle successful mutation', async () => {
      mockedAxios.delete.mockResolvedValue({ data: undefined })

      const { result } = renderHook(() => useDeleteActionMutation(missionId), { wrapper })

      result.current.mutate({ missionId, actionId })

      await waitFor(() => {
        expect(result.current.isSuccess).toBe(true)
        expect(result.current.error).toBeNull()
      })
    })

    it('should handle failed mutation', async () => {
      const errorMessage = 'Network error'
      mockedAxios.delete.mockRejectedValue(new Error(errorMessage))

      const { result } = renderHook(() => useDeleteActionMutation(missionId), { wrapper })

      result.current.mutate({ missionId, actionId })

      await waitFor(() => {
        expect(result.current.isError).toBe(true)
        expect(result.current.error?.message).toBe(errorMessage)
      })
    })
  })

  describe('offline mutation defaults', () => {
    beforeEach(() => {
      queryClient.setMutationDefaults(actionsKeys.delete(), offlineDeleteActionMutationDefaults)
    })

    it('should perform optimistic update on mutate', async () => {
      await setupMissionQuery(queryClient, missionId, mockMission)
      mockedAxios.delete.mockImplementation(() => new Promise(() => {})) // Never resolves

      const { result } = renderHook(() => useDeleteActionMutation(missionId), { wrapper })

      result.current.mutate({ missionId, actionId })

      // Check that the action was optimistically removed
      await waitFor(() => {
        const updatedMission = queryClient.getQueryData<Mission2>(missionsKeys.byId(missionId))
        expect(updatedMission?.actions).toHaveLength(1)
        expect(updatedMission?.actions[0].id).toBe('action-2')
      })
    })
    it('should remove action from individual cache', async () => {
      await setupMissionQuery(queryClient, missionId, mockMission)

      // Set up individual action cache
      await queryClient.prefetchQuery({
        queryKey: actionsKeys.byId(actionId),
        queryFn: () => Promise.resolve({ id: actionId, title: 'Action 1' }),
        staleTime: Infinity
      })

      mockedAxios.delete.mockResolvedValue({ data: undefined })

      const { result } = renderHook(() => useDeleteActionMutation(missionId), { wrapper })

      result.current.mutate({ missionId, actionId })

      await waitFor(() => {
        const actionData = queryClient.getQueryData(actionsKeys.byId(actionId))
        expect(actionData).toBeUndefined()
      })
    })
    it('should invalidate mission queries on settle', async () => {
      const invalidateQueriesSpy = vi.spyOn(queryClient, 'invalidateQueries')

      mockedAxios.delete.mockResolvedValue({ data: undefined })

      const { result } = renderHook(() => useDeleteActionMutation(missionId), { wrapper })

      result.current.mutate({ missionId, actionId })
      await waitFor(() => {
        expect(invalidateQueriesSpy).toHaveBeenCalledWith({
          queryKey: missionsKeys.byId(missionId),
          type: 'all'
        })
      })
    })

    it('should handle rollback on error', async () => {
      await setupMissionQuery(queryClient, missionId, mockMission)

      mockedAxios.delete.mockRejectedValue(new Error('Network error'))

      const { result } = renderHook(() => useDeleteActionMutation(missionId), { wrapper })

      result.current.mutate({ missionId, actionId })

      await waitFor(() => {
        expect(result.current.isError).toBe(true)
      })

      // After error and invalidation, the original data should be restored
      await waitFor(() => {
        const mission = queryClient.getQueryData<Mission2>(missionsKeys.byId(missionId))
        expect(mission?.actions).toHaveLength(2) // Should be restored
      })
    })
  })
})
