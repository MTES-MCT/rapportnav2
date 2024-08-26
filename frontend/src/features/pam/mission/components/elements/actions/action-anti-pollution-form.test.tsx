import { expect, vi } from 'vitest'
import { fireEvent, render, screen } from '../../../../../../test-utils.tsx'
import { mockQueryResult } from '../../../../../../test-utils.tsx'
import { Action, ActionAntiPollution } from '@common/types/action-types.ts'
import { ActionTypeEnum, MissionSourceEnum } from '@common/types/env-mission-types.ts'
import ActionAntiPollutionForm from './action-anti-pollution-form.tsx'
import useActionById from '../../../hooks/use-action-by-id.tsx'

const action: Action = { source: MissionSourceEnum.RAPPORTNAV, type: ActionTypeEnum.ANTI_POLLUTION } as Action

const response = {
  data: {
    id: '',
    startDateTimeUtc: '2024-01-01T00:00:00Z',
    endDateTimeUtc: '2024-01-12T01:00:00Z',
    longitude: 3,
    latitude: 5,
    observations: '',
    detectedPollution: false,
    pollutionObservedByAuthorizedAgent: false,
    diversionCarriedOut: false,
    isSimpleBrewingOperationDone: false,
    isAntiPolDeviceDeployed: false
  } as ActionAntiPollution
}

const updateAntiPollutionActionMock = vi.fn()

vi.mock('../others/anti-pollution/use-add-anti-pollution.tsx', () => ({
  default: () => [updateAntiPollutionActionMock, { error: undefined }]
}))
vi.mock('./use-action-by-id', async importOriginal => {
  const actual = await importOriginal()
  return {
    ...actual,
    default: vi.fn()
  }
})

describe('ActionAntiPollutionForm', () => {
  beforeEach(() => {
    ;(useActionById as any).mockReturnValue(mockQueryResult(response))
  })
  afterEach(() => {
    vi.clearAllMocks()
    vi.resetAllMocks()
  })

  it('should render action anti pollution form with the right value', () => {
    render(<ActionAntiPollutionForm action={action} />)
    expect(updateAntiPollutionActionMock).not.toHaveBeenCalled()
    expect(screen.getByText(`Opération de brassage simple effectuée`)).toBeInTheDocument()
    expect(screen.getByText(`Mise en place d'un dispositif ANTIPOL (dispersant, barrage, etc...)`)).toBeInTheDocument()
  })

  it('should call update action on brassage simple effectué', () => {
    const wrapper = render(<ActionAntiPollutionForm action={action} />)
    const element = wrapper.getByTestId('action-antipol-simple-brewing-operation')
    fireEvent.click(element)
    expect(updateAntiPollutionActionMock).toHaveBeenCalledTimes(1)
    expect(updateAntiPollutionActionMock).toHaveBeenCalledWith({
      variables: { antiPollutionAction: expect.objectContaining({ isSimpleBrewingOperationDone: true }) }
    })
  })

  it('should call update action on dospositif ANTIPOL with the right value', () => {
    const wrapper = render(<ActionAntiPollutionForm action={action} />)
    const element = wrapper.getByTestId('action-antipol-device-deployed')
    fireEvent.click(element)
    expect(updateAntiPollutionActionMock).toHaveBeenCalledTimes(1)
    expect(updateAntiPollutionActionMock).toHaveBeenCalledWith({
      variables: { antiPollutionAction: expect.objectContaining({ isAntiPolDeviceDeployed: true }) }
    })
  })
})
