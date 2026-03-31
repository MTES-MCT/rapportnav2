import { describe, it, expect } from 'vitest'
import { object } from 'yup'
import { CONTROL_NAUTICAL_LEISURE_SCHEMA } from '../conrtol-nautical-leisure'
import { LeisureType } from '../../../common/types/leisure-fishing-gear-type'

const schema = object(CONTROL_NAUTICAL_LEISURE_SCHEMA)

describe('CONTROL_NAUTICAL_LEISURE_SCHEMA', () => {
  describe('leisureType', () => {
    it('should require leisureType', async () => {
      const data = {
        nbrOfControl: 5,
        nbrOfControl300m: 2,
        nbrOfControlAmp: 1
      }
      await expect(schema.validate(data)).rejects.toThrow('Type de loisir est requis.')
    })

    it('should accept valid LeisureType enum values', async () => {
      const data = {
        leisureType: LeisureType.SAILING,
        nbrOfControl: 5,
        nbrOfControl300m: 2,
        nbrOfControlAmp: 1
      }
      await expect(schema.validate(data)).resolves.toBeDefined()
    })

    it('should reject invalid leisureType values', async () => {
      const data = {
        leisureType: 'INVALID_TYPE',
        nbrOfControl: 5,
        nbrOfControl300m: 2,
        nbrOfControlAmp: 1
      }
      await expect(schema.validate(data)).rejects.toThrow('Type de loisir invalide.')
    })
  })

  describe('nbrOfControl', () => {
    it('should require nbrOfControl', async () => {
      const data = {
        leisureType: LeisureType.SAILING,
        nbrOfControl300m: 0,
        nbrOfControlAmp: 0
      }
      await expect(schema.validateAt('nbrOfControl', data)).rejects.toThrow('Nombre de contrôles est requis.')
    })
  })

  describe('nbrOfControl300m', () => {
    it('should require nbrOfControl300m', async () => {
      const data = {
        leisureType: LeisureType.SAILING,
        nbrOfControl: 5,
        nbrOfControlAmp: 1
      }
      await expect(schema.validate(data)).rejects.toThrow('Nombre de contrôles 300m est requis.')
    })

    it('should reject nbrOfControl300m greater than nbrOfControl', async () => {
      const data = {
        leisureType: LeisureType.SAILING,
        nbrOfControl: 5,
        nbrOfControl300m: 10,
        nbrOfControlAmp: 1
      }
      await expect(schema.validate(data)).rejects.toThrow('Doit être inférieur au Nombre total de contrôles.')
    })

    it('should accept nbrOfControl300m equal to nbrOfControl', async () => {
      const data = {
        leisureType: LeisureType.SAILING,
        nbrOfControl: 5,
        nbrOfControl300m: 5,
        nbrOfControlAmp: 5
      }
      await expect(schema.validate(data)).resolves.toBeDefined()
    })
  })

  describe('nbrOfControlAmp', () => {
    it('should require nbrOfControlAmp', async () => {
      const data = {
        leisureType: LeisureType.SAILING,
        nbrOfControl: 5,
        nbrOfControl300m: 2
      }
      await expect(schema.validate(data)).rejects.toThrow('Nombre de contrôles Amp est requis.')
    })

    it('should reject nbrOfControlAmp greater than nbrOfControl', async () => {
      const data = {
        leisureType: LeisureType.SAILING,
        nbrOfControl: 5,
        nbrOfControl300m: 2,
        nbrOfControlAmp: 10
      }
      await expect(schema.validate(data)).rejects.toThrow('Doit être inférieur au Nombre total de contrôles.')
    })
  })

  describe('valid data', () => {
    it('should validate complete valid data', async () => {
      const data = {
        leisureType: LeisureType.KITE_SURF,
        nbrOfControl: 10,
        nbrOfControl300m: 3,
        nbrOfControlAmp: 2
      }
      const result = await schema.validate(data)
      expect(result).toEqual(data)
    })
  })
})