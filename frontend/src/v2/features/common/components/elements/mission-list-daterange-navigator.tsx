import React, { JSX, useEffect } from 'react'
import { Accent, Button, Icon, THEME } from '@mtes-mct/monitor-ui'
import useDateRangeNavigator from '../../hooks/use-daterange-navigator.tsx'
import styled from 'styled-components'

interface MissionListDateRangeNavigatorProps {
  startDateTimeUtc?: string
  onUpdateCurrentDate?: (date: Date) => void
  exportButton?: JSX.Element
}

const DateRangeContainer = styled.div`
  width: 100%;
  height: 79px;
  align-items: center;
  justify-content: center;
  position: relative;
  font-size: 16px;
  font-weight: bold;
  background-color: ${THEME.color.charcoal};
  color: ${THEME.color.white};
  margin-bottom: 25px;
  display: flex;
`

const MissionListDateRangeNavigator: React.FC<MissionListDateRangeNavigatorProps> = ({startDateTimeUtc,
                                                                                       onUpdateCurrentDate,
                                                                                       exportButton}) => {
  const {
    capitalizedFormattedDate,
    goToPreviousMonth,
    goToNextMonth,
    currentDate
  } = useDateRangeNavigator(startDateTimeUtc)

  useEffect(() => {
    onUpdateCurrentDate?.(currentDate); // Suppression de onUpdateCurrentDate des dépendances
  }, [currentDate]);



  return (
    <DateRangeContainer>
      <button onClick={goToPreviousMonth} data-testid={'previous-button'} style={{backgroundColor: 'transparent',
        paddingTop: '5px'}}>
        <Icon.Chevron style={{transform: 'rotate(90deg)'}}/>
      </button>

      <span style={{textAlign: 'center'}} data-testid={'date-display'}>
        {capitalizedFormattedDate}
      </span>

      <button onClick={goToNextMonth} data-testid={'next-button'} style={{backgroundColor: 'transparent',
        paddingTop: '5px'}}>
        <Icon.Chevron style={{transform: 'rotate(-90deg)'}}/>
      </button>

      <>{exportButton}</>

    </DateRangeContainer>
  )
}

export default MissionListDateRangeNavigator
