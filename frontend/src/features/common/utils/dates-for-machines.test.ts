import {
  isISODateString,
  convertSingleUTCToLocalISO,
  convertSingleLocalToUTCISO,
  convertUTCToLocalISO,
  convertLocalToUTCDates
} from './dates-for-machines.ts'

describe('dates-for-machines', () => {
  describe('isISODateString', () => {
    it('should return true for valid ISO string', () => {
      const validISO = '2023-09-01T10:15:30Z'
      expect(isISODateString(validISO)).toBe(true)
    })

    it('should return false for invalid ISO string', () => {
      const invalidISO = '2023-09-01 10:15:30'
      expect(isISODateString(invalidISO)).toBe(false)
    })

    it('should return false for integer strings', () => {
      const integerString = '1234567890'
      expect(isISODateString(integerString)).toBe(false)
    })

    it('should return false for non-string inputs', () => {
      const nonString = 12345
      expect(isISODateString(nonString)).toBe(false)
    })

    it('should return false for empty strings', () => {
      const emptyString = ''
      expect(isISODateString(emptyString)).toBe(false)
    })
  })

  describe('convertSingleUTCToLocalISO', () => {
    it('should convert UTC ISO string to local ISO string in specified timezone', () => {
      const utcString = '2023-09-01T10:15:30Z'
      const result = convertSingleUTCToLocalISO(utcString)
      expect(result).toBe('2023-09-01T12:15:30.000+02:00') // Example result for Europe/Paris
    })
  })

  describe('convertSingleLocalToUTCISO', () => {
    it('should convert local Date to UTC ISO string', () => {
      const localDate = new Date('2023-09-01T12:15:30+02:00')
      const result = convertSingleLocalToUTCISO(localDate)
      expect(result).toBe('2023-09-01T10:15:30.000Z')
    })
  })

  describe('convertUTCToLocalISO', () => {
    it('should convert single ISO date string from UTC to local', () => {
      const data = '2023-09-01T10:15:30Z'
      const result = convertUTCToLocalISO(data)
      expect(result).toBe('2023-09-01T12:15:30.000+02:00')
    })

    it('should convert an array of ISO date strings from UTC to local', () => {
      const data = ['2023-09-01T10:15:30Z', '2023-09-01T11:00:00Z']
      const result = convertUTCToLocalISO(data)
      expect(result).toEqual(['2023-09-01T12:15:30.000+02:00', '2023-09-01T13:00:00.000+02:00'])
    })

    it('should convert nested objects containing ISO date strings from UTC to local', () => {
      const data = { date: '2023-09-01T10:15:30Z', nested: { date: '2023-09-01T11:00:00Z' } }
      const result = convertUTCToLocalISO(data)
      expect(result).toEqual({
        date: '2023-09-01T12:15:30.000+02:00',
        nested: { date: '2023-09-01T13:00:00.000+02:00' }
      })
    })

    it('should return non-date values unchanged', () => {
      const data = { name: 'John', age: 30 }
      const result = convertUTCToLocalISO(data)
      expect(result).toEqual(data)
    })
  })

  describe('convertLocalToUTCDates', () => {
    it('should convert single Date to UTC ISO string', () => {
      const localDate = new Date('2023-09-01T12:15:30+02:00')
      const result = convertLocalToUTCDates(localDate)
      expect(result).toBe('2023-09-01T10:15:30.000Z')
    })

    it('should convert an array of Date objects to UTC ISO strings', () => {
      const data = [new Date('2023-09-01T12:15:30+02:00'), new Date('2023-09-01T13:00:00+02:00')]
      const result = convertLocalToUTCDates(data)
      expect(result).toEqual(['2023-09-01T10:15:30.000Z', '2023-09-01T11:00:00.000Z'])
    })

    it('should convert nested objects containing Date objects to UTC ISO strings', () => {
      const data = {
        date: new Date('2023-09-01T12:15:30+02:00'),
        nested: { date: new Date('2023-09-01T13:00:00+02:00') }
      }
      const result = convertLocalToUTCDates(data)
      expect(result).toEqual({
        date: '2023-09-01T10:15:30.000Z',
        nested: { date: '2023-09-01T11:00:00.000Z' }
      })
    })

    it('should return non-date values unchanged', () => {
      const data = { name: 'John', age: 30 }
      const result = convertLocalToUTCDates(data)
      expect(result).toEqual(data)
    })
  })
})
