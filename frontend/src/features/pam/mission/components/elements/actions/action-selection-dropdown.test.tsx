import { render, screen } from '../../../../../../test-utils.tsx'
import ActionSelectionDropdown from './action-selection-dropdown.tsx'

describe('ActionSelectionDropdown', () => {
  test('renders control selection options', () => {
    render(<ActionSelectionDropdown onSelect={vi.fn()} />)

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
