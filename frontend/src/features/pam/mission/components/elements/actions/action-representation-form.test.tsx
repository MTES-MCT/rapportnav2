import { vi } from 'vitest'
import { render } from '../../../../../../test-utils.tsx'
import { Action, ActionRepresentation, ActionRescue } from '@common/types/action-types.ts'
import { ActionTypeEnum, MissionSourceEnum } from '@common/types/env-mission-types.ts'
import * as useActionByIdModule from '@features/pam/mission/hooks/use-action-by-id'
import * as useAddRepresentationModule from '@features/pam/mission/hooks/representation/use-add-representation'
import * as useDeleteRepresentationModule from '@features/pam/mission/hooks/representation/use-delete-representation'
import ActionRepresentationForm from '@features/pam/mission/components/elements/actions/action-representation-form.tsx'

const action: Action = { source: MissionSourceEnum.RAPPORTNAV, type: ActionTypeEnum.RESCUE } as Action

const response = {
  data: {
    id: '',
    startDateTimeUtc: '2024-01-01T00:00:00Z',
    endDateTimeUtc: '2024-01-12T01:00:00Z',
    observations: 'some random obs'
  } as ActionRepresentation
}

const mutateMock = vi.fn()
const deleteMock = vi.fn()
describe('ActionRepresentationForm', () => {
  beforeEach(() => {
    vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: response, loading: false, error: null })
    vi.spyOn(useAddRepresentationModule, 'default').mockReturnValue([mutateMock, { error: undefined }])
    vi.spyOn(useDeleteRepresentationModule, 'default').mockReturnValue([deleteMock, { error: undefined }])
  })

  it('should render', async () => {
    const wrapper = render(<ActionRepresentationForm action={action} />)
    expect(wrapper.getByText('Représentation')).toBeInTheDocument()
    expect(wrapper.getByText('some random obs')).toBeInTheDocument()
  })
})
