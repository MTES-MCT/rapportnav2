import { FC } from 'react'
import PublicPageLayout from '../features/public/components/layout/public-page-layout.tsx'

// TODO: replace the placeholder once the text is provided.
const LegalNoticePage: FC = () => (
  <PublicPageLayout title="Mentions légales">
    <h1>Mentions légales</h1>
    <div className="fr-alert fr-alert--info">
      <p>Cette page est en cours de rédaction.</p>
    </div>
  </PublicPageLayout>
)

export default LegalNoticePage
