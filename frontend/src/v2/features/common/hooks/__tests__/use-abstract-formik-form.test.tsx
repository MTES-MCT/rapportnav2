import { renderHook } from '@testing-library/react'
import { vi } from 'vitest'
import { pruneEmptyShells, useAbstractFormik } from '../use-abstract-formik-form'

type FormValue = {
  text1: string
  text2: number
  text3?: string
  boolean1: boolean | null
  boolean2: boolean | null
}
type FormInput = FormValue & {}

describe('UseAbstractFormik', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('should call fromFieldValueToInput while instantiation, and convert boolean null to false', () => {
    const value = {
      text2: 3,
      text1: 'Text1',
      text3: undefined,
      boolean1: null,
      boolean2: null
    }
    const fromFieldValueToInput = vi.fn()
    const fromInputToFieldValue = vi.fn()
    renderHook(() =>
      useAbstractFormik<FormValue, FormInput>(value, fromFieldValueToInput, fromInputToFieldValue, [
        'boolean1',
        'boolean2'
      ])
    )
    expect(fromFieldValueToInput).toHaveBeenCalledTimes(1)
    expect(fromFieldValueToInput).toHaveBeenCalledWith({
      text2: 3,
      text1: 'Text1',
      boolean1: false,
      boolean2: false
    })
  })

  it('should submit changes', () => {
    const fromInputToFieldValue = vi.fn()
    const fromFieldValueToInput = vi.fn()
    const input = {
      text2: 45,
      text1: 'my new text',
      boolean1: false,
      boolean2: false
    }

    const { result } = renderHook(() =>
      useAbstractFormik<FormValue, FormInput>({} as FormValue, fromFieldValueToInput, fromInputToFieldValue, [
        'boolean1',
        'boolean2'
      ])
    )
    result.current.handleSubmit(input)
    expect(fromInputToFieldValue).toHaveBeenCalledTimes(1)
    expect(fromInputToFieldValue).toHaveBeenCalledWith(input)
  })
  it('should not submit if initValue and input are the same', () => {
    const value = {
      text2: 3,
      text1: 'Text1',
      boolean1: null,
      boolean2: null
    }

    const fromInputToFieldValue = vi.fn()
    const fromFieldValueToInput = (value: FormValue) => value as FormInput

    const { result } = renderHook(() =>
      useAbstractFormik<FormValue, FormInput>(value, fromFieldValueToInput, fromInputToFieldValue, [
        'boolean1',
        'boolean2'
      ])
    )
    result.current.handleSubmit({
      text2: 3,
      text1: 'Text1',
      boolean1: false,
      boolean2: false
    })
    expect(fromInputToFieldValue).not.toHaveBeenCalled()
  })

  it('should not submit when the only difference is a phantom shell object created by a checkbox mount-write', () => {
    const value = {
      text2: 3,
      text1: 'Text1',
      boolean1: null,
      boolean2: null
    }

    const fromInputToFieldValue = vi.fn()
    const fromFieldValueToInput = (value: FormValue) => value as FormInput

    const { result } = renderHook(() =>
      useAbstractFormik<FormValue, FormInput>(value, fromFieldValueToInput, fromInputToFieldValue, [
        'boolean1',
        'boolean2'
      ])
    )
    result.current.handleSubmit({
      text2: 3,
      text1: 'Text1',
      boolean1: false,
      boolean2: false,
      nested: { deep: { signature: false } }
    } as unknown as FormInput)
    expect(fromInputToFieldValue).not.toHaveBeenCalled()
  })
})

describe('pruneEmptyShells', () => {
  it('collapses an object whose every property is false/undefined down to undefined', () => {
    expect(pruneEmptyShells({ signature: false })).toBeUndefined()
    expect(pruneEmptyShells({ master: { signature: false } })).toBeUndefined()
    expect(pruneEmptyShells({ vessel: { master: { signature: false } } })).toBeUndefined()
  })

  it('collapses an empty array the same way', () => {
    expect(pruneEmptyShells({ otherInspectors: [] })).toBeUndefined()
  })

  it('keeps an object that has any real (non-default) value alongside a default one', () => {
    expect(pruneEmptyShells({ signature: false, fullName: 'Jean Dupont' })).toEqual({
      signature: false,
      fullName: 'Jean Dupont'
    })
    expect(pruneEmptyShells({ master: { signature: true } })).toEqual({ master: { signature: true } })
  })

  it('leaves top-level primitives untouched', () => {
    expect(pruneEmptyShells(false)).toBe(false)
    expect(pruneEmptyShells('text')).toBe('text')
    expect(pruneEmptyShells(undefined)).toBeUndefined()
  })
})
