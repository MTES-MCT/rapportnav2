import { Mission } from '@common/types/mission-types.ts'
import React, { useEffect, useRef, useState } from 'react'
import { Icon, THEME } from '@mtes-mct/monitor-ui'
import { Divider, FlexboxGrid } from 'rsuite'
import { Link } from 'react-router-dom'
import styled from 'styled-components'
import { useControlUnitResourceLabel } from '../../hooks/use-ulam-home-unit-resources.tsx'
import { formatDateForFrenchHumans, formatDateForMissionName } from '@common/utils/dates-for-humans.ts'
import MissionCompletenessForStatsTag from '../../../common/components/elements/mission-completeness-for-stats-tag.tsx'
import MissionStatusTag from '../../../common/components/elements/mission-status-tag.tsx'
import MissionOpenByTag from '@features/pam/mission/components/elements/mission-open-by-tag.tsx'
import { useCrewForMissionList } from '../../hooks/use-crew-for-mission-list.tsx'
import { ULAM_V2_HOME_PATH } from '@router/router.tsx'
import Text from '@common/components/ui/text.tsx'

interface MissionListItemProps {
  mission?: Mission
  index: number
  openIndex: number | null
  setOpenIndex: (index: number | null) => void
}

const ListItemWithHover = styled.div`
  height: inherit;
  background-color: ${THEME.color.cultured};
  transition: background-color 0.3s ease;

  &:hover {
    background-color: ${THEME.color.blueGray25};
  }
`

const MissionCrewItem = styled.div`
  color: ${THEME.color.charcoal};
  font-size: 12px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  padding-right: 8px;
`

const MissionListItemUlam: React.FC<MissionListItemProps> = ({ mission, index, openIndex, setOpenIndex }) => {
  const controlUnitResourcesText = useControlUnitResourceLabel(mission?.controlUnits)
  const missionName = formatDateForMissionName(mission?.startDateTimeUtc)
  const missionDate = formatDateForFrenchHumans(mission?.startDateTimeUtc)
  const missionCrew = useCrewForMissionList(mission?.crew)

  const listItemRef = useRef<HTMLDivElement>(null)
  const isOpen = openIndex === index

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (listItemRef.current && !listItemRef.current.contains(event.target as Node)) {
        setOpenIndex(null)
      }
    }

    if (isOpen) {
      document.addEventListener('mousedown', handleClickOutside)
    } else {
      document.removeEventListener('mousedown', handleClickOutside)
    }

    return () => {
      document.removeEventListener('mousedown', handleClickOutside)
    }
  }, [isOpen, setOpenIndex])

  return (
    <ListItemWithHover
      ref={listItemRef}
      onClick={() => setOpenIndex(isOpen ? null : index)}
      data-testid="mission-list-item-with-hover"
    >
      <Link
        to={ULAM_V2_HOME_PATH}
        style={{
          textDecoration: 'none'
        }}
      >
        <FlexboxGrid align="middle" style={{ height: '100%', padding: '0.5rem 1rem', marginBottom: '15px' }}>
          <FlexboxGrid.Item colspan={1} style={{ paddingTop: '8px' }} data-testid={'mission-list-item-icon'}>
            <Icon.MissionAction size={28} color={THEME.color.charcoal} />
          </FlexboxGrid.Item>

          <FlexboxGrid.Item colspan={3} data-testid={'mission-list-item-mission_number'}>
            <Text weight={'bold'} as={'h3'}>
              Mission n°{missionName}
            </Text>
          </FlexboxGrid.Item>

          <FlexboxGrid.Item colspan={3} data-testid={'mission-list-item-open_by'}>
            <MissionOpenByTag missionSource={mission?.missionSource} isFake={mission?.openBy === 'fake'} />
          </FlexboxGrid.Item>

          <FlexboxGrid.Item colspan={2} data-testid={'mission-list-item-start_date'}>
            <Text as={'h3'}>{missionDate}</Text>
          </FlexboxGrid.Item>

          <>
            <FlexboxGrid.Item colspan={4} data-testid={'mission-list-item-control_unit_resources'}>
              <Text weight={'bold'} as={'h3'}>
                {controlUnitResourcesText}
              </Text>
            </FlexboxGrid.Item>
            <FlexboxGrid.Item colspan={4} data-testid={'mission-list-item-crew'}>
              <MissionCrewItem data-testid={'mission-list-item-crew__text'} title={missionCrew.text}>
                {missionCrew.text}
              </MissionCrewItem>
            </FlexboxGrid.Item>
          </>

          <FlexboxGrid.Item colspan={2} data-testid={'mission-list-item-mission_status'}>
            <MissionStatusTag status={mission?.status} />
          </FlexboxGrid.Item>

          <FlexboxGrid.Item colspan={3} data-testid={'mission-list-item-completeness'}>
            <MissionCompletenessForStatsTag
              completenessForStats={mission?.completenessForStats}
              missionStatus={mission?.status}
            />
          </FlexboxGrid.Item>

          <FlexboxGrid.Item colspan={1} data-testid={'mission-list-item-icon-edit'}>
            <Icon.Chevron size={20} style={{ color: THEME.color.charcoal }} />
          </FlexboxGrid.Item>

          <FlexboxGrid.Item colspan={1} data-testid={'mission-list-item-icon-edit'}>
            <Icon.Edit size={20} style={{ color: THEME.color.charcoal }} />
          </FlexboxGrid.Item>

          {isOpen && mission?.observationsByUnit && (
            <FlexboxGrid.Item colspan={24} data-testid={'mission-list-item-more'}>
              <Divider
                style={{
                  backgroundColor: THEME.color.charcoal
                }}
              />
              <FlexboxGrid justify="space-between" style={{ width: '100%' }}>
                <FlexboxGrid.Item style={{ maxWidth: '60%', overflowWrap: 'break-word' }}>
                  <p
                    style={{
                      color: THEME.color.gunMetal,
                      fontSize: '13px',
                      paddingBottom: '15px'
                    }}
                  >
                    {' '}
                    {mission?.observationsByUnit}
                  </p>
                </FlexboxGrid.Item>
              </FlexboxGrid>
            </FlexboxGrid.Item>
          )}
        </FlexboxGrid>
      </Link>
    </ListItemWithHover>
  )
}

export default MissionListItemUlam
