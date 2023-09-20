import React from 'react'
import { THEME } from '@mtes-mct/monitor-ui'
import { NavAction } from '../../mission-types'

interface ActionControlNavProps {
  action: NavAction
}

const ActionControlNav: React.FC<ActionControlNavProps> = ({ action }) => {
  return (
    <div>
      <h1>Controles nav</h1>

      <div></div>
      <div></div>
    </div>
  )
}

export default ActionControlNav
