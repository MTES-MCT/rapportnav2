import { useState, useEffect } from 'react';
import { addMonths, subMonths, format, startOfMonth, endOfMonth } from 'date-fns';
import { fr } from 'date-fns/locale';

function useDateRangeNavigator(startDateTimeUtc, onUpdateDateRange) {
  const initialDate = startDateTimeUtc ? new Date(startDateTimeUtc) : new Date();
  const [currentDate, setCurrentDate] = useState(startOfMonth(initialDate));

  const goToPreviousMonth = () => {
    setCurrentDate((prevDate) => startOfMonth(subMonths(prevDate, 1)));
  };

  const goToNextMonth = () => {
    setCurrentDate((prevDate) => startOfMonth(addMonths(prevDate, 1)));
  };

  const formattedDate = format(currentDate, 'MMMM yyyy', { locale: fr });
  const capitalizedFormattedDate = formattedDate.charAt(0).toUpperCase() + formattedDate.slice(1);

  return {
    currentDate,
    capitalizedFormattedDate,
    goToPreviousMonth,
    goToNextMonth,
  };
}

export default useDateRangeNavigator;
