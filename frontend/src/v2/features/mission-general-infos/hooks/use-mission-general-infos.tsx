import { Mission2, MissionGeneralInfo2 } from '../../common/types/mission-types'

type MissionGeneralInfosHook = {
  getGeneralInfos: (mission?: Mission2, controlUnitId?: number) => MissionGeneralInfo2
}

export function useMissionGeneralInfos(): MissionGeneralInfosHook {
  const getGeneralInfos = (mission?: Mission2, controlUnitId?: number): MissionGeneralInfo2 => {
    return {
      ...(mission?.generalInfos ?? {}),
      missionTypes: mission?.data?.missionTypes ?? [],
      observations: mission?.data?.observationsByUnit,
      startDateTimeUtc: mission?.data?.startDateTimeUtc,
      endDateTimeUtc: mission?.data?.endDateTimeUtc,
      resources: mission?.data?.controlUnits.find(unit => unit.id === controlUnitId)?.resources
    }
  }
  return {
    getGeneralInfos
  }
}
