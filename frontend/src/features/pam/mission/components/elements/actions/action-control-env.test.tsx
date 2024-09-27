import { render, screen } from '../../../../../../test-utils.tsx'
import { ActionTargetTypeEnum, ActionTypeEnum, EnvAction, MissionSourceEnum } from '@common/types/env-mission-types.ts'
import ActionControlEnv from './action-control-env.tsx'
import { Action, ActionStatusType } from '@common/types/action-types.ts'
import { vi } from 'vitest'
import * as useActionByIdModule from '@features/pam/mission/hooks/use-action-by-id'
import { GraphQLError } from 'graphql/error'
import ActionControlNav from './action-control-nav.tsx'

const actionMock = {
  id: '1',
  missionId: 1,
  type: ActionTypeEnum.CONTROL,
  source: MissionSourceEnum.MONITORENV,
  status: ActionStatusType.DOCKED,
  startDateTimeUtc: '2022-01-01T00:00:00Z',
  endDateTimeUtc: '2022-01-01T01:00:00Z',
  summaryTags: undefined,
  controlsToComplete: undefined,
  data: {
    actionNumberOfControls: 3,
    actionTargetType: ActionTargetTypeEnum.VEHICLE,
    actionType: ActionTypeEnum.CONTROL,
    infractions: [],
    observations: null,
    geom: 'MULTIPOINT ((-8.52318191 48.30305604))',
    formattedControlPlans: [
      {
        subThemes: ['subtheme1', 'subtheme2'],
        themes: ['rejet illicite']
      }
    ]
  } as any as EnvAction
}

describe('ActionControlEnv', () => {
  describe('Testing rendering according to Query result', () => {
    test('renders loading state', async () => {
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: actionMock, loading: true, error: undefined })
      render(<ActionControlEnv action={actionMock} />)
      expect(screen.getByText('Chargement...')).toBeInTheDocument()
    })

    test('renders error state', async () => {
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({
        data: actionMock,
        loading: false,
        error: new GraphQLError('Error!')
      })
      render(<ActionControlEnv action={actionMock} />)
      expect(screen.getByText('error')).toBeInTheDocument()
    })

    test('renders data', async () => {
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: actionMock, loading: false, error: undefined })
      render(<ActionControlEnv action={actionMock} />)
      expect(screen.getByText('Thématique')).toBeInTheDocument()
    })
    test('renders null when none above', async () => {
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: actionMock, loading: false, error: undefined })
      render(<ActionControlNav action={actionMock} />)
      expect(screen.queryByTestId('action-control-env')).not.toBeInTheDocument()
    })
  })
  describe('Observations by unit', () => {
    test('should be rendered when not nullish', () => {
      const mock = { ...actionMock, data: { ...actionMock.data, observationsByUnit: 'observationsByUnit' } }
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: mock, loading: false, error: undefined })
      render(<ActionControlEnv action={mock} />)
      expect(screen.getByText('observationsByUnit')).toBeInTheDocument()
    })
  })
})
