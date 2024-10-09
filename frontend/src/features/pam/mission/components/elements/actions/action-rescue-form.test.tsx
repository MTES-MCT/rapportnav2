import { vi } from 'vitest'
import { fireEvent, render, waitFor } from '../../../../../../test-utils.tsx'
import { Action, ActionRescue } from '@common/types/action-types.ts'
import { ActionTypeEnum, MissionSourceEnum } from '@common/types/env-mission-types.ts'
import ActionRescueForm from './action-rescue-form.tsx'
import * as useActionByIdModule from '@features/pam/mission/hooks/use-action-by-id'
import * as useAddOrUpdateRescueModule from '@features/pam/mission/hooks/rescues/use-add-update-rescue'

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

const mutateMock = vi.fn()
describe('ActionRescueForm', () => {
  beforeEach(() => {
    vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: response, loading: false, error: null })
    vi.spyOn(useAddOrUpdateRescueModule, 'default').mockReturnValue([mutateMock, { error: undefined }])
  })

  it('should render vessel notice label', async () => {
    const wrapper = render(<ActionRescueForm action={action} />)
    const radio = wrapper.getByLabelText('Assistance de navire en difficulté')
    fireEvent.click(radio)
    await waitFor(() => {
      expect(wrapper.getByText('Le navire a été mis en demeure avant intervention')).toBeInTheDocument()
    })
  })

  it('renders the person rescue fields to be show if action is a person rescue action', async () => {
    const data = { ...action, data: { ...action.data, isPersonRescue: true } }
    const wrapper = render(<ActionRescueForm action={data} />)
    expect(wrapper.getByText('Nb de personnes secourues')).toBeInTheDocument()
    expect(wrapper.getByText('Nb de personnes disparues / décédées')).toBeInTheDocument()
    expect(wrapper.getByText('Opération en zone SRR ou suivie par un CROSS / MRCC')).toBeInTheDocument()
  })
})
