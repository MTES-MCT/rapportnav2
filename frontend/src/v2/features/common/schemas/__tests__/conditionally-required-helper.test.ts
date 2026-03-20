import { number, object, string } from 'yup'
import conditionallyRequired from '../conditionally-required-helper'

describe('conditionallyRequired', () => {
  describe('when isRequired is false', () => {
    it('should return base schema without required validation', async () => {
      const schema = object().shape({
        name: conditionallyRequired(
          () => string().nullable(),
          [],
          true,
          'Name is required'
        )(false)
      })

      // Should pass with null value
      await expect(schema.validate({ name: null })).resolves.toEqual({ name: null })

      // Should pass with undefined value
      await expect(schema.validate({ name: undefined })).resolves.toEqual({ name: undefined })
    })

    it('should not apply required even with form field dependency', async () => {
      const schema = object().shape({
        type: string(),
        value: conditionallyRequired(
          () => number().nullable(),
          ['type'],
          (val: string) => val === 'active',
          'Value is required when type is active'
        )(false)
      })

      // Should pass even when type is 'active' and value is null
      await expect(schema.validate({ type: 'active', value: null })).resolves.toEqual({
        type: 'active',
        value: null
      })
    })
  })

  describe('when isRequired is true and no form field dependency (empty array)', () => {
    it('should apply required validation directly', async () => {
      const schema = object().shape({
        name: conditionallyRequired(
          () => string().nullable(),
          [],
          true,
          'Name is required'
        )(true)
      })

      // Should fail with null value
      await expect(schema.validate({ name: null })).rejects.toThrow('Name is required')

      // Should fail with undefined value
      await expect(schema.validate({ name: undefined })).rejects.toThrow('Name is required')

      // Should pass with valid value
      await expect(schema.validate({ name: 'John' })).resolves.toEqual({ name: 'John' })
    })

    it('should apply transformThen when provided', async () => {
      const schema = object().shape({
        count: conditionallyRequired(
          () => number().nullable(),
          [],
          true,
          'Count is required',
          schema => schema.min(1, 'Count must be at least 1')
        )(true)
      })

      // Should fail with 0 due to min(1)
      await expect(schema.validate({ count: 0 })).rejects.toThrow('Count must be at least 1')

      // Should pass with value >= 1
      await expect(schema.validate({ count: 5 })).resolves.toEqual({ count: 5 })
    })
  })

  describe('when isRequired is true with form field dependency', () => {
    it('should apply required when condition matches', async () => {
      const schema = object().shape({
        isActive: string(),
        value: conditionallyRequired(
          () => number().nullable(),
          ['isActive'],
          (val: string) => val === 'yes',
          'Value is required when active'
        )(true)
      })

      // Should fail when isActive is 'yes' and value is null
      await expect(schema.validate({ isActive: 'yes', value: null })).rejects.toThrow(
        'Value is required when active'
      )

      // Should pass when isActive is 'yes' and value is provided
      await expect(schema.validate({ isActive: 'yes', value: 10 })).resolves.toEqual({
        isActive: 'yes',
        value: 10
      })
    })

    it('should not apply required when condition does not match', async () => {
      const schema = object().shape({
        isActive: string(),
        value: conditionallyRequired(
          () => number().nullable(),
          ['isActive'],
          (val: string) => val === 'yes',
          'Value is required when active'
        )(true)
      })

      // Should pass when isActive is 'no' even with null value
      await expect(schema.validate({ isActive: 'no', value: null })).resolves.toEqual({
        isActive: 'no',
        value: null
      })
    })

    it('should apply transformOtherwise when condition does not match', async () => {
      const schema = object().shape({
        isActive: string(),
        value: conditionallyRequired(
          () => number().nullable(),
          ['isActive'],
          (val: string) => val === 'yes',
          'Value is required when active',
          undefined,
          schema => schema.max(100, 'Value must be at most 100 when inactive')
        )(true)
      })

      // Should apply max(100) when isActive is 'no'
      await expect(schema.validate({ isActive: 'no', value: 150 })).rejects.toThrow(
        'Value must be at most 100 when inactive'
      )

      // Should pass when within limit
      await expect(schema.validate({ isActive: 'no', value: 50 })).resolves.toEqual({
        isActive: 'no',
        value: 50
      })
    })

    it('should work with boolean condition on boolean field', async () => {
      const schema = object().shape({
        enabled: string(),
        name: conditionallyRequired(
          () => string().nullable(),
          ['enabled'],
          'yes', // condition matches when enabled === 'yes'
          'Name is required when enabled'
        )(true)
      })

      // Should require when enabled matches the condition value
      await expect(schema.validate({ enabled: 'yes', name: null })).rejects.toThrow(
        'Name is required when enabled'
      )

      // Should not require when enabled doesn't match
      await expect(schema.validate({ enabled: 'no', name: null })).resolves.toEqual({
        enabled: 'no',
        name: null
      })
    })

    it('should work with multiple dependencies', async () => {
      const schema = object().shape({
        type: string(),
        status: string(),
        value: conditionallyRequired(
          () => number().nullable(),
          ['type', 'status'],
          (type: string, status: string) => type === 'premium' && status === 'active',
          'Value is required for active premium'
        )(true)
      })

      // Should require when both conditions match
      await expect(
        schema.validate({ type: 'premium', status: 'active', value: null })
      ).rejects.toThrow('Value is required for active premium')

      // Should not require when only one condition matches
      await expect(
        schema.validate({ type: 'premium', status: 'inactive', value: null })
      ).resolves.toEqual({
        type: 'premium',
        status: 'inactive',
        value: null
      })
    })
  })

  describe('with string dependsOn (not array)', () => {
    it('should work with single string dependency', async () => {
      const schema = object().shape({
        status: string(),
        reason: conditionallyRequired(
          () => string().nullable(),
          'status',
          (val: string) => val === 'rejected',
          'Reason is required when rejected'
        )(true)
      })

      // Should require reason when status is rejected
      await expect(schema.validate({ status: 'rejected', reason: null })).rejects.toThrow(
        'Reason is required when rejected'
      )

      // Should not require reason when status is approved
      await expect(schema.validate({ status: 'approved', reason: null })).resolves.toEqual({
        status: 'approved',
        reason: null
      })
    })
  })

  describe('edge cases', () => {
    it('should treat empty string in dependsOn as no dependency', async () => {
      const schema = object().shape({
        name: conditionallyRequired(
          () => string().nullable(),
          '',
          true,
          'Name is required'
        )(true)
      })

      // Should apply required directly (no form field dependency)
      await expect(schema.validate({ name: null })).rejects.toThrow('Name is required')
    })

    it('should handle default error message when not provided', async () => {
      const schema = object().shape({
        name: conditionallyRequired(
          () => string().nullable(),
          [],
          true
        )(true)
      })

      // Should fail with default yup required message
      await expect(schema.validate({ name: null })).rejects.toThrow()
    })
  })
})
