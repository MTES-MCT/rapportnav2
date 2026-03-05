import { useQueryClient } from '@tanstack/react-query'
import { missionsKeys } from '../services/query-keys.ts'
import { Mission2 } from '../types/mission-types.ts'

export type MissionDates = {
  startDateTimeUtc?: string
  endDateTimeUtc?: string
}

export function useMissionDates(missionId?: string): MissionDates {
  const queryClient = useQueryClient()

  const mission: Mission2 | undefined = missionId ? queryClient.getQueryData(missionsKeys.byId(missionId)) : undefined

  return {
    startDateTimeUtc: mission?.data?.startDateTimeUtc,
    endDateTimeUtc: mission?.data?.endDateTimeUtc
  }
}
