import { render, screen, fireEvent } from '../../../../../../test-utils.tsx'
import Timeline from './timeline.tsx'
import { ActionControl, ActionStatusType } from '@common/types/action-types.ts'
import { vi } from 'vitest'
import * as useTimelineModule from '@features/pam/mission/hooks/use-mission-timeline'
import { CompletenessForStatsStatusEnum, Mission, VesselTypeEnum } from '@common/types/mission-types.ts'
import { GraphQLError } from 'graphql/error'
import { ActionTypeEnum, MissionSourceEnum } from '@common/types/env-mission-types.ts'
import { ControlMethod } from '@common/types/control-types.ts'

const actionMock1 = {
  id: '1',
  missionId: 1,
  type: ActionTypeEnum.CONTROL,
  source: MissionSourceEnum.RAPPORTNAV,
  status: ActionStatusType.DOCKED,
  startDateTimeUtc: '2022-01-01T00:00:00Z',
  endDateTimeUtc: '2022-01-01T01:00:00Z',
  summaryTags: undefined,
  controlsToComplete: undefined,
  isCompleteForStats: true,
  completenessForStats: {
    status: CompletenessForStatsStatusEnum.COMPLETE,
    sources: undefined
  },
  data: {
    controlMethod: ControlMethod.SEA,
    latitude: 123,
    longitude: 123,
    startDateTimeUtc: '2022-01-01T00:00:00Z',
    endDateTimeUtc: '2022-01-01T01:00:00Z',
    vesselType: VesselTypeEnum.FISHING
  } as any as ActionControl
}

// set a different date
const actionMock2 = { ...actionMock1, startDateTimeUtc: '2022-01-02T00:00:00Z' }

const actionStatusMock = { ...actionMock2, type: ActionTypeEnum.STATUS }

const props = () => ({
  missionId: '1',
  onSelectAction: vi.fn()
})
describe('Timeline', () => {
  describe('testing rendering', () => {
    test('should render loading', () => {
      vi.spyOn(useTimelineModule, 'default').mockReturnValue({ data: undefined, loading: true, error: undefined })
      render(<Timeline {...props()} />)
      expect(screen.getByTestId('timeline-loading')).toBeInTheDocument()
    })
    test('should render error', () => {
      vi.spyOn(useTimelineModule, 'default').mockReturnValue({
        data: undefined,
        loading: false,
        error: new GraphQLError('Error!')
      })
      render(<Timeline {...props()} />)
      expect(screen.getByTestId('timeline-error')).toBeInTheDocument()
    })
    test('should render empty action message', () => {
      const actions = { actions: [] }
      vi.spyOn(useTimelineModule, 'default').mockReturnValue({ data: actions, loading: false, error: undefined })
      render(<Timeline {...props()} />)
      expect(screen.getByText("Aucune action n'est ajoutée pour le moment")).toBeInTheDocument()
    })
    test('should render content', () => {
      const actions = { actions: [actionMock1, actionMock2] }
      vi.spyOn(useTimelineModule, 'default').mockReturnValue({ data: actions, loading: false, error: undefined })
      render(<Timeline {...props()} />)
      expect(screen.getByTestId('timeline-content')).toBeInTheDocument()
    })
    test('should render 1 less divider as there are actions', () => {
      const actions = { actions: [actionMock1, actionMock2] }
      vi.spyOn(useTimelineModule, 'default').mockReturnValue({ data: actions, loading: false, error: undefined })
      render(<Timeline {...props()} />)
      expect(screen.queryAllByTestId('timeline-day-divider')).toHaveLength(actions.actions.length - 1)
    })
    test('should not render status tag for status actions', () => {
      const actions = { actions: [actionMock1, actionStatusMock] }
      vi.spyOn(useTimelineModule, 'default').mockReturnValue({ data: actions, loading: false, error: undefined })
      render(<Timeline {...props()} />)
      expect(screen.queryAllByTestId('timeline-item-status')).toHaveLength(actions.actions.length - 1)
    })
    test('should not render the warning icon when data is incomplete', () => {
      const actions = {
        actions: [
          {
            ...actionMock1,
            completenessForStats: { status: CompletenessForStatsStatusEnum.INCOMPLETE }
          }
        ]
      }
      vi.spyOn(useTimelineModule, 'default').mockReturnValue({ data: actions, loading: false, error: undefined })
      render(<Timeline {...props()} />)
      expect(screen.getByTestId('timeline-item-incomplete-report')).toBeInTheDocument()
    })
    test('should render the date in french', () => {
      const actions = { actions: [actionMock1, actionMock2] }
      vi.spyOn(useTimelineModule, 'default').mockReturnValue({ data: actions, loading: false, error: undefined })
      render(<Timeline {...props()} />)
      expect(screen.getByText('01 janv.')).toBeInTheDocument()
      expect(screen.getByText('02 janv.')).toBeInTheDocument()
    })
  })
  describe('testing the action', () => {
    test('should call onSelect when clicking an item', () => {
      const actions = { actions: [actionMock1, actionStatusMock] }
      vi.spyOn(useTimelineModule, 'default').mockReturnValue({ data: actions, loading: false, error: undefined })
      const spy = vi.fn()
      render(<Timeline {...{ ...props(), onSelectAction: spy }} />)

      const item = screen.getByText('en Mer - Navire de pêche professionnelle')
      fireEvent.click(item)
      expect(spy).toHaveBeenCalled()
    })
  })
})
