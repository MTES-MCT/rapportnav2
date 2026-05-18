import { renderHook, act } from '@testing-library/react'
import { describe, expect, it, beforeEach, vi } from 'vitest'
import { useFormValidationReporter } from '../use-form-validation-reporter'

const mockSetFormValidation = vi.fn()
const mockResetFormValidation = vi.fn()

vi.mock('../../../../store/slices/form-validation-reducer', () => ({
  setFormValidation: (...args: any[]) => mockSetFormValidation(...args),
  resetFormValidation: (...args: any[]) => mockResetFormValidation(...args)
}))

describe('useFormValidationReporter', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('should return an onFormError callback', () => {
    const { result } = renderHook(() => useFormValidationReporter())
    expect(typeof result.current.onFormError).toBe('function')
  })

  it('should call setFormValidation(true) when errors object is empty', () => {
    const { result } = renderHook(() => useFormValidationReporter())
    act(() => {
      result.current.onFormError({})
    })
    expect(mockSetFormValidation).toHaveBeenCalledWith(true)
  })

  it('should call setFormValidation(false) when errors object has entries', () => {
    const { result } = renderHook(() => useFormValidationReporter())
    act(() => {
      result.current.onFormError({ dates: 'Date de fin avant date de début' })
    })
    expect(mockSetFormValidation).toHaveBeenCalledWith(false)
  })

  it('should call resetFormValidation on unmount', () => {
    const { unmount } = renderHook(() => useFormValidationReporter())
    unmount()
    expect(mockResetFormValidation).toHaveBeenCalled()
  })
})
