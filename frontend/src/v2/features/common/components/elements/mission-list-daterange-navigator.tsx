import React, { useState } from 'react'
import { Icon, THEME } from '@mtes-mct/monitor-ui'
import useDateRangeNavigator, { handleNextMonth } from '../../hooks/use-daterange-navigator.tsx'
import { FlexboxGrid } from 'rsuite'

interface MissionListDateRangeNavigatorProps {
  startDateTimeUtc?: string
}

const MissionListDateRangeNavigator: React.FC<MissionListDateRangeNavigatorProps> = ({startDateTimeUtc}) => {
  const {
    formattedDate,
    goToPreviousMonth,
    goToNextMonth,
    styles
  } = useDateRangeNavigator(startDateTimeUtc)

  return (
    <FlexboxGrid style={styles.container}>
      <button onClick={goToPreviousMonth} data-testid={'previous-button'} style={styles.previousButton}>
        <Icon.Chevron style={styles.previousChevron}/>
      </button>

      <span style={{textAlign: 'center'}} data-testid={'date-display'}>
        {formattedDate}
      </span>

      <button onClick={goToNextMonth} data-testid={'next-button'} style={styles.nextButton}>
        <Icon.Chevron style={styles.nextChevron}/>
      </button>
    </FlexboxGrid>
  )
}

export default MissionListDateRangeNavigator
