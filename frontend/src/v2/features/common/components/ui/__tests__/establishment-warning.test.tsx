import { fireEvent, render, screen } from '../../../../../../test-utils'
import { describe, expect, it, vi } from 'vitest'
import { EstablishmentWarning } from '../establishment-warning'

describe('EstablishmentWarning', () => {
  it('should render the confirmation title and the explanation', () => {
    render(<EstablishmentWarning onYes={vi.fn()} onNo={vi.fn()} />)

    expect(screen.getByText(/Je confirme que l’établissement n’a pas été trouvé/)).toBeInTheDocument()
    expect(screen.getByText('SIRET')).toBeInTheDocument()
    expect(screen.getByText(/Attention, si l’entreprise est/)).toBeInTheDocument()
    expect(screen.getByText('française et déclarée, elle existe forcément')).toBeInTheDocument()
  })

  it('should call onYes only when clicking the manual add button', () => {
    const onYes = vi.fn()
    const onNo = vi.fn()
    render(<EstablishmentWarning onYes={onYes} onNo={onNo} />)

    fireEvent.click(screen.getByText('Oui, je dois ajouter l’établissement manuellement'))

    expect(onYes).toHaveBeenCalledTimes(1)
    expect(onNo).not.toHaveBeenCalled()
  })

  it('should call onNo only when clicking the search again button', () => {
    const onYes = vi.fn()
    const onNo = vi.fn()
    render(<EstablishmentWarning onYes={onYes} onNo={onNo} />)

    fireEvent.click(screen.getByText('Non, je recherche à nouveau'))

    expect(onNo).toHaveBeenCalledTimes(1)
    expect(onYes).not.toHaveBeenCalled()
  })
})
