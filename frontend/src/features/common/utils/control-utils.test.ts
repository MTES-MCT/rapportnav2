import { describe, it, expect } from 'vitest'
import { ControlMethod, ControlType } from '@common/types/control-types'
import { VesselTypeEnum } from '@common/types/mission-types'
import { controlMethodToHumanString, vesselTypeToHumanString, getDisabledControlTypes } from './control-utils'

describe('control-utils', () => {
  describe('controlMethodToHumanString', () => {
    it('should return "aérien" for AIR', () => {
      expect(controlMethodToHumanString(ControlMethod.AIR)).toBe('aérien')
    })

    it('should return "à Terre" for LAND', () => {
      expect(controlMethodToHumanString(ControlMethod.LAND)).toBe('à Terre')
    })

    it('should return "en Mer" for SEA', () => {
      expect(controlMethodToHumanString(ControlMethod.SEA)).toBe('en Mer')
    })

    it('should return empty string for undefined', () => {
      expect(controlMethodToHumanString(undefined)).toBe('')
    })

    it('should return empty string for null', () => {
      expect(controlMethodToHumanString(null)).toBe('')
    })
  })

  describe('vesselTypeToHumanString', () => {
    it('should return "Navire de pêche professionnelle" for FISHING', () => {
      expect(vesselTypeToHumanString(VesselTypeEnum.FISHING)).toBe('Navire de pêche professionnelle')
    })

    it('should return "Navire de commerce" for COMMERCIAL', () => {
      expect(vesselTypeToHumanString(VesselTypeEnum.COMMERCIAL)).toBe('Navire de commerce')
    })

    it('should return "Navire de service" for MOTOR', () => {
      expect(vesselTypeToHumanString(VesselTypeEnum.MOTOR)).toBe('Navire de service')
    })

    it('should return "Navire de plaisance professionnelle" for SAILING', () => {
      expect(vesselTypeToHumanString(VesselTypeEnum.SAILING)).toBe('Navire de plaisance professionnelle')
    })

    it('should return "Navire de plaisance de loisir" for SAILING_LEISURE', () => {
      expect(vesselTypeToHumanString(VesselTypeEnum.SAILING_LEISURE)).toBe('Navire de plaisance de loisir')
    })

    it('should return "Navire conchylicole" for SHELLFISH', () => {
      expect(vesselTypeToHumanString(VesselTypeEnum.SHELLFISH)).toBe('Navire conchylicole')
    })

    it('should return empty string for undefined', () => {
      expect(vesselTypeToHumanString(undefined)).toBe('')
    })

    it('should return empty string for null', () => {
      expect(vesselTypeToHumanString(null)).toBe('')
    })

    it('should return empty string for unhandled vessel types', () => {
      expect(vesselTypeToHumanString(VesselTypeEnum.SCHOOL)).toBe('')
      expect(vesselTypeToHumanString(VesselTypeEnum.PASSENGER)).toBe('')
    })
  })

  describe('getDisabledControlTypes', () => {
    it('should return all control types when enabledControlTypes is undefined', () => {
      const result = getDisabledControlTypes(undefined)
      expect(result).toEqual(Object.values(ControlType))
    })

    it('should return all control types when enabledControlTypes is empty', () => {
      const result = getDisabledControlTypes([])
      expect(result).toEqual(Object.values(ControlType))
    })

    it('should return disabled control types excluding enabled ones', () => {
      const enabled = [ControlType.ADMINISTRATIVE, ControlType.SECURITY]
      const result = getDisabledControlTypes(enabled)

      expect(result).not.toContain(ControlType.ADMINISTRATIVE)
      expect(result).not.toContain(ControlType.SECURITY)
      expect(result).toContain(ControlType.GENS_DE_MER)
      expect(result).toContain(ControlType.NAVIGATION)
    })

    it('should return empty array when all control types are enabled', () => {
      const allTypes = Object.values(ControlType)
      const result = getDisabledControlTypes(allTypes)
      expect(result).toEqual([])
    })

    it('should return correct disabled types for single enabled type', () => {
      const enabled = [ControlType.NAVIGATION]
      const result = getDisabledControlTypes(enabled)
      const allTypes = Object.values(ControlType)

      expect(result).toHaveLength(allTypes.length - 1)
      expect(result).not.toContain(ControlType.NAVIGATION)
    })
  })
})