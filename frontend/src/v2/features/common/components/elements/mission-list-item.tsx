import { Mission } from '@common/types/mission-types.ts'
import React from 'react'
import { Icon, THEME } from '@mtes-mct/monitor-ui'
import { Divider, FlexboxGrid, Stack } from 'rsuite'
import { Link } from 'react-router-dom'
import styled from 'styled-components'
import { useControlUnitResourceLabel } from '../../../ulam/hooks/use-ulam-home-unit-resources.tsx'
import { formatDateForFrenchHumans, formatDateForMissionName } from '@common/utils/dates-for-humans.ts'
import MissionCompletenessForStatsTag from './mission-completeness-for-stats-tag.tsx'
import MissionStatusTag from './mission-status-tag.tsx'
import MissionOpenByTag from '@features/pam/mission/components/elements/mission-open-by-tag.tsx'
import { useCrewForMissionList } from '../../../ulam/hooks/use-crew-for-mission-list.tsx'

interface MissionListItemProps {
  mission?: Mission,
  isUlam: boolean
}

const ListItemWithHover = styled.div`
  height: inherit;
  background-color: ${THEME.color.cultured};
  transition: background-color 0.3s ease;

  &:hover {
    background-color: ${THEME.color.blueGray25};
  }
`

const MissionListItem: React.FC<MissionListItemProps> = ({mission, isUlam}) => {

  const controlUnitResourcesText = useControlUnitResourceLabel(mission?.controlUnits)
  const missionName = formatDateForMissionName(mission?.startDateTimeUtc)
  const missionDate = formatDateForFrenchHumans(mission?.startDateTimeUtc)
  const missionCrew = useCrewForMissionList(mission?.crew)

  return (
    <Stack>
      <Stack.Item  style={{ backgroundColor: THEME.color.cultured, width: '100%', height: '100%' }}>
        <ListItemWithHover data-testid="list-item-with-hover">
          <Link
            to={'/v2/ulam/missions'}
            style={{
              textDecoration: 'none'
            }}
          >
            <FlexboxGrid align="middle" style={{ height: '100%', padding: '0.5rem 1rem' }}>
              <FlexboxGrid.Item colspan={1} style={{ paddingTop: '8px' }} data-testid={'mission-list-item-icon'}>
                <Icon.MissionAction size={28} color={THEME.color.charcoal} />
              </FlexboxGrid.Item>

              <FlexboxGrid.Item colspan={4} data-testid={'mission-list-item-mission_number'}>
                <p style={{ color: THEME.color.charcoal, fontSize: '16px', fontWeight: 'bold' }}>
                  Mission n°{missionName}
                </p>
              </FlexboxGrid.Item>

              <FlexboxGrid.Item colspan={3} data-testid={'mission-list-item-open_by'}>
                <p style={{ color: THEME.color.charcoal, fontSize: '13px' }}>
                  <MissionOpenByTag missionSource={mission?.missionSource} />
                </p>
              </FlexboxGrid.Item>

              <FlexboxGrid.Item colspan={2} data-testid={'mission-list-item-start_date'}>
                <p style={{ color: THEME.color.charcoal, fontSize: '13px' }}>
                  {missionDate}
                </p>
              </FlexboxGrid.Item>

              {isUlam && (
                <>
                  <FlexboxGrid.Item colspan={3} data-testid={'mission-list-item-control_unit_resources'}>
                    <p style={{ color: THEME.color.charcoal, fontSize: '13px', fontWeight: 'bold' }}>
                      {controlUnitResourcesText}
                    </p>
                  </FlexboxGrid.Item>
                  <FlexboxGrid.Item colspan={4} data-testid={'mission-list-item-crew'}>
                  <p style={missionCrew.style} data-testid={'mission-list-item-crew__text'}>
                    {missionCrew.text}
                  </p>
                  </FlexboxGrid.Item>
                </>
              )}

              <FlexboxGrid.Item colspan={2} data-testid={'mission-list-item-mission_status'}>
                <p style={{ color: THEME.color.charcoal, fontSize: '13px' }}>
                  <MissionStatusTag status={mission?.status} />
                </p>
              </FlexboxGrid.Item>

              <FlexboxGrid.Item colspan={3} data-testid={'mission-list-item-completeness'}>
                <p style={{ color: THEME.color.charcoal, fontSize: '13px' }}>
                  <MissionCompletenessForStatsTag
                    completenessForStats={mission?.completenessForStats}
                    missionStatus={mission?.status} />
                </p>
              </FlexboxGrid.Item>

              <FlexboxGrid.Item colspan={1} data-testid={'mission-list-item-icon-edit'}>
                <Icon.Edit size={20} style={{ color: THEME.color.charcoal }} />
              </FlexboxGrid.Item>
              <FlexboxGrid.Item colspan={24} data-testid={'mission-list-item-more'}>
                <Divider/>
                <FlexboxGrid justify="space-between" style={{width: '100%'}}>
                  <FlexboxGrid.Item style={{ maxWidth: '60%', overflowWrap: 'break-word' }}>
                    <p>
                      {mission?.observationsByUnit}
                    </p>
                  </FlexboxGrid.Item>
                </FlexboxGrid>
              </FlexboxGrid.Item>
            </FlexboxGrid>
          </Link>
        </ListItemWithHover>
      </Stack.Item>
    </Stack>
  )
}

export default MissionListItem
