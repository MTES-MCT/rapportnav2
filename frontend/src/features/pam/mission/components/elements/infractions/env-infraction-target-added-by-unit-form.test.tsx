import { render, screen, fireEvent } from '../../../../../../test-utils.tsx'
import EnvInfractionTargetAddedByUnitForm from './env-infraction-target-added-by-unit-form.tsx'
import { ControlType } from '@common/types/control-types.ts'
import { InfractionTypeEnum } from '@common/types/env-mission-types.ts'

const infractionMock = {
  id: '123',
  controlType: ControlType.ADMINISTRATIVE,
  infractionType: InfractionTypeEnum.WITHOUT_REPORT,
  natinfs: ['123'],
  observations: undefined,
  target: undefined
}

const props = (infraction = infractionMock) => ({
  infraction,
  availableControlTypesForInfraction: [ControlType.ADMINISTRATIVE],
  onChange: vi.fn(),
  onChangeTarget: vi.fn(),
  onCancel: vi.fn()
})
describe('EnvInfractionTargetAddedByUnitForm', () => {
  test('renders the component', async () => {
    render(<EnvInfractionTargetAddedByUnitForm {...props()} />)
    expect(screen.getByText('Type de contrôle avec infraction')).toBeInTheDocument()
  })
  describe('changing the target data', () => {
    test('calls onChangeTarget when changing vesselIdentifier', async () => {
      const onChangeTarget = vi.fn()
      render(<EnvInfractionTargetAddedByUnitForm {...{ ...props(), onChangeTarget }} />)
      const field = screen.getByRole('vesselIdentifier')
      const value = 'test'
      fireEvent.change(field, { target: { value } })
      fireEvent.blur(field)
      expect(onChangeTarget).toHaveBeenCalled()
    })
    test('calls onChangeTarget when changing identityControlledPerson', async () => {
      const onChangeTarget = vi.fn()
      render(<EnvInfractionTargetAddedByUnitForm {...{ ...props(), onChangeTarget }} />)
      const field = screen.getByRole('identityControlledPerson')
      const value = 'test'
      fireEvent.change(field, { target: { value } })
      fireEvent.blur(field)
      expect(onChangeTarget).toHaveBeenCalled()
    })
  })
})
