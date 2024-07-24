import { describe, expect, test, vi } from 'vitest'
import { render, screen } from '../../test-utils'

import {Mission} from '../../types/mission-types.ts'
import { CompletenessForStatsStatusEnum, MissionStatusEnum } from '../../types/mission-types.ts'
import MissionItem from './mission-item.tsx'
import { fireEvent } from '@testing-library/react'
import useIsMissionCompleteForStats from './use-is-mission-complete-for-stats.tsx'


const mission = {
  id: 3,
  startDateTimeUtc: '2022-08-07T12:00:00Z',
  endDateTimeUtc: '2022-08-19T12:00:00Z',
  status: MissionStatusEnum.ENDED,
  completenessForStats: {
    status: CompletenessForStatsStatusEnum.COMPLETE
  }
} as unknown as Mission


vi.mock('./use-is-mission-complete-for-stats');
const mockedUseIsMissionCompleteForStats = useIsMissionCompleteForStats as jest.Mock;


describe('Export mission odt button', () => {
  test('should render the Exporter le rapport de mission button on mouse over', () => {
    mockedUseIsMissionCompleteForStats.mockReturnValue(true);

    const { container } = render(<MissionItem mission={mission} prefetchMission={vi.fn()} />)
    const missionItemElement = container.firstChild

    expect(screen.queryByText('Exporter le rapport de la mission', { exact: false })).not.toBeInTheDocument()
    fireEvent.mouseOver(missionItemElement)
    expect(screen.getByText('Exporter le rapport de la mission', { exact: false })).toBeInTheDocument()
  })
})

describe('Export mission odt button not available', () => {
  test('should not render the Exporter le rapport de mission button on mouse over', () => {
    mockedUseIsMissionCompleteForStats.mockReturnValue(false);

    const { container } = render(<MissionItem mission={mission} prefetchMission={vi.fn()} />)
    const missionItemElement = container.firstChild
    fireEvent.mouseOver(missionItemElement)
    expect(screen.queryByText('Exporter le rapport de la mission', { exact: false })).not.toBeInTheDocument()
  })
})
