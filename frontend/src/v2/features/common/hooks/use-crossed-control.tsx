import { ControlType } from '@common/types/control-types'
import {
  CrossControlConclusionType,
  CrossControlOriginType,
  CrossControlStatusType,
  CrossControlTargetType
} from '../types/crossed-control-type'

type CrossControlTargetTypeRegistry = { [key in CrossControlTargetType]: string }
type CrossControlOriginTypeRegistry = { [key in CrossControlOriginType]: string }
type CrossControlStatusTypeRegistry = { [key in CrossControlStatusType]: string }
type CrossControlConclusionTypepeRegistry = { [key in CrossControlConclusionType]: string }

const CROSS_CONTROL_STATUS_TYPE_REGISTRY: CrossControlStatusTypeRegistry = {
  [CrossControlStatusType.CLOSED]: ``,
  [CrossControlStatusType.NEW]: `Nouveau contrôle croisé`,
  [CrossControlStatusType.FOLLOW_UP]: `Suite d'un contrôle croisé`
}

const CROSS_CONTROL_TARGET_TYPE_REGISTRY: CrossControlTargetTypeRegistry = {
  [CrossControlTargetType.VEHICLE]: `Navire`,
  [CrossControlTargetType.COMPANY]: `Etablissement`
}

const CROSS_CONTROL_ORIGIN_TYPE_REGISTRY: CrossControlOriginTypeRegistry = {
  [CrossControlOriginType.OPPORTUNITY_CONTROL]: `Contrôle d'opportunité`,
  [CrossControlOriginType.FOLLOW_UP_CONTROL]: `Suite d'un contrôle physique (mer, terre)`,
  [CrossControlOriginType.CNSP_REPORTING]: `Signalement CNSP`,
  [CrossControlOriginType.OTHER_REPORTING]: `Autre Signalement`,
  [CrossControlOriginType.URCEM_DEDICATED_STATION]: `Poste dédié aux contrôles croisés (URCEM)`
}

const CROSS_CONTROL_CONCLUSION_TYPE_REGISTRY: CrossControlConclusionTypepeRegistry = {
  [CrossControlConclusionType.NO_FOLLOW_UP]: `Sans suite`,
  [CrossControlConclusionType.WITH_REPORT]: `Révélation d'infraction`
}

interface CrossControlHook {
  availableControlTypes: ControlType[]
  getCrossControlOriginType: (type?: CrossControlOriginType) => string | undefined
  crossControlOriginOptions: { label: string; value: CrossControlOriginType }[]
  getCrossControlStatusType: (type?: CrossControlStatusType) => string | undefined
  crossControlStatusOptions: { label: string; value: CrossControlStatusType }[]
  getCrossControlTargetType: (type?: CrossControlTargetType) => string | undefined
  crossControlTargetOptions: { label: string; value: CrossControlTargetType }[]
  getCrossControlConclusionType: (type?: CrossControlConclusionType) => string | undefined
  crossControlConclusionOptions: { label: string; value: CrossControlConclusionType }[]
}

export function useCrossControl(): CrossControlHook {
  const getCrossControlOriginType = (type?: CrossControlOriginType) =>
    type ? CROSS_CONTROL_ORIGIN_TYPE_REGISTRY[type] : ''
  const getCrossControlOriginTypeOptions = () =>
    Object.keys(CrossControlOriginType)?.map(key => ({
      value: CrossControlOriginType[key as keyof typeof CrossControlOriginType],
      label: CROSS_CONTROL_ORIGIN_TYPE_REGISTRY[key as keyof typeof CrossControlOriginType]
    }))

  const getCrossControlStatusType = (type?: CrossControlStatusType) =>
    type ? CROSS_CONTROL_STATUS_TYPE_REGISTRY[type] : ''
  const getCrossControlStatusTypeOptions = () =>
    Object.keys(CrossControlStatusType)
      ?.map(key => ({
        value: CrossControlStatusType[key as keyof typeof CrossControlStatusType],
        label: CROSS_CONTROL_STATUS_TYPE_REGISTRY[key as keyof typeof CrossControlStatusType]
      }))
      .filter(s => s.value !== CrossControlStatusType.CLOSED)

  const getCrossControlConclusionType = (type?: CrossControlConclusionType) =>
    type ? CROSS_CONTROL_CONCLUSION_TYPE_REGISTRY[type] : ''
  const getCrossControlConclusionTypeOptions = () =>
    Object.keys(CrossControlConclusionType)?.map(key => ({
      value: CrossControlConclusionType[key as keyof typeof CrossControlConclusionType],
      label: CROSS_CONTROL_CONCLUSION_TYPE_REGISTRY[key as keyof typeof CrossControlConclusionType]
    }))

  const getCrossControlTargetType = (type?: CrossControlTargetType) =>
    type ? CROSS_CONTROL_TARGET_TYPE_REGISTRY[type] : ''
  const getCrossControlTargetTypeOptions = () =>
    Object.keys(CrossControlTargetType)?.map(key => ({
      value: CrossControlTargetType[key as keyof typeof CrossControlTargetType],
      label: CROSS_CONTROL_TARGET_TYPE_REGISTRY[key as keyof typeof CrossControlTargetType]
    }))

  return {
    availableControlTypes: [
      ControlType.SECURITY,
      ControlType.NAVIGATION,
      ControlType.GENS_DE_MER,
      ControlType.ADMINISTRATIVE
    ],
    getCrossControlOriginType,
    getCrossControlStatusType,
    getCrossControlTargetType,
    getCrossControlConclusionType,
    crossControlOriginOptions: getCrossControlOriginTypeOptions(),
    crossControlStatusOptions: getCrossControlStatusTypeOptions(),
    crossControlTargetOptions: getCrossControlTargetTypeOptions(),
    crossControlConclusionOptions: getCrossControlConclusionTypeOptions()
  }
}
