import { missionsKeys } from '../missions/queries'
import { Mission } from '../mission-types'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { authenticatedHttpClient } from '../../http-client'

export const missionKeys = {
  details: () => [...missionsKeys.all, 'detail'] as const,
  detail: (id: number) => [...missionKeys.details(), id] as const
}

export const fetchMission = (id: number) => authenticatedHttpClient.get(`/api/v1/missions/${id}`).json()
export const updateMission = (id: number, mission: Mission) =>
  authenticatedHttpClient.put(`/api/v1/missions/${id}`, { json: mission }).json()

export const useMutateMission = (missionId: number, successCallback?: () => void, errorCallback?: () => void) => {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (newMission: Mission) => updateMission(missionId, newMission),
    onSuccess: async (newMission: any) => {
      await queryClient.cancelQueries({
        queryKey: missionKeys.detail(missionId)
      })
      const previousMission = queryClient.getQueryData(missionKeys.detail(missionId))
      queryClient.setQueryData(missionKeys.detail(missionId), newMission)

      // ✅ update the list we are currently on instantly
      queryClient.setQueryData(missionsKeys.lists(), (previous: any) => {
        previous.map((mission: any) => (mission.id === missionId ? newMission : mission))
      })

      if (successCallback) {
        successCallback()
      }
      // Return a context with the previous and new mission
      return { previousMission, newMission }
    },
    // If the mutation fails, use the context we returned above
    onError: (err, newMission, context: any) => {
      // queryClient.setQueryData(missionKeys.detail(missionId), context.previousMission,)
      if (errorCallback) {
        errorCallback()
      }
    },
    onSettled: (newMission: any) => {
      debugger
      // Always refetch after error or success:
      queryClient.invalidateQueries({ queryKey: missionsKeys.lists() })
      queryClient.invalidateQueries({
        queryKey: missionKeys.detail(missionId)
      })
    }
  })
}
