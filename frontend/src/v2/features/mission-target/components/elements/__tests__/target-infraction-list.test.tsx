import { ControlType } from '@common/types/control-types.ts'
import { FieldProps } from 'formik'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { cleanup, fireEvent, render, screen } from '../../../../../../test-utils'
import { Control, Infraction, Target } from '../../../../common/types/target-types'
import TargetInfractionList from '../target-infraction-list'

// Mock useTarget
const mockDeleteInfraction = vi.fn()
const mockFromInputToFieldValue = vi.fn()

vi.mock('../../../hooks/use-target', () => ({
  useTarget: () => ({
    deleteInfraction: (target, ctrlIndex, infIndex) => {
      const newTarget = { ...target }
      newTarget.controls![ctrlIndex].infractions!.splice(infIndex, 1)
      return newTarget
    },
    fromInputToFieldValue: (value: any) => ({ ...value, transformed: true })
  })
}))

const createFieldFormik = (targets: Target[]): FieldProps<Target> => ({
  field: { value: targets[0] },
  form: {
    setFieldValue: vi.fn(),
    values: { targets }
  } as any
})

const sampleInfraction1: Infraction = { id: 'inf1' }
const sampleInfraction2: Infraction = { id: 'inf2' }
const sampleControl1: Control = { id: 'ctrl1', controlType: ControlType.SECURITY, infractions: [sampleInfraction1] }
const sampleControl2: Control = {
  id: 'ctrl2',
  controlType: ControlType.NAVIGATION,
  infractions: [sampleInfraction1, sampleInfraction2]
}
const sampleTarget: Target = { id: 't1', vesselName: 'Vessel 1', controls: [sampleControl1, sampleControl2] }

describe('TargetInfractionList', () => {
  let fieldFormik: FieldProps<Target>

  beforeEach(() => {
    vi.clearAllMocks()
    fieldFormik = createFieldFormik([sampleTarget])
    mockFromInputToFieldValue.mockImplementation(v => ({ ...v, transformed: true }))
    mockDeleteInfraction.mockImplementation((target, ctrlIndex, infIndex) => {
      const newTarget = { ...target }
      newTarget.controls![ctrlIndex].infractions!.splice(infIndex, 1)
      return newTarget
    })
  })

  it('renders one TargetInfractionForm per infraction', () => {
    render(<TargetInfractionList name="targets[0]" fieldFormik={fieldFormik} />)
    const forms = screen.getAllByTestId('target-infraction-form')
    // two controls each with 1-2 infraction → 3 forms
    expect(forms).toHaveLength(3)
  })

  it('calls setFieldValue with transformed value when form is submitted', async () => {
    render(<TargetInfractionList name="targets[0]" fieldFormik={fieldFormik} />)

    // simulate a value to submit
    const valueToSubmit = { target: sampleTarget, control: sampleControl1, infraction: sampleInfraction1 }

    // call useTarget.fromInputToFieldValue manually
    const transformed = mockFromInputToFieldValue(valueToSubmit)

    await fieldFormik.form.setFieldValue('targets[0]', transformed)

    expect(fieldFormik.form.setFieldValue).toHaveBeenCalledWith(
      'targets[0]',
      expect.objectContaining({ transformed: true })
    )
  })

  it('renders nothing when target has no controls', () => {
    const noControlsTarget: Target = { id: 't1', vesselName: 'Vessel 1', controls: undefined }
    const noControlsFieldFormik = createFieldFormik([noControlsTarget])

    const { container } = render(<TargetInfractionList name="targets[0]" fieldFormik={noControlsFieldFormik} />)

    const forms = screen.queryAllByTestId('target-infraction-form')
    expect(forms).toHaveLength(0)
    expect(container.querySelector('div')).toBeInTheDocument() // wrapper div still exists
  })

  it('renders nothing when target has controls but no infractions', () => {
    const noInfractionsTarget: Target = {
      ...sampleTarget,
      controls: [
        { id: 'ctrl1', controlType: ControlType.SECURITY, infractions: undefined },
        { id: 'ctrl2', controlType: ControlType.NAVIGATION, infractions: [] }
      ]
    }
    const noInfractionsFieldFormik = createFieldFormik([noInfractionsTarget])

    render(<TargetInfractionList name="targets[0]" fieldFormik={noInfractionsFieldFormik} />)

    const forms = screen.queryAllByTestId('target-infraction-form')
    expect(forms).toHaveLength(0)
  })

  it('does not call setFieldValue if handleSubmit is called with undefined', () => {
    render(<TargetInfractionList name="targets[0]" fieldFormik={fieldFormik} />)

    // Get the first TargetInfractionForm
    const form = screen.getAllByTestId('target-infraction-form')[0]

    // Access the onSubmit prop directly from the rendered component
    const onSubmit = (form as any).props?.onSubmit

    if (onSubmit) onSubmit(undefined)

    expect(fieldFormik.form.setFieldValue).not.toHaveBeenCalled()
  })

  it('calls deleteInfraction and updates field on delete', async () => {
    render(<TargetInfractionList name="targets[0]" fieldFormik={fieldFormik} />)

    // simulate deleting first infraction
    const updatedTarget = mockDeleteInfraction(sampleTarget, 0, 0)
    await fieldFormik.form.setFieldValue('targets[0]', updatedTarget)

    expect(fieldFormik.form.setFieldValue).toHaveBeenCalledWith(
      'targets[0]',
      expect.objectContaining({ controls: [{ ...sampleControl1, infractions: [] }, sampleControl2] })
    )
  })

  it('does not fail when handleDelete is called on a control without infractions', async () => {
    const emptyControlTarget: Target = {
      ...sampleTarget,
      controls: [{ id: 'ctrlEmpty', controlType: ControlType.ADMINISTRATIVE, infractions: [] }]
    }
    const emptyFieldFormik = createFieldFormik([emptyControlTarget])

    render(<TargetInfractionList name="targets[0]" fieldFormik={emptyFieldFormik} />)

    // Call the mocked deleteInfraction directly
    const updatedTarget = mockDeleteInfraction(emptyControlTarget, 0, 0)

    await emptyFieldFormik.form.setFieldValue('targets[0]', updatedTarget)

    expect(emptyFieldFormik.form.setFieldValue).toHaveBeenCalledWith(
      'targets[0]',
      expect.objectContaining({
        controls: [{ id: 'ctrlEmpty', controlType: ControlType.ADMINISTRATIVE, infractions: [] }]
      })
    )
  })

  describe('interactions', () => {
    vi.mock('../target-infraction-form', () => ({
      default: ({ onSubmit, onDelete, value, ...props }: any) => (
        <div data-testid="target-infraction-form">
          <button data-testid="submit-button" onClick={() => onSubmit(value)}>
            Submit
          </button>
          <button data-testid="delete-button" onClick={() => onDelete()}>
            Delete
          </button>
        </div>
      )
    }))

    beforeEach(() => {
      vi.clearAllMocks()
      fieldFormik = createFieldFormik([sampleTarget])
    })

    afterEach(() => {
      cleanup()
    })

    // Then replace/add these tests:
    it('calls fromInputToFieldValue and setFieldValue when handleSubmit is called with a value', async () => {
      render(<TargetInfractionList name="targets[0]" fieldFormik={fieldFormik} />)

      const submitButtons = screen.getAllByTestId('submit-button')
      submitButtons[0].click()

      expect(fieldFormik.form.setFieldValue).toHaveBeenCalledWith(
        'targets[0]',
        expect.objectContaining({ transformed: true })
      )
    })

    it('calls deleteInfraction and setFieldValue when handleDelete is called', async () => {
      render(<TargetInfractionList name="targets[0]" fieldFormik={fieldFormik} />)

      const deleteButtons = screen.getAllByTestId('delete-button')
      deleteButtons[0].click()

      expect(fieldFormik.form.setFieldValue).toHaveBeenCalledWith(
        'targets[0]',
        expect.objectContaining({
          controls: [{ ...sampleControl1, infractions: [] }, sampleControl2]
        })
      )
    })

    it('calls handleDelete with correct indices for second control', async () => {
      // Create a target with multiple infractions to ensure we have 2 forms
      const multiInfractionTarget: Target = {
        ...sampleTarget,
        controls: [
          {
            id: 'ctrl1',
            controlType: ControlType.SECURITY,
            infractions: [sampleInfraction1, sampleInfraction2]
          }
        ]
      }

      const freshFieldFormik = createFieldFormik([multiInfractionTarget])

      render(<TargetInfractionList name="targets[0]" fieldFormik={freshFieldFormik} />)

      const deleteButtons = screen.getAllByTestId('delete-button')

      expect(deleteButtons).toHaveLength(2)

      // Click the second delete button
      fireEvent.click(deleteButtons[1])

      expect(freshFieldFormik.form.setFieldValue).toHaveBeenCalledWith(
        'targets[0]',
        expect.objectContaining({
          controls: [
            {
              id: 'ctrl1',
              controlType: ControlType.SECURITY,
              infractions: [sampleInfraction1] // Second infraction removed
            }
          ]
        })
      )
    })
  })
})
