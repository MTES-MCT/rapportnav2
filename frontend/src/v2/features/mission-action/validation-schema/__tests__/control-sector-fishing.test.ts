import { describe, it, expect } from 'vitest'
import { ObjectSchema, object, ValidationError } from 'yup'
import { SectorFishingType } from '../../../common/types/sector-types'
import {
  getControlSectorFishingSchema,
  isControlLocation,
  isFishAuction,
  isLandingSite
} from '../control-sector-fishing'

const finishedSchema = object(getControlSectorFishingSchema(true))
const ongoingSchema = object(getControlSectorFishingSchema(false))

const establishment = { name: 'Poissonnerie du port' }
const fishAuction = { id: 1, name: 'Criée de Lorient', facade: 'NAMO' }

// every violated field, not only the first one yup happens to report
const errorPaths = async (schema: ObjectSchema<object>, data: object): Promise<string[]> => {
  try {
    await schema.validate(data, { abortEarly: false })
    return []
  } catch (error) {
    return (error as ValidationError).inner.map(e => e.path ?? '')
  }
}

describe('control sector fishing predicates', () => {
  it('should detect the landing site and the fish auction', () => {
    expect(isLandingSite(SectorFishingType.LANDING_SITE)).toBe(true)
    expect(isLandingSite(SectorFishingType.FISH_AUCTION)).toBe(false)
    expect(isFishAuction(SectorFishingType.FISH_AUCTION)).toBe(true)
    expect(isFishAuction(SectorFishingType.GMS)).toBe(false)
  })

  it('should consider only the landing site and the fish auction as control locations', () => {
    expect(isControlLocation(SectorFishingType.LANDING_SITE)).toBe(true)
    expect(isControlLocation(SectorFishingType.FISH_AUCTION)).toBe(true)
    expect(isControlLocation(SectorFishingType.GMS)).toBe(false)
    expect(isControlLocation(undefined)).toBe(false)
  })
})

describe('getControlSectorFishingSchema', () => {
  it('should not carry the legacy sectorType', () => {
    expect(Object.keys(getControlSectorFishingSchema(true))).not.toContain('sectorType')
    expect(Object.keys(getControlSectorFishingSchema(false))).not.toContain('sectorType')
  })

  describe('mission not finished', () => {
    it('should accept an empty action', async () => {
      expect(await errorPaths(ongoingSchema, {})).toEqual([])
    })

    it('should accept null values', async () => {
      const data = { sectorEstablishmentType: null, establishment: null, portLocode: null, fishAuction: null }
      expect(await errorPaths(ongoingSchema, data)).toEqual([])
    })
  })

  describe('mission finished', () => {
    it('should require sectorEstablishmentType', async () => {
      expect(await errorPaths(finishedSchema, { establishment })).toEqual(['sectorEstablishmentType'])
    })

    it.each([
      SectorFishingType.GMS,
      SectorFishingType.RESTAURANT,
      SectorFishingType.MOBILE_FISHMONGER,
      SectorFishingType.SEDENTARY_FISHMONGER,
      SectorFishingType.FISHMONGER,
      SectorFishingType.OTHERS
    ])('should require the establishment, and nothing else, when %s', async sectorEstablishmentType => {
      expect(await errorPaths(finishedSchema, { sectorEstablishmentType })).toEqual(['establishment.name'])
      expect(await errorPaths(finishedSchema, { sectorEstablishmentType, establishment: null })).toEqual([
        'establishment'
      ])
      expect(await errorPaths(finishedSchema, { sectorEstablishmentType, establishment })).toEqual([])
    })

    it('should require an establishment with a name', async () => {
      const data = { sectorEstablishmentType: SectorFishingType.GMS, establishment: { name: '' } }
      expect(await errorPaths(finishedSchema, data)).toEqual(['establishment.name'])
    })

    it('should require the fish auction, and not the establishment nor the port, when FISH_AUCTION', async () => {
      const data = { sectorEstablishmentType: SectorFishingType.FISH_AUCTION }
      expect(await errorPaths(finishedSchema, data)).toEqual(['fishAuction'])
      expect(await errorPaths(finishedSchema, { ...data, fishAuction })).toEqual([])
    })

    it('should require the port, and not the establishment nor the fish auction, when LANDING_SITE', async () => {
      const data = { sectorEstablishmentType: SectorFishingType.LANDING_SITE }
      expect(await errorPaths(finishedSchema, data)).toEqual(['portLocode'])
      expect(await errorPaths(finishedSchema, { ...data, portLocode: 'FRBOD' })).toEqual([])
    })

    it.each([SectorFishingType.FISH_AUCTION, SectorFishingType.LANDING_SITE])(
      'should accept an absent (undefined) or null establishment when %s',
      async sectorEstablishmentType => {
        const data = { sectorEstablishmentType, fishAuction, portLocode: 'FRBOD' }
        expect(await errorPaths(finishedSchema, { ...data, establishment: undefined })).toEqual([])
        expect(await errorPaths(finishedSchema, { ...data, establishment: null })).toEqual([])
      }
    )
  })
})
