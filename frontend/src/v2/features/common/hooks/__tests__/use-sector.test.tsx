import { renderHook } from '@testing-library/react'
import { useSector } from '../use-sector'
import { SectorType, SectorFishingType, SectorPleasureType } from '../../types/sector-types'

describe('useSector', () => {
  it('should return sector type options', () => {
    const { result } = renderHook(() => useSector())
    expect(result.current.sectorTypeOptions).toHaveLength(2)
    expect(result.current.sectorTypeOptions).toContainEqual({
      value: SectorType.FISHING,
      label: 'Filière pêche'
    })
    expect(result.current.sectorTypeOptions).toContainEqual({
      value: SectorType.PLEASURE,
      label: 'Filière Plaisance'
    })
  })

  it('should return fishing establishment types for FISHING sector', () => {
    const { result } = renderHook(() => useSector())
    const options = result.current.getSectionEtablishmentTypeOptions(SectorType.FISHING)

    expect(options).toContainEqual({
      value: SectorFishingType.GMS,
      label: 'GMS'
    })
    expect(options).toContainEqual({
      value: SectorFishingType.FISH_AUCTION,
      label: 'Criée / Halle à marée'
    })
    expect(options).toContainEqual({
      value: SectorFishingType.LANDING_SITE,
      label: 'Site de débarquement'
    })
    expect(options).toContainEqual({
      value: SectorFishingType.RESTAURANT,
      label: 'Restaurant'
    })
    expect(options).toContainEqual({
      value: SectorFishingType.MOBILE_FISHMONGER,
      label: 'Poissonnerie ambulante'
    })
    expect(options).toContainEqual({
      value: SectorFishingType.SEDENTARY_FISHMONGER,
      label: 'Poissonnerie sédentaire'
    })
    expect(options).toContainEqual({
      value: SectorFishingType.ROADSIDE_INSPECTION,
      label: 'Contrôle routier'
    })
    expect(options).toContainEqual({
      value: SectorFishingType.FISHMONGER,
      label: 'Mareyeur'
    })
    expect(options).toContainEqual({
      value: SectorFishingType.OTHERS,
      label: 'Autre'
    })
  })

  it('should return pleasure establishment types for PLEASURE sector', () => {
    const { result } = renderHook(() => useSector())
    const options = result.current.getSectionEtablishmentTypeOptions(SectorType.PLEASURE)

    expect(options).toContainEqual({
      value: SectorPleasureType.PLEASURE_MARKET,
      label: 'Marché de la plaisance (vente, location)'
    })
    expect(options).toContainEqual({
      value: SectorPleasureType.SEA_DRIVING_LESSON,
      label: 'Formation à la conduite mer et eaux internes'
    })
    expect(options).toContainEqual({
      value: SectorPleasureType.OTHERS,
      label: 'Autre'
    })
  })

  it('should return empty array when no sector type provided', () => {
    const { result } = renderHook(() => useSector())
    const options = result.current.getSectionEtablishmentTypeOptions(undefined)
    expect(options).toEqual([])
  })

  it('should return all fishing types for FISHING sector', () => {
    const { result } = renderHook(() => useSector())
    const options = result.current.getSectionEtablishmentTypeOptions(SectorType.FISHING)

    // Should have 9 fishing types
    expect(options).toHaveLength(9)
  })

  it('should return all pleasure types for PLEASURE sector', () => {
    const { result } = renderHook(() => useSector())
    const options = result.current.getSectionEtablishmentTypeOptions(SectorType.PLEASURE)

    // Should have 3 pleasure types
    expect(options).toHaveLength(3)
  })
})
