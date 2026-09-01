import { FC } from 'react'
import logo from '../../../../../assets/images/logo.png'

const LoginHeader: FC = () => (
  <header role="banner" className="fr-header">
    <div className="fr-header__body">
      <div className="fr-container">
        <div className="fr-header__body-row">
          <div className="fr-header__brand fr-enlarge-link">
            <div className="fr-header__brand-top">
              <div className="fr-header__operator">
                <img className="fr-responsive-img" src={logo} alt="RapportNav" style={{ maxWidth: '12rem' }} />
              </div>
            </div>
            <div className="fr-header__service">
              <p className="fr-header__service-title">RapportNav</p>
              <p className="fr-header__service-tagline">Rapports de mission en mer</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </header>
)

export default LoginHeader
