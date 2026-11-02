import { render, screen } from '../../../../../../test-utils'
import PublicPageLayout from '../public-page-layout'
import { describe, it, expect } from 'vitest'

describe('PublicPageLayout', () => {
  it('should render its children inside the main landmark, between header and footer', () => {
    render(
      <PublicPageLayout title="Plan du site">
        <h1>Plan du site</h1>
      </PublicPageLayout>
    )
    expect(screen.getByRole('banner')).toBeInTheDocument()
    expect(screen.getByRole('main')).toHaveAttribute('id', 'content')
    expect(screen.getByRole('main')).toContainElement(screen.getByRole('heading', { level: 1, name: 'Plan du site' }))
    expect(screen.getByRole('contentinfo')).toBeInTheDocument()
  })

  it('should render the skiplinks', () => {
    render(<PublicPageLayout title="Plan du site">content</PublicPageLayout>)
    expect(screen.getByRole('link', { name: 'Contenu' })).toHaveAttribute('href', '#content')
    expect(screen.getByRole('link', { name: 'Pied de page' })).toHaveAttribute('href', '#footer')
  })

  it('should set the document title and restore it on unmount', () => {
    document.title = 'RapportNav'
    const { unmount } = render(<PublicPageLayout title="Plan du site">content</PublicPageLayout>)
    expect(document.title).toBe('Plan du site - RapportNav')
    unmount()
    expect(document.title).toBe('RapportNav')
  })
})
