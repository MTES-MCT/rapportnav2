import { describe, it, expect, vi } from 'vitest'
import mutationDateConvertedLink from './mutation-date-converter-link.ts'
import * as dateUtils from '@common/utils/dates-for-machines'

// Mock the convertLocalToUTCDates function
vi.mock('@common/utils/dates-for-machines', () => ({
  convertLocalToUTCDates: vi.fn(data => data)
}))

describe('mutationDateConvertedLink', () => {
  it('should call convertLocalToUTCDates when operation has variables', () => {
    const mockVariables = {
      someField: 'someValue',
      dateField: '2023-09-15T14:00:00+02:00'
    }

    const mockOperation = {
      variables: mockVariables
    }

    const mockForward = vi.fn(() => 'forward result')

    mutationDateConvertedLink.request(mockOperation as any, mockForward as any)

    expect(dateUtils.convertLocalToUTCDates).toHaveBeenCalledWith(mockVariables)
    expect(mockForward).toHaveBeenCalledWith(mockOperation)
  })

  it('should not call convertLocalToUTCDates when operation has no variables', () => {
    const mockOperation = {}

    const mockForward = vi.fn(() => 'forward result')

    mutationDateConvertedLink.request(mockOperation as any, mockForward as any)

    expect(dateUtils.convertLocalToUTCDates).not.toHaveBeenCalled()
    expect(mockForward).toHaveBeenCalledWith(mockOperation)
  })

  it('should return the result of forward', () => {
    const mockOperation = {
      variables: { someField: 'someValue' }
    }

    const mockForward = vi.fn(() => 'forward result')

    const result = mutationDateConvertedLink.request(mockOperation as any, mockForward as any)

    expect(result).toBe('forward result')
  })

  afterEach(() => {
    vi.clearAllMocks()
  })
})
