import { VesselSizeEnum, VesselTypeEnum } from '@common/types/mission-types'
import { omit } from 'lodash'
import IconVesselCommerce from '../components/ui/icon-vessel-commerce'
import IconVesselFishing from '../components/ui/icon-vessel-fishing'
import IconVesselPassenger from '../components/ui/icon-vessel-passenger'
import IconVesselSailingLeisure from '../components/ui/icon-vessel-sailing-leisure'
import IconVesselSailingPro from '../components/ui/icon-vessel-sailing-pro'
import IconVesselServices from '../components/ui/icon-vessel-school'
import { ModuleType } from '../types/module-type'

type VesselType = { label: string; icon?: JSX.Element }

type VesselTypeRegistry = {
  [key in VesselTypeEnum]: VesselType
}
type VesselSizeRegistry = {
  [key in VesselSizeEnum]: string
}

const VESSEL_SIZE_REGISTRY: VesselSizeRegistry = {
  [VesselSizeEnum.LESS_THAN_12m]: 'Moins de 12m',
  [VesselSizeEnum.MORE_THAN_46m]: 'Plus de 46m',
  [VesselSizeEnum.FROM_12_TO_24m]: 'Entre 12m et 24m',
  [VesselSizeEnum.FROM_24_TO_46m]: 'Entre 24m et 46m'
}

const VESSEL_TYPE_REGISTRY: VesselTypeRegistry = {
  [VesselTypeEnum.MOTOR]: {
    icon: <IconVesselServices />,
    label: 'Navire de services (travaux...)'
  },
  [VesselTypeEnum.COMMERCIAL]: {
    icon: <IconVesselCommerce />,
    label: 'Navire de commerce'
  },
  [VesselTypeEnum.FISHING]: {
    icon: <IconVesselFishing />,
    label: 'Navire de pêche professionnelle'
  },
  [VesselTypeEnum.SAILING]: {
    icon: <IconVesselSailingPro />,
    label: 'Navire de plaisance professionnelle'
  },
  [VesselTypeEnum.SAILING_LEISURE]: {
    icon: <IconVesselSailingLeisure />,
    label: 'Navire de plaisance de loisir'
  },
  [VesselTypeEnum.SCHOOL]: {
    icon: IconVesselCommerce(),
    label: 'bateaux école'
  },
  [VesselTypeEnum.PASSENGER]: {
    icon: <IconVesselPassenger />,
    label: 'Navires de passagers (navettes transports...)'
  }
}

interface VesselHook {
  getVesselName: (name?: string) => string | undefined
  getFullVesselName: (name?: string, flagState?: string, externalReferenceNumber?: string) => string | undefined
  getVesselSize: (size?: VesselSizeEnum) => string | undefined
  getVesselTypeName: (size?: VesselTypeEnum) => string | undefined
  vesselTypeOptions: { label: string; value: VesselTypeEnum }[]
  vesselSizeOptions: { label: string; value: VesselSizeEnum }[]
  getVesselType: (size?: VesselTypeEnum) => VesselType | undefined
  getVesselTypeByModule: (moduleType: ModuleType) => ({ key: VesselTypeEnum } & VesselType)[]
}

export function useVessel(): VesselHook {
  const getVesselSize = (size?: VesselSizeEnum) => (size ? VESSEL_SIZE_REGISTRY[size] : '')
  const getVesselTypeName = (type?: VesselTypeEnum) => (type ? VESSEL_TYPE_REGISTRY[type]?.label : '')
  const getVesselType = (type?: VesselTypeEnum) => (type ? VESSEL_TYPE_REGISTRY[type] : ({} as VesselType))
  const getVesselSizeOptions = () =>
    Object.keys(VesselSizeEnum)?.map(key => ({
      value: VesselSizeEnum[key as keyof typeof VesselSizeEnum],
      label: VESSEL_SIZE_REGISTRY[key as keyof typeof VesselSizeEnum]
    }))
  const getVesselTypeOptions = () =>
    Object.keys(VesselTypeEnum)?.map(key => ({
      value: VesselTypeEnum[key as keyof typeof VesselTypeEnum],
      label: VESSEL_TYPE_REGISTRY[key as keyof typeof VesselTypeEnum].label
    }))

  const getVesselName = (name?: string): string | undefined =>
    !name ? '' : name === 'UNKNOWN' ? 'Navire inconnu' : name

  const getFullVesselName = (
    name?: string,
    flagState?: string,
    externalReferenceNumber?: string
  ): string | undefined => {
    const parts: string[] = []

    // Always show the formatted vessel name
    parts.push(getVesselName(name))

    // Optional fields
    if (flagState) {
      parts.push(flagState)
    }

    if (externalReferenceNumber) {
      parts.push(externalReferenceNumber)
    }

    return parts.join(' - ')
  }

  const getVesselTypeByModule = (moduleType: ModuleType) => {
    const entry =
      moduleType === ModuleType.ULAM
        ? VESSEL_TYPE_REGISTRY
        : omit(VESSEL_TYPE_REGISTRY, [VesselTypeEnum.SCHOOL, VesselTypeEnum.PASSENGER])
    return Object.keys(entry)?.map(key => ({
      key: VesselTypeEnum[key as keyof typeof VesselTypeEnum],
      ...VESSEL_TYPE_REGISTRY[key as keyof typeof VesselTypeEnum]
    }))
  }

  return {
    getVesselType,
    getVesselName,
    getFullVesselName,
    getVesselSize,
    getVesselTypeName,
    getVesselTypeByModule,
    vesselSizeOptions: getVesselSizeOptions(),
    vesselTypeOptions: getVesselTypeOptions()
  }
}
