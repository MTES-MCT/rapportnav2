import { useQueryClient } from '@tanstack/react-query'
import { missionsKeys } from '../services/query-keys.ts'
import { Mission2, MissionStatusEnum } from '../types/mission-types.ts'

export function useMissionFinished(missionId?: string): boolean {
  const queryClient = useQueryClient()

  const mission: Mission2 | undefined = queryClient.getQueryData(missionsKeys.byId(Number(missionId)))

  return mission?.status === MissionStatusEnum.ENDED
}
