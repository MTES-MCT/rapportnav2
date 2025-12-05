import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen } from '../../../../../../test-utils'
import MissionTargetControlNav from '../mission-target-control-nav'
import { ControlType } from '@common/types/control-types'

const createMockFieldArray = (targets = []) => ({
  form: {
    values: { targets },
    setFieldValue: vi.fn()
  },
  remove: vi.fn(),
  push: vi.fn()
})

describe('MissionTargetControlNav', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('renders label correctly', () => {
    const targets = [{ controls: [] }]
    const fieldArray = createMockFieldArray(targets)

    render(<MissionTargetControlNav name="targets" label="Navigation Controls" fieldArray={fieldArray} />, {
      formikValues: { targets }
    })

    expect(screen.getByText('Navigation Controls')).toBeInTheDocument()
  })

  it('renders all controls for a target', () => {
    const targets = [
      {
        controls: [
          { controlType: ControlType.SECURITY },
          { controlType: ControlType.NAVIGATION },
          { controlType: ControlType.GENS_DE_MER }
        ]
      }
    ]

    const fieldArray = createMockFieldArray(targets)

    render(<MissionTargetControlNav name="targets" label="Controls" fieldArray={fieldArray} />, {
      formikValues: { targets }
    })

    expect(screen.getAllByTestId('control-form-nav').length).toBe(3)
  })

  it('hides GENS_DE_MER control when hideGensDeMer is true', () => {
    const targets = [
      {
        controls: [
          { controlType: ControlType.SECURITY },
          { controlType: ControlType.NAVIGATION },
          { controlType: ControlType.GENS_DE_MER }
        ]
      }
    ]

    const fieldArray = createMockFieldArray(targets)

    render(<MissionTargetControlNav name="targets" label="Controls" fieldArray={fieldArray} hideGensDeMer={true} />, {
      formikValues: { targets }
    })

    // Should only render 2 controls (SECURITY and NAVIGATION)
    expect(screen.getAllByTestId('control-form-nav').length).toBe(2)
  })

  it('shows GENS_DE_MER control when hideGensDeMer is false', () => {
    const targets = [
      {
        controls: [{ controlType: ControlType.SECURITY }, { controlType: ControlType.GENS_DE_MER }]
      }
    ]

    const fieldArray = createMockFieldArray(targets)

    render(<MissionTargetControlNav name="targets" label="Controls" fieldArray={fieldArray} hideGensDeMer={false} />, {
      formikValues: { targets }
    })

    expect(screen.getAllByTestId('control-form-nav').length).toBe(2)
  })

  it('shows GENS_DE_MER control when hideGensDeMer is undefined', () => {
    const targets = [
      {
        controls: [{ controlType: ControlType.GENS_DE_MER }]
      }
    ]

    const fieldArray = createMockFieldArray(targets)

    render(<MissionTargetControlNav name="targets" label="Controls" fieldArray={fieldArray} />, {
      formikValues: { targets }
    })

    expect(screen.getByTestId('control-form-nav')).toBeInTheDocument()
  })

  it('marks controls as "to complete" when included in controlsToComplete', () => {
    const targets = [
      {
        controls: [{ controlType: ControlType.NAVIGATION }, { controlType: ControlType.SECURITY }]
      }
    ]

    const fieldArray = createMockFieldArray(targets)

    render(
      <MissionTargetControlNav
        name="targets"
        label="Controls"
        fieldArray={fieldArray}
        controlsToComplete={[ControlType.NAVIGATION]}
      />,
      { formikValues: { targets } }
    )

    const forms = screen.getAllByTestId('control-form-nav')
    expect(forms.length).toBe(2)

    // Check that one control is marked as incomplete
    expect(screen.getAllByTestId('control-incomplete-title')).toHaveLength(1)
  })

  it('renders multiple targets with their controls', () => {
    const targets = [
      {
        controls: [{ controlType: ControlType.SECURITY }]
      },
      {
        controls: [{ controlType: ControlType.NAVIGATION }]
      }
    ]

    const fieldArray = createMockFieldArray(targets)

    render(<MissionTargetControlNav name="targets" label="Controls" fieldArray={fieldArray} />, {
      formikValues: { targets }
    })

    expect(screen.getAllByTestId('control-form-nav').length).toBe(2)
  })

  it('renders no controls when control list is empty', () => {
    const targets = [{ controls: [] }]

    const fieldArray = createMockFieldArray(targets)

    render(<MissionTargetControlNav name="targets" label="Controls" fieldArray={fieldArray} />, {
      formikValues: { targets }
    })

    expect(screen.queryByTestId('control-form-nav')).toBeNull()
  })

  it('renders no controls when targets array is empty', () => {
    const targets = []

    const fieldArray = createMockFieldArray(targets)

    render(<MissionTargetControlNav name="targets" label="Controls" fieldArray={fieldArray} />, {
      formikValues: { targets }
    })

    expect(screen.queryByTestId('control-form-nav')).toBeNull()
  })

  it('handles multiple controls with GENS_DE_MER hidden', () => {
    const targets = [
      {
        controls: [
          { controlType: ControlType.SECURITY },
          { controlType: ControlType.GENS_DE_MER },
          { controlType: ControlType.NAVIGATION },
          { controlType: ControlType.GENS_DE_MER }
        ]
      }
    ]

    const fieldArray = createMockFieldArray(targets)

    render(<MissionTargetControlNav name="targets" label="Controls" fieldArray={fieldArray} hideGensDeMer={true} />, {
      formikValues: { targets }
    })

    // Should only render SECURITY and NAVIGATION (2 controls)
    expect(screen.getAllByTestId('control-form-nav').length).toBe(2)
  })

  it('marks multiple controls as "to complete"', () => {
    const targets = [
      {
        controls: [
          { controlType: ControlType.NAVIGATION },
          { controlType: ControlType.SECURITY },
          { controlType: ControlType.GENS_DE_MER }
        ]
      }
    ]

    const fieldArray = createMockFieldArray(targets)

    render(
      <MissionTargetControlNav
        name="targets"
        label="Controls"
        fieldArray={fieldArray}
        controlsToComplete={[ControlType.NAVIGATION, ControlType.SECURITY]}
      />,
      { formikValues: { targets } }
    )

    const forms = screen.getAllByTestId('control-form-nav')
    expect(forms.length).toBe(3)

    // Check that two controls are marked as incomplete
    expect(screen.getAllByTestId('control-incomplete-title')).toHaveLength(2)
  })
})
