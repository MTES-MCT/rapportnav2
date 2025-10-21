import { ControlType } from '@common/types/control-types'
import { Icon, IconProps, THEME } from '@mtes-mct/monitor-ui'
import { FunctionComponent } from 'react'
import { Inquiry, InquiryConclusionType, InquiryOriginType, InquiryTargetType } from '../../common/types/inquiry'
import { CompletenessForStatsStatusEnum } from '../../common/types/mission-types'
import { getInquirySchema } from './inquiry-schema'

type InquiryTargetTypeRegistry = { [key in InquiryTargetType]: string }
type InquiryOriginTypeRegistry = { [key in InquiryOriginType]: string }
type InquiryConclusionTypepeRegistry = { [key in InquiryConclusionType]: string }

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

const REPORT_STATUS = {
  [CompletenessForStatsStatusEnum.INCOMPLETE]: {
    text: 'À compléter',
    icon: Icon.AttentionFilled,
    color: THEME.color.charcoal
  },
  [CompletenessForStatsStatusEnum.COMPLETE]: {
    text: 'Données à jour',
    icon: Icon.Confirm,
    color: THEME.color.mediumSeaGreen
  }
}

interface InquiryHook {
  availableControlTypes: ControlType[]
  getInquiryOriginType: (type?: InquiryOriginType) => string | undefined
  inquiryOriginOptions: { label: string; value: InquiryOriginType }[]
  getInquiryTargetType: (type?: InquiryTargetType) => string | undefined
  inquiryTargetOptions: { label: string; value: InquiryTargetType }[]
  getInquiryConclusionType: (type?: InquiryConclusionType) => string | undefined
  inquiryConclusionOptions: { label: string; value: InquiryConclusionType }[]
  isClosable: (inquiry?: Inquiry) => boolean | undefined
  getStatusReport: (inquiry?: Inquiry) => { text: string; color: string; icon: FunctionComponent<IconProps> }
}

export function useInquiry(): InquiryHook {
  const getInquiryOriginType = (type?: InquiryOriginType) => (type ? INQUIRY_ORIGIN_TYPE_REGISTRY[type] : '')
  const getInquiryOriginTypeOptions = () =>
    Object.keys(InquiryOriginType)?.map(key => ({
      value: InquiryOriginType[key as keyof typeof InquiryOriginType],
      label: INQUIRY_ORIGIN_TYPE_REGISTRY[key as keyof typeof InquiryOriginType]
    }))

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

  const isClosable = (inquiry?: Inquiry) => {
    return (
      getInquirySchema().isValidSync(inquiry) &&
      inquiry?.actions?.every(action => action.completenessForStats?.status === CompletenessForStatsStatusEnum.COMPLETE)
    )
  }

  const getStatusReport = (inquiry?: Inquiry) => {
    return isClosable(inquiry)
      ? REPORT_STATUS[CompletenessForStatsStatusEnum.COMPLETE]
      : REPORT_STATUS[CompletenessForStatsStatusEnum.INCOMPLETE]
  }

  return {
    getStatusReport,
    isClosable,
    availableControlTypes: [
      ControlType.SECURITY,
      ControlType.NAVIGATION,
      ControlType.GENS_DE_MER,
      ControlType.ADMINISTRATIVE
    ],
    getInquiryOriginType,
    getInquiryTargetType,
    getInquiryConclusionType,
    inquiryOriginOptions: getInquiryOriginTypeOptions(),
    inquiryTargetOptions: getInquiryTargetTypeOptions(),
    inquiryConclusionOptions: getInquiryConclusionTypeOptions()
  }
}
