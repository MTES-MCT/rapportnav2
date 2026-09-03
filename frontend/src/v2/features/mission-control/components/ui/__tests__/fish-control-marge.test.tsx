import { Marge, MetricType, SpeciesControl } from '@common/types/fish-mission-types'
import { fireEvent } from '@testing-library/react'
import { vi } from 'vitest'
import { render, screen } from '../../../../../../test-utils'
import FishControlMarge from '../fish-control-marge'

const species: SpeciesControl = {
  speciesCode: 'COD',
  speciesName: 'Cabillaud',
  isNotLanded: false,
  nbFish: undefined,
  declaredWeight: 100,
  controlledWeight: 95,
  underSized: undefined,
  underSizedWeight: undefined,
  presentationCodes: undefined,
  faoZones: undefined
}

const onClose = vi.fn()
const onSubmit = vi.fn()
const onChangeMarge = vi.fn()

const renderMarge = (currentMarge?: Marge) =>
  render(
    <FishControlMarge
      species={species}
      currentMarge={currentMarge}
      onClose={onClose}
      onSubmit={onSubmit}
      onChangeMarge={onChangeMarge}
    />
  )

describe('FishControlMarge', () => {
  beforeEach(() => {
    onClose.mockClear()
    onSubmit.mockClear()
    onChangeMarge.mockClear()
  })

  it('renders the dialog title with the species code and name', () => {
    renderMarge()
    expect(screen.getByText('Mise à jour marge (COD – Cabillaud)')).toBeInTheDocument()
  })

  it('renders the declared and controlled weights', () => {
    renderMarge()
    expect(screen.getByText('100 kg')).toBeInTheDocument()
    expect(screen.getByText('95 kg')).toBeInTheDocument()
  })

  it('falls back to a dash when the controlled weight is missing', () => {
    render(
      <FishControlMarge
        species={{ ...species, controlledWeight: undefined }}
        onClose={onClose}
        onSubmit={onSubmit}
        onChangeMarge={onChangeMarge}
      />
    )
    expect(screen.getByText('- kg')).toBeInTheDocument()
  })

  it('calls onClose when the close button is clicked', () => {
    renderMarge()
    fireEvent.click(screen.getByTestId('close-dialog-form'))
    expect(onClose).toHaveBeenCalledTimes(1)
  })

  it('calls onSubmit with false when cancel is clicked', () => {
    renderMarge()
    fireEvent.click(screen.getByTestId('dialog-form-cancel-button'))
    expect(onSubmit).toHaveBeenCalledWith(false, undefined)
  })

  it('disables the confirm button until a value and metric are set', () => {
    renderMarge()
    expect(screen.getByTestId('dialog-form-confirm-button')).toBeDisabled()
  })

  it('keeps the confirm button disabled when only the metric is set', () => {
    renderMarge({ metric: MetricType.KG })
    expect(screen.getByTestId('dialog-form-confirm-button')).toBeDisabled()
  })

  it('enables the confirm button once a value and metric are set', () => {
    renderMarge({ value: 5, metric: MetricType.KG })
    expect(screen.getByTestId('dialog-form-confirm-button')).not.toBeDisabled()
  })

  it('calls onSubmit with the current marge when confirm is clicked', () => {
    const currentMarge = { value: 5, metric: MetricType.KG }
    renderMarge(currentMarge)
    fireEvent.click(screen.getByTestId('dialog-form-confirm-button'))
    expect(onSubmit).toHaveBeenCalledWith(true, currentMarge)
  })
})
