import { renderHook } from '@testing-library/react'
import { vi } from 'vitest'
import { useAbstractFormik } from '../use-abstract-formik-form'

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

  it('should call fromFieldValueToInput while intantiation, and convert boolean null to false', () => {
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

  it('should submit changes if there is not errors', () => {
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
})
