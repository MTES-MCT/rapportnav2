import { useQueryClient } from '@tanstack/react-query'
import { missionsKeys } from '../services/query-keys.ts'
import { Mission2 } from '../types/mission-types.ts'

export function useMissionInterServices(missionId?: string): boolean | undefined {
  const queryClient = useQueryClient()

  const mission: Mission2 | undefined = missionId ? queryClient.getQueryData(missionsKeys.byId(missionId)) : undefined
  if (!mission) {
    return undefined
  }

  // Mission is interservices if there are >= 2 control units
  const isInterServices = (mission?.data?.controlUnits ?? []).length > 1
  return isInterServices
}
