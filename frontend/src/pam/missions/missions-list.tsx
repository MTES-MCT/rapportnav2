import { getMissionStatus, Mission, MissionStatusEnum } from '../../types/env-mission-types'
import { FlexboxGrid, Stack } from 'rsuite'
import { Link } from 'react-router-dom'
import MissionOpenByTag from './mission-open-by-tag'
import { Icon, THEME } from '@mtes-mct/monitor-ui'
import { formatDateForFrenchHumans } from '../../utils/dates.ts'
import React from 'react'
import styled from "styled-components";
import { formatMissionName } from "../mission/utils.ts";

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

const MissionStatus: React.FC<{ mission: Mission }> = ({mission}) => {
  const missionStatus = getMissionStatus(mission)
  switch (missionStatus) {
    case MissionStatusEnum.UPCOMING:
      return <p style={{color: THEME.color.charcoal, fontSize: '13px'}}>&#8674; À venir</p>
    case MissionStatusEnum.PENDING:
      return <p style={{color: THEME.color.maximumRed, fontSize: '13px'}}>&#9679; Brouillon</p>
    case MissionStatusEnum.ENDED:
      return <p style={{color: THEME.color.charcoal, fontSize: '13px'}}>&#10003; Clôturée</p>
    default:
      return <p style={{color: THEME.color.charcoal, fontSize: '13px'}}>&#10060; N/A</p>
  }
}

const MissionsList: React.FC<MissionsListProps> = ({missions, prefetchMission}) => {
  return (
    <Stack direction="column" alignItems="flex-start" spacing="0.5rem" style={{width: '100%'}}>
      <Stack.Item style={{width: '100%'}}>
        <FlexboxGrid align="middle" style={{height: '100%', padding: '0.5rem 1rem'}}>
          <FlexboxGrid.Item colspan={1} style={{paddingTop: '8px'}}></FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={4}></FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={5}></FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={3}>
            <p style={{color: THEME.color.slateGray, fontSize: '12px'}}>Date de début</p>
          </FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={3}>
            <p style={{color: THEME.color.slateGray, fontSize: '12px'}}>Statut</p>
          </FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={7}></FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={1}></FlexboxGrid.Item>
        </FlexboxGrid>
      </Stack.Item>
      {missions.map((mission: Mission) => (
        <Stack.Item key={mission.id}
                    style={{backgroundColor: THEME.color.cultured, width: '100%', height: '64px'}}>
          <ListItemWithHover>
            <Link
              to={`/pam/missions/${mission.id}`}
              style={{
                textDecoration: 'none'
              }}
              onMouseOver={(event: any) => {
                prefetchMission(mission.id)
              }}
            >
              <FlexboxGrid align="middle" style={{height: '100%', padding: '0.5rem 1rem'}}>
                <FlexboxGrid.Item colspan={1} style={{paddingTop: '8px'}}>
                  <Icon.MissionAction size={28}/>
                </FlexboxGrid.Item>
                <FlexboxGrid.Item colspan={4}>
                  <p
                    style={{color: THEME.color.charcoal, fontSize: '16px', fontWeight: 'bold'}}
                  >{formatMissionName(mission.startDateTimeUtc)}</p>
                </FlexboxGrid.Item>
                <FlexboxGrid.Item colspan={5}>
                  <MissionOpenByTag missionSource={mission.missionSource} isFake={mission.openBy === 'fake'}/>
                </FlexboxGrid.Item>
                <FlexboxGrid.Item colspan={3}>
                  <p style={{color: THEME.color.charcoal, fontSize: '13px'}}>
                    {' '}
                    {formatDateForFrenchHumans(mission.startDateTimeUtc)}
                  </p>
                </FlexboxGrid.Item>
                <FlexboxGrid.Item colspan={3}>
                  <MissionStatus mission={mission}/>
                </FlexboxGrid.Item>
                <FlexboxGrid.Item colspan={7}></FlexboxGrid.Item>
                <FlexboxGrid.Item colspan={1}>
                  <Icon.Edit size={20} style={{color: THEME.color.charcoal}}/>
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
