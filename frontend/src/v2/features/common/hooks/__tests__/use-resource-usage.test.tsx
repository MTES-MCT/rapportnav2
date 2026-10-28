import { renderHook } from '@testing-library/react'
import { describe, expect, it } from 'vitest'
import * as Yup from 'yup'
import { ControlUnitResourceType } from '../../types/control-unit-types.ts'
import { useResourceUsage } from '../use-resource-usage.tsx'

type Row = { type?: ControlUnitResourceType; nbKms?: number | null; nbEngineHours?: number | null }

// The hook returns a `{ resources }` schema fragment; wrap it the same way its consumers do to validate.
const schemaFor = (isMissionFinished: boolean) =>
  Yup.object().shape(renderHook(() => useResourceUsage(isMissionFinished)).result.current.validationSchema)

const isValid = (isMissionFinished: boolean, resources: Row[]) =>
  schemaFor(isMissionFinished).isValidSync({ resources })

const errorsFor = (isMissionFinished: boolean, resources: Row[]): string[] => {
  try {
    schemaFor(isMissionFinished).validateSync({ resources }, { abortEarly: false })
    return []
  } catch (e) {
    return (e as Yup.ValidationError).errors
  }
}

describe('useResourceUsage', () => {
  it('exposes a resources validation schema', () => {
    const { result } = renderHook(() => useResourceUsage(true))
    expect(result.current.validationSchema).toHaveProperty('resources')
  })

  it('memoizes the schema, keeping it stable until isMissionFinished changes', () => {
    const { result, rerender } = renderHook(({ finished }) => useResourceUsage(finished), {
      initialProps: { finished: true }
    })
    const first = result.current.validationSchema

    rerender({ finished: true })
    expect(result.current.validationSchema).toBe(first)

    rerender({ finished: false })
    expect(result.current.validationSchema).not.toBe(first)
  })

  describe('when the mission is not finished', () => {
    it('never requires a usage value', () => {
      expect(isValid(false, [{ type: ControlUnitResourceType.CAR }])).toBe(true)
      expect(isValid(false, [{ type: ControlUnitResourceType.FRIGATE }])).toBe(true)
    })
  })

  describe('when the mission is finished', () => {
    it('requires km for a KM resource (CAR) and reports the message', () => {
      expect(isValid(true, [{ type: ControlUnitResourceType.CAR }])).toBe(false)
      expect(errorsFor(true, [{ type: ControlUnitResourceType.CAR }])).toContain('Km parcourus obligatoire')
    })

    it('accepts a KM resource that carries its value, including zero', () => {
      expect(isValid(true, [{ type: ControlUnitResourceType.CAR, nbKms: 42 }])).toBe(true)
      expect(isValid(true, [{ type: ControlUnitResourceType.CAR, nbKms: 0 }])).toBe(true)
    })

    it('requires engine hours for a boat resource (FRIGATE) and reports the message', () => {
      expect(isValid(true, [{ type: ControlUnitResourceType.FRIGATE }])).toBe(false)
      expect(errorsFor(true, [{ type: ControlUnitResourceType.FRIGATE }])).toContain("Nb d'heures moteur obligatoire")
    })

    it('accepts a boat resource that carries its engine hours', () => {
      expect(isValid(true, [{ type: ControlUnitResourceType.FRIGATE, nbEngineHours: 5 }])).toBe(true)
    })

    it('does not require a value for a non-eligible resource (PEDESTRIAN)', () => {
      expect(isValid(true, [{ type: ControlUnitResourceType.PEDESTRIAN }])).toBe(true)
    })

    it('only checks the field matching the resource kind', () => {
      // A boat needs engine hours, not km; a car needs km, not engine hours.
      expect(isValid(true, [{ type: ControlUnitResourceType.FRIGATE, nbEngineHours: 3 }])).toBe(true)
      expect(isValid(true, [{ type: ControlUnitResourceType.CAR, nbKms: 10 }])).toBe(true)
    })

    it('rejects a negative usage value', () => {
      expect(isValid(true, [{ type: ControlUnitResourceType.CAR, nbKms: -1 }])).toBe(false)
      expect(errorsFor(true, [{ type: ControlUnitResourceType.CAR, nbKms: -1 }])).toContain(
        'La distance parcourue doit être positive'
      )
    })
  })
})
