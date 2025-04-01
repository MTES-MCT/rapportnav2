import { act, renderHook, waitFor } from '../../../../../test-utils.tsx'
import useUpdateGeneralInfoMutation, { offlineUpdateGeneralInfoMutationDefault } from '../use-update-general-info.tsx'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import queryClient from '../../../../../query-client'
import React from 'react'
import { afterEach, beforeEach, vi } from 'vitest'
import axios from '../../../../../query-client/axios.ts'
import { missionsKeys } from '../query-keys.ts'
import { Mission2, MissionGeneralInfo2 } from '../../types/mission-types.ts'
import { MissionAction } from '../../types/mission-action.ts'

// Mock axios
vi.mock('../../../../../query-client/axios.ts')
const mockedAxios = vi.mocked(axios)

describe('Hook useUpdateGeneralInfoMutation', () => {
  let wrapper: React.FC<{ children: React.ReactNode }>
  const missionId = 'mission-1'
  const mockMission: Mission2 = {
    id: missionId,
    generalInfos: { distanceInNauticalMiles: 1 } as MissionGeneralInfo2,
    actions: [{ id: 'action-1' } as MissionAction, { id: 'action-2' } as MissionAction]
  }
  const mockGeneralInfo = {
    distanceInNauticalMiles: 3
  } as MissionGeneralInfo2

  // Test utility to properly set up queries
  const setupMissionQuery = async (queryClient: QueryClient, missionId: string, data: Mission2) => {
    await queryClient.prefetchQuery({
      queryKey: missionsKeys.byId(missionId),
      queryFn: () => Promise.resolve(data),
      staleTime: Infinity
    })
  }

  beforeEach(() => {
    wrapper = ({ children }) => <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
    // Reset mocks
    vi.clearAllMocks()
  })

  afterEach(() => {
    queryClient.clear()
  })

  describe('hook behavior', () => {
    it('should call putAction with correct parameters', async () => {
      mockedAxios.put.mockResolvedValue({ data: undefined })

      const { result } = renderHook(() => useUpdateGeneralInfoMutation(missionId), { wrapper })

      result.current.mutate({ missionId, generalInfo: mockGeneralInfo })

      await waitFor(() => {
        expect(mockedAxios.put).toHaveBeenCalledWith('/missions/mission-1/general_infos', mockGeneralInfo)
      })
    })

    it('should handle successful mutation', async () => {
      mockedAxios.put.mockResolvedValue({ data: undefined })

      const { result } = renderHook(() => useUpdateGeneralInfoMutation(missionId), { wrapper })

      result.current.mutate({ missionId, generalInfo: mockGeneralInfo })

      await waitFor(() => {
        expect(result.current.isSuccess).toBe(true)
        expect(result.current.error).toBeNull()
      })
    })

    it('should handle failed mutation', async () => {
      const errorMessage = 'Network error'
      mockedAxios.put.mockRejectedValue(new Error(errorMessage))

      const { result } = renderHook(() => useUpdateGeneralInfoMutation(missionId), { wrapper })

      result.current.mutate({ missionId, generalInfo: mockGeneralInfo })

      await waitFor(() => {
        expect(result.current.isError).toBe(true)
        expect(result.current.error?.message).toBe(errorMessage)
      })
    })
  })

  describe('offline mutation defaults', () => {
    beforeEach(async () => {
      queryClient.setMutationDefaults(missionsKeys.update(), offlineUpdateGeneralInfoMutationDefault)
      await setupMissionQuery(queryClient, missionId, mockMission)
    })

    it('should perform optimistic update on mutate', async () => {
      const setQueryDataSpy = vi.spyOn(queryClient, 'setQueryData')
      mockedAxios.put.mockImplementation(() => new Promise(() => {})) // Never resolves

      const { result } = renderHook(() => useUpdateGeneralInfoMutation(missionId), { wrapper })

      await act(async () => {
        result.current.mutate({ missionId, generalInfo: mockGeneralInfo })
        await Promise.resolve()
      })
      expect(setQueryDataSpy).toHaveBeenCalled()

      // Check that the action was optimistically removed
      await waitFor(() => {
        const updatedMission = queryClient.getQueryData<Mission2>(missionsKeys.byId(missionId))
        expect(updatedMission?.generalInfos.distanceInNauticalMiles).toEqual(mockGeneralInfo.distanceInNauticalMiles)
      })
    })

    it('should invalidate mission queries on settle', async () => {
      const invalidateQueriesSpy = vi.spyOn(queryClient, 'invalidateQueries')

      mockedAxios.put.mockResolvedValue({ data: undefined })

      const { result } = renderHook(() => useUpdateGeneralInfoMutation(missionId), { wrapper })

      result.current.mutate({ missionId, generalInfo: mockGeneralInfo })
      await waitFor(() => {
        expect(invalidateQueriesSpy).toHaveBeenCalledWith({
          queryKey: missionsKeys.byId(missionId),
          type: 'all'
        })
      })
    })

    it('should handle rollback on error', async () => {
      mockedAxios.put.mockRejectedValue(new Error('Network error'))

      const { result } = renderHook(() => useUpdateGeneralInfoMutation(missionId), { wrapper })

      result.current.mutate({ missionId, generalInfo: mockGeneralInfo })

      await waitFor(() => {
        expect(result.current.isError).toBe(true)
      })

      // After error and invalidation, the original data should be restored
      await waitFor(() => {
        const mission = queryClient.getQueryData<Mission2>(missionsKeys.byId(missionId))
        expect(mission?.generalInfos.distanceInNauticalMiles).toEqual(mockMission.generalInfos.distanceInNauticalMiles) // Should be restored
      })
    })
  })
})
