import { render, screen } from '../../../test-utils.tsx'
import LegalNoticePage from '../legal-notice-page.tsx'
import PersonalDataPage from '../personal-data-page.tsx'
import SitemapPage from '../sitemap-page.tsx'
import { describe, expect, it } from 'vitest'

describe('LegalNoticePage', () => {
  it('should render the title and the placeholder notice', () => {
    render(<LegalNoticePage />)
    expect(screen.getByRole('heading', { level: 1, name: 'Mentions légales' })).toBeInTheDocument()
    expect(screen.getByText('Cette page est en cours de rédaction.')).toBeInTheDocument()
    expect(document.title).toBe('Mentions légales - RapportNav')
  })
})

describe('PersonalDataPage', () => {
  it('should render the title and the placeholder notice', () => {
    render(<PersonalDataPage />)
    expect(screen.getByRole('heading', { level: 1, name: 'Données personnelles' })).toBeInTheDocument()
    expect(screen.getByText('Cette page est en cours de rédaction.')).toBeInTheDocument()
    expect(document.title).toBe('Données personnelles - RapportNav')
  })
})

describe('SitemapPage', () => {
  it('should list the public pages', () => {
    render(<SitemapPage />)
    expect(screen.getByRole('heading', { level: 1, name: 'Plan du site' })).toBeInTheDocument()
    const main = screen.getByRole('main')
    const links = Array.from(main.querySelectorAll('a')).map(a => [a.textContent, a.getAttribute('href')])
    expect(links).toEqual([
      ['Connexion', '/login'],
      ['Déclaration d’accessibilité', '/accessibilite'],
      ['Mentions légales', '/mentions-legales'],
      ['Données personnelles', '/donnees-personnelles']
    ])
    expect(document.title).toBe('Plan du site - RapportNav')
  })
})
