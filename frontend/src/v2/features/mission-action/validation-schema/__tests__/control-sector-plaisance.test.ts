import { describe, it, expect } from 'vitest'
import { ObjectSchema, object, ValidationError } from 'yup'
import { SectorPleasureType } from '../../../common/types/sector-types'
import { getControlSectorPlaisanceSchema } from '../control-sector-plaisance'

const finishedSchema = object(getControlSectorPlaisanceSchema(true))
const ongoingSchema = object(getControlSectorPlaisanceSchema(false))

const establishment = { name: 'Location de bateaux' }

// every violated field, not only the first one yup happens to report
const errorPaths = async (schema: ObjectSchema<object>, data: object): Promise<string[]> => {
  try {
    await schema.validate(data, { abortEarly: false })
    return []
  } catch (error) {
    return (error as ValidationError).inner.map(e => e.path ?? '')
  }
}

describe('getControlSectorPlaisanceSchema', () => {
  it('should not carry the legacy sectorType, nor the fishing-only fields', () => {
    const keys = Object.keys(getControlSectorPlaisanceSchema(true))

    expect(keys).toEqual(['sectorEstablishmentType', 'establishment'])
  })

  describe('mission not finished', () => {
    it('should accept an empty action', async () => {
      expect(await errorPaths(ongoingSchema, {})).toEqual([])
    })

    it('should accept null values', async () => {
      expect(await errorPaths(ongoingSchema, { sectorEstablishmentType: null, establishment: null })).toEqual([])
    })
  })

  describe('mission finished', () => {
    it('should require sectorEstablishmentType', async () => {
      expect(await errorPaths(finishedSchema, { establishment })).toEqual(['sectorEstablishmentType'])
    })

    it.each([SectorPleasureType.PLEASURE_MARKET, SectorPleasureType.SEA_DRIVING_LESSON, SectorPleasureType.OTHERS])(
      'should always require the establishment when %s',
      async sectorEstablishmentType => {
        expect(await errorPaths(finishedSchema, { sectorEstablishmentType })).toEqual(['establishment.name'])
        expect(await errorPaths(finishedSchema, { sectorEstablishmentType, establishment: null })).toEqual([
          'establishment'
        ])
        expect(await errorPaths(finishedSchema, { sectorEstablishmentType, establishment })).toEqual([])
      }
    )

    it('should require an establishment with a name', async () => {
      const data = { sectorEstablishmentType: SectorPleasureType.OTHERS, establishment: { name: '' } }
      expect(await errorPaths(finishedSchema, data)).toEqual(['establishment.name'])
    })

    it('should require both fields on an empty action', async () => {
      expect(await errorPaths(finishedSchema, {})).toEqual(
        expect.arrayContaining(['sectorEstablishmentType', 'establishment.name'])
      )
    })
  })
})
