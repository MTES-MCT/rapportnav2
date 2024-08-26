import { render, screen } from '../../../../../../test-utils.tsx'
import EnvInfractionTargetAddedByCacemForm from './env-infraction-target-added-by-cacem-form.tsx'
import { ControlType } from '../../../../../common/types/control-types.ts'
import { InfractionTypeEnum, VesselTypeEnum } from '../../../../../common/types/env-mission-types.ts'
import { Infraction, InfractionByTarget } from '../../../../../common/types/infraction-types.ts'

const infractionMock = {
  id: '123',
  controlType: ControlType.ADMINISTRATIVE,
  infractionType: InfractionTypeEnum.WITHOUT_REPORT,
  natinfs: ['123'],
  observations: undefined,
  target: undefined
}

const infractionByTargetMock = () => ({
  vesselIdentifier: '123',
  vesselType: VesselTypeEnum.COMMERCIAL,
  infractions: [infractionMock],
  controlTypesWithInfraction: [ControlType.ADMINISTRATIVE],
  targetAddedByUnit: false
})

const props = (infractionByTarget: InfractionByTarget, formData: Infraction) => ({
  infraction: infractionByTarget,
  formData: formData,
  onChange: vi.fn(),
  onChangeTarget: vi.fn(),
  onCancel: vi.fn()
})
describe('EnvInfractionTargetAddedByCacemForm', () => {
  test('renders the component', async () => {
    render(<EnvInfractionTargetAddedByCacemForm {...props(infractionByTargetMock(), undefined)} />)
    expect(screen.getByText('Ajout d’une infraction pour cette cible')).toBeInTheDocument()
  })

  describe('when formData is empty', () => {
    test('renders empty text for observations', async () => {
      render(<EnvInfractionTargetAddedByCacemForm {...props(undefined, undefined)} />)
      expect(screen.getByTestId('observations').textContent).toEqual('--')
    })
    test('renders empty text for relevantCourt', async () => {
      render(<EnvInfractionTargetAddedByCacemForm {...props(undefined, undefined)} />)
      expect(screen.getByTestId('relevantCourt').textContent).toEqual('--')
    })
  })
})
