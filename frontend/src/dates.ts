import { parseISO, format } from 'date-fns'

function formatDateForFrenchHumans(inputDate?: string): string {
  const emptyDateFormat = '--/--/----'
  if (!inputDate) {
    return emptyDateFormat
  }
  try {
    const dateObj = parseISO(inputDate)
    const formattedDate = format(dateObj, 'dd/MM/yyyy')
    return formattedDate
  } catch (e) {
    return emptyDateFormat
  }
}

export { formatDateForFrenchHumans }
export * from 'date-fns'
