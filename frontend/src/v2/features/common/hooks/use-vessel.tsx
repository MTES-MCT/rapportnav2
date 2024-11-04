import { VesselSizeEnum, VesselTypeEnum } from '@common/types/mission-types'

type VesselTypeRegistry = { [key in VesselTypeEnum]: string }
type VesselSizeRegistry = { [key in VesselSizeEnum]: string }

const VESSEL_SIZE_REGISTRY: VesselSizeRegistry = {
  [VesselSizeEnum.LESS_THAN_12m]: 'Moins de 12m',
  [VesselSizeEnum.MORE_THAN_46m]: 'Plus de 46m',
  [VesselSizeEnum.FROM_12_TO_24m]: 'Entre 12m et 24m',
  [VesselSizeEnum.FROM_24_TO_46m]: 'Entre 24m et 46m'
}

const VESSEL_TYPE_REGISTRY: VesselTypeRegistry = {
  [VesselTypeEnum.MOTOR]: 'Navire de service',
  [VesselTypeEnum.COMMERCIAL]: 'Navire de commerce',
  [VesselTypeEnum.FISHING]: 'Navire de pêche professionnelle',
  [VesselTypeEnum.SAILING]: 'Navire de plaisance professionnelle',
  [VesselTypeEnum.SAILING_LEISURE]: 'Navire de plaisance de loisir'
}

interface VesselHook {
  getVesselName: (name?: string) => string | undefined
  getVesselSize: (size?: VesselSizeEnum) => string | undefined
  getVesselType: (size?: VesselTypeEnum) => string | undefined
  vesselTypeOptions: { label: string; value: VesselTypeEnum }[]
  vesselSizeOptions: { label: string; value: VesselSizeEnum }[]
}

export function useVessel(): VesselHook {
  const getVesselSize = (size?: VesselSizeEnum) => (size ? VESSEL_SIZE_REGISTRY[size] : '')
  const getVesselType = (type?: VesselTypeEnum) => (type ? VESSEL_TYPE_REGISTRY[type] : '')
  const getVesselSizeOptions = () =>
    Object.keys(VesselSizeEnum)?.map(key => ({
      value: VesselSizeEnum[key as keyof typeof VesselSizeEnum],
      label: VESSEL_SIZE_REGISTRY[key as keyof typeof VesselSizeEnum]
    }))
  const getVesselTypeOptions = () =>
    Object.keys(VesselTypeEnum)?.map(key => ({
      value: VesselTypeEnum[key as keyof typeof VesselTypeEnum],
      label: VESSEL_TYPE_REGISTRY[key as keyof typeof VesselTypeEnum]
    }))

  const getVesselName = (name?: string): string | undefined =>
    !name ? '' : name === 'UNKNOWN' ? 'Navire inconnu' : name
  return {
    getVesselName,
    getVesselType,
    getVesselSize,
    vesselSizeOptions: getVesselSizeOptions(),
    vesselTypeOptions: getVesselTypeOptions()
  }
}
