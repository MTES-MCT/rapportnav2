import { render, screen } from '../../../test-utils'
import { ActionTargetTypeEnum, ActionTypeEnum, EnvAction, MissionSourceEnum } from '../../../types/env-mission-types.ts'
import { Action, ActionStatusType } from '../../../types/action-types.ts'
import { vi } from 'vitest'
import useActionById from './use-action-by-id.tsx'
import { GraphQLError } from 'graphql/error'
import ActionSurveillanceEnv from './action-surveillance-env.tsx'

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
  type: ActionTypeEnum.SURVEILLANCE,
  source: MissionSourceEnum.MONITORENV,
  status: ActionStatusType.UNKNOWN,
  startDateTimeUtc: '2022-01-01T00:00:00Z',
  endDateTimeUtc: '2022-01-01T01:00:00Z',
  data: {
    observations: null,
    geom: 'MULTIPOINT ((-8.52318191 48.30305604))',
    formattedControlPlans: {
      subThemes: ['mouillage individuel', 'ZMEL'],
      themes: ['police des mouillages']
    }
  } as any as EnvAction
}

// Set up the mock implementation for useActionById
const mockedQueryResult = (action: Action = actionMock as any, loading: boolean = false, error: any = undefined) => ({
  data: action,
  loading,
  error
})

describe('ActionSurveillanceEnv', () => {
  describe('Testing rendering according to Query result', () => {
    test('renders loading state', async () => {
      ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, true))
      render(<ActionSurveillanceEnv action={actionMock} />)
      expect(screen.getByText('Chargement...')).toBeInTheDocument()
    })

    test('renders error state', async () => {
      ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, false, new GraphQLError('Error!')))
      render(<ActionSurveillanceEnv action={actionMock} />)
      expect(screen.getByText('error')).toBeInTheDocument()
    })

    test('renders data', async () => {
      ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, false))
      render(<ActionSurveillanceEnv action={actionMock} />)
      expect(screen.getByText('Surveillance Environnement')).toBeInTheDocument()
    })
  })
  describe('Themes and SubThemes', () => {
    test('should render themes and subthemes', async () => {
      ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, false))
      render(<ActionSurveillanceEnv action={actionMock} />)
      expect(screen.getByText('police des mouillages')).toBeInTheDocument()
      expect(screen.getByText('mouillage individuel, ZMEL')).toBeInTheDocument()
    })
    test('should render fallback text when themes and subthemes are undefined', async () => {
      const mock = {
        ...actionMock,
        data: {
          observations: null,
          geom: 'MULTIPOINT ((-8.52318191 48.30305604))',
          formattedControlPlans: {
            subThemes: undefined,
            themes: undefined
          }
        }
      }
      ;(useActionById as any).mockReturnValue(mockedQueryResult(mock as any, false))
      render(<ActionSurveillanceEnv action={mock} />)
      expect(screen.getByText('inconnue')).toBeInTheDocument()
      expect(screen.getByText('inconnues')).toBeInTheDocument()
    })
    test('should render fallback text when themes and subthemes are empty arrays', async () => {
      const mock = {
        ...actionMock,
        data: {
          observations: null,
          geom: 'MULTIPOINT ((-8.52318191 48.30305604))',
          formattedControlPlans: {
            subThemes: [],
            themes: []
          }
        }
      }
      ;(useActionById as any).mockReturnValue(mockedQueryResult(mock as any, false))
      render(<ActionSurveillanceEnv action={mock} />)
      expect(screen.getByText('inconnue')).toBeInTheDocument()
      expect(screen.getByText('inconnues')).toBeInTheDocument()
    })
  })
})
