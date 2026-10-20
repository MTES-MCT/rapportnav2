import { FC } from 'react'
import PublicPageLayout from '../features/public/components/layout/public-page-layout.tsx'

// Generated with https://betagouv.github.io/a11y-generateur-declaration/
const AccessibilityPage: FC = () => (
  <PublicPageLayout title="Déclaration d’accessibilité">
    <h1>Déclaration d’accessibilité</h1>
    <p>
      Établie le <span>22 septembre 2026</span>.
    </p>
    <p>
      Direction générale des affaires maritimes, de la pêche et de l'aquaculture s’engage à rendre son service
      accessible, conformément à l’article 47 de la loi n° 2005-102 du 11 février 2005.
    </p>
    <p>
      Cette déclaration d’accessibilité s’applique à <strong>RapportNav</strong>{' '}
      (https://rapport-nav.din.developpement-durable.gouv.fr).
    </p>

    <h2>État de conformité</h2>
    <p>
      <strong>RapportNav</strong> est <strong>non conforme</strong> avec le{' '}
      <abbr title="Référentiel général d’amélioration de l’accessibilité">RGAA</abbr>. Le site n’a encore pas été
      audité.
    </p>

    <h2>Amélioration et contact</h2>
    <p>
      Si vous n’arrivez pas à accéder à un contenu ou à un service, vous pouvez contacter le responsable de RapportNav
      pour être orienté vers une alternative accessible ou obtenir le contenu sous une autre forme.
    </p>
    <ul>
      <li>Téléphone&nbsp;: 0659459120</li>
      <li>
        E-mail&nbsp;: <a href="mailto:alexandre.pardoux@mer.gouv.fr">alexandre.pardoux@mer.gouv.fr</a>
      </li>
      <li>Adresse&nbsp;: DGAMPA, La Défense</li>
    </ul>

    <h2>Voie de recours</h2>
    <p>
      Cette procédure est à utiliser dans le cas suivant&nbsp;: vous avez signalé au responsable du site internet un
      défaut d’accessibilité qui vous empêche d’accéder à un contenu ou à un des services du portail et vous n’avez pas
      obtenu de réponse satisfaisante.
    </p>
    <p>Vous pouvez&nbsp;:</p>
    <ul>
      <li>
        Écrire un message au{' '}
        <a
          href="https://formulaire.defenseurdesdroits.fr/"
          target="_blank"
          rel="noopener external noreferrer"
          title="Défenseur des droits - nouvelle fenêtre"
        >
          Défenseur des droits
        </a>
      </li>
      <li>
        Contacter{' '}
        <a
          href="https://www.defenseurdesdroits.fr/saisir/delegues"
          target="_blank"
          rel="noopener external noreferrer"
          title="le délégué du Défenseur des droits dans votre région - nouvelle fenêtre"
        >
          le délégué du Défenseur des droits dans votre région
        </a>
      </li>
      <li>
        Envoyer un courrier par la poste (gratuit, ne pas mettre de timbre)&nbsp;:
        <br />
        Défenseur des droits
        <br />
        Libre réponse 71120 75342 Paris CEDEX 07
      </li>
    </ul>

    <hr />
    <p>
      Cette déclaration d’accessibilité a été créée le <span>22 septembre 2026</span> grâce au{' '}
      <a
        href="https://betagouv.github.io/a11y-generateur-declaration/#create"
        target="_blank"
        rel="noopener external noreferrer"
        title="Générateur de Déclaration d’Accessibilité - nouvelle fenêtre"
      >
        Générateur de Déclaration d’Accessibilité
      </a>
      .
    </p>
  </PublicPageLayout>
)

export default AccessibilityPage
