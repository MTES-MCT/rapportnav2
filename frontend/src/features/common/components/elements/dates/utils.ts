import { DateRange } from '@mtes-mct/monitor-ui'
import { formatInTimeZone } from 'date-fns-tz'
import { addMinutes, isValid } from 'date-fns'
import { TIME_ZONE } from '@common/utils/dates-for-machines.ts'

export type Validation = {
  ok: boolean
  message?: string
}

export const validate = (minDate: Date, maxDate: Date, actualDate?: Date): Validation | undefined => {
  if (!actualDate) return undefined
  if (actualDate < minDate || actualDate > maxDate) {
    return {
      ok: false,
      message: 'La date choisie doit être comprise entre les dates de début et de fin de mission'
    }
  }
  return {
    ok: true,
    message: undefined
  }
}

export const validateRange = (minDate: Date, maxDate: Date, actualRange: DateRange): Validation | undefined => {
  if (actualRange[0] < minDate || actualRange[0] > maxDate) {
    return {
      ok: false,
      message: 'La date de début doit être comprise entre les dates de début et de fin de mission'
    }
  } else if (actualRange[1] < minDate || actualRange[1] > maxDate) {
    return {
      ok: false,
      message: 'La date de fin doit être comprise entre les dates de début et de fin de mission'
    }
  } else {
    return {
      ok: true,
      message: undefined
    }
  }
}

// Preprocessing before sending to datepicker
export const preprocessDateForPicker = (date?: Date): Date | undefined => {
  if (!date || !isValid(date)) {
    return
  }
  // Convert the local date to a UTC date that, when converted back to ISO string,
  // will represent the correct local time
  const localISOString = formatInTimeZone(date, TIME_ZONE, "yyyy-MM-dd'T'HH:mm:ss.SSS")
  return new Date(localISOString + 'Z') // Append 'Z' to treat it as UTC
}

// Postprocessing after receiving from datepicker
export const postprocessDateFromPicker = (date?: Date): Date | undefined => {
  if (!date || !isValid(date)) {
    return
  }
  // Get the local timezone offset in minutes
  const localOffset = new Date().getTimezoneOffset()

  // Add the offset to correct the date
  return addMinutes(date, localOffset)
}
