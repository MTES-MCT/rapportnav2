import { SectorFishingType, SectorPleasureType } from '../types/sector-types'

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
  FISH_AUCTION: 'Criée / Halle à marée',
  FISHMONGER: 'Mareyeur',
  LANDING_SITE: 'Site de débarquement',
  OTHERS: 'Autre'
}

interface SectorHook {
  sectorFishingTypeOptions: { value: SectorFishingType; label: string }[]
  sectorPleasureTypeOptions: { value: SectorPleasureType; label: string }[]
}

export function useSector(): SectorHook {
  const sectorFishingTypeOptions = Object.keys(SectorFishingType).map(key => ({
    value: SectorFishingType[key as keyof typeof SectorFishingType],
    label: SECTOR_FISHING_TYPES[key as keyof typeof SectorFishingType]
  }))

  const sectorPleasureTypeOptions = Object.keys(SectorPleasureType).map(key => ({
    value: SectorPleasureType[key as keyof typeof SectorPleasureType],
    label: SECTOR_PLEASURE_TYPES[key as keyof typeof SectorPleasureType]
  }))

  return { sectorFishingTypeOptions, sectorPleasureTypeOptions }
}
