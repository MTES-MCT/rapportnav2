import { vi } from 'vitest'
import { fireEvent, mockQueryResult, render, waitFor } from '../../../test-utils.tsx'
import { Action, ActionRescue } from '../../../types/action-types.ts'
import { ActionTypeEnum, MissionSourceEnum } from '../../../types/env-mission-types.ts'
import ActionRescueForm from './action-rescue-form.tsx'
import useActionById from './use-action-by-id.tsx'

const mutateMock = vi.fn()

vi.mock('./use-action-by-id', async importOriginal => {
  const actual = await importOriginal()
  return {
    ...actual,
    default: vi.fn()
  }
})

vi.mock('../rescues/use-add-update-rescue', () => ({
  default: () => [mutateMock]
}))

const action: Action = { source: MissionSourceEnum.RAPPORTNAV, type: ActionTypeEnum.RESCUE } as Action

const response = {
  data: {
    id: '',
    startDateTimeUtc: '2024-01-01T00:00:00Z',
    endDateTimeUtc: '2024-01-12T01:00:00Z',
    longitude: 3,
    latitude: 5,
    isVesselRescue: false,
    isPersonRescue: false,
    isInSRRorFollowedByCROSSMRCC: false,
    isVesselNoticed: true,
    isVesselTowed: false,
    numberPersonsRescued: 3,
    numberOfDeaths: 5,
    operationFollowsDEFREP: false,
    observations: 'string',
    locationDescription: 'string',
    isMigrationRescue: false,
    nbAssistedVesselsReturningToShore: 3,
    nbOfVesselsTrackedWithoutIntervention: 4
  } as ActionRescue
}

describe('ActionRescueForm', () => {
  beforeEach(() => {
    ;(useActionById as any).mockReturnValue(mockQueryResult(response))
  })

  it('should render vessel notice label', async () => {
    const wrapper = render(<ActionRescueForm action={action} />)
    const radio = wrapper.getByLabelText('Assistance de navire en difficulté')
    fireEvent.click(radio)
    await waitFor(() => {
      expect(wrapper.getByText('Le navire a été mis en demeure avant intervention')).toBeInTheDocument()
    })
  })
})
