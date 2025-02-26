import { THEME } from '@mtes-mct/monitor-ui'
import React from 'react'
import { Stack } from 'rsuite'
import { User } from '../../types/user'

type MissionListPageTitleProps = {
  user?: User
}

const MissionListPageTitle: React.FC<MissionListPageTitleProps> = ({ user }) => {
  return (
    <Stack direction="row" alignItems="center" style={{ alignItems: 'center', width: '100%' }}>
      <Stack.Item>
        <h4>Rapport Nav </h4>
      </Stack.Item>
      <Stack.Item style={{ marginLeft: 2, marginRight: 2 }}>{user?.serviceName && <h4> | </h4>}</Stack.Item>
      <Stack.Item>
        {user?.serviceName && <h4 style={{ color: THEME.color.blueGray }}> {user?.serviceName}</h4>}
      </Stack.Item>
    </Stack>
  )
}

export default MissionListPageTitle
