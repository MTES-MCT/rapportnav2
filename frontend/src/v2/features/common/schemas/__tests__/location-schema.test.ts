import { object } from 'yup'
import getLocationSchema from '../location-schema'
import { LocationType } from '../../types/location-type'

describe('getLocationSchema', () => {
  describe('when mission is NOT finished', () => {
    const schema = object().shape(getLocationSchema(false))

    it('should accept all null fields', async () => {
      await expect(
        schema.validate({ locationType: null, geoCoords: null, portLocode: null, zipCode: null })
      ).resolves.toBeTruthy()
    })

    it('should accept valid GPS data without requiring it', async () => {
      await expect(
        schema.validate({ locationType: LocationType.GPS, geoCoords: [48.85, 2.35], portLocode: null, zipCode: null })
      ).resolves.toBeTruthy()
    })

    it('should accept PORT with portLocode without requiring it', async () => {
      await expect(
        schema.validate({ locationType: LocationType.PORT, geoCoords: null, portLocode: 'FRPAR', zipCode: null })
      ).resolves.toBeTruthy()
    })

    it('should accept COMMUNE with zipCode without requiring it', async () => {
      await expect(
        schema.validate({ locationType: LocationType.COMMUNE, geoCoords: null, portLocode: null, zipCode: '75000' })
      ).resolves.toBeTruthy()
    })
  })

  describe('when mission IS finished', () => {
    const schema = object().shape(getLocationSchema(true))

    it('should reject null locationType', async () => {
      await expect(
        schema.validate({ locationType: null, geoCoords: null, portLocode: null, zipCode: null })
      ).rejects.toThrow()
    })

    it('should reject invalid locationType enum value', async () => {
      await expect(
        schema.validate({ locationType: 'INVALID', geoCoords: null, portLocode: null, zipCode: null })
      ).rejects.toThrow()
    })

    describe('GPS location type', () => {
      it('should require geoCoords', async () => {
        await expect(
          schema.validate({ locationType: LocationType.GPS, geoCoords: null, portLocode: null, zipCode: null })
        ).rejects.toThrow()
      })

      it('should accept valid geoCoords', async () => {
        await expect(
          schema.validate({ locationType: LocationType.GPS, geoCoords: [48.85, 2.35], portLocode: null, zipCode: null })
        ).resolves.toBeTruthy()
      })

      it('should not require portLocode or zipCode', async () => {
        await expect(
          schema.validate({ locationType: LocationType.GPS, geoCoords: [48.85, 2.35], portLocode: null, zipCode: null })
        ).resolves.toBeTruthy()
      })
    })

    describe('PORT location type', () => {
      it('should require portLocode', async () => {
        await expect(
          schema.validate({ locationType: LocationType.PORT, geoCoords: null, portLocode: null, zipCode: null })
        ).rejects.toThrow('Location description is required')
      })

      it('should accept valid portLocode', async () => {
        await expect(
          schema.validate({ locationType: LocationType.PORT, geoCoords: null, portLocode: 'FRPAR', zipCode: null })
        ).resolves.toBeTruthy()
      })

      it('should not require zipCode', async () => {
        await expect(
          schema.validate({ locationType: LocationType.PORT, geoCoords: null, portLocode: 'FRPAR', zipCode: null })
        ).resolves.toBeTruthy()
      })
    })

    describe('COMMUNE location type', () => {
      it('should require zipCode', async () => {
        await expect(
          schema.validate({ locationType: LocationType.COMMUNE, geoCoords: null, portLocode: null, zipCode: null })
        ).rejects.toThrow('Location description is required')
      })

      it('should accept valid zipCode', async () => {
        await expect(
          schema.validate({ locationType: LocationType.COMMUNE, geoCoords: null, portLocode: null, zipCode: '75000' })
        ).resolves.toBeTruthy()
      })

      it('should not require portLocode', async () => {
        await expect(
          schema.validate({ locationType: LocationType.COMMUNE, geoCoords: null, portLocode: null, zipCode: '75000' })
        ).resolves.toBeTruthy()
      })
    })
  })

  describe('when called with no argument', () => {
    const schema = object().shape(getLocationSchema())

    it('should behave like mission not finished (all fields optional)', async () => {
      await expect(
        schema.validate({ locationType: null, geoCoords: null, portLocode: null, zipCode: null })
      ).resolves.toBeTruthy()
    })
  })
})
