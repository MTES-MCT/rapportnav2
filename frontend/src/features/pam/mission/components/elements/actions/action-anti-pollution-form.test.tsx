import { expect, vi, beforeEach, afterEach, describe, it } from 'vitest'
import { fireEvent, render, screen } from '../../../../../../test-utils.tsx'
import ActionAntiPollutionForm from './action-anti-pollution-form'
import { Action, ActionAntiPollution } from '@common/types/action-types'
import { ActionTypeEnum, MissionSourceEnum } from '@common/types/env-mission-types'
import * as useActionByIdModule from '@features/pam/mission/hooks/use-action-by-id'
import * as useAddAntiPollutionModule from '@features/pam/mission/hooks/anti-pollution/use-add-anti-pollution'

const mockAction: Action = { source: MissionSourceEnum.RAPPORTNAV, type: ActionTypeEnum.ANTI_POLLUTION } as Action

const mockResponse = {
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

describe('ActionAntiPollutionForm', () => {
  beforeEach(() => {
    vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: mockResponse, loading: false, error: undefined })
    vi.spyOn(useAddAntiPollutionModule, 'default').mockReturnValue([
      updateAntiPollutionActionMock,
      { error: undefined }
    ])
  })

  afterEach(() => {
    vi.clearAllMocks()
  })

  it('should render action anti pollution form with the right value', () => {
    render(<ActionAntiPollutionForm action={mockAction} />)
    expect(updateAntiPollutionActionMock).not.toHaveBeenCalled()
    expect(screen.getByText('Opération de brassage simple effectuée')).toBeInTheDocument()
    expect(screen.getByText("Mise en place d'un dispositif ANTIPOL (dispersant, barrage, etc...)")).toBeInTheDocument()
  })

  it('should call update action on brassage simple effectué', () => {
    render(<ActionAntiPollutionForm action={mockAction} />)
    fireEvent.click(screen.getByTestId('action-antipol-simple-brewing-operation'))
    expect(updateAntiPollutionActionMock).toHaveBeenCalledTimes(1)
    expect(updateAntiPollutionActionMock).toHaveBeenCalledWith({
      variables: { antiPollutionAction: expect.objectContaining({ isSimpleBrewingOperationDone: true }) }
    })
  })

  it('should call update action on dispositif ANTIPOL with the right value', () => {
    render(<ActionAntiPollutionForm action={mockAction} />)
    fireEvent.click(screen.getByTestId('action-antipol-device-deployed'))
    expect(updateAntiPollutionActionMock).toHaveBeenCalledTimes(1)
    expect(updateAntiPollutionActionMock).toHaveBeenCalledWith({
      variables: { antiPollutionAction: expect.objectContaining({ isAntiPolDeviceDeployed: true }) }
    })
  })
})
