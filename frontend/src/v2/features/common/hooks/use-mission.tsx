import { Mission2, MissionSourceEnum } from '../types/mission-types.ts'

interface MissionHook {
  isMissionNotDeletable: (value?: Mission2) => boolean | undefined
}

export function useMission(): MissionHook {
  const isMissionNotDeletable = (value?: Mission2) =>
    value?.actions?.some(a => [MissionSourceEnum.MONITORENV, MissionSourceEnum.MONITORFISH].includes(a.source))

  return {
    isMissionNotDeletable
  }
}
