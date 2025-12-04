import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen } from '../../../../../../test-utils'
import MissionTargetControlEnv from '../mission-target-control-env'
import { ControlType } from '@common/types/control-types'

// Mock useTarget
const mockIsDefaultTarget = vi.fn()

vi.mock('../../../hooks/use-target', () => ({
  useTarget: () => ({
    isDefaultTarget: mockIsDefaultTarget
  })
}))

const createMockFieldArray = (targets = []) => ({
  form: {
    values: { targets },
    setFieldValue: vi.fn()
  },
  remove: vi.fn(),
  push: vi.fn()
})

describe('MissionTargetControlEnv', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('renders controls for default target', () => {
    mockIsDefaultTarget.mockReturnValue(true)

    const targets = [
      {
        controls: [{ controlType: ControlType.SECURITY }, { controlType: ControlType.NAVIGATION }]
      }
    ]

    const fieldArray = createMockFieldArray(targets)

    render(
      <MissionTargetControlEnv
        name="targets"
        fieldArray={fieldArray}
        controlsToComplete={[]}
        actionNumberOfControls={5}
      />,
      { formikValues: { targets } }
    )

    expect(screen.getAllByTestId('control-form-env').length).toBe(2)
  })

  it('does not render controls for non-default target', () => {
    mockIsDefaultTarget.mockReturnValue(false)

    const targets = [
      {
        controls: [{ controlType: ControlType.SECURITY }]
      }
    ]

    const fieldArray = createMockFieldArray(targets)

    render(
      <MissionTargetControlEnv
        name="targets"
        fieldArray={fieldArray}
        controlsToComplete={[]}
        actionNumberOfControls={5}
      />
    )

    expect(screen.queryByTestId('control-form-env')).not.toBeInTheDocument()
  })

  it('marks controls as "to complete" when included in controlsToComplete', () => {
    mockIsDefaultTarget.mockReturnValue(true)

    const targets = [
      {
        controls: [{ controlType: ControlType.NAVIGATION }, { controlType: ControlType.SECURITY }]
      }
    ]

    const fieldArray = createMockFieldArray(targets)

    render(
      <MissionTargetControlEnv
        name="targets"
        fieldArray={fieldArray}
        controlsToComplete={[ControlType.NAVIGATION]}
        actionNumberOfControls={10}
      />,
      { formikValues: { targets } }
    )

    const forms = screen.getAllByTestId('control-form-env')
    expect(forms.length).toBe(2)

    expect(screen.getAllByTestId('control-incomplete-title')).toHaveLength(1)
  })

  it('renders no controls when control list is empty', () => {
    mockIsDefaultTarget.mockReturnValue(true)

    const targets = [{ controls: [] }]

    const fieldArray = createMockFieldArray(targets)

    render(
      <MissionTargetControlEnv
        name="targets"
        fieldArray={fieldArray}
        controlsToComplete={[]}
        actionNumberOfControls={5}
      />,
      { formikValues: { targets } }
    )

    expect(screen.queryByTestId('control-form-env')).toBeNull()
  })
})
