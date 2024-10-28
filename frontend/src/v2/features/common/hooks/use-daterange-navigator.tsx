import { useState } from 'react';
import { THEME } from '@mtes-mct/monitor-ui'

function useDateRangeNavigator(startDateTimeUtc) {
  let initialDate = new Date();
  if (startDateTimeUtc) {
    initialDate = new Date(startDateTimeUtc)
  }
  const [currentDate, setCurrentDate] = useState(initialDate);

  const months = [
    "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
    "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
  ];

  const styles = {
    container: {
      width: '100%',
      height: '79px',
      alignItems: 'center',
      justifyContent: 'center',
      position: 'relative',
      fontSize: '16px',
      fontWeight: 'bold',
      backgroundColor: THEME.color.charcoal,
      color: THEME.color.white,
      marginBottom: '25px'
    },
    previousButton: {
      backgroundColor: 'transparent',
      paddingTop: '5px'
    },
    previousChevron: {
      transform: 'rotate(90deg)'
    },
    nextButton: {
      backgroundColor: 'transparent',
      paddingTop: '5px'
    },
    nextChevron: {
      transform: 'rotate(-90deg)'
    }

  };

  const goToPreviousMonth = () => {
    setCurrentDate((prevDate) => {
      const newDate = new Date(prevDate.getFullYear(), prevDate.getMonth() - 1, 1);
      newDate.setHours(0, 0, 0, 0);
      return newDate;
    });
  };

  const goToNextMonth = () => {
    setCurrentDate((prevDate) => {
      const newDate = new Date(prevDate.getFullYear(), prevDate.getMonth() + 1, 1);
      newDate.setHours(0, 0, 0, 0);
      return newDate;
    });
  };

  const getLastDayOfMonth = () => {
    const endDate = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0);
    endDate.setHours(0, 0, 0, 0);
    return endDate;
  };

  return {
    currentDate,
    formattedDate: `${months[currentDate.getMonth()]} ${currentDate.getFullYear()}`,
    goToPreviousMonth,
    goToNextMonth,
    getLastDayOfMonth,
    styles
  };
}

export default useDateRangeNavigator;
