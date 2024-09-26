import { formatInTimeZone } from 'date-fns-tz'
import { addMinutes, isValid } from 'date-fns'
import { TIME_ZONE } from '@common/utils/dates-for-machines.ts'
import { isNaN } from 'lodash'

export type DatePickerValidation = {
  ok: boolean
  message?: string
  field?: string
}

export const validate = (
  allowedRange: [Date | undefined, Date | undefined],
  date?: Date | undefined
): DatePickerValidation | undefined => {
  if (!date || isNaN(date.getTime())) {
    return {
      ok: false,
      message: 'La date est requise'
    }
  }
  if (allowedRange) {
    if (date < allowedRange[0] || date > allowedRange[1]) {
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
  return {
    ok: true,
    message: undefined
  }
}

export const validateRange = (
  actualRange: [Date | undefined, Date | undefined],
  allowedRange?: [Date | undefined, Date | undefined]
): DatePickerValidation | undefined => {
  if (!actualRange[0]) {
    return {
      ok: false,
      field: 'startDate',
      message: 'La date de début est requise'
    }
  } else if (!actualRange[1]) {
    return {
      ok: false,
      field: 'endDate',
      message: 'La date de fin est requise'
    }
  }

  if (allowedRange) {
    if (actualRange[0] < allowedRange[0] || actualRange[0] > allowedRange[1]) {
      return {
        ok: false,
        field: 'startDate',
        message: 'La date de début doit être comprise entre les dates de début et de fin de mission'
      }
    } else if (actualRange[1] < allowedRange[0] || actualRange[1] > allowedRange[1]) {
      return {
        ok: false,
        field: 'endDate',
        message: 'La date de fin doit être comprise entre les dates de début et de fin de mission'
      }
    } else if (actualRange[1] < actualRange[0]) {
      return {
        ok: false,
        field: 'endDate',
        message: "La date de fin doit être postérieure à la date de début de l'action"
      }
    } else {
      return {
        ok: true,
        message: undefined
      }
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
