import { act, renderHook, waitFor } from '../../../../../test-utils.tsx'
import useUpdateMissionAction, { offlineUpdateActionDefaults } from '../use-update-action.tsx'
import { onlineManager, QueryClient, QueryClientProvider } from '@tanstack/react-query'
import React from 'react'
import { afterEach, beforeEach, vi } from 'vitest'
import axios from '../../../../../query-client/axios.ts'
import { actionsKeys, missionsKeys } from '../query-keys.ts'
import { Mission2 } from '../../types/mission-types.ts'
import { MissionAction, MissionNavAction } from '../../types/mission-action.ts'
import queryClient from '../../../../../query-client'
import { NetworkSyncStatus } from '../../types/network-types.ts'

// Mock axios
vi.mock('../../../../../query-client/axios.ts')
const mockedAxios = vi.mocked(axios)

const setupMissionQuery = async (queryClient: QueryClient, missionId: string, data: Mission2) => {
  await queryClient.prefetchQuery({
    queryKey: missionsKeys.byId(missionId),
    queryFn: () => Promise.resolve(data),
    staleTime: Infinity
  })
}
const setupMissionActionQuery = async (queryClient: QueryClient, actionId: string, data: MissionAction) => {
  await queryClient.prefetchQuery({
    queryKey: actionsKeys.byId(actionId),
    queryFn: () => Promise.resolve(data),
    staleTime: Infinity
  })
}

describe('Hook useUpdateMissionAction', () => {
  const missionId = 'mission-1'
  const actionId = 'action-1'
  const action = { id: actionId }
  const mockAction: MissionAction = {
    id: actionId,
    data: { startDateTimeUtc: '2024-01-01T10:00:00Z' }
  }
  const mockMission: Mission2 = {
    id: missionId,
    actions: [
      { id: actionId, data: { startDateTimeUtc: '2024-01-01T09:30:00Z' } } as any as MissionAction,
      { id: 'action-2', data: { startDateTimeUtc: '2024-01-01T09:00:00Z' } } as any as MissionAction
    ]
  }

  let wrapper: React.FC<{ children: React.ReactNode }>
  let invalidateQueriesSpy: vi.SpyInstance

  beforeEach(async () => {
    invalidateQueriesSpy = vi.spyOn(queryClient, 'invalidateQueries')

    wrapper = ({ children }) => <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>

    // Reset mocks
    vi.clearAllMocks()

    // Wait for any pending mutations to complete
    await queryClient.getMutationCache().clear()

    // Reset online manager state
    onlineManager.setOnline(true)
  })

  afterEach(async () => {
    // Cancel all ongoing mutations
    queryClient.getMutationCache().clear()

    // Clear all queries
    queryClient.clear()

    // Wait for any pending async operations
    await act(async () => {
      await new Promise(resolve => setTimeout(resolve, 0))
    })

    invalidateQueriesSpy.mockRestore()

    // Reset all mocks
    vi.clearAllMocks()
  })

  describe('hook behavior', () => {
    it('should call putAction with correct parameters', async () => {
      mockedAxios.put.mockResolvedValue({ data: undefined })

      const { result } = renderHook(() => useUpdateMissionAction(), { wrapper })

      result.current.mutate({ ownerId: missionId, action })

      await waitFor(() => {
        expect(mockedAxios.put).toHaveBeenCalledWith('owners/mission-1/actions/action-1', action)
      })
    })

    it('should handle successful mutation', async () => {
      mockedAxios.put.mockResolvedValue({ data: undefined })

      const { result } = renderHook(() => useUpdateMissionAction(), { wrapper })

      result.current.mutate({ ownerId: missionId, action })

      await waitFor(() => {
        expect(result.current.isSuccess).toBe(true)
        expect(result.current.error).toBeNull()
      })
    })

    it('should handle failed mutation', async () => {
      const errorMessage = 'Network error'
      mockedAxios.put.mockRejectedValue(new Error(errorMessage))

      const { result } = renderHook(() => useUpdateMissionAction(), { wrapper })

      result.current.mutate({ ownerId: missionId, action })

      await waitFor(() => {
        expect(result.current.isError).toBe(true)
        expect(result.current.error?.message).toBe(errorMessage)
      })
    })
  })

  describe('offline mutation defaults', () => {
    beforeEach(async () => {
      queryClient.setMutationDefaults(actionsKeys.update(), offlineUpdateActionDefaults)
      await setupMissionQuery(queryClient, missionId, mockMission)
      await setupMissionActionQuery(queryClient, actionId, mockAction)
    })

    describe('onMutate behavior', () => {
      it('should create optimistic action when online', async () => {
        mockedAxios.put.mockImplementation(() => new Promise(() => {})) // Never resolves

        const { result } = renderHook(() => useUpdateMissionAction(missionId, actionId), { wrapper })

        await act(async () => {
          result.current.mutate({ ownerId: missionId, action: mockAction })
          await new Promise(resolve => setTimeout(resolve, 0))
        })

        await waitFor(() => {
          const updatedAction = queryClient.getQueryData<MissionAction>(actionsKeys.byId(actionId))
          expect(updatedAction?.data?.startDateTimeUtc).toBe(mockAction.data.startDateTimeUtc)
          const updatedMission = queryClient.getQueryData<Mission2>(missionsKeys.byId(missionId))
          const updatedActionInMission = updatedMission?.actions.find(a => a.id === actionId)
          expect(updatedActionInMission?.data?.startDateTimeUtc).toBe(mockAction.data.startDateTimeUtc)
          expect(updatedActionInMission?.networkSyncStatus).toBe(NetworkSyncStatus.SYNC)
        })
      })

      it('should create optimistic action when offline', async () => {
        const isOnlineSpy = vi.spyOn(onlineManager, 'isOnline').mockReturnValue(false)
        mockedAxios.put.mockImplementation(() => new Promise(() => {})) // Never resolves

        const { result } = renderHook(() => useUpdateMissionAction(missionId, actionId), { wrapper })

        await act(async () => {
          result.current.mutate({ ownerId: missionId, action: mockAction })
          await new Promise(resolve => setTimeout(resolve, 0))
        })

        await waitFor(() => {
          const updatedAction = queryClient.getQueryData<MissionAction>(actionsKeys.byId(actionId))
          expect(updatedAction?.data?.startDateTimeUtc).toBe(mockAction.data.startDateTimeUtc)
          const updatedMission = queryClient.getQueryData<Mission2>(missionsKeys.byId(missionId))
          const updatedActionInMission = updatedMission?.actions.find(a => a.id === actionId)
          expect(updatedActionInMission?.data?.startDateTimeUtc).toBe(mockAction.data.startDateTimeUtc)
          expect(updatedActionInMission?.networkSyncStatus).toBe(NetworkSyncStatus.UNSYNC)
        })

        isOnlineSpy.mockRestore()
      })

      it('should set individual action query data', async () => {
        mockedAxios.put.mockImplementation(() => new Promise(() => {}))

        const { result } = renderHook(() => useUpdateMissionAction(missionId, actionId), { wrapper })

        result.current.mutate({ ownerId: missionId, action: mockAction })

        await waitFor(() => {
          const actionData = queryClient.getQueryData(actionsKeys.byId(actionId))
          expect(actionData).toBeDefined()
          expect((actionData as MissionNavAction).id).toBe(actionId)
        })
      })

      it('should cancel pending create mutations with same action id', async () => {
        await setupMissionQuery(queryClient, missionId, mockMission)

        // Create a mock pending create mutation
        const mutationCache = queryClient.getMutationCache()
        const mockMutation = {
          state: {
            status: 'pending',
            variables: { action: { id: actionId } }
          },
          destroy: vi.fn(),
          options: { mutationKey: actionsKeys.create() }
        }

        const findAllSpy = vi.spyOn(mutationCache, 'findAll').mockReturnValue([mockMutation as any])
        const removeSpy = vi.spyOn(mutationCache, 'remove').mockImplementation(() => {})

        mockedAxios.put.mockImplementation(() => new Promise(() => {}))

        const { result } = renderHook(() => useUpdateMissionAction(missionId, actionId), { wrapper })

        await act(async () => {
          result.current.mutate({ ownerId: missionId, action: mockAction })
          await new Promise(resolve => setTimeout(resolve, 0))
        })

        await waitFor(() => {
          expect(findAllSpy).toHaveBeenCalledWith({ mutationKey: actionsKeys.create() })
          expect(mockMutation.destroy).toHaveBeenCalled()
          expect(removeSpy).toHaveBeenCalledWith(mockMutation)
        })

        findAllSpy.mockRestore()
        removeSpy.mockRestore()
      })
    })

    describe('onSettled behavior', () => {
      // Add individual beforeEach/afterEach for better isolation
      beforeEach(() => {
        vi.clearAllMocks()
        invalidateQueriesSpy.mockClear()
      })

      afterEach(async () => {
        // Wait for any pending invalidations to complete
        await act(async () => {
          await new Promise(resolve => setTimeout(resolve, 10))
        })
      })

      it('should invalidate action and mission queries on success', async () => {
        mockedAxios.put.mockResolvedValue({ data: mockAction })

        const { result } = renderHook(() => useUpdateMissionAction(missionId, actionId), { wrapper })

        await act(async () => {
          result.current.mutate({ ownerId: missionId, action: mockAction })
        })

        await waitFor(() => {
          expect(result.current.isSuccess).toBe(true)
        })

        // Wait a bit longer for invalidation to complete
        await waitFor(() => {
          expect(invalidateQueriesSpy).toHaveBeenCalledWith({
            queryKey: actionsKeys.byId(actionId),
            refetchType: 'active'
          })
          expect(invalidateQueriesSpy).toHaveBeenCalledWith({
            queryKey: missionsKeys.byId(missionId),
            type: 'all'
          })
        })
      })

      it('should handle invalidation errors gracefully', async () => {
        mockedAxios.put.mockResolvedValue({ data: mockAction })
        invalidateQueriesSpy.mockRejectedValueOnce(new Error('Invalidation error'))

        const { result } = renderHook(() => useUpdateMissionAction(missionId, actionId), { wrapper })

        await act(async () => {
          result.current.mutate({ ownerId: missionId, action: mockAction })
        })

        await waitFor(() => {
          expect(result.current.isSuccess).toBe(true)
        })
      })

      it('should invalidate queries on error', async () => {
        mockedAxios.put.mockRejectedValue(new Error('Network error'))

        const { result } = renderHook(() => useUpdateMissionAction(missionId, actionId), { wrapper })

        await act(async () => {
          result.current.mutate({ ownerId: missionId, action: mockAction })
        })

        await waitFor(() => {
          expect(result.current.isError).toBe(true)
        })

        await waitFor(() => {
          expect(invalidateQueriesSpy).toHaveBeenCalledWith({
            queryKey: actionsKeys.byId(actionId),
            refetchType: 'active'
          })
        })
      })
    })
  })
})
