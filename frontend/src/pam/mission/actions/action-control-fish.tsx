import React from 'react'
import { THEME } from '@mtes-mct/monitor-ui'
import { FishAction } from '../../fish-mission-types'

interface ActionControlPropsFish {
  action: FishAction
}

const ActionControlFish: React.FC<ActionControlPropsFish> = ({ action }) => {
  return (
    <div>
      <h1>Controles</h1>

      <div></div>
      <div></div>
    </div>
  )
}

export default ActionControlFish
