import React from 'react'
import { THEME } from '@mtes-mct/monitor-ui'
import { ActionControlEnv as ActionControlTypeEnv } from '../../env-mission-types'

interface ActionControlPropsEnv {
  action: ActionControlTypeEnv
}

const ActionControlEnv: React.FC<ActionControlPropsEnv> = ({ action }) => {
  return (
    <div>
      <h1>Controles</h1>

      <div>Thématique de contrôle</div>
      <div>{action.data?.themes[0].theme ?? '-'}</div>
      <br />
      <br />
      <div>Sous-thématiques de contrôle</div>
      <div>{action.data?.themes[0].subThemes?.join(', ') ?? '-'}</div>
      <br />
      <br />
      <div>Date et heure de début et de fin</div>
      <div>{action.data?.actionStartDateTimeUtc}</div>
      <div>{action.data?.actionEndDateTimeUtc}</div>
      <br />
      <br />
      <div>Lieu du contrôle</div>
      <div>{action.data?.geom?.join(' ') ?? '-'}</div>
      <br />
      <br />
      <div>----------------CONTROLES-------------------------</div>
      <div>Nbre total de contrôles</div>
      <div>{action.data?.actionNumberOfControls ?? '-'}</div>
      <br />
      <br />
      <div>Type de cible</div>
      <div>{action.data?.actionTargetType ?? '-'}</div>
      <br />
      <br />
      <div>Type de véhicule</div>
      <div>{action.data?.vehicleType ?? '-'}</div>
      <br />
      <br />
      <div>Observations</div>
      <div>{action.data?.observations ?? '-'}</div>
      <br />
      <br />
      <div></div>
      <div></div>
    </div>
  )
}

export default ActionControlEnv
