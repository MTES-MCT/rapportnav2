import { SectorEtablishmentType, SectorFishingType, SectorPleasureType, SectorType } from '../types/sector-types'

type SectorTypeRegistry = { [key in SectorType]: string }

const SECTOR_TYPES: SectorTypeRegistry = {
  FISHING: 'Filière pêche',
  PLEASURE: 'Filière Plaisance'
}

const SECTOR_PLEASURE_TYPES: Record<SectorPleasureType, string> = {
  SEA_DRIVING_LESSON: 'Formation à la conduite mer et eaux internes',
  PLEASURE_MARKET: 'Marché de la plaisance (vente, location)',
  OTHERS: 'Autre'
}

const SECTOR_FISHING_TYPES: Record<SectorFishingType, string> = {
  GMS: 'GMS',
  RESTAURANT: 'Restaurant',
  MOBILE_FISHMONGER: 'Poissonnerie ambulante',
  SEDENTARY_FISHMONGER: 'Poissonnerie sédentaire',
  ROADSIDE_INSPECTION: 'Contrôle routier',
  SHOUTED: 'Criée / Halle à marée',
  FISHMONGER: 'Mareyeur',
  LANDING_SITE: 'Site de débarquement',
  OTHERS: 'Autre'
}

interface SectorHook {
  sectorTypeOptions: { label: string; value: SectorType }[]
  getSectionEtablishmentTypeOptions: (type?: SectorType) => { value: SectorEtablishmentType; label: string }[]
}

export function useSector(): SectorHook {
  const getSectorTypeOptions = () =>
    Object.keys(SectorType)?.map(key => ({
      value: SectorType[key as keyof typeof SectorType],
      label: SECTOR_TYPES[key as keyof typeof SectorType]
    }))

  const getSectorFishingTypeOptions = () =>
    Object.keys(SectorFishingType)?.map(key => ({
      value: SectorFishingType[key as keyof typeof SectorFishingType],
      label: SECTOR_FISHING_TYPES[key as keyof typeof SectorFishingType]
    }))

  const getSectorPleasureTypeOptions = () =>
    Object.keys(SectorPleasureType)?.map(key => ({
      value: SectorPleasureType[key as keyof typeof SectorPleasureType],
      label: SECTOR_PLEASURE_TYPES[key as keyof typeof SectorPleasureType]
    }))

  const getSectionEtablishmentTypeOptions = (type?: SectorType) => {
    if (!type) return []
    return type === SectorType?.FISHING ? getSectorFishingTypeOptions() : getSectorPleasureTypeOptions()
  }

  return {
    getSectionEtablishmentTypeOptions,
    sectorTypeOptions: getSectorTypeOptions()
  }
}
