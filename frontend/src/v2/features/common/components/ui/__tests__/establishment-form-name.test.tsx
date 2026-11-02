import { fireEvent, render, screen } from '../../../../../../test-utils'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { useDelayFormik } from '../../../hooks/use-delay-formik'
import { EstablishmentFormName } from '../establishment-form-name'

vi.mock('../../../hooks/use-delay-formik', () => ({ useDelayFormik: vi.fn() }))

const onChange = vi.fn()

describe('EstablishmentFormName', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    vi.mocked(useDelayFormik).mockReturnValue({ value: 'FOE', onChange })
  })

  it('should show the delayed value in the name input', () => {
    render(<EstablishmentFormName name="name" label="Etablissement" initValue="FOE" handleSubmit={vi.fn()} />)

    expect(screen.getByDisplayValue('FOE')).toBeInTheDocument()
  })

  it('should not render the message when none is given', () => {
    render(<EstablishmentFormName name="name" label="Etablissement" handleSubmit={vi.fn()} />)

    expect(screen.queryByText('Un message')).not.toBeInTheDocument()
  })

  it('should render the message when given', () => {
    render(
      <EstablishmentFormName
        name="name"
        label="Etablissement"
        message={<span>Un message</span>}
        handleSubmit={vi.fn()}
      />
    )

    expect(screen.getByText('Un message')).toBeInTheDocument()
  })

  it('should pass the init value and a 3 seconds delay to the delay hook', () => {
    render(<EstablishmentFormName name="name" label="Etablissement" initValue="FOE" handleSubmit={vi.fn()} />)

    expect(useDelayFormik).toHaveBeenCalledWith('FOE', expect.any(Function), 3000)
  })

  it('should forward the typed value to the delay hook', () => {
    render(<EstablishmentFormName name="name" label="Etablissement" initValue="FOE" handleSubmit={vi.fn()} />)

    fireEvent.change(screen.getByDisplayValue('FOE'), { target: { value: 'FOE 2' } })

    expect(onChange).toHaveBeenCalledWith('FOE 2')
  })

  it('should call handleSubmit with the value once the delay hook fires', () => {
    const handleSubmit = vi.fn()
    render(<EstablishmentFormName name="name" label="Etablissement" initValue="FOE" handleSubmit={handleSubmit} />)

    const submit = vi.mocked(useDelayFormik).mock.calls[0][1]
    submit('FOE final')

    expect(handleSubmit).toHaveBeenCalledWith('FOE final')
  })
})
