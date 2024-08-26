import { describe, expect, test, vi } from 'vitest'
import { render, screen } from '../../../../test-utils.tsx'

import MissionsList from './missions-list.tsx'
import { Mission } from '../../../common/types/env-mission-types.ts'
import { MissionStatusEnum } from '../../../common/types/mission-types.ts'

const openedMission = {
  id: 1,
  startDateTimeUtc: Date.now().toString(),
  status: MissionStatusEnum.IN_PROGRESS
} as unknown as Mission

const closedMission = {
  id: 2,
  startDateTimeUtc: '2022-08-07T12:00:00Z',
  status: MissionStatusEnum.CLOSED
} as unknown as Mission

const endedMission = {
  id: 3,
  startDateTimeUtc: '2022-08-07T12:00:00Z',
  endDateTimeUtc: '2022-08-19T12:00:00Z',
  status: MissionStatusEnum.ENDED
} as unknown as Mission

describe('MissionsList component', () => {
  test('should render the En cours status', () => {
    render(<MissionsList missions={[openedMission]} prefetchMission={vi.fn()} />)
    expect(screen.getByText('En cours', { exact: false })).toBeInTheDocument()
  })
  test('should render the Indisponible status', () => {
    render(<MissionsList missions={[closedMission]} prefetchMission={vi.fn()} />)
    expect(screen.getByText('Indisponible', { exact: false })).toBeInTheDocument()
  })
  test('should render the Terminée status', () => {
    render(<MissionsList missions={[endedMission]} prefetchMission={vi.fn()} />)
    expect(screen.getByText('Terminée', { exact: false })).toBeInTheDocument()
  })
})
