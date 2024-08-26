import { render, screen } from '../../../../../../test-utils.tsx'
import { ActionTypeEnum, MissionSourceEnum } from '../../../../../common/types/env-mission-types.ts'
import ActionControlFish from './action-control-fish.tsx'
import { Action, ActionStatusType } from '../../../../../common/types/action-types.ts'
import { vi } from 'vitest'
import useActionById from '../../../hooks/use-action-by-id.tsx'
import { GraphQLError } from 'graphql/error'
import { FishAction } from '../../../../../common/types/fish-mission-types.ts'
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
  endDateTimeUtc: null,
  summaryTags: undefined,
  controlsToComplete: undefined,
  data: {
    actionDatetimeUtc: '2022-01-01T00:00:00Z'
  } as any as FishAction
}

// Set up the mock implementation for useActionById
const mockedQueryResult = (action: Action = actionMock as any, loading: boolean = false, error: any = undefined) => ({
  data: action,
  loading,
  error
})

describe('ActionControlFish', () => {
  describe('Testing rendering according to Query result', () => {
    test('renders loading state', async () => {
      ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, true))
      render(<ActionControlFish action={actionMock} />)
      expect(screen.getByText('Chargement...')).toBeInTheDocument()
    })

    test('renders error state', async () => {
      ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, false, new GraphQLError('Error!')))
      render(<ActionControlFish action={actionMock} />)
      expect(screen.getByText('error')).toBeInTheDocument()
    })

    test('renders data', async () => {
      ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, false))
      render(<ActionControlFish action={actionMock} />)
      expect(screen.getByText('Autre(s) contrôle(s) effectué(s) par l’unité sur le navire')).toBeInTheDocument()
    })
    test('renders null when none above', async () => {
      ;(useActionById as any).mockReturnValue({ ...mockedQueryResult(undefined, false), data: null })
      render(<ActionControlNav action={actionMock} />)
      expect(screen.queryByTestId('action-control-fish')).not.toBeInTheDocument()
    })
  })
})
