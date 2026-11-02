import { renderHook } from '@testing-library/react'
import { useSector } from '../use-sector'
import { SectorFishingType, SectorPleasureType } from '../../types/sector-types'

describe('useSector', () => {
  it('should return fishing establishment types', () => {
    const { result } = renderHook(() => useSector())
    const options = result.current.sectorFishingTypeOptions

    expect(options).toContainEqual({ value: SectorFishingType.GMS, label: 'GMS' })
    expect(options).toContainEqual({ value: SectorFishingType.FISH_AUCTION, label: 'Criée / Halle à marée' })
    expect(options).toContainEqual({ value: SectorFishingType.LANDING_SITE, label: 'Site de débarquement' })
    expect(options).toContainEqual({ value: SectorFishingType.RESTAURANT, label: 'Restaurant' })
    expect(options).toContainEqual({ value: SectorFishingType.MOBILE_FISHMONGER, label: 'Poissonnerie ambulante' })
    expect(options).toContainEqual({ value: SectorFishingType.SEDENTARY_FISHMONGER, label: 'Poissonnerie sédentaire' })
    expect(options).toContainEqual({ value: SectorFishingType.FISHMONGER, label: 'Mareyeur' })
    expect(options).toContainEqual({ value: SectorFishingType.OTHERS, label: 'Autre' })
  })

  it('should return all 8 fishing types, without the roadside inspection', () => {
    const { result } = renderHook(() => useSector())
    const options = result.current.sectorFishingTypeOptions

    expect(options).toHaveLength(8)
    expect(options.map(option => option.value as string)).not.toContain('ROADSIDE_INSPECTION')
  })

  it('should return pleasure establishment types', () => {
    const { result } = renderHook(() => useSector())
    const options = result.current.sectorPleasureTypeOptions

    expect(options).toContainEqual({
      value: SectorPleasureType.PLEASURE_MARKET,
      label: 'Marché de la plaisance (vente, location)'
    })
    expect(options).toContainEqual({
      value: SectorPleasureType.SEA_DRIVING_LESSON,
      label: 'Formation à la conduite mer et eaux internes'
    })
    expect(options).toContainEqual({ value: SectorPleasureType.OTHERS, label: 'Autre' })
  })

  it('should return all 3 pleasure types', () => {
    const { result } = renderHook(() => useSector())

    expect(result.current.sectorPleasureTypeOptions).toHaveLength(3)
  })
})
