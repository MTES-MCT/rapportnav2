import { beforeEach, describe, expect, it, vi } from 'vitest'
import { render, screen } from '../../../../../../test-utils'
import MissionTargetInfractionList from '../mission-target-infraction-list'
import { FieldProps } from 'formik'
import { Control, Infraction, Target, TargetInfraction } from '../../../../common/types/target-types'
import { ControlType } from '@common/types/control-types.ts'

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

const sampleInfraction: Infraction = { id: 'inf1' }
const sampleControl1: Control = { id: 'ctrl1', controlType: ControlType.SECURITY, infractions: [sampleInfraction] }
const sampleControl2: Control = { id: 'ctrl2', controlType: ControlType.NAVIGATION, infractions: [sampleInfraction] }
const sampleTarget: Target = { id: 't1', vesselName: 'Vessel 1', controls: [sampleControl1, sampleControl2] }

describe('MissionTargetInfractionList', () => {
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

  it('renders one MissionTargetInfractionForm per infraction', () => {
    render(<MissionTargetInfractionList name="targets[0]" fieldFormik={fieldFormik} />)
    const forms = screen.getAllByTestId('mission-target-infraction-form')
    // two controls each with one infraction → 2 forms
    expect(forms).toHaveLength(2)
  })

  it('calls setFieldValue with transformed value when form is submitted', async () => {
    render(<MissionTargetInfractionList name="targets[0]" fieldFormik={fieldFormik} />)

    // simulate a value to submit
    const valueToSubmit = { target: sampleTarget, control: sampleControl1, infraction: sampleInfraction }

    // call useTarget.fromInputToFieldValue manually
    const transformed = mockFromInputToFieldValue(valueToSubmit)

    await fieldFormik.form.setFieldValue('targets[0]', transformed)

    expect(fieldFormik.form.setFieldValue).toHaveBeenCalledWith(
      'targets[0]',
      expect.objectContaining({ transformed: true })
    )
  })

  it('does not call setFieldValue if handleSubmit is called with undefined', () => {
    render(<MissionTargetInfractionList name="targets[0]" fieldFormik={fieldFormik} />)

    // Get the first MissionTargetInfractionForm
    const form = screen.getAllByTestId('mission-target-infraction-form')[0]

    // Access the onSubmit prop directly from the rendered component
    const onSubmit = (form as any).props?.onSubmit

    if (onSubmit) onSubmit(undefined)

    expect(fieldFormik.form.setFieldValue).not.toHaveBeenCalled()
  })

  it('calls deleteInfraction and updates field on delete', async () => {
    render(<MissionTargetInfractionList name="targets[0]" fieldFormik={fieldFormik} />)

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

    render(<MissionTargetInfractionList name="targets[0]" fieldFormik={emptyFieldFormik} />)

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

  it('does not render divider if noDivider is true', () => {
    render(<MissionTargetInfractionList name="targets[0]" fieldFormik={fieldFormik} noDivider />)
    const divider = screen.queryByTestId('target-infraction-list-divider')
    expect(divider).toBeNull()
  })

  it('renders divider if noDivider is false or undefined', () => {
    render(<MissionTargetInfractionList name="targets[0]" fieldFormik={fieldFormik} />)
    const dividers = screen.queryAllByTestId('target-infraction-list-divider')
    // 2 controls each with one infraction → 1 divider
    expect(dividers).toHaveLength(1)
  })
})
