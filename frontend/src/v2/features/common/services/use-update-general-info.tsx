import { logSoftError } from '@mtes-mct/monitor-ui'
import * as Sentry from '@sentry/react'
import { useMutation, UseMutationResult } from '@tanstack/react-query'
import axios from '../../../../query-client/axios'
import { Mission2, MissionGeneralInfo2 } from '../types/mission-types.ts'
import { missionsKeys } from './query-keys.ts'
import queryClient from '../../../../query-client'

type UseUpdateMissionActionInput = { missionId: string; generalInfo: MissionGeneralInfo2 }

const updateGeneralInfos = ({ missionId, generalInfo }: UseUpdateMissionActionInput): Promise<MissionGeneralInfo2> =>
  axios.put(`/missions/${missionId}/general_infos`, generalInfo).then(response => response.data)

export const offlineUpdateGeneralInfoMutationDefault = {
  mutationFn: updateGeneralInfos,
  onMutate: async (input: UseUpdateMissionActionInput) => {
    const { missionId, generalInfo } = input

    await queryClient.cancelQueries({ queryKey: missionsKeys.byId(missionId) })

    // update general infos in cache for mission
    queryClient.setQueryData<Mission2>(missionsKeys.byId(missionId), old => {
      if (!old) return old
      return {
        ...old,
        generalInfos: {
          ...(old.generalInfos ?? {}),
          ...generalInfo
        }
      }
    })

    // ✅ Cancel existing mutations with same mission id
    const cleanupMutations = (mutationKey, missionId, keepLatest = false) => {
      const mutations = queryClient
        .getMutationCache()
        .findAll({ mutationKey })
        .filter(m => {
          return m.state.status === 'pending' && m.state.variables?.missionId === missionId
        })

      const toCleanup = keepLatest ? mutations.sort((a, b) => b.mutationId - a.mutationId).slice(1) : mutations
      toCleanup.forEach(m => (m.destroy?.(), mutationCache.remove(m as any)))
    }

    const mutationCache = queryClient.getMutationCache()
    cleanupMutations(missionsKeys.update(), missionId, true)
  },
  onSuccess: async (serverResponse, data, _context) => {
    const { missionId, missionIdUUID } = serverResponse.data
    await queryClient.invalidateQueries({
      queryKey: missionsKeys.byId(missionIdUUID ?? missionId),
      type: 'all'
    })
  },
  onError: (error: any) => {
    console.error(error)
    Sentry.captureException(error)
    const problemDetail = error?.problemDetail
    const userMessage = error.message || problemDetail?.detail || `Une erreur s'est produite lors de l'enregistrement.`
    logSoftError({
      isSideWindowError: false,
      message: error.message,
      userMessage
    })
  },
  scope: {
    id: `update-general-info`
  }
}

export const onlineUpdateGeneralInfoMutationDefault = {
  mutationFn: updateGeneralInfos,
  onSuccess: async (serverResponse, data, _context) => {
    const { missionId, missionIdUUID } = serverResponse.data
    await queryClient.invalidateQueries({
      queryKey: missionsKeys.byId(missionIdUUID ?? missionId),
      type: 'all'
    })
  },
  onError: (error: any) => {
    console.error(error)
    Sentry.captureException(error)
    const problemDetail = error?.problemDetail
    const userMessage = error.message || problemDetail?.detail || `Une erreur s'est produite lors de l'enregistrement.`
    logSoftError({
      isSideWindowError: false,
      message: error.message,
      userMessage
    })
  },
  scope: {
    id: `update-general-info`
  }
}

const useUpdateGeneralInfoMutation = (): UseMutationResult<
  MissionGeneralInfo2,
  Error,
  MissionGeneralInfo2,
  unknown
> => {
  const mutation = useMutation({
    mutationKey: missionsKeys.update(),
    mutationFn: updateGeneralInfos
  })
  return mutation
}

export default useUpdateGeneralInfoMutation
