import { Icon, THEME } from '@mtes-mct/monitor-ui'
import React, { JSX, useEffect } from 'react'
import styled from 'styled-components'
import useDateRangeNavigator from '../../hooks/use-daterange-navigator.tsx'

interface ItemListDateRangeNavigatorProps {
  startDateTimeUtc?: string | null
  onUpdateCurrentDate?: (date: Date) => void
  exportButton?: JSX.Element
  timeframe: 'month' | 'year'
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

const ItemListDateRangeNavigator: React.FC<ItemListDateRangeNavigatorProps> = ({
  startDateTimeUtc,
  onUpdateCurrentDate,
  exportButton,
  timeframe
}) => {
  const { formattedDate, goToPrevious, goToNext, currentDate } = useDateRangeNavigator(startDateTimeUtc, timeframe)

  useEffect(() => {
    onUpdateCurrentDate?.(currentDate) // Suppression de onUpdateCurrentDate des dépendances
  }, [currentDate])

  return (
    <DateRangeContainer>
      <button
        onClick={goToPrevious}
        data-testid={'previous-button'}
        style={{
          backgroundColor: 'transparent',
          paddingTop: '5px'
        }}
      >
        <Icon.Chevron style={{ transform: 'rotate(90deg)' }} />
      </button>

      <span style={{ textAlign: 'center' }} data-testid={'date-display'}>
        {formattedDate}
      </span>

      <button
        onClick={goToNext}
        data-testid={'next-button'}
        style={{
          backgroundColor: 'transparent',
          paddingTop: '5px'
        }}
      >
        <Icon.Chevron style={{ transform: 'rotate(-90deg)' }} />
      </button>

      <>{exportButton}</>
    </DateRangeContainer>
  )
}

export default ItemListDateRangeNavigator
