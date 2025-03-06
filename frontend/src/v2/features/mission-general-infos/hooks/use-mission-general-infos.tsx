import { Mission2, MissionGeneralInfo2 } from '../../common/types/mission-types'

type MissionGeneralInfosHook = {
  getGeneralInfos: (mission?: Mission2, controlUnitId?: number) => MissionGeneralInfo2
}

export function useMissionGeneralInfos(): MissionGeneralInfosHook {
  const getGeneralInfos = (mission?: Mission2, controlUnitId?: number): MissionGeneralInfo2 => {
    return {
      ...(mission?.generalInfos ?? {}),
      missionTypes: mission?.envData?.missionTypes ?? [],
      observations: mission?.envData.observationsByUnit,
      startDateTimeUtc: mission?.envData?.startDateTimeUtc,
      endDateTimeUtc: mission?.envData?.endDateTimeUtc,
      resources: mission?.envData?.controlUnits.find(unit => unit.id === controlUnitId)?.resources
    }
  }
  return {
    getGeneralInfos
  }
}
