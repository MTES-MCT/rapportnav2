import { useState } from 'react'
import { addMonths, subMonths, startOfMonth, startOfYear, subYears, addYears } from 'date-fns'
import { formatMonthYear, formatYear } from '@common/utils/dates-for-humans.ts'

function useDateRangeNavigator(startDateTimeUtc, timeframe) {
  const initialDate = startDateTimeUtc ? new Date(startDateTimeUtc) : new Date()
  const [currentDate, setCurrentDate] = useState(startOfMonth(initialDate))

  const goToPrevious = () => {
    if (timeframe === 'month') {
      setCurrentDate(prevDate => startOfMonth(subMonths(prevDate, 1)))
    } else if (timeframe === 'year') {
      setCurrentDate(prevDate => subMonths(startOfYear(subYears(prevDate, 1)), 1))
    }
  }

  const goToNext = () => {
    if (timeframe === 'month') {
      setCurrentDate(prevDate => startOfMonth(addMonths(prevDate, 1)))
    } else if (timeframe === 'year') {
      const date = setCurrentDate(prevDate => subMonths(startOfYear(addYears(prevDate, 1)), 1))
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
