import { format, parseISO } from 'date-fns'
import frLocale from 'date-fns/locale/fr';

const MISSION_NAME_FORMAT = 'yyyy-MM-dd'
const FRENCH_DAY_MONTH_YEAR = 'dd/MM/yyyy'
const EMPTY_FRENCH_DAY_MONTH_YEAR = '--/--/----'
const SHORT_DAY_MONTH = 'dd MMM'
const EMPTY_SHORT_DAY_MONTH = 'n/a'
const SHORT_TIME = 'HH:mm'
const EMPTY_SHORT_TIME = '--:--'
const FRENCH_DAY_MONTH_YEAR_DATETIME = `${SHORT_DAY_MONTH} à ${SHORT_TIME}`
const EMPTY_FRENCH_DAY_MONTH_YEAR_DATETIME = `${EMPTY_SHORT_DAY_MONTH} - ${EMPTY_SHORT_TIME}`
const SERVER_FORMAT = "yyyy-MM-dd'T'HH:mm'Z'"

type DateTypes = Date | string | undefined | null

function toLocalISOString(date: DateTypes = new Date()): string | undefined {
  if (!date) {
    return
  }
  const userTimeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;
  const userTime = new Date(date.toLocaleString('en-US', {timeZone: userTimeZone}));

// Convert user's local time to UTC
  const utcTime = new Date(userTime.getTime() - userTime.getTimezoneOffset() * 60000);

// Use toISOString to get the UTC string
  const isoString = utcTime.toISOString();
  return isoString
}


function formatDate(date: DateTypes, dateFormat: string, emptyDateFormat: string, timeZone: string = 'Europe/Paris'): string {
  if (!date) {
    return emptyDateFormat;
  }

  try {
    // Assuming date is a string in ISO format, parse it to Date
    const dateObj = parseISO(date);

    // Set the timezone explicitly to UTC
    const dateInUTC = new Date(dateObj.getUTCFullYear(), dateObj.getUTCMonth(), dateObj.getUTCDate(), dateObj.getUTCHours(), dateObj.getUTCMinutes(), dateObj.getUTCSeconds());

    // Format the date
    const formattedDate = format(dateInUTC, dateFormat, {locale: frLocale});
    return formattedDate;

  } catch (e) {
    return emptyDateFormat;
  }
}

const formatDateForServers = (date: DateTypes): string =>
  formatDate(date, SERVER_FORMAT, '')


const formatDateForMissionName = (date: DateTypes): string =>
  formatDate(date, MISSION_NAME_FORMAT, EMPTY_FRENCH_DAY_MONTH_YEAR)

const formatDateForFrenchHumans = (date: DateTypes): string =>
  formatDate(date, FRENCH_DAY_MONTH_YEAR, EMPTY_FRENCH_DAY_MONTH_YEAR)

const formatDateTimeForFrenchHumans = (date: DateTypes): string =>
  formatDate(date, FRENCH_DAY_MONTH_YEAR_DATETIME, EMPTY_FRENCH_DAY_MONTH_YEAR_DATETIME)

const formatShortDate = (date: DateTypes): string => formatDate(date, SHORT_DAY_MONTH, EMPTY_SHORT_DAY_MONTH)

const formatTime = (date: DateTypes): string => formatDate(date, SHORT_TIME, EMPTY_SHORT_TIME)

export {
  formatDateForServers,
  formatDateForMissionName,
  formatDateForFrenchHumans,
  formatDateTimeForFrenchHumans,
  formatShortDate,
  formatTime,
  toLocalISOString
}
export * from 'date-fns'
