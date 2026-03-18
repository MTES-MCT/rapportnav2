import { FieldProps } from 'formik'
import { describe, expect, it, vi } from 'vitest'
import { render, screen } from '../../../../../../test-utils'
import { MissionActionFormikCoordinateInputDMD } from '../mission-action-formik-coordonate-input-dmd'

const createFieldFormik = (value: number[] | undefined, error?: string): FieldProps<number[]> => {
  const setFieldValue = vi.fn()
  return {
    field: {
      name: 'geoCoords',
      value: value as number[],
      onChange: vi.fn(),
      onBlur: vi.fn()
    },
    meta: {
      value: value as number[],
      error,
      touched: false,
      initialValue: value as number[],
      initialTouched: false,
      initialError: undefined
    },
    form: {
      setFieldValue
    } as any
  }
}

describe('MissionActionFormikCoordinateInputDMD', () => {
  it('renders the coordinate input with label', () => {
    const fieldFormik = createFieldFormik([48.8566, 2.3522])

    render(<MissionActionFormikCoordinateInputDMD name="geoCoords" fieldFormik={fieldFormik} />)

    expect(screen.getByText(/Lieu de l'opération/)).toBeInTheDocument()
  })

  it('does not render when fieldFormik value is undefined', () => {
    const fieldFormik = createFieldFormik(undefined)

    const { container } = render(
      <MissionActionFormikCoordinateInputDMD name="geoCoords" fieldFormik={fieldFormik} />
    )

    expect(container.querySelector('input')).not.toBeInTheDocument()
  })

  it('renders coordinate inputs when value is provided', () => {
    const fieldFormik = createFieldFormik([48.8566, 2.3522])

    render(<MissionActionFormikCoordinateInputDMD name="geoCoords" fieldFormik={fieldFormik} />)

    const inputs = screen.getAllByRole('textbox')
    expect(inputs.length).toBeGreaterThan(0)
  })

  it('does not call setFieldValue on initial render', () => {
    const fieldFormik = createFieldFormik([48.8566, 2.3522])

    render(<MissionActionFormikCoordinateInputDMD name="geoCoords" fieldFormik={fieldFormik} />)

    // isCoordsEqual prevents unnecessary updates on initial render
    expect(fieldFormik.form.setFieldValue).not.toHaveBeenCalled()
  })
})