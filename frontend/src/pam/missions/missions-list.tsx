import { FlexboxGrid, Stack } from 'rsuite'
import { Link } from 'react-router-dom'
import MissionOpenByTag from '../mission/mission-open-by-tag.tsx'
import { Icon, THEME } from '@mtes-mct/monitor-ui'
import { formatDateForFrenchHumans } from '../../utils/dates.ts'
import React from 'react'
import styled from 'styled-components'
import { formatMissionName } from '../mission/utils.ts'
import { Mission } from '../../types/mission-types.ts'
import MissionStatusTag from '../mission/mission-status-tag.tsx'
import MissionReportStatusTag from '../mission/mission-report-status-tag.tsx'

const ListItemWithHover = styled.div`
  height: inherit;
  background-color: ${THEME.color.cultured};
  transition: background-color 0.3s ease;

  &:hover {
    background-color: ${THEME.color.blueGray25};
  }
`

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
          <FlexboxGrid.Item colspan={3}>
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
        <Stack.Item key={mission.id} style={{ backgroundColor: THEME.color.cultured, width: '100%', height: '64px' }}>
          <ListItemWithHover>
            <Link
              to={`/pam/missions/${mission.id}`}
              style={{
                textDecoration: 'none'
              }}
              onMouseOver={() => {
                prefetchMission(mission.id)
              }}
            >
              <FlexboxGrid align="middle" style={{ height: '100%', padding: '0.5rem 1rem' }}>
                <FlexboxGrid.Item colspan={1} style={{ paddingTop: '8px' }}>
                  <Icon.MissionAction size={28} color={THEME.color.charcoal} />
                </FlexboxGrid.Item>
                <FlexboxGrid.Item colspan={5}>
                  <p style={{ color: THEME.color.charcoal, fontSize: '16px', fontWeight: 'bold' }}>
                    {formatMissionName(mission.startDateTimeUtc)}
                  </p>
                </FlexboxGrid.Item>
                <FlexboxGrid.Item colspan={5}>
                  <MissionOpenByTag missionSource={mission.missionSource} isFake={mission.openBy === 'fake'} />
                </FlexboxGrid.Item>
                <FlexboxGrid.Item colspan={3}>
                  <p style={{ color: THEME.color.charcoal, fontSize: '13px' }}>
                    {' '}
                    {formatDateForFrenchHumans(mission.startDateTimeUtc)}
                  </p>
                </FlexboxGrid.Item>
                <FlexboxGrid.Item colspan={3}>
                  <MissionStatusTag status={mission.status} />
                </FlexboxGrid.Item>
                <FlexboxGrid.Item colspan={3}>
                  <MissionReportStatusTag missionStatus={mission.status} reportStatus={mission.reportStatus?.status} />
                </FlexboxGrid.Item>
                <FlexboxGrid.Item colspan={3}></FlexboxGrid.Item>
                <FlexboxGrid.Item colspan={1}>
                  <Icon.Edit size={20} style={{ color: THEME.color.charcoal }} />
                </FlexboxGrid.Item>
              </FlexboxGrid>
            </Link>
          </ListItemWithHover>
        </Stack.Item>
      ))}
    </Stack>
  )
}

export default MissionsList
