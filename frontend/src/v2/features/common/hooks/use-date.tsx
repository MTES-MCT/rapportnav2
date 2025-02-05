import { addMinutes, isValid, parseISO } from 'date-fns'
import { formatInTimeZone } from 'date-fns-tz'
import frLocale from 'date-fns/locale/fr'

const TIME_ZONE = 'Europe/Paris'
const DEFAULT_TIMEZONE = 'Europe/Paris'
const MISSION_NAME_FORMAT = 'yyyy-MM-dd'
const FRENCH_DAY_MONTH_YEAR = 'dd/MM/yyyy'
const EMPTY_FRENCH_DAY_MONTH_YEAR = '--/--/----'
const SHORT_DAY_MONTH = 'dd MMM'
const EMPTY_SHORT_DAY_MONTH = 'n/a'
const SHORT_TIME = 'HH:mm'
const EMPTY_SHORT_TIME = '--:--'
const FRENCH_DAY_MONTH_YEAR_DATETIME = `${SHORT_DAY_MONTH} à ${SHORT_TIME}`
const EMPTY_FRENCH_DAY_MONTH_YEAR_DATETIME = `${EMPTY_SHORT_DAY_MONTH} - ${EMPTY_SHORT_TIME}`

type DateTypes = Date | string | undefined | null

interface DateHook {
  formatTime: (date: DateTypes) => string
  formatShortDate: (date: DateTypes) => string
  formatMissionName: (startDate?: string) => string
  groupByDay: (obj: any[], dateField: string) => any
  formatDateForMissionName: (date: DateTypes) => string
  formatDateForFrenchHumans: (date: DateTypes) => string

  formatDateTimeForFrenchHumans: (date: DateTypes) => string
  postprocessDateFromPicker: (value?: Date) => string
  preprocessDateForPicker: (value?: string | null) => Date | undefined
}

export function useDate(): DateHook {
  const formatDate = (
    date: DateTypes,
    dateFormat: string,
    emptyDateFormat: string,
    timeZone: string = DEFAULT_TIMEZONE
  ): string => {
    if (!date) return emptyDateFormat

    try {
      // Parse the date if it's a string, otherwise use it as is
      const dateObj = typeof date === 'string' ? parseISO(date) : date

      // Use formatInTimeZone to handle the timezone conversion
      return formatInTimeZone(dateObj, timeZone, dateFormat, { locale: frLocale })
    } catch (e) {
      console.error('Error formatting date:', e)
      return emptyDateFormat
    }
  }

  const groupByDay = (obj: any[], dateField: string) => {
    return obj.reduce((groupedObj, subObj) => {
      const day = new Date(subObj[dateField]).toLocaleDateString()
      groupedObj[day] = groupedObj[day] || []
      groupedObj[day].push(subObj)

      return groupedObj
    }, {})
  }

  const formatMissionName = (startDate?: string): string => `Mission #${formatDateForMissionName(startDate)}`

  const formatDateForMissionName = (date: DateTypes): string =>
    formatDate(date, MISSION_NAME_FORMAT, EMPTY_FRENCH_DAY_MONTH_YEAR)

  const formatDateForFrenchHumans = (date: DateTypes): string =>
    formatDate(date, FRENCH_DAY_MONTH_YEAR, EMPTY_FRENCH_DAY_MONTH_YEAR)

  const formatDateTimeForFrenchHumans = (date: DateTypes): string =>
    formatDate(date, FRENCH_DAY_MONTH_YEAR_DATETIME, EMPTY_FRENCH_DAY_MONTH_YEAR_DATETIME)

  const formatShortDate = (date: DateTypes): string => formatDate(date, SHORT_DAY_MONTH, EMPTY_SHORT_DAY_MONTH)

  const formatTime = (date: DateTypes): string => formatDate(date, SHORT_TIME, EMPTY_SHORT_TIME)

  const preprocessDateForPicker = (value?: string | null): Date | undefined => {
    if (!value) return undefined
    let date = new Date(value)
    if (!isValid(date)) date = new Date()
    return new Date(`${formatInTimeZone(date, TIME_ZONE, "yyyy-MM-dd'T'HH:mm:ss.SSS")}Z`)
  }

  const postprocessDateFromPicker = (value?: Date): string => {
    let date = value || new Date()
    if (!isValid(date)) date = new Date()
    return formatInTimeZone(addMinutes(date, new Date().getTimezoneOffset()), TIME_ZONE, "yyyy-MM-dd'T'HH:mm:ss.SSSxxx")
  }

  return {
    formatTime,
    groupByDay,
    formatShortDate,
    formatMissionName,
    formatDateForMissionName,
    formatDateForFrenchHumans,
    preprocessDateForPicker,
    postprocessDateFromPicker,
    formatDateTimeForFrenchHumans
  }
}
