import { parseISO } from 'date-fns'
import { formatInTimeZone } from 'date-fns-tz'
import frLocale from 'date-fns/locale/fr'

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

function formatDate(
  date: DateTypes,
  dateFormat: string,
  emptyDateFormat: string,
  timeZone: string = DEFAULT_TIMEZONE
): string {
  if (!date) {
    return emptyDateFormat
  }

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

const formatDateForMissionName = (date: DateTypes): string =>
  formatDate(date, MISSION_NAME_FORMAT, EMPTY_FRENCH_DAY_MONTH_YEAR)

const formatDateForFrenchHumans = (date: DateTypes): string =>
  formatDate(date, FRENCH_DAY_MONTH_YEAR, EMPTY_FRENCH_DAY_MONTH_YEAR)

const formatDateTimeForFrenchHumans = (date: DateTypes): string =>
  formatDate(date, FRENCH_DAY_MONTH_YEAR_DATETIME, EMPTY_FRENCH_DAY_MONTH_YEAR_DATETIME)

const formatShortDate = (date: DateTypes): string => formatDate(date, SHORT_DAY_MONTH, EMPTY_SHORT_DAY_MONTH)

const formatTime = (date: DateTypes): string => formatDate(date, SHORT_TIME, EMPTY_SHORT_TIME)

export {
  formatDateForMissionName,
  formatDateForFrenchHumans,
  formatDateTimeForFrenchHumans,
  formatShortDate,
  formatTime
}
export * from 'date-fns'
