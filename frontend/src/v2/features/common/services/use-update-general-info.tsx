import * as Sentry from '@sentry/react'
import { useMutation, UseMutationResult } from '@tanstack/react-query'
import axios from '../../../../query-client/axios'
import { Mission2, MissionGeneralInfo2 } from '../types/mission-types.ts'
import { missionsKeys } from './query-keys.ts'
import queryClient from '../../../../query-client'
import { endOfYear, startOfYear } from 'date-fns'

type UseUpdateMissionActionInput = { missionId: string; generalInfo: MissionGeneralInfo2 }

const updateGeneralInfos = ({ missionId, generalInfo }: UseUpdateMissionActionInput): Promise<MissionGeneralInfo2> =>
  axios.put(`/missions/${missionId}/general_infos`, generalInfo).then(response => response.data)

export const offlineUpdateGeneralInfoMutationDefault = {
  mutationFn: updateGeneralInfos,
  onMutate: async (input: UseUpdateMissionActionInput) => {
    const { missionId, generalInfo } = input
    const mission = queryClient.getQueryData(missionsKeys.byId(missionId))

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

    try {
      // ✅ Update any list cache that contains this mission
      queryClient.setQueriesData(
        {
          queryKey: missionsKeys.filter(
            JSON.stringify({
              startDateTimeUtc: startOfYear(mission.data.startDateTimeUtc),
              endDateTimeUtc: endOfYear(mission.data.endDateTimeUtc)
            })
          )
        }, // match all filters
        (old: Mission2[] | undefined) =>
          old?.map(m =>
            m.id.toString() === missionId
              ? {
                  ...m,
                  generalInfos: {
                    ...(m.generalInfos ?? {}),
                    ...generalInfo
                  }
                }
              : m
          )
      )
    } catch (e) {}

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
  onSettled: async (_data, _error, variables, _context) => {
    const { missionId } = variables
    await queryClient.invalidateQueries({
      queryKey: missionsKeys.byId(missionId),
      type: 'all'
    })
  },
  onError: (error: Error) => {
    console.error(error)
    Sentry.captureException(error)
  },
  scope: {
    id: `update-general-info`
  }
}

export const onlineUpdateGeneralInfoMutationDefault = {
  mutationFn: updateGeneralInfos,
  onSettled: async (_data, _error, variables, _context) => {
    await queryClient.invalidateQueries({
      queryKey: missionsKeys.byId(variables.missionId),
      type: 'all'
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
