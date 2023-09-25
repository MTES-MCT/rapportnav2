import { parseISO, format } from 'date-fns'

const FRENCH_DAY_MONTH_YEAR = 'dd/MM/yyyy'
const EMPTY_FRENCH_DAY_MONTH_YEAR = '--/--/----'
const SHORT_DAY_MONTH = 'dd MMM'
const EMPTY_SHORT_DAY_MONTH = 'n/a'
const SHORT_TIME = 'hh:mm'
const EMPTY_SHORT_TIME = '--:--'

type DateTypes = string | undefined | null

function formatDate(date: DateTypes, dateFormat: string, emptyDateFormat: string) {
  if (!date) {
    return emptyDateFormat
  }
  try {
    const dateObj = parseISO(date)
    const formattedDate = format(dateObj, dateFormat)
    return formattedDate
  } catch (e) {
    return emptyDateFormat
  }
}

const formatDateForFrenchHumans = (date: DateTypes): string =>
  formatDate(date, FRENCH_DAY_MONTH_YEAR, EMPTY_FRENCH_DAY_MONTH_YEAR)

const formatShortDate = (date: DateTypes): string => formatDate(date, SHORT_DAY_MONTH, EMPTY_SHORT_DAY_MONTH)

const formatTime = (date: DateTypes): string => formatDate(date, SHORT_TIME, EMPTY_SHORT_TIME)

export { formatDateForFrenchHumans, formatShortDate, formatTime }
export * from 'date-fns'
