import { THEME } from '@mtes-mct/monitor-ui'
import React from 'react'
import { Stack } from 'rsuite'
import useGetUserQuery from '../../services/use-user'

type MissionListPageTitleProps = {
  userId?: number
}

const MissionListPageTitle: React.FC<MissionListPageTitleProps> = ({ userId }) => {
  const { data: user } = useGetUserQuery(userId)
  return (
    <Stack direction="row" alignItems="center" style={{ alignItems: 'center', width: '100%' }}>
      <Stack.Item>
        <h4>Rapport Nav </h4>
      </Stack.Item>
      <Stack.Item style={{ marginLeft: 2, marginRight: 2 }}>{user && <h4> | </h4>}</Stack.Item>
      <Stack.Item>{user && <h4 style={{ color: THEME.color.blueYonder }}> {user?.serviceName}</h4>}</Stack.Item>
    </Stack>
  )
}

export default MissionListPageTitle
