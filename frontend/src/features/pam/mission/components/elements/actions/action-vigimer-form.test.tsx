import { vi } from 'vitest'
import { render } from '../../../../../../test-utils.tsx'
import { Action, ActionVigimer } from '@common/types/action-types.ts'
import { ActionTypeEnum, MissionSourceEnum } from '@common/types/env-mission-types.ts'
import * as useActionByIdModule from '@features/pam/mission/hooks/use-action-by-id'
import * as useAddModule from '@features/pam/mission/hooks/vigimer/use-add-vigimer'
import * as useDeleteModule from '@features/pam/mission/hooks/vigimer/use-delete-vigimer'
import ActionVigimerForm from '@features/pam/mission/components/elements/actions/action-vigimer-form.tsx'

const action: Action = { source: MissionSourceEnum.RAPPORTNAV, type: ActionTypeEnum.RESCUE } as Action

const response = {
  data: {
    id: '',
    startDateTimeUtc: '2024-01-01T00:00:00Z',
    endDateTimeUtc: '2024-01-12T01:00:00Z',
    observations: 'some random obs'
  } as ActionVigimer
}

const mutateMock = vi.fn()
const deleteMock = vi.fn()
describe('ActionVigimerForm', () => {
  beforeEach(() => {
    vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: response, loading: false, error: null })
    vi.spyOn(useAddModule, 'default').mockReturnValue([mutateMock, { error: undefined }])
    vi.spyOn(useDeleteModule, 'default').mockReturnValue([deleteMock, { error: undefined }])
  })

  it('should render', async () => {
    const wrapper = render(<ActionVigimerForm action={action} />)
    expect(wrapper.getByText('Permanence Vigimer')).toBeInTheDocument()
    expect(wrapper.getByText('some random obs')).toBeInTheDocument()
  })
})
