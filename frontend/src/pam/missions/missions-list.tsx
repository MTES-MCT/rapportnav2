import { FlexboxGrid, Stack } from 'rsuite'
import { THEME } from '@mtes-mct/monitor-ui'
import React from 'react'
import { Mission } from '../../types/mission-types.ts'
import MissionItem from '../mission/mission-item.tsx'

interface MissionsListProps {
  missions: Mission[]
  prefetchMission: (missionId: string) => void
}

const MissionsList: React.FC<MissionsListProps> = ({ missions, prefetchMission }) => {
  return (
    <Stack direction="column" alignItems="flex-start" spacing="0.5rem" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <FlexboxGrid align="middle" style={{ height: '100%', padding: '0.5rem 1rem' }}>
          <FlexboxGrid.Item colspan={1} style={{ paddingTop: '8px' }}></FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={5}></FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={5}></FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={3}>
            <p style={{ color: THEME.color.slateGray, fontSize: '12px' }}>Date de début</p>
          </FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={4}>
            <p style={{ color: THEME.color.slateGray, fontSize: '12px' }}>Statut mission</p>
          </FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={3}>
            <p style={{ color: THEME.color.slateGray, fontSize: '12px' }}>Statut du rapport</p>
          </FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={3}></FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={1}></FlexboxGrid.Item>
        </FlexboxGrid>
      </Stack.Item>
      {missions.map((mission: Mission) => (
        <Stack.Item style={{ width: '100%', height: '100%' }}>
          <MissionItem mission={mission} prefetchMission={prefetchMission}></MissionItem>
        </Stack.Item>
      ))}
    </Stack>
  )
}

export default MissionsList
