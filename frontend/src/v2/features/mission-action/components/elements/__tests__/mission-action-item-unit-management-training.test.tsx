import { screen } from '@testing-library/react'
import { vi } from 'vitest'
import { render } from '../../../../../../test-utils'
import { ActionType } from '../../../../common/types/action-type'
import { MissionAction } from '../../../../common/types/mission-action'
import { ModuleType } from '../../../../common/types/module-type'
import MissionActionItemUnitManagementTraining, {
  getTrainingTypeOptions
} from '../mission-action-item-unit-management-training'

vi.mock('@mtes-mct/monitor-ui', async () => {
  const actual = await vi.importActual('@mtes-mct/monitor-ui')
  return {
    ...actual,
    FormikSelect: ({ name, options }: any) => (
      <div data-testid={`formik-select-${name}`} data-options={JSON.stringify(options)} />
    ),
    FormikMultiSelect: () => null
  }
})

vi.mock('../mission-action-item-generic-date-observation', () => ({
  default: ({ children }: { children: React.ReactNode }) => <div>{children}</div>
}))

vi.mock('../../../../common/services/use-agents.tsx', () => ({
  default: () => ({ data: [] })
}))

vi.mock('../../../../common/hooks/use-mission-finished.tsx', () => ({
  useMissionFinished: () => false
}))

vi.mock('formik', async () => {
  const actual = await vi.importActual('formik')
  return { ...actual, Field: () => null }
})

const action = {
  id: 'action-id',
  ownerId: 'mission-id',
  actionType: ActionType.UNIT_MANAGEMENT_TRAINING
} as MissionAction

const ULAM_LABELS = [
  'Tir',
  'Plongée',
  "Récupération d'homme à la mer",
  "Techniques d'interventions (menottage...)"
]

const renderedLabels = () =>
  JSON.parse(screen.getByTestId('formik-select-unitManagementTrainingType').getAttribute('data-options') ?? '[]').map(
    (option: { label: string }) => option.label
  )

describe('getTrainingTypeOptions', () => {
  it('should return the 4 shared training types for ULAM', () => {
    expect(getTrainingTypeOptions(ModuleType.ULAM).map(o => o.label)).toEqual(ULAM_LABELS)
  })

  it('should append the PAM-only training types for PAM', () => {
    expect(getTrainingTypeOptions(ModuleType.PAM)).toEqual([
      ...getTrainingTypeOptions(ModuleType.ULAM),
      { value: 'FIRE', label: 'Incendie' },
      { value: 'ABANDON_SHIP', label: 'Abandon du navire' }
    ])
  })
})

describe('MissionActionItemUnitManagementTraining', () => {
  it('should default to ULAM options when no module type is given', () => {
    render(<MissionActionItemUnitManagementTraining action={action} onChange={vi.fn()} />)
    expect(renderedLabels()).toEqual(ULAM_LABELS)
  })

  it('should show the PAM-only options for PAM', () => {
    render(<MissionActionItemUnitManagementTraining action={action} onChange={vi.fn()} moduleType={ModuleType.PAM} />)
    expect(renderedLabels()).toEqual([...ULAM_LABELS, 'Incendie', 'Abandon du navire'])
  })
})
