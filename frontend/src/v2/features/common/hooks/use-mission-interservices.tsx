import { useQueryClient } from '@tanstack/react-query'
import { missionsKeys } from '../services/query-keys.ts'
import { Mission2 } from '../types/mission-types.ts'

export function useMissionInterServices(missionId?: string): boolean | undefined {
  const queryClient = useQueryClient()

  const mission: Mission2 | undefined = missionId ? queryClient.getQueryData(missionsKeys.byId(missionId)) : undefined
  if (!mission) {
    return undefined
  }

  // Extract control units
  const controlUnits = mission?.data?.controlUnits ?? []

  // Compute unique administrations, ignoring null, empty, or archived units
  const administrations = controlUnits
    .filter(u => !u.isArchived)
    .map(u => u.administration?.trim())
    .filter((a): a is string => !!a)

  // Mission is interservices if there are >= 2 distinct administrations
  const isInterServices = new Set(administrations).size > 1

  return isInterServices
}
