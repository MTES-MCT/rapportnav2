import { MissionTypeEnum } from '@common/types/env-mission-types.ts'
import { MissionReinforcementTypeEnum, MissionReportTypeEnum, MissionType } from '../types/mission-types.ts'

interface MissionTypeHook {
  getMissionTypeLabel: (type?: MissionType) => string | undefined
  getReinforcementTypeLabel: (type?: MissionReinforcementTypeEnum) => string | undefined
  getReportTypeLabel: (type?: MissionReportTypeEnum) => string | undefined
  missionTypeOptions: { label: string; value: MissionType }[]
  reinforcementTypeOptions: { label: string; value: MissionReinforcementTypeEnum }[]
  reportTypeOptions: { label: string; value: MissionReportTypeEnum }[]
  isExternalReinforcementTime: (missionReportType?: MissionReportTypeEnum) => boolean
  isMissionTypeSea: (missionTypes?: MissionTypeEnum[]) => boolean | undefined
  isEnvMission: (missionReportType?: MissionReportTypeEnum) => boolean
}

const MISSION_TYPE_REGISTRY: Record<MissionType, string> = {
  [MissionType.LAND]: 'Terre',
  [MissionType.SEA]: 'Mer',
  [MissionType.AIR]: 'Air'
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

export function useMissionType(): MissionTypeHook {
  const getMissionTypeLabel = (type?: MissionType): string | undefined => (type ? MISSION_TYPE_REGISTRY[type] : '')

  const getReinforcementTypeLabel = (type?: MissionReinforcementTypeEnum): string | undefined =>
    type ? REINFORCEMENT_TYPE_REGISTRY[type] : ''

  const getReportTypeLabel = (type?: MissionReportTypeEnum): string | undefined =>
    type ? REPORT_TYPE_REGISTRY[type] : ''

  const getMissionTypeOptions = () =>
    Object.keys(MissionType).map(key => ({
      value: MissionType[key as keyof typeof MissionType],
      label: MISSION_TYPE_REGISTRY[key as keyof typeof MissionType]
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

  const isExternalReinforcementTime = (missionReportType?: MissionReportTypeEnum) =>
    missionReportType === MissionReportTypeEnum.EXTERNAL_REINFORCEMENT_TIME_REPORT

  const isEnvMission = (missionReportType?: MissionReportTypeEnum) =>
    missionReportType === MissionReportTypeEnum.FIELD_REPORT

  const isMissionTypeSea = (missionTypes?: MissionTypeEnum[]) => missionTypes?.includes(MissionTypeEnum.SEA)

  return {
    getMissionTypeLabel,
    getReinforcementTypeLabel,
    getReportTypeLabel,
    missionTypeOptions: getMissionTypeOptions(),
    reinforcementTypeOptions: getReinforcementTypeOptions(),
    reportTypeOptions: getReportTypeOptions(),
    isMissionTypeSea,
    isExternalReinforcementTime,
    isEnvMission
  }
}
