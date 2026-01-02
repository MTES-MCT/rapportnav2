import { ControlMethod, ControlResult, ControlType } from '@common/types/control-types'
import { describe, expect, it } from 'vitest'
import { useControlRegistry } from '../use-control-registry.tsx'

describe('useControlRegistry', () => {
  const hook = useControlRegistry()

  describe('getControlType', () => {
    it('should return the label for a valid control type', () => {
      expect(hook.getControlType(ControlType.NAVIGATION)).toBe('Respect des règles de navigation')
      expect(hook.getControlType(ControlType.ADMINISTRATIVE)).toBe('Contrôle administratif navire')
      expect(hook.getControlType(ControlType.GENS_DE_MER)).toBe('Contrôle administratif gens de mer')
      expect(hook.getControlType(ControlType.SECURITY)).toBe('Équipements et respect des normes de sécurité')
    })

    it('should return empty string when type is undefined', () => {
      expect(hook.getControlType(undefined)).toBe('')
    })

    it('should return empty string when called without arguments', () => {
      expect(hook.getControlType()).toBe('')
    })
  })

  describe('getControlMethod', () => {
    it('should return the label for a valid control method', () => {
      expect(hook.getControlMethod(ControlMethod.SEA)).toBe('en Mer')
      expect(hook.getControlMethod(ControlMethod.AIR)).toBe('aérien')
      expect(hook.getControlMethod(ControlMethod.LAND)).toBe('à Terre')
    })

    it('should return empty string when method is undefined', () => {
      expect(hook.getControlMethod(undefined)).toBe('')
    })

    it('should return empty string when called without arguments', () => {
      expect(hook.getControlMethod()).toBe('')
    })
  })

  describe('getControlResultOptions', () => {
    it('should return all options including NOT_CONCERNED when withExtra is true', () => {
      const options = hook.getControlResultOptions(true)
      expect(options).toHaveLength(4)
      expect(options.map(o => o.value)).toContain(ControlResult.NOT_CONCERNED)
      expect(options.find(o => o.value === ControlResult.NOT_CONCERNED)?.label).toBe('Non concerné')
    })

    it('should exclude NOT_CONCERNED when withExtra is false', () => {
      const options = hook.getControlResultOptions(false)
      expect(options).toHaveLength(3)
      expect(options.map(o => o.value)).not.toContain(ControlResult.NOT_CONCERNED)
    })

    it('should exclude NOT_CONCERNED when called without arguments', () => {
      const options = hook.getControlResultOptions()
      expect(options).toHaveLength(3)
      expect(options.map(o => o.value)).not.toContain(ControlResult.NOT_CONCERNED)
    })

    it('should return correct labels for all control results', () => {
      const options = hook.getControlResultOptions(true)
      expect(options.find(o => o.value === ControlResult.YES)?.label).toBe('Oui')
      expect(options.find(o => o.value === ControlResult.NO)?.label).toBe('Non')
      expect(options.find(o => o.value === ControlResult.NOT_CONTROLLED)?.label).toBe('Non contrôlé')
    })
  })

  describe('getDisabledControlTypes', () => {
    it('should return all control types when enabledControlTypes is undefined', () => {
      const disabled = hook.getDisabledControlTypes(undefined)
      expect(disabled).toHaveLength(11)
      expect(disabled).toContain(ControlType.NAVIGATION)
      expect(disabled).toContain(ControlType.ADMINISTRATIVE)
      expect(disabled).toContain(ControlType.GENS_DE_MER)
      expect(disabled).toContain(ControlType.SECURITY)

      expect(disabled).toContain(ControlType.OTHER)
      expect(disabled).toContain(ControlType.INN_ACTIVITY)
      expect(disabled).toContain(ControlType.SECTOR)
      expect(disabled).toContain(ControlType.TRANSPORT)
      expect(disabled).toContain(ControlType.LANDING_OBLIGATION)
      expect(disabled).toContain(ControlType.TECHNICAL_MEASURE)
      expect(disabled).toContain(ControlType.FISHING_REPORTING_OBLIGATION)
    })

    it('should return all control types when called without arguments', () => {
      const disabled = hook.getDisabledControlTypes()
      expect(disabled).toHaveLength(11)
    })

    it('should exclude enabled control types from disabled list', () => {
      const disabled = hook.getDisabledControlTypes([ControlType.NAVIGATION, ControlType.SECURITY])
      expect(disabled).toHaveLength(9)
      expect(disabled).not.toContain(ControlType.NAVIGATION)
      expect(disabled).not.toContain(ControlType.SECURITY)
      expect(disabled).toContain(ControlType.ADMINISTRATIVE)
      expect(disabled).toContain(ControlType.GENS_DE_MER)
    })

    it('should return empty array when all control types are enabled', () => {
      const disabled = hook.getDisabledControlTypes([
        ControlType.NAVIGATION,
        ControlType.ADMINISTRATIVE,
        ControlType.GENS_DE_MER,
        ControlType.SECURITY,
        ControlType.INN_ACTIVITY,
        ControlType.LANDING_OBLIGATION,
        ControlType.OTHER,
        ControlType.SECTOR,
        ControlType.TECHNICAL_MEASURE,
        ControlType.TRANSPORT,
        ControlType.FISHING_REPORTING_OBLIGATION
      ])
      expect(disabled).toHaveLength(0)
    })
  })

  describe('getRadios', () => {
    it('should return correct radios for ADMINISTRATIVE type', () => {
      const radios = hook.getRadios(ControlType.ADMINISTRATIVE)
      expect(radios).toHaveLength(3)
      expect(radios[0]).toEqual({
        name: 'compliantOperatingPermit',
        label: 'Permis de mise en exploitation (autorisation à pêcher) conforme'
      })
      expect(radios[1]).toEqual({
        name: 'upToDateNavigationPermit',
        label: 'Permis de navigation à jour'
      })
      expect(radios[2]).toEqual({
        name: 'compliantSecurityDocuments',
        label: 'Titres de sécurité conformes'
      })
    })

    it('should return correct radios for GENS_DE_MER type', () => {
      const radios = hook.getRadios(ControlType.GENS_DE_MER)
      expect(radios).toHaveLength(3)
      expect(radios[0].name).toEqual('staffOutnumbered')
      expect(radios[0].label).toMatch('effectif conforme au nombre de personnes à bord')
      expect(radios[1]).toEqual({
        name: 'upToDateMedicalCheck',
        label: 'Aptitudes médicales ; Visites médicales à jour'
      })
      expect(radios[2]).toEqual({
        name: 'knowledgeOfFrenchLawAndLanguage',
        label: 'Connaissance suffisante de la langue et de la loi français (navires fr/esp)',
        extra: true
      })
    })

    it('should return empty array for NAVIGATION type', () => {
      const radios = hook.getRadios(ControlType.NAVIGATION)
      expect(radios).toEqual([])
    })

    it('should return empty array for SECURITY type', () => {
      const radios = hook.getRadios(ControlType.SECURITY)
      expect(radios).toEqual([])
    })

    it('should return empty array when controlType is undefined', () => {
      const radios = hook.getRadios(undefined)
      expect(radios).toEqual([])
    })

    it('should return empty array when called without arguments', () => {
      const radios = hook.getRadios()
      expect(radios).toEqual([])
    })
  })

  describe('getEmptyValues', () => {
    it('should return default empty values for NAVIGATION type', () => {
      const emptyValues = hook.getEmptyValues(ControlType.NAVIGATION)
      expect(emptyValues).toEqual({
        infractions: [],
        observations: undefined
      })
    })

    it('should return default empty values for SECURITY type', () => {
      const emptyValues = hook.getEmptyValues(ControlType.SECURITY)
      expect(emptyValues).toEqual({
        infractions: [],
        observations: undefined
      })
    })

    it('should return ADMINISTRATIVE empty values for ADMINISTRATIVE type', () => {
      const emptyValues = hook.getEmptyValues(ControlType.ADMINISTRATIVE)
      expect(emptyValues).toEqual({
        infractions: [],
        observations: undefined,
        compliantOperatingPermit: undefined,
        upToDateNavigationPermit: undefined,
        compliantSecurityDocuments: undefined
      })
    })

    it('should return GENS_DE_MER empty values for GENS_DE_MER type', () => {
      const emptyValues = hook.getEmptyValues(ControlType.GENS_DE_MER)
      expect(emptyValues).toEqual({
        infractions: [],
        observations: undefined,
        staffOutnumbered: undefined,
        upToDateMedicalCheck: undefined,
        knowledgeOfFrenchLawAndLanguage: undefined
      })
    })

    it('should return default empty values when controlType is undefined', () => {
      const emptyValues = hook.getEmptyValues(undefined)
      expect(emptyValues).toEqual({
        infractions: [],
        observations: undefined
      })
    })

    it('should return default empty values when called without arguments', () => {
      const emptyValues = hook.getEmptyValues()
      expect(emptyValues).toEqual({
        infractions: [],
        observations: undefined
      })
    })
  })

  describe('controlTypeOptions', () => {
    it('should return all control type options with correct labels', () => {
      expect(hook.controlTypeOptions).toHaveLength(11)
      expect(hook.controlTypeOptions).toContainEqual({
        value: ControlType.NAVIGATION,
        label: 'Respect des règles de navigation'
      })
      expect(hook.controlTypeOptions).toContainEqual({
        value: ControlType.ADMINISTRATIVE,
        label: 'Contrôle administratif navire'
      })
      expect(hook.controlTypeOptions).toContainEqual({
        value: ControlType.GENS_DE_MER,
        label: 'Contrôle administratif gens de mer'
      })
      expect(hook.controlTypeOptions).toContainEqual({
        value: ControlType.SECURITY,
        label: 'Équipements et respect des normes de sécurité'
      })
    })
  })

  describe('controlResultOptions', () => {
    it('should exclude NOT_CONCERNED from default control result options', () => {
      expect(hook.controlResultOptions).toHaveLength(3)
      expect(hook.controlResultOptions.map(o => o.value)).not.toContain(ControlResult.NOT_CONCERNED)
    })
  })

  describe('controlResultOptionsExtra', () => {
    it('should include NOT_CONCERNED in extra control result options', () => {
      expect(hook.controlResultOptionsExtra).toHaveLength(4)
      expect(hook.controlResultOptionsExtra.map(o => o.value)).toContain(ControlResult.NOT_CONCERNED)
    })
  })
})
