import { describe, it, expect } from 'vitest'
import { isServerError, isUsageError, shouldReportToSentry } from '../error-classification'

describe('error-classification', () => {
  describe('isUsageError', () => {
    it('treats 400 (usage) and 403 (authorization denial / cloisonnement) as expected usage errors', () => {
      expect(isUsageError(400)).toBe(true)
      expect(isUsageError(403)).toBe(true)
    })

    it('does not treat other statuses as usage errors', () => {
      expect(isUsageError(401)).toBe(false)
      expect(isUsageError(404)).toBe(false)
      expect(isUsageError(409)).toBe(false)
      expect(isUsageError(500)).toBe(false)
      expect(isUsageError(undefined)).toBe(false)
    })
  })

  describe('shouldReportToSentry', () => {
    it('does NOT report expected usage errors (400, 403)', () => {
      expect(shouldReportToSentry(400)).toBe(false)
      expect(shouldReportToSentry(403)).toBe(false)
    })

    it('reports server errors, unexpected statuses, and network errors (undefined status)', () => {
      expect(shouldReportToSentry(500)).toBe(true)
      expect(shouldReportToSentry(502)).toBe(true)
      expect(shouldReportToSentry(404)).toBe(true)
      expect(shouldReportToSentry(401)).toBe(true)
      expect(shouldReportToSentry(undefined)).toBe(true)
    })
  })

  describe('isServerError', () => {
    it('is true for 5xx', () => {
      expect(isServerError(500)).toBe(true)
      expect(isServerError(503)).toBe(true)
    })

    it('is false for sub-500 statuses and undefined', () => {
      expect(isServerError(400)).toBe(false)
      expect(isServerError(403)).toBe(false)
      expect(isServerError(499)).toBe(false)
      expect(isServerError(undefined)).toBe(false)
    })
  })
})
