import { render, screen } from '../../../../../../test-utils.tsx'
import MissionActionEnvThemes from '../mission-action-env-themes.tsx'
import { describe, it, expect } from 'vitest'
import { EnvTheme } from '@common/types/env-themes'

// A helper factory for themes
const makeTheme = (id: string, name?: string, subThemes?: { id: string; name: string }[]): EnvTheme => ({
  id,
  name,
  subThemes
})

describe('MissionActionEnvThemes', () => {
  it('renders nothing if no themes are provided', () => {
    render(<MissionActionEnvThemes />)
    expect(screen.queryByTestId('theme')).toBeNull()
  })

  it('renders a single theme with label "Thématique"', () => {
    render(<MissionActionEnvThemes themes={[makeTheme('1', 'Biodiversité')]} />)
    expect(screen.getByText('Thématique')).toBeInTheDocument()
    expect(screen.getByText('Biodiversité')).toBeInTheDocument()
  })

  it('renders multiple themes with numbering in labels', () => {
    render(<MissionActionEnvThemes themes={[makeTheme('1', 'Air'), makeTheme('2', 'Eau')]} />)
    expect(screen.getByText('Thématique (1)')).toBeInTheDocument()
    expect(screen.getByText('Thématique (2)')).toBeInTheDocument()
  })

  it('falls back to "inconnue" if theme name is missing', () => {
    render(<MissionActionEnvThemes themes={[makeTheme('1')]} />)
    expect(screen.getByText('inconnue')).toBeInTheDocument()
  })

  it('renders sub-themes joined by comma', () => {
    render(
      <MissionActionEnvThemes
        themes={[
          makeTheme('1', 'Mer', [
            { id: 'st1', name: 'Poissons' },
            { id: 'st2', name: 'Algues' }
          ])
        ]}
      />
    )
    expect(screen.getByText('Poissons, Algues')).toBeInTheDocument()
  })

  it('falls back to "inconnues" if sub-themes are missing', () => {
    render(<MissionActionEnvThemes themes={[makeTheme('1', 'Forêt')]} />)
    expect(screen.getByText('inconnues')).toBeInTheDocument()
  })

  it('renders a testid for each theme', () => {
    render(<MissionActionEnvThemes themes={[makeTheme('1', 'Air'), makeTheme('2', 'Eau'), makeTheme('3', 'Sol')]} />)
    const items = screen.getAllByTestId('theme')
    expect(items).toHaveLength(3)
  })
})
