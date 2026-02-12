import { useQueryClient } from '@tanstack/react-query'
import { missionsKeys } from '../services/query-keys.ts'
import { Mission2 } from '../types/mission-types.ts'

export function useMissionDates(missionId?: string): [string | undefined, string | undefined] {
  const queryClient = useQueryClient()

  const mission: Mission2 | undefined = missionId ? queryClient.getQueryData(missionsKeys.byId(missionId)) : undefined

  return [mission?.data?.startDateTimeUtc, mission?.data?.endDateTimeUtc]
}
