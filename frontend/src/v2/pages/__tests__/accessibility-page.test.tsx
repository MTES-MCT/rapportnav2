import { render, screen } from '../../../test-utils.tsx'
import AccessibilityPage from '../accessibility-page.tsx'
import { describe, expect, it } from 'vitest'

describe('AccessibilityPage', () => {
  it('should render the declaration headings', () => {
    render(<AccessibilityPage />)
    expect(screen.getAllByRole('heading', { level: 1 })).toHaveLength(1)
    expect(screen.getByRole('heading', { level: 1, name: 'Déclaration d’accessibilité' })).toBeInTheDocument()
    expect(screen.getByRole('heading', { level: 2, name: 'État de conformité' })).toBeInTheDocument()
    expect(screen.getByRole('heading', { level: 2, name: 'Amélioration et contact' })).toBeInTheDocument()
    expect(screen.getByRole('heading', { level: 2, name: 'Voie de recours' })).toBeInTheDocument()
  })

  it('should render the compliance status and the establishment date', () => {
    render(<AccessibilityPage />)
    expect(screen.getByText('non conforme')).toBeInTheDocument()
    expect(screen.getAllByText('22 septembre 2026')).toHaveLength(2)
  })

  it('should render the contact and recourse links', () => {
    render(<AccessibilityPage />)
    expect(screen.getByRole('link', { name: 'alexandre.pardoux@mer.gouv.fr' })).toHaveAttribute(
      'href',
      'mailto:alexandre.pardoux@mer.gouv.fr'
    )
    const defenseur = screen.getByRole('link', { name: 'Défenseur des droits' })
    expect(defenseur).toHaveAttribute('href', 'https://formulaire.defenseurdesdroits.fr/')
    expect(defenseur).toHaveAttribute('target', '_blank')
    expect(defenseur).toHaveAttribute('title', 'Défenseur des droits - nouvelle fenêtre')
    expect(screen.getByRole('link', { name: 'le délégué du Défenseur des droits dans votre région' })).toHaveAttribute(
      'href',
      'https://www.defenseurdesdroits.fr/saisir/delegues'
    )
  })

  it('should render the page landmarks', () => {
    render(<AccessibilityPage />)
    expect(screen.getByRole('banner')).toBeInTheDocument()
    expect(screen.getByRole('main')).toHaveAttribute('id', 'content')
    expect(screen.getByRole('contentinfo')).toBeInTheDocument()
  })

  it('should set the document title', () => {
    render(<AccessibilityPage />)
    expect(document.title).toBe('Déclaration d’accessibilité - RapportNav')
  })
})
