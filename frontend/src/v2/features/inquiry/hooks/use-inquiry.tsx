import { ControlType } from '@common/types/control-types'
import {
  InquiryConclusionType,
  InquiryOriginType,
  InquiryStatusType,
  InquiryTargetType
} from '../../common/types/inquiry'

type InquiryTargetTypeRegistry = { [key in InquiryTargetType]: string }
type InquiryOriginTypeRegistry = { [key in InquiryOriginType]: string }
type InquiryStatusTypeRegistry = { [key in InquiryStatusType]: string }
type InquiryConclusionTypepeRegistry = { [key in InquiryConclusionType]: string }

const INQUIRY_STATUS_TYPE_REGISTRY: InquiryStatusTypeRegistry = {
  [InquiryStatusType.CLOSED]: ``,
  [InquiryStatusType.NEW]: `Nouveau contrôle croisé`,
  [InquiryStatusType.FOLLOW_UP]: `Suite d'un contrôle croisé`
}

const INQUIRY_TARGET_TYPE_REGISTRY: InquiryTargetTypeRegistry = {
  [InquiryTargetType.VEHICLE]: `Navire`,
  [InquiryTargetType.COMPANY]: `Etablissement`
}

const INQUIRY_ORIGIN_TYPE_REGISTRY: InquiryOriginTypeRegistry = {
  [InquiryOriginType.OPPORTUNITY_CONTROL]: `Contrôle d'opportunité`,
  [InquiryOriginType.FOLLOW_UP_CONTROL]: `Suite d'un contrôle physique (mer, terre)`,
  [InquiryOriginType.CNSP_REPORTING]: `Signalement CNSP`,
  [InquiryOriginType.OTHER_REPORTING]: `Autre Signalement`,
  [InquiryOriginType.URCEM_DEDICATED_STATION]: `Poste dédié aux contrôles croisés (URCEM)`
}

const INQUIRY_CONCLUSION_TYPE_REGISTRY: InquiryConclusionTypepeRegistry = {
  [InquiryConclusionType.NO_FOLLOW_UP]: `Sans suite`,
  [InquiryConclusionType.WITH_REPORT]: `Révélation d'infraction`
}

interface InquiryHook {
  availableControlTypes: ControlType[]
  getInquiryOriginType: (type?: InquiryOriginType) => string | undefined
  inquiryOriginOptions: { label: string; value: InquiryOriginType }[]
  getInquiryStatusType: (type?: InquiryStatusType) => string | undefined
  inquiryStatusOptions: { label: string; value: InquiryStatusType }[]
  getInquiryTargetType: (type?: InquiryTargetType) => string | undefined
  inquiryTargetOptions: { label: string; value: InquiryTargetType }[]
  getInquiryConclusionType: (type?: InquiryConclusionType) => string | undefined
  inquiryConclusionOptions: { label: string; value: InquiryConclusionType }[]
}

export function useInquiry(): InquiryHook {
  const getInquiryOriginType = (type?: InquiryOriginType) => (type ? INQUIRY_ORIGIN_TYPE_REGISTRY[type] : '')
  const getInquiryOriginTypeOptions = () =>
    Object.keys(InquiryOriginType)?.map(key => ({
      value: InquiryOriginType[key as keyof typeof InquiryOriginType],
      label: INQUIRY_ORIGIN_TYPE_REGISTRY[key as keyof typeof InquiryOriginType]
    }))

  const getInquiryStatusType = (type?: InquiryStatusType) => (type ? INQUIRY_STATUS_TYPE_REGISTRY[type] : '')
  const getInquiryStatusTypeOptions = () =>
    Object.keys(InquiryStatusType)
      ?.map(key => ({
        value: InquiryStatusType[key as keyof typeof InquiryStatusType],
        label: INQUIRY_STATUS_TYPE_REGISTRY[key as keyof typeof InquiryStatusType]
      }))
      .filter(s => s.value !== InquiryStatusType.CLOSED)

  const getInquiryConclusionType = (type?: InquiryConclusionType) =>
    type ? INQUIRY_CONCLUSION_TYPE_REGISTRY[type] : ''
  const getInquiryConclusionTypeOptions = () =>
    Object.keys(InquiryConclusionType)?.map(key => ({
      value: InquiryConclusionType[key as keyof typeof InquiryConclusionType],
      label: INQUIRY_CONCLUSION_TYPE_REGISTRY[key as keyof typeof InquiryConclusionType]
    }))

  const getInquiryTargetType = (type?: InquiryTargetType) => (type ? INQUIRY_TARGET_TYPE_REGISTRY[type] : '')
  const getInquiryTargetTypeOptions = () =>
    Object.keys(InquiryTargetType)?.map(key => ({
      value: InquiryTargetType[key as keyof typeof InquiryTargetType],
      label: INQUIRY_TARGET_TYPE_REGISTRY[key as keyof typeof InquiryTargetType]
    }))

  return {
    availableControlTypes: [
      ControlType.SECURITY,
      ControlType.NAVIGATION,
      ControlType.GENS_DE_MER,
      ControlType.ADMINISTRATIVE
    ],
    getInquiryOriginType,
    getInquiryStatusType,
    getInquiryTargetType,
    getInquiryConclusionType,
    inquiryOriginOptions: getInquiryOriginTypeOptions(),
    inquiryStatusOptions: getInquiryStatusTypeOptions(),
    inquiryTargetOptions: getInquiryTargetTypeOptions(),
    inquiryConclusionOptions: getInquiryConclusionTypeOptions()
  }
}
