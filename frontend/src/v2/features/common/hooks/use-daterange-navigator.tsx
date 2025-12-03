import { useState } from 'react'
import { addMonths, subMonths, startOfMonth, startOfYear, subYears, addYears } from 'date-fns'
import { formatMonthYear, formatYear } from '@common/utils/dates-for-humans.ts'
import { UTCDate } from '@date-fns/utc'

function useDateRangeNavigator(startDateTimeUtc, timeframe) {
  const initialDate = startDateTimeUtc ? new Date(startDateTimeUtc) : new UTCDate()
  const [currentDate, setCurrentDate] = useState(startOfMonth(initialDate))

  const goToPrevious = () => {
    if (timeframe === 'month') {
      setCurrentDate(prevDate => startOfMonth(subMonths(prevDate, 1)))
    } else if (timeframe === 'year') {
      setCurrentDate(prevDate => startOfYear(subYears(prevDate, 1)))
    }
  }

  const goToNext = () => {
    if (timeframe === 'month') {
      setCurrentDate(prevDate => startOfMonth(addMonths(prevDate, 1)))
    } else if (timeframe === 'year') {
      setCurrentDate(prevDate => startOfYear(addYears(prevDate, 1)))
    }
  }

  const formattedDate = timeframe === 'month' ? formatMonthYear(currentDate) : formatYear(currentDate)

  return {
    currentDate,
    formattedDate,
    goToPrevious,
    goToNext
  }
}

export default useDateRangeNavigator
