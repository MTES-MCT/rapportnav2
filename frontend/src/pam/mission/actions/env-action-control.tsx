import React from 'react'
import { THEME } from '@mtes-mct/monitor-ui'
import { EnvActionControl as EnvActionControlType } from '../../env-mission-types'

interface EnvActionControlProps {
  action: EnvActionControlType
}

const EnvActionControl: React.FC<EnvActionControlProps> = ({ action }) => {
  return (
    <div>
      <h1>Controles</h1>

      <div>Thématique de contrôle</div>
      <div>{action.themes[0].theme ?? '-'}</div>
      <br />
      <br />
      <div>Sous-thématiques de contrôle</div>
      <div>{action.themes[0].subThemes?.join(', ') ?? '-'}</div>
      <br />
      <br />
      <div>Date et heure de début et de fin</div>
      <div>{action.actionStartDateTimeUtc}</div>
      <div>{action.actionEndDateTimeUtc}</div>
      <br />
      <br />
      <div>Lieu du contrôle</div>
      <div>{action.geom?.join(' ') ?? '-'}</div>
      <br />
      <br />
      <div>----------------CONTROLES-------------------------</div>
      <div>Nbre total de contrôles</div>
      <div>{action.actionNumberOfControls ?? '-'}</div>
      <br />
      <br />
      <div>Type de cible</div>
      <div>{action.actionTargetType ?? '-'}</div>
      <br />
      <br />
      <div>Type de véhicule</div>
      <div>{action.vehicleType ?? '-'}</div>
      <br />
      <br />
      <div>Observations</div>
      <div>{action.observations ?? '-'}</div>
      <br />
      <br />
      <div></div>
      <div></div>
    </div>
  )
}

export default EnvActionControl
