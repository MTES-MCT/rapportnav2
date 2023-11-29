import { Mission, getMissionStatus, MissionStatusEnum } from '../../types/env-mission-types'
import { FlexboxGrid, Stack } from 'rsuite'
import { Link } from 'react-router-dom'
import MissionOpenByTag from './mission-open-by-tag'
import { Icon, THEME } from '@mtes-mct/monitor-ui'
import { formatDateForFrenchHumans } from '../../dates'
import React from 'react'

interface MissionsListProps {
  missions: Mission[]
  prefetchMission: (missionId: string) => void
}

const MissionStatus: React.FC<{ mission: Mission }> = ({ mission }) => {
  const missionStatus = getMissionStatus(mission)
  switch (missionStatus) {
    case MissionStatusEnum.PENDING:
      return <p style={{ color: THEME.color.maximumRed, fontSize: '13px' }}>&#9679; Brouillon</p>
    case MissionStatusEnum.ENDED:
      return <p style={{ color: THEME.color.charcoal, fontSize: '13px' }}>&#10003; Clôturée</p>
    default:
      return <p style={{ color: THEME.color.charcoal, fontSize: '13px' }}>&#10060; N/A</p>
  }
}

const MissionsList: React.FC<MissionsListProps> = ({ missions, prefetchMission }) => {
  return (
    <Stack direction="column" alignItems="flex-start" spacing="0.5rem" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <FlexboxGrid align="middle" style={{ height: '100%', padding: '0.5rem 1rem' }}>
          <FlexboxGrid.Item colspan={1} style={{ paddingTop: '8px' }}></FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={4}></FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={5}></FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={3}>
            <p style={{ color: THEME.color.slateGray, fontSize: '12px' }}>Date de début</p>
          </FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={3}>
            <p style={{ color: THEME.color.slateGray, fontSize: '12px' }}>Statut</p>
          </FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={7}></FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={1}></FlexboxGrid.Item>
        </FlexboxGrid>
      </Stack.Item>
      {missions.map((mission: Mission) => (
        <Stack.Item key={mission.id} style={{ backgroundColor: THEME.color.cultured, width: '100%', height: '64px' }}>
          <FlexboxGrid align="middle" style={{ height: '100%', padding: '0.5rem 1rem' }}>
            <FlexboxGrid.Item colspan={1} style={{ paddingTop: '8px' }}>
              <Icon.MissionAction size={28} />
            </FlexboxGrid.Item>
            <FlexboxGrid.Item colspan={4}>
              <p
                style={{ color: THEME.color.charcoal, fontSize: '16px', fontWeight: 'bold' }}
              >{`Mission #${mission.id}`}</p>
            </FlexboxGrid.Item>
            <FlexboxGrid.Item colspan={5}>
              <MissionOpenByTag missionSource={mission.missionSource} />
            </FlexboxGrid.Item>
            <FlexboxGrid.Item colspan={3}>
              <p style={{ color: THEME.color.charcoal, fontSize: '13px' }}>
                {' '}
                {formatDateForFrenchHumans(mission.startDateTimeUtc)}
              </p>
            </FlexboxGrid.Item>
            <FlexboxGrid.Item colspan={3}>
              <MissionStatus mission={mission} />
            </FlexboxGrid.Item>
            <FlexboxGrid.Item colspan={7}></FlexboxGrid.Item>
            <FlexboxGrid.Item colspan={1}>
              <Link
                to={`/pam/missions/${mission.id}`}
                style={{ color: THEME.color.charcoal }}
                onMouseOver={() => {
                  prefetchMission(mission.id)
                }}
              >
                <Icon.Edit size={20} />
              </Link>
            </FlexboxGrid.Item>
          </FlexboxGrid>
        </Stack.Item>
      ))}
    </Stack>
  )
}

export default MissionsList
