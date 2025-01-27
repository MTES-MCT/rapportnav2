import { render, screen } from '../../../../../../test-utils.tsx'
import { ActionTypeEnum, MissionSourceEnum } from '@common/types/env-mission-types.ts'
import ActionControlFish from './action-control-fish.tsx'
import { ActionStatusType } from '@common/types/action-types.ts'
import { vi } from 'vitest'
import { GraphQLError } from 'graphql/error'
import { FishAction, MissionActionType } from '@common/types/fish-mission-types.ts'
import ActionControlNav from './action-control-nav.tsx'
import * as useActionByIdModule from '@features/pam/mission/hooks/use-action-by-id'
import ActionControlEnv from '@features/pam/mission/components/elements/actions/action-control-env.tsx'

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

describe('ActionControlFish', () => {
  describe('Testing rendering according to Query result', () => {
    test('renders loading state', async () => {
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: actionMock, loading: true, error: undefined })
      render(<ActionControlFish action={actionMock} />)
      expect(screen.getByText('Chargement...')).toBeInTheDocument()
    })

    test('renders error state', async () => {
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({
        data: actionMock,
        loading: false,
        error: new GraphQLError('Error!')
      })
      render(<ActionControlFish action={actionMock} />)
      expect(screen.getByText('error')).toBeInTheDocument()
    })

    test('renders data', async () => {
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: actionMock, loading: false, error: undefined })
      render(<ActionControlFish action={actionMock} />)
      expect(screen.getByText('Autre(s) contrôle(s) effectué(s) par l’unité sur le navire')).toBeInTheDocument()
      expect(screen.queryByTestId('portName')).not.toBeInTheDocument()
    })
    test('renders null when none above', async () => {
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: null, loading: false, error: undefined })
      render(<ActionControlNav action={actionMock} />)
      expect(screen.queryByTestId('action-control-fish')).not.toBeInTheDocument()
    })
    test('Render port name when land control', () => {
      const mock = {
        ...actionMock,
        data: { ...actionMock.data, actionType: MissionActionType.LAND_CONTROL, portName: 'dummy port' }
      }
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: mock, loading: false, error: undefined })
      render(<ActionControlFish action={mock} />)
      const inputElement = screen.getByTestId('portName')
      expect(inputElement).toBeInTheDocument()
      expect(inputElement).toHaveValue(mock.data.portName)
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
