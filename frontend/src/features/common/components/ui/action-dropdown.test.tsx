import { vi } from 'vitest'
import { act, fireEvent, render, screen } from '../../../../test-utils'
import ActionDropdown from './action-dropdown'

const onSelect = vi.fn()

describe('ActionDropdown', () => {
  it('should render main items menu', () => {
    render(<ActionDropdown onSelect={onSelect} />)

    expect(screen.getByText('Ajouter des contrôles')).toBeInTheDocument()
    expect(screen.getByText('Ajouter une note libre')).toBeInTheDocument()
    expect(screen.getByText('Ajouter une assistance / sauvetage')).toBeInTheDocument()
    expect(screen.getByText('Ajouter une autre activité de mission')).toBeInTheDocument()
  })

  it('should render main items and sub items', async () => {
    const wrapper = render(<ActionDropdown onSelect={onSelect} />)
    const item = wrapper.getByText('Ajouter une autre activité de mission')
    act(() => {
      fireEvent.click(item)
    })
    expect(screen.getByText('Ajouter des contrôles')).toBeInTheDocument()
    expect(screen.getByText('Ajouter une note libre')).toBeInTheDocument()
    expect(screen.getByText('Ajouter une assistance / sauvetage')).toBeInTheDocument()
    expect(screen.getByText('Sécu de manifestation nautique')).toBeInTheDocument()
    expect(screen.getByText('Permanence Vigimer')).toBeInTheDocument()
    expect(screen.getByText('Opération de lutte anti-pollution')).toBeInTheDocument()
    expect(screen.getByText('Permanence BAAEM')).toBeInTheDocument()
    expect(screen.getByText("Maintien de l'ordre public")).toBeInTheDocument()
    expect(screen.getByText('Représentation')).toBeInTheDocument()
    expect(screen.getByText("Lutte contre l'immigration illégale")).toBeInTheDocument()
  })
})
