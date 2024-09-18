import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { Observable } from '@apollo/client'
import logTimeLink from './log-time-link'
import localStorageMock from '@common/utils/localstorage-mock.ts'

describe('logTimeLink', () => {
  let mockDate: number
  let dispatchEventSpy: vi.SpyInstance

  beforeEach(() => {
    mockDate = 1631234567890 // Example timestamp
    vi.spyOn(Date.prototype, 'getTime').mockReturnValue(mockDate)
    dispatchEventSpy = vi.spyOn(window, 'dispatchEvent')
    localStorageMock.clear()
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  it('should update localStorage and dispatch event when forwarding data', () => {
    const mockData = { data: { someField: 'someValue' } }
    const mockForward = () =>
      new Observable(observer => {
        observer.next(mockData)
        observer.complete()
      })

    return new Promise<void>((resolve, reject) => {
      logTimeLink.request({} as any, mockForward).subscribe({
        next: result => {
          expect(result).toEqual(mockData)
          expect(localStorageMock.getItem('lastSyncTimestamp')).toBe(mockDate.toString())
          expect(dispatchEventSpy).toHaveBeenCalledWith(
            expect.objectContaining({
              type: 'storage',
              key: 'lastSyncTimestamp',
              newValue: mockDate.toString()
            })
          )
          resolve()
        },
        error: error => reject(error)
      })
    })
  })

  it('should not update localStorage or dispatch event if forward throws an error', () => {
    const mockError = new Error('Test error')
    const mockForward = () =>
      new Observable(observer => {
        observer.error(mockError)
      })

    return new Promise<void>((resolve, reject) => {
      logTimeLink.request({} as any, mockForward).subscribe({
        next: () => reject(new Error('Should not have called next')),
        error: error => {
          expect(error).toBe(mockError)
          expect(localStorageMock.getItem('lastSyncTimestamp')).toBeNull()
          expect(dispatchEventSpy).not.toHaveBeenCalled()
          resolve()
        }
      })
    })
  })

  it('should pass through the operation to forward', () => {
    const mockOperation = { operationName: 'TestOperation' }
    const mockForward = vi.fn(
      () =>
        new Observable(observer => {
          observer.next({})
          observer.complete()
        })
    )

    return new Promise<void>((resolve, reject) => {
      logTimeLink.request(mockOperation as any, mockForward).subscribe({
        next: () => {
          expect(mockForward).toHaveBeenCalledWith(mockOperation)
          resolve()
        },
        error: error => reject(error)
      })
    })
  })
})
