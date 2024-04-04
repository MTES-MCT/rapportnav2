import { describe, expect, test } from 'vitest'
import { render, screen } from '../../test-utils'

import MissionsList from './missions-list'
import { Mission } from '../../types/env-mission-types'

const openedMission = {
  id: 1,
  startDateTimeUtc: Date.now().toString()
} as Mission

const closedMission = {
  id: 2,
  isClosed: true,
  startDateTimeUtc: '2022-08-07T12:00:00Z'
} as Mission

const endedMission = {
  id: 3,
  startDateTimeUtc: '2022-08-07T12:00:00Z',
  endDateTimeUtc: '2022-08-19T12:00:00Z'
} as Mission

describe('MissionsList component', () => {
  test('should render the brouillon status', () => {
    render(<MissionsList missions={[openedMission]} />)
    expect(screen.getByText('● Brouillon')).toBeInTheDocument()
  })
  test('should render the NA status', () => {
    render(<MissionsList missions={[closedMission]} />)
    expect(screen.getByText('❌ N/A')).toBeInTheDocument()
  })
  test('should render the cloturée status', () => {
    render(<MissionsList missions={[endedMission]} />)
    expect(screen.getByText('✓ Clôturée')).toBeInTheDocument()
  })
})
