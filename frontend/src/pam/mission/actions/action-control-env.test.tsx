import { render, screen } from '../../../test-utils'
import { ActionTargetTypeEnum, ActionTypeEnum, EnvAction, MissionSourceEnum } from '../../../types/env-mission-types.ts'
import ActionControlEnv from './action-control-env.tsx'
import { Action, ActionStatusType } from '../../../types/action-types.ts'
import { vi } from 'vitest'
import useActionById from './use-action-by-id.tsx'
import { GraphQLError } from 'graphql/error'
import ActionControlNav from './action-control-nav.tsx'

vi.mock('./use-action-by-id.tsx', async importOriginal => {
  const actual = await importOriginal()
  return {
    ...actual,
    default: vi.fn()
  }
})

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

// Set up the mock implementation for useActionById
const mockedQueryResult = (action: Action = actionMock as any, loading: boolean = false, error: any = undefined) => ({
  data: action,
  loading,
  error
})

describe('ActionControlEnv', () => {
  describe('Testing rendering according to Query result', () => {
    test('renders loading state', async () => {
      ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, true))
      render(<ActionControlEnv action={actionMock} />)
      expect(screen.getByText('Chargement...')).toBeInTheDocument()
    })

    test('renders error state', async () => {
      ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, false, new GraphQLError('Error!')))
      render(<ActionControlEnv action={actionMock} />)
      expect(screen.getByText('error')).toBeInTheDocument()
    })

    test('renders data', async () => {
      ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, false))
      render(<ActionControlEnv action={actionMock} />)
      expect(screen.getByText('Thématique de contrôle')).toBeInTheDocument()
    })
    test('renders null when none above', async () => {
      ;(useActionById as any).mockReturnValue({ ...mockedQueryResult(undefined, false), data: null })
      render(<ActionControlNav action={actionMock} />)
      expect(screen.queryByTestId('action-control-env')).not.toBeInTheDocument()
    })
  })
})
