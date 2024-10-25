import { Mission } from '@common/types/mission-types.ts'
import React from 'react'
import { Icon, THEME } from '@mtes-mct/monitor-ui'
import { FlexboxGrid, Stack } from 'rsuite'
import { Link } from 'react-router-dom'
import styled from 'styled-components'
import { useControlUnitResourceLabel } from '../../../ulam/hooks/use-ulam-home-unit-resources.tsx'
import { formatDateForMissionName } from '@common/utils/dates-for-humans.ts'

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
                  ouvert par l'unité
                </p>
              </FlexboxGrid.Item>

              <FlexboxGrid.Item colspan={3} data-testid={'mission-list-item-start_date'}>
                <p style={{ color: THEME.color.charcoal, fontSize: '13px' }}>
                  startdateutc
                </p>
              </FlexboxGrid.Item>

              {isUlam && (
                <>
                  <FlexboxGrid.Item colspan={3} data-testid={'mission-list-item-control_unit_resources'}>
                    <p style={{ color: THEME.color.charcoal, fontSize: '13px', fontWeight: 'bold' }}>
                      {controlUnitResourcesText}
                    </p>
                  </FlexboxGrid.Item>
                  <FlexboxGrid.Item colspan={3} data-testid={'mission-list-item-crew'}>
                  <p style={{ color: THEME.color.charcoal, fontSize: '13px' }}>
                    Clémence Buffeteau, Aleck Vincent, Christian Tonye, Louis Hache
                  </p>
                  </FlexboxGrid.Item>
                </>
              )}

              <FlexboxGrid.Item colspan={2} data-testid={'mission-list-item-mission_status'}>
                <p style={{ color: THEME.color.charcoal, fontSize: '13px' }}>
                  Terminée
                </p>
              </FlexboxGrid.Item>

              <FlexboxGrid.Item colspan={3} data-testid={'mission-list-item-completeness'}>
                <p style={{ color: THEME.color.charcoal, fontSize: '13px' }}>
                  statustag
                </p>
              </FlexboxGrid.Item>

              <FlexboxGrid.Item colspan={1} data-testid={'mission-list-item-icon-edit'}>
                <Icon.Edit size={20} style={{ color: THEME.color.charcoal }} />
              </FlexboxGrid.Item>
            </FlexboxGrid>
          </Link>
        </ListItemWithHover>
      </Stack.Item>
    </Stack>
  )
}

export default MissionListItem
