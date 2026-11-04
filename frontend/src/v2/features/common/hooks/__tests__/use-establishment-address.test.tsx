import { renderHook } from '@testing-library/react'
import { describe, expect, it } from 'vitest'
import { Establishment } from '../../types/etablishment'
import { Address } from '../../types/sati'
import { useEstablishmentAddress } from '../use-establishment-address'

const establishment: Establishment = {
  id: '13',
  name: 'FOE',
  siren: '513691477',
  siret: '51369147700014',
  address: '12 Quai des Chartrons',
  city: 'Bordeaux',
  zipcode: '33000',
  country: 'FRA',
  isForeign: false,
  isHeadquarter: true
}

const address: Address = { street: '1 rue de la mer', town: 'Brest', zipcode: '29200', country: 'FRA' }

const setup = () => renderHook(() => useEstablishmentAddress()).result.current

describe('useEstablishmentAddress', () => {
  describe('establishmentToAddress', () => {
    it('should map the establishment address fields to an address', () => {
      expect(setup().establishmentToAddress(establishment)).toEqual({
        street: '12 Quai des Chartrons',
        town: 'Bordeaux',
        zipcode: '33000',
        country: 'FRA'
      })
    })

    it('should return undefined without establishment', () => {
      expect(setup().establishmentToAddress(undefined)).toBeUndefined()
    })

    it('should keep missing fields undefined', () => {
      expect(setup().establishmentToAddress({ name: 'FOE' })).toEqual({
        street: undefined,
        town: undefined,
        zipcode: undefined,
        country: undefined
      })
    })
  })

  describe('addressToEstablishment', () => {
    it('should write the address on the establishment and keep the other fields', () => {
      const result = setup().addressToEstablishment(address, establishment)

      expect(result).toMatchObject({
        id: '13',
        name: 'FOE',
        siren: '513691477',
        siret: '51369147700014',
        isHeadquarter: true,
        address: '1 rue de la mer',
        city: 'Brest',
        zipcode: '29200',
        country: 'FRA'
      })
    })

    it('should not mutate the given establishment', () => {
      const copy = { ...establishment }
      setup().addressToEstablishment(address, establishment)
      expect(establishment).toEqual(copy)
    })

    it('should clear the address fields when no address is given', () => {
      const result = setup().addressToEstablishment(undefined, establishment)

      expect(result.address).toBeUndefined()
      expect(result.city).toBeUndefined()
      expect(result.zipcode).toBeUndefined()
      expect(result.country).toBeUndefined()
      expect(result.name).toBe('FOE')
    })

    it('should build an establishment from the address alone', () => {
      expect(setup().addressToEstablishment(address)).toEqual({
        address: '1 rue de la mer',
        city: 'Brest',
        zipcode: '29200',
        country: 'FRA'
      })
    })

    it('should round trip with establishmentToAddress', () => {
      const hook = setup()
      expect(hook.establishmentToAddress(hook.addressToEstablishment(address, establishment))).toEqual(address)
    })
  })

  describe('resetAddress', () => {
    it('should clear the address fields and keep the company', () => {
      const result = setup().resetAddress(establishment)

      expect(result).toMatchObject({ id: '13', name: 'FOE', siren: '513691477', siret: '51369147700014' })
      expect(result.address).toBeUndefined()
      expect(result.city).toBeUndefined()
      expect(result.zipcode).toBeUndefined()
      expect(result.country).toBeUndefined()
    })

    it('should accept an undefined establishment', () => {
      expect(setup().resetAddress(undefined)).toEqual({
        address: undefined,
        city: undefined,
        zipcode: undefined,
        country: undefined
      })
    })
  })

  describe('resetCompany', () => {
    it('should clear name, siren, siret and isHeadquarter and keep the rest', () => {
      const result = setup().resetCompany(establishment)

      expect(result.name).toBeUndefined()
      expect(result.siren).toBeUndefined()
      expect(result.siret).toBeUndefined()
      expect(result.isHeadquarter).toBeUndefined()
      expect(result).toMatchObject({
        id: '13',
        isForeign: false,
        address: '12 Quai des Chartrons',
        city: 'Bordeaux',
        zipcode: '33000',
        country: 'FRA'
      })
    })

    it('should not mutate the given establishment', () => {
      const copy = { ...establishment }
      setup().resetCompany(establishment)
      expect(establishment).toEqual(copy)
    })
  })
})
