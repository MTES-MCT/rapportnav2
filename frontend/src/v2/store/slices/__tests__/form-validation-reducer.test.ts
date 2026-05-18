import { describe, expect, it, beforeEach } from 'vitest'
import { store } from '../..'
import { resetFormValidation, setFormValidation } from '../form-validation-reducer'

describe('form-validation-reducer', () => {
  beforeEach(() => {
    resetFormValidation()
  })

  describe('setFormValidation', () => {
    it('should set isValid to true', () => {
      setFormValidation(true)
      expect(store.state.formValidation.isValid).toBe(true)
    })

    it('should set isValid to false', () => {
      setFormValidation(false)
      expect(store.state.formValidation.isValid).toBe(false)
    })

    it('should overwrite previous value', () => {
      setFormValidation(true)
      setFormValidation(false)
      expect(store.state.formValidation.isValid).toBe(false)
    })
  })

  describe('resetFormValidation', () => {
    it('should clear isValid to undefined', () => {
      setFormValidation(true)
      resetFormValidation()
      expect(store.state.formValidation.isValid).toBeUndefined()
    })
  })
})
