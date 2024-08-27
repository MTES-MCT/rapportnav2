import { render, screen } from '../../../../../../test-utils.tsx'
import { ActionTypeEnum, EnvAction, MissionSourceEnum } from '@common/types/env-mission-types.ts'
import { ActionStatusType } from '@common/types/action-types.ts'
import { vi } from 'vitest'
import * as useActionByIdModule from '@features/pam/mission/hooks/use-action-by-id'
import { GraphQLError } from 'graphql/error'
import ActionSurveillanceEnv from './action-surveillance-env.tsx'

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

describe('ActionSurveillanceEnv', () => {
  describe('Testing rendering according to Query result', () => {
    test('renders loading state', async () => {
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: undefined, loading: true, error: null })

      render(<ActionSurveillanceEnv action={actionMock} />)
      expect(screen.getByText('Chargement...')).toBeInTheDocument()
    })

    test('renders error state', async () => {
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({
        data: undefined,
        loading: false,
        error: new GraphQLError('Error!')
      })
      render(<ActionSurveillanceEnv action={actionMock} />)
      expect(screen.getByText('error')).toBeInTheDocument()
    })

    test('renders data', async () => {
      // ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, false))
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: actionMock, loading: false, error: null })
      render(<ActionSurveillanceEnv action={actionMock} />)
      expect(screen.getByText('Surveillance Environnement')).toBeInTheDocument()
    })
  })
  describe('Themes and SubThemes', () => {
    test('should render themes and subthemes', async () => {
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: actionMock, loading: false, error: null })
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
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: mock, loading: false, error: null })
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
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: mock, loading: false, error: null })
      render(<ActionSurveillanceEnv action={mock} />)
      expect(screen.getByText('inconnue')).toBeInTheDocument()
      expect(screen.getByText('inconnues')).toBeInTheDocument()
    })
  })
  describe('Observations by unit', () => {
    test('should be rendered when not nullish', () => {
      const mock = { ...actionMock, data: { ...actionMock.data, observationsByUnit: 'observationsByUnit' } }
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: mock, loading: false, error: null })
      render(<ActionSurveillanceEnv action={actionMock} />)
      expect(screen.getByText('observationsByUnit')).toBeInTheDocument()
    })
  })
})
