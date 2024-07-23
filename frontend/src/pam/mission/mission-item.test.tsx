import { describe, expect, test, vi } from 'vitest'
import { render, screen } from '../../test-utils'

import {Mission} from '../../types/mission-types.ts'
import { CompletenessForStatsStatusEnum, MissionStatusEnum } from '../../types/mission-types.ts'
import MissionItem from './mission-item.tsx'
import { fireEvent } from '@testing-library/react'


const completeMission = {
  id: 3,
  startDateTimeUtc: '2022-08-07T12:00:00Z',
  endDateTimeUtc: '2022-08-19T12:00:00Z',
  status: MissionStatusEnum.ENDED,
  completenessForStats: {
    status: CompletenessForStatsStatusEnum.COMPLETE
  }
} as unknown as Mission

const incompleteMission = {
  id: 3,
  startDateTimeUtc: '2022-08-07T12:00:00Z',
  endDateTimeUtc: '2022-08-19T12:00:00Z',
  status: MissionStatusEnum.ENDED,
  completenessForStats: {
    status: CompletenessForStatsStatusEnum.INCOMPLETE
  }
} as unknown as Mission

describe('MissionsItem component', () => {
  test('should render the Complété status', () => {
    render(<MissionItem mission={completeMission} prefetchMission={vi.fn()} />)
    expect(screen.getByText('Complété', { exact: false })).toBeInTheDocument()
  })
})

/*describe('Export mission odt button', () => {
  test('should render the Exporter le rapport de mission button', () => {
    // Render the MissionItem component
    const { container } = render(<MissionItem mission={completeMission} prefetchMission={vi.fn()} />)

    // Find the root element of the MissionItem component
    const missionItemElement = container.firstChild

    // Ensure the button is not in the document before the hover event
    expect(screen.queryByText('Exporter le rapport de mission', { exact: false })).not.toBeInTheDocument()

    // Ensure the element is not null before firing the mouseOver event
    if (missionItemElement) {
      fireEvent.mouseOver(missionItemElement)
    } else {
      throw new Error('MissionItem element not found')
    }

    // Expect the button to be in the document after the hover event
    expect(screen.getByText('Exporter le rapport de mission', { exact: false })).toBeInTheDocument()
  })
})*/

/*describe('Export mission odt button not available', () => {
  test('should render the Exporter le rapport de mission button', () => {
    render(<MissionItem mission={incompleteMission} prefetchMission={vi.fn()} />)
    expect(screen.getByText('Exporter le rapport de mission', { exact: false })).not.toBeInTheDocument()
  })
})*/
