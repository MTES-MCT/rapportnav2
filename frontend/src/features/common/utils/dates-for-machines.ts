import { isValid, parseISO } from 'date-fns'
import { formatInTimeZone } from 'date-fns-tz'

export const TIME_ZONE = 'Europe/Paris' // or use Intl.DateTimeFormat().resolvedOptions().timeZone for the local browser time zone

// Helper function to check if a string is a valid ISO date
export const isISODateString = (value: any): boolean => {
  // Check if the value is a string and not an integer
  if (typeof value !== 'string' || /^\d+$/.test(value)) {
    return false
  }

  // Regular expression for ISO 8601 date format
  const isoDatePattern = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(?:\.\d{1,3})?(?:Z|[+-]\d{2}:?\d{2})$/

  if (!isoDatePattern.test(value)) {
    return false
  }

  // Additional check using date-fns
  return isValid(parseISO(value))
}

// Convert a single UTC ISO string to local ISO string
export const convertSingleUTCToLocalISO = (utcString: string): string => {
  return formatInTimeZone(utcString, TIME_ZONE, "yyyy-MM-dd'T'HH:mm:ss.SSSxxx")
}

// Convert a single local Date to UTC ISO string
export const convertSingleLocalToUTCISO = (localDate: Date): string => {
  return localDate.toISOString()
}

// Recursive function to convert UTC ISO strings to local ISO strings
export const convertUTCToLocalISO = (data: any): any => {
  if (data === null || data === undefined) {
    return data
  }

  if (typeof data !== 'object') {
    if (isISODateString(data)) {
      return convertSingleUTCToLocalISO(data)
    }
    return data
  }

  if (Array.isArray(data)) {
    return data.map(convertUTCToLocalISO)
  }

  const result: { [key: string]: any } = {}
  for (const [key, value] of Object.entries(data)) {
    result[key] = convertUTCToLocalISO(value)
  }

  return result
}

// Recursive function to convert local dates to UTC ISO strings
export const convertLocalToUTCDates = (data: any): any => {
  if (data === null || data === undefined) {
    return data
  }

  if (data instanceof Date) {
    return convertSingleLocalToUTCISO(data)
  }

  if (typeof data !== 'object') {
    return data
  }

  if (Array.isArray(data)) {
    return data.map(convertLocalToUTCDates)
  }

  const result: { [key: string]: any } = {}
  for (const [key, value] of Object.entries(data)) {
    result[key] = convertLocalToUTCDates(value)
  }

  return result
}
