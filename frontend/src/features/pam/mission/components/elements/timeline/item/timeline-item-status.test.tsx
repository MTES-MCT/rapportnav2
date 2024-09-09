import { render, screen } from '../../../../../../../test-utils.tsx'
import { Action } from '@sentry/react/types/types'
import { ActionTypeEnum, MissionSourceEnum } from '@common/types/env-mission-types.ts'
import {
  ActionStatusType,
  ActionStatus as ActionStatusBaseType,
  ActionStatusReason
} from '../../../../../../common/types/action-types.ts'
import ActionStatus from './timeline-item-status.tsx'

const actionMock = {
  id: '1',
  missionId: 1,
  type: ActionTypeEnum.CONTROL,
  source: MissionSourceEnum.RAPPORTNAV,
  status: ActionStatusType.DOCKED,
  startDateTimeUtc: '2022-01-01T00:00:00Z',
  endDateTimeUtc: '2022-01-01T01:00:00Z',
  summaryTags: undefined,
  controlsToComplete: undefined,
  data: {
    startDateTimeUtc: '2022-01-01T00:00:00Z',
    endDateTimeUtc: '2022-01-01T01:00:00Z',
    status: ActionStatusType.UNAVAILABLE,
    reason: ActionStatusReason.TECHNICAL,
    observations: 'obs'
  } as any as ActionStatusBaseType
}
const previousActionMock = {
  id: '1',
  missionId: 1,
  type: ActionTypeEnum.CONTROL,
  source: MissionSourceEnum.RAPPORTNAV,
  status: ActionStatusType.UNAVAILABLE,
  startDateTimeUtc: '2022-01-01T00:00:00Z',
  endDateTimeUtc: '2022-01-01T01:00:00Z',
  summaryTags: undefined,
  controlsToComplete: undefined,
  data: {
    startDateTimeUtc: '2022-01-01T00:00:00Z',
    endDateTimeUtc: '2022-01-01T01:00:00Z',
    status: ActionStatusType.UNAVAILABLE,
    reason: ActionStatusReason.TECHNICAL,
    observations: 'obs'
  } as any as ActionStatusBaseType
}

const props = (action: Action = actionMock, previousAction = null, onClick = vi.fn()) => ({
  action,
  previousActionWithSameType: previousAction,
  onClick
})
describe('ActionStatus', () => {
  test('should render', () => {
    render(<ActionStatus {...props()} />)
    expect(screen.getByText('Indisponibilité - début - Technique')).toBeInTheDocument()
  })
  test('should render observations', () => {
    render(<ActionStatus {...props()} />)
    expect(screen.getByText('- obs')).toBeInTheDocument()
  })
  test('should not render the reason when none', () => {
    const mock = { ...actionMock, data: { ...actionMock.data, reason: undefined } }
    render(<ActionStatus {...props(mock)} />)
    expect(screen.queryByText('Indisponibilité - début - Technique')).toBeNull()
    expect(screen.queryByText('Technique')).toBeNull()
  })
  test('should not render observations when none', () => {
    const mock = { ...actionMock, data: { ...actionMock.data, observations: undefined } }
    render(<ActionStatus {...props(mock)} />)
    expect(screen.queryByText('- obs')).toBeNull()
  })
  test('should not render the previous status when not available', () => {
    const mock = { ...actionMock, data: { ...actionMock.data, observations: undefined } }
    render(<ActionStatus {...props(mock, null)} />)
    expect(screen.queryByText('Indisponibilité - Technique - fin')).toBeNull()
  })
  test('should render the previous status when available', () => {
    const mock = { ...actionMock, data: { ...actionMock.data, observations: undefined } }
    render(<ActionStatus {...props(mock, previousActionMock)} />)
    expect(screen.queryByText('Indisponibilité - Technique - fin')).toBeInTheDocument()
  })
  test('should truncate observations longer than 35 characters and add ellipsis', () => {
    const longObservation = 'This is a very long observation that exceeds thirty-five characters.'

    const mock = { ...actionMock, data: { ...actionMock.data, observations: longObservation } }
    render(<ActionStatus {...props(mock, previousActionMock)} />)

    const expectedTruncatedText = longObservation.slice(0, 35) + '...'
    expect(screen.getByText(new RegExp(expectedTruncatedText))).toBeInTheDocument()
  })
  it('should display observations as is if shorter than 35 characters', () => {
    const shortObservation = 'This is short.'

    const mock = { ...actionMock, data: { ...actionMock.data, observations: shortObservation } }
    render(<ActionStatus {...props(mock, previousActionMock)} />)

    expect(screen.getByText('- ' + shortObservation)).toBeInTheDocument()
  })
})
