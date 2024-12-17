import { MissionReinforcementTypeEnum, MissionReportTypeEnum, MissionTypeEnum } from '../types/mission-types.ts'


interface MissionHook {
  getMissionTypeLabel: (type?: MissionTypeEnum) => string | undefined
  getReinforcementTypeLabel: (type?: MissionReinforcementTypeEnum) => string | undefined
  getReportTypeLabel: (type?: MissionReportTypeEnum) => string | undefined
  missionTypeOptions: { label: string; value: MissionTypeEnum }[]
  reinforcementTypeOptions: { label: string; value: MissionReinforcementTypeEnum }[]
  reportTypeOptions: { label: string; value: MissionReportTypeEnum }[]
}

const MISSION_TYPE_REGISTRY: Record<MissionTypeEnum, string> = {
  [MissionTypeEnum.LAND]: 'Terre',
  [MissionTypeEnum.SEA]: 'Mer',
  [MissionTypeEnum.AIR]: 'Air'
}

const REINFORCEMENT_TYPE_REGISTRY: Record<MissionReinforcementTypeEnum, string> = {
  [MissionReinforcementTypeEnum.PATROL]: 'Patrouille (mission PAM)',
  [MissionReinforcementTypeEnum.JDP]: 'JDP',
  [MissionReinforcementTypeEnum.DIRM]: 'DIRM',
  [MissionReinforcementTypeEnum.OTHER_ULAM]: 'Autre ULAM',
  [MissionReinforcementTypeEnum.SEA_TRAINER]: 'Formateur ESP Mer',
  [MissionReinforcementTypeEnum.OTHER]: 'Autre'
}

const REPORT_TYPE_REGISTRY: Record<MissionReportTypeEnum, string> = {
  [MissionReportTypeEnum.FIELD_REPORT]: 'Rapport avec sortie terrain',
  [MissionReportTypeEnum.OFFICE_REPORT]: 'Rapport sans sortie terrain (admin. uniquement)',
  [MissionReportTypeEnum.EXTERNAL_REINFORCEMENT_TIME_REPORT]: 'Rapport de temps agent en renfort extérieur'
}

export function useMissionType(): MissionHook {
  const getMissionTypeLabel = (type?: MissionTypeEnum): string | undefined =>
    type ? MISSION_TYPE_REGISTRY[type] : ''

  const getReinforcementTypeLabel = (type?: MissionReinforcementTypeEnum): string | undefined =>
    type ? REINFORCEMENT_TYPE_REGISTRY[type] : ''

  const getReportTypeLabel = (type?: MissionReportTypeEnum): string | undefined =>
    type ? REPORT_TYPE_REGISTRY[type] : ''

  const getMissionTypeOptions = () =>
    Object.keys(MissionTypeEnum).map(key => ({
      value: MissionTypeEnum[key as keyof typeof MissionTypeEnum],
      label: MISSION_TYPE_REGISTRY[key as keyof typeof MissionTypeEnum]
    }))

  const getReinforcementTypeOptions = () =>
    Object.keys(MissionReinforcementTypeEnum).map(key => ({
      value: MissionReinforcementTypeEnum[key as keyof typeof MissionReinforcementTypeEnum],
      label: REINFORCEMENT_TYPE_REGISTRY[key as keyof typeof MissionReinforcementTypeEnum]
    }))

  const getReportTypeOptions = () =>
    Object.keys(MissionReportTypeEnum).map(key => ({
      value: MissionReportTypeEnum[key as keyof typeof MissionReportTypeEnum],
      label: REPORT_TYPE_REGISTRY[key as keyof typeof MissionReportTypeEnum]
    }))

  return {
    getMissionTypeLabel,
    getReinforcementTypeLabel,
    getReportTypeLabel,
    missionTypeOptions: getMissionTypeOptions(),
    reinforcementTypeOptions: getReinforcementTypeOptions(),
    reportTypeOptions: getReportTypeOptions()
  }
}
