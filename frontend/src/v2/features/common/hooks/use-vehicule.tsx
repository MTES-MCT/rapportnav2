import { VehicleTypeEnum } from '@common/types/env-mission-types'

type VehiculeTypeRegistry = { [key in VehicleTypeEnum]: string }

const VEHICULE_TYPE_REGISTRY: VehiculeTypeRegistry = {
  [VehicleTypeEnum.VESSEL]: 'Navire',
  [VehicleTypeEnum.VEHICLE_AIR]: 'Véhicule aérien',
  [VehicleTypeEnum.OTHER_SEA]: 'Autre véhicule marin',
  [VehicleTypeEnum.VEHICLE_LAND]: 'Véhicule terrestre'
}

interface VehiculeHook {
  getVehiculeType: (type?: VehicleTypeEnum) => string | undefined
  vehiculeTypeOptions: { label: string; value: VehicleTypeEnum }[]
}

export function useVehicule(): VehiculeHook {
  const getVehiculeType = (type?: VehicleTypeEnum) => (type ? VEHICULE_TYPE_REGISTRY[type] : '')
  const getVehiculeTypeOptions = () =>
    Object.keys(VehicleTypeEnum)?.map(key => ({
      value: VehicleTypeEnum[key as keyof typeof VehicleTypeEnum],
      label: VEHICULE_TYPE_REGISTRY[key as keyof typeof VehicleTypeEnum]
    }))
  return { getVehiculeType, vehiculeTypeOptions: getVehiculeTypeOptions() }
}
