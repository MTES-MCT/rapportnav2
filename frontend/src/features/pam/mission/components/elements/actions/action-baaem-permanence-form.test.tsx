import { Action, ActionBAAEMPermanence } from '@common/types/action-types.ts'
import { ActionTypeEnum, MissionSourceEnum } from '@common/types/env-mission-types.ts'
import ActionBAAEMPermanenceForm from '@features/pam/mission/components/elements/actions/action-baaem-permanence-form.tsx'
import * as useAddModule from '@features/pam/mission/hooks/baaem/use-add-baaem-permanence'
import * as useDeleteModule from '@features/pam/mission/hooks/baaem/use-delete-baaem-permanence'
import * as useActionByIdModule from '@features/pam/mission/hooks/use-action-by-id'
import { vi } from 'vitest'
import { render } from '../../../../../../test-utils.tsx'

const action: Action = { source: MissionSourceEnum.RAPPORTNAV, type: ActionTypeEnum.RESCUE } as Action

const response = {
  data: {
    id: '',
    startDateTimeUtc: '2024-01-01T00:00:00Z',
    endDateTimeUtc: '2024-01-12T01:00:00Z',
    observations: 'some random obs'
  } as ActionBAAEMPermanence
}

const mutateMock = vi.fn()
const deleteMock = vi.fn()
describe('ActionBAAEMPermanenceForm', () => {
  beforeEach(() => {
    vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: response, loading: false, error: null })
    vi.spyOn(useAddModule, 'default').mockReturnValue([mutateMock, { error: undefined }])
    vi.spyOn(useDeleteModule, 'default').mockReturnValue([deleteMock, { error: undefined }])
  })

  it('should render', async () => {
    const wrapper = render(<ActionBAAEMPermanenceForm action={action} />)
    expect(wrapper.getByText('Permanence BAAEM')).toBeInTheDocument()
    expect(wrapper.getByText('some random obs')).toBeInTheDocument()
  })
})
