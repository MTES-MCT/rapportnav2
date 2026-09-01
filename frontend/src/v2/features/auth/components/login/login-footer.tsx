import { FC } from 'react'
import logo from '../../../../../assets/images/logo.png'

const LoginFooter: FC = () => (
  <footer className="fr-footer" role="contentinfo" id="footer" tabIndex={-1}>
    <div className="fr-container">
      <div className="fr-footer__body">
        <div className="fr-footer__brand">
          <img className="fr-responsive-img" src={logo} alt="RapportNav" style={{ maxWidth: '12rem' }} />
        </div>
        <div className="fr-footer__content">
          <p className="fr-footer__content-desc">
            RapportNav est l'outil de rapportage des missions des agents de contrôle des affaires maritimes.
          </p>
          <ul className="fr-footer__content-list">
            <li className="fr-footer__content-item">
              <a
                className="fr-footer__content-link"
                target="_blank"
                rel="noopener external noreferrer"
                title="info.gouv.fr - nouvelle fenêtre"
                href="https://info.gouv.fr"
              >
                info.gouv.fr
              </a>
            </li>
            <li className="fr-footer__content-item">
              <a
                className="fr-footer__content-link"
                target="_blank"
                rel="noopener external noreferrer"
                title="service-public.fr - nouvelle fenêtre"
                href="https://service-public.fr"
              >
                service-public.fr
              </a>
            </li>
            <li className="fr-footer__content-item">
              <a
                className="fr-footer__content-link"
                target="_blank"
                rel="noopener external noreferrer"
                title="legifrance.gouv.fr - nouvelle fenêtre"
                href="https://legifrance.gouv.fr"
              >
                legifrance.gouv.fr
              </a>
            </li>
            <li className="fr-footer__content-item">
              <a
                className="fr-footer__content-link"
                target="_blank"
                rel="noopener external noreferrer"
                title="data.gouv.fr - nouvelle fenêtre"
                href="https://data.gouv.fr"
              >
                data.gouv.fr
              </a>
            </li>
          </ul>
        </div>
      </div>
      <div className="fr-footer__bottom">
        <div className="fr-footer__bottom-copy">
          <p>
            Sauf mention explicite de propriété intellectuelle détenue par des tiers, les contenus de ce site sont
            proposés sous{' '}
            <a
              href="https://github.com/etalab/licence-ouverte/blob/master/LO.md"
              target="_blank"
              rel="noopener external noreferrer"
              title="Licence etalab-2.0 - nouvelle fenêtre"
            >
              licence etalab-2.0
            </a>
          </p>
        </div>
      </div>
    </div>
  </footer>
)

export default LoginFooter
