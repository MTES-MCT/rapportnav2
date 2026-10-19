import { FC } from 'react'
import { ACCESSIBILITY_PATH, LEGAL_NOTICE_PATH, LOGIN_PATH, PERSONAL_DATA_PATH } from '@router/routes.tsx'
import PublicPageLayout from '../features/public/components/layout/public-page-layout.tsx'

const PUBLIC_PAGES = [
  { label: 'Connexion', path: LOGIN_PATH },
  { label: 'Déclaration d’accessibilité', path: ACCESSIBILITY_PATH },
  { label: 'Mentions légales', path: LEGAL_NOTICE_PATH },
  { label: 'Données personnelles', path: PERSONAL_DATA_PATH }
]

const SitemapPage: FC = () => (
  <PublicPageLayout title="Plan du site">
    <h1>Plan du site</h1>
    <ul>
      {PUBLIC_PAGES.map(({ label, path }) => (
        <li key={path}>
          <a href={path}>{label}</a>
        </li>
      ))}
    </ul>
  </PublicPageLayout>
)

export default SitemapPage
