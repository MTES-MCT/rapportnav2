import { FC } from 'react'
import LoginHeader from '../features/auth/components/login/login-header.tsx'
import LoginFooter from '../features/auth/components/login/login-footer.tsx'
import LoginForm from '../features/auth/components/login/login-form.tsx'
import '@gouvfr/dsfr/dist/dsfr.min.css'
// Color/background utility classes (e.g. fr-background-alt--grey) live in a
// separate utility bundle; the core dsfr.min.css does not include them.
import '@gouvfr/dsfr/dist/utility/colors/colors.min.css'

// Re-exported for the auth API mock in tests (see __tests__/test-server.ts).
export { LOGIN_ENDPOINT } from '../features/auth/components/login/login-form.tsx'

const LoginPage: FC = () => (
  <div style={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
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

    <LoginHeader />

    <main className="fr-pt-md-14v" role="main" id="content" tabIndex={-1} style={{ flex: '1 0 auto' }}>
      <div className="fr-container fr-container--fluid fr-mb-md-14v">
        <div className="fr-grid-row fr-grid-row--gutters fr-grid-row--center">
          <div className="fr-col-12 fr-col-md-8 fr-col-lg-8">
            <div className="fr-container fr-background-alt--grey fr-px-md-0 fr-py-10v fr-py-md-14v">
              <div className="fr-grid-row fr-grid-row--gutters fr-grid-row--center">
                <div className="fr-col-12 fr-col-md-9 fr-col-lg-8">
                  <h1>Connexion à RapportNav</h1>
                  <LoginForm />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>

    <LoginFooter />
  </div>
)

export default LoginPage
