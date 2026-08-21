import { vi } from 'vitest'
import { fireEvent, render, screen } from '../../../../../../test-utils.tsx'
import MissionListEmptyFiltered from '../mission-list-empty-filtered.tsx'

describe('MissionListEmptyFiltered', () => {
  test('renders the empty-state message and the reset button', () => {
    render(<MissionListEmptyFiltered onReset={vi.fn()} />)
    expect(screen.getByText('Aucune mission')).toBeInTheDocument()
    expect(
      screen.getByText("Les filtres sélectionnés ne correspondent à aucune mission de l'unité")
    ).toBeInTheDocument()
    expect(screen.getByText('Réinitialiser les filtres')).toBeInTheDocument()
  })

  test('calls onReset when the reset button is clicked', () => {
    const onReset = vi.fn()
    render(<MissionListEmptyFiltered onReset={onReset} />)
    fireEvent.click(screen.getByText('Réinitialiser les filtres'))
    expect(onReset).toHaveBeenCalledTimes(1)
  })
})
