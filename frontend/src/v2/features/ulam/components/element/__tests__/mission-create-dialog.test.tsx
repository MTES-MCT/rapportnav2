import { render, screen, fireEvent } from '../../../../../../test-utils.tsx'
import { describe, it, vi } from 'vitest'
import MissionCreateDialog from '../mission-create-dialog.tsx'

vi.mock('../../../services/use-create-mission', () => ({
  __esModule: true,
  default: vi.fn(),
}));

describe('MissionCreateDialog', () => {
  it('renders the dialog when isOpen is true', () => {


    render(<MissionCreateDialog isOpen={true} onClose={vi.fn()} />)
    expect(screen.getByText("Création d'un rapport de mission")).toBeInTheDocument()
  })

  it('does not render the dialog when isOpen is false', () => {
    render(<MissionCreateDialog isOpen={false} onClose={vi.fn()} />)
    expect(screen.queryByText("Création d'un rapport de mission")).not.toBeInTheDocument()
  })

  it('calls onClose when the close button is clicked', () => {
    const handleClose = vi.fn()
    render(<MissionCreateDialog isOpen={true} onClose={handleClose} />)

    fireEvent.click(screen.getByText('Annuler'))
    expect(handleClose).toHaveBeenCalled()
  })
})
