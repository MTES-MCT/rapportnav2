import { FC, ReactNode, useEffect } from 'react'
import PublicHeader from './public-header.tsx'
import PublicFooter from './public-footer.tsx'
import '@gouvfr/dsfr/dist/dsfr.min.css'
// Color/background utility classes (e.g. fr-background-alt--grey) live in a
// separate utility bundle; the core dsfr.min.css does not include them.
import '@gouvfr/dsfr/dist/utility/colors/colors.min.css'
import '../../../../pages/login-page.css'

type PublicPageLayoutProps = {
  title: string
  children: ReactNode
}

// DSFR shell for public (unauthenticated) content pages: accessibilité, mentions légales, etc.
const PublicPageLayout: FC<PublicPageLayoutProps> = ({ title, children }) => {
  useEffect(() => {
    const previousTitle = document.title
    document.title = `${title} - RapportNav`
    return () => {
      document.title = previousTitle
    }
  }, [title])

  return (
    <div className="login-page" style={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      <div className="fr-skiplinks">
        <nav className="fr-container" role="navigation" aria-label="Accès rapide">
          <ul className="fr-skiplinks__list">
            <li>
              <a className="fr-link" href="#content">
                Contenu
              </a>
            </li>
            <li>
              <a className="fr-link" href="#footer">
                Pied de page
              </a>
            </li>
          </ul>
        </nav>
      </div>

      <PublicHeader />

      <main className="fr-py-md-14v fr-py-8v" role="main" id="content" tabIndex={-1} style={{ flex: '1 0 auto' }}>
        <div className="fr-container">
          <div className="fr-grid-row fr-grid-row--center">
            <div className="fr-col-12 fr-col-md-10 fr-col-lg-8">{children}</div>
          </div>
        </div>
      </main>

      <PublicFooter />
    </div>
  )
}

export default PublicPageLayout
