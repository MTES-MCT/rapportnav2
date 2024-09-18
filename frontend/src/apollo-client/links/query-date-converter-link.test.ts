import { describe, it, expect, vi } from 'vitest'
import { Observable } from '@apollo/client'
import queryDateConverterLink from './query-date-converter-link.ts'
import * as dateUtils from '@common/utils/dates-for-machines'

// Mock the convertUTCToLocalISO function
vi.mock('@common/utils/dates-for-machines', () => ({
  convertUTCToLocalISO: vi.fn(data => data)
}))

describe('queryDateConverterLink', () => {
  it('should call convertUTCToLocalISO when response has data', () => {
    const mockResponse = {
      data: {
        someField: 'someValue',
        dateField: '2023-09-15T12:00:00Z'
      }
    }

    const mockForward = (operation: any) =>
      new Observable(observer => {
        observer.next(mockResponse)
        observer.complete()
      })

    return new Promise<void>((resolve, reject) => {
      const link = queryDateConverterLink.request({} as any, mockForward)

      link.subscribe({
        next: result => {
          expect(dateUtils.convertUTCToLocalISO).toHaveBeenCalledWith(mockResponse.data)
          expect(result).toEqual(mockResponse)
          resolve()
        },
        error: error => reject(error)
      })
    })
  })

  it('should not call convertUTCToLocalISO when response has no data', () => {
    const mockResponse = { errors: [{ message: 'Some error' }] }

    const mockForward = (operation: any) =>
      new Observable(observer => {
        observer.next(mockResponse)
        observer.complete()
      })

    return new Promise<void>((resolve, reject) => {
      const link = queryDateConverterLink.request({} as any, mockForward)

      link.subscribe({
        next: result => {
          expect(dateUtils.convertUTCToLocalISO).not.toHaveBeenCalled()
          expect(result).toEqual(mockResponse)
          resolve()
        },
        error: error => reject(error)
      })
    })
  })

  afterEach(() => {
    vi.clearAllMocks()
  })
})
