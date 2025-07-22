import { act, renderHook, waitFor } from '../../../../../test-utils.tsx'
import useCreateMissionActionMutation, { offlineCreateMissionActionDefaults } from '../use-create-mission-action.tsx'
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

describe('Hook useCreateMissionActionMutation', () => {
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
      { id: 'action-3', data: { startDateTimeUtc: '2024-01-01T09:30:00Z' } } as any as MissionAction,
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
      mockedAxios.post.mockResolvedValue({ data: undefined })

      const { result } = renderHook(() => useCreateMissionActionMutation(missionId), { wrapper })

      // Fixed: Pass the correct input format
      result.current.mutate({ missionId, action })

      await waitFor(() => {
        expect(mockedAxios.post).toHaveBeenCalledWith('missions/mission-1/actions', action)
      })
    })

    it('should handle successful mutation', async () => {
      mockedAxios.post.mockResolvedValue({ data: undefined })

      const { result } = renderHook(() => useCreateMissionActionMutation(missionId), { wrapper })

      result.current.mutate({ missionId, action })

      await waitFor(() => {
        expect(result.current.isSuccess).toBe(true)
        expect(result.current.error).toBeNull()
      })
    })

    it('should handle failed mutation', async () => {
      const errorMessage = 'Network error'
      mockedAxios.post.mockRejectedValue(new Error(errorMessage))

      const { result } = renderHook(() => useCreateMissionActionMutation(missionId), { wrapper })

      result.current.mutate({ missionId, action })

      await waitFor(() => {
        expect(result.current.isError).toBe(true)
        expect(result.current.error?.message).toBe(errorMessage)
      })
    })
  })

  describe('offline mutation defaults', () => {
    beforeEach(async () => {
      // Set mutation defaults BEFORE setting up queries
      queryClient.setMutationDefaults(actionsKeys.create(), offlineCreateMissionActionDefaults)

      await setupMissionQuery(queryClient, missionId, mockMission)
      await setupMissionActionQuery(queryClient, actionId, mockAction)
    })

    describe('onMutate behavior', () => {
      it('should create optimistic action when online', async () => {
        mockedAxios.post.mockImplementation(() => new Promise(() => {})) // Never resolves

        const { result } = renderHook(() => useCreateMissionActionMutation(missionId), { wrapper })

        await act(async () => {
          result.current.mutate({ missionId, action: mockAction })
          // Wait for the mutation to start and onMutate to execute
          await new Promise(resolve => setTimeout(resolve, 50))
        })

        // Wait for optimistic update to complete
        await waitFor(
          () => {
            const updatedAction = queryClient.getQueryData<MissionAction>(actionsKeys.byId(actionId))
            expect(updatedAction?.data?.startDateTimeUtc).toBe(mockAction.data.startDateTimeUtc)
            expect(updatedAction?.networkSyncStatus).toBe(NetworkSyncStatus.SYNC)
          },
          { timeout: 2000 }
        )
      })

      it('should create optimistic action when offline', async () => {
        const isOnlineSpy = vi.spyOn(onlineManager, 'isOnline').mockReturnValue(false)
        mockedAxios.post.mockImplementation(() => new Promise(() => {})) // Never resolves

        const { result } = renderHook(() => useCreateMissionActionMutation(missionId), { wrapper })

        await act(async () => {
          result.current.mutate({ missionId, action: mockAction })
          // Wait for the mutation to start and onMutate to execute
          await new Promise(resolve => setTimeout(resolve, 50))
        })

        await waitFor(
          () => {
            const updatedAction = queryClient.getQueryData<MissionAction>(actionsKeys.byId(actionId))
            expect(updatedAction?.data?.startDateTimeUtc).toBe(mockAction.data.startDateTimeUtc)
            expect(updatedAction?.networkSyncStatus).toBe(NetworkSyncStatus.UNSYNC)
          },
          { timeout: 2000 }
        )

        isOnlineSpy.mockRestore()
      })

      it('should set individual action query data', async () => {
        mockedAxios.post.mockImplementation(() => new Promise(() => {}))

        const { result } = renderHook(() => useCreateMissionActionMutation(missionId), { wrapper })

        await act(async () => {
          result.current.mutate({ missionId, action: mockAction })
          // Wait for the mutation to start and onMutate to execute
          await new Promise(resolve => setTimeout(resolve, 50))
        })

        await waitFor(
          () => {
            const actionData = queryClient.getQueryData(actionsKeys.byId(actionId))
            expect(actionData).toBeDefined()
            expect((actionData as MissionNavAction).id).toBe(actionId)
          },
          { timeout: 2000 }
        )
      })

      describe('onSuccess behavior', () => {
        beforeEach(() => {
          vi.clearAllMocks()
          invalidateQueriesSpy.mockClear()
        })

        it('should invalidate action query on success', async () => {
          mockedAxios.post.mockResolvedValue({ data: mockAction })

          const { result } = renderHook(() => useCreateMissionActionMutation(missionId), { wrapper })

          await act(async () => {
            result.current.mutate({ missionId, action: mockAction })
          })

          // Wait for mutation to complete
          await waitFor(
            () => {
              expect(result.current.isSuccess).toBe(true)
            },
            { timeout: 2000 }
          )

          // Check that onSuccess invalidated the action query
          await waitFor(
            () => {
              expect(invalidateQueriesSpy).toHaveBeenCalled()
            },
            { timeout: 2000 }
          )
        })

        it('should call onSuccess with correct parameters', async () => {
          const serverResponse = { data: mockAction }
          mockedAxios.post.mockResolvedValue(serverResponse)

          const onSuccessSpy = vi.fn()
          const customDefaults = {
            ...offlineCreateMissionActionDefaults,
            onSuccess: onSuccessSpy
          }
          queryClient.setMutationDefaults(actionsKeys.create(), customDefaults)

          const { result } = renderHook(() => useCreateMissionActionMutation(missionId), { wrapper })

          await act(async () => {
            result.current.mutate({ missionId, action: mockAction })
          })

          await waitFor(
            () => {
              expect(result.current.isSuccess).toBe(true)
              expect(onSuccessSpy).toHaveBeenCalledWith(
                serverResponse,
                { missionId, action: mockAction },
                expect.objectContaining({ action: expect.any(Object) })
              )
            },
            { timeout: 2000 }
          )
        })
      })

      describe('onError behavior', () => {
        beforeEach(() => {
          vi.clearAllMocks()
          invalidateQueriesSpy.mockClear()
        })

        it('should remove optimistic action query on error', async () => {
          const removeQueriesSpy = vi.spyOn(queryClient, 'removeQueries')
          mockedAxios.post.mockRejectedValue(new Error('Network error'))

          const { result } = renderHook(() => useCreateMissionActionMutation(missionId), { wrapper })

          await act(async () => {
            result.current.mutate({ missionId, action: mockAction })
          })

          // Wait for mutation to fail
          await waitFor(
            () => {
              expect(result.current.isError).toBe(true)
            },
            { timeout: 2000 }
          )

          // Check that onError removed the optimistic action query
          await waitFor(
            () => {
              expect(removeQueriesSpy).toHaveBeenCalled()
            },
            { timeout: 2000 }
          )

          removeQueriesSpy.mockRestore()
        })

        it('should call onError with correct parameters', async () => {
          const error = new Error('Network error')
          mockedAxios.post.mockRejectedValue(error)

          const onErrorSpy = vi.fn()
          const customDefaults = {
            ...offlineCreateMissionActionDefaults,
            onError: onErrorSpy
          }
          queryClient.setMutationDefaults(actionsKeys.create(), customDefaults)

          const { result } = renderHook(() => useCreateMissionActionMutation(missionId), { wrapper })

          await act(async () => {
            result.current.mutate({ missionId, action: mockAction })
          })

          await waitFor(
            () => {
              expect(result.current.isError).toBe(true)
              expect(onErrorSpy).toHaveBeenCalledWith(
                error,
                { missionId, action: mockAction },
                expect.objectContaining({ action: expect.any(Object) })
              )
            },
            { timeout: 2000 }
          )
        })
      })

      describe('onSettled behavior', () => {
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
          mockedAxios.post.mockResolvedValue({ data: mockAction })

          const { result } = renderHook(() => useCreateMissionActionMutation(missionId), { wrapper })

          await act(async () => {
            result.current.mutate({ missionId, action: mockAction })
          })

          // Wait for mutation to complete
          await waitFor(
            () => {
              expect(result.current.isSuccess).toBe(true)
            },
            { timeout: 2000 }
          )

          // Wait for invalidation to complete
          await waitFor(
            () => {
              expect(invalidateQueriesSpy).toHaveBeenCalledWith({
                queryKey: missionsKeys.byId(missionId),
                type: 'all'
              })
            },
            { timeout: 2000 }
          )
        })
      })
    })
  })
})
