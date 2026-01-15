import Text from '@common/components/ui/text.tsx'
import { Accent, Icon, IconButton, THEME } from '@mtes-mct/monitor-ui'
import { useGlobalRoutes } from '@router/use-global-routes.tsx'
import React, { MouseEvent, useEffect, useRef } from 'react'
import { useNavigate } from 'react-router-dom'
import { Divider, FlexboxGrid, Stack } from 'rsuite'
import styled from 'styled-components'
import MissionCompletenessForStatsTag from '../../../../common/components/elements/mission-completeness-for-stats-tag.tsx'
import MissionSourceTag from '../../../../common/components/ui/mission-source-tag.tsx'
import MissionStatusTag from '../../../../common/components/ui/mission-status-tag.tsx'
import { MissionListItem } from '../../../../common/types/mission-types.ts'
import { OwnerType } from '../../../../common/types/owner-type.ts'
import { User } from '../../../../common/types/user.ts'
import { useUlamCrewForMissionList } from '../../../hooks/use-ulam-crew-for-mission-list.tsx'
import { useControlUnitResourceLabel } from '../../../hooks/use-ulam-home-unit-resources.tsx'
import MissionIconUlam from '../../ui/mission-icon-ulam.tsx'

interface MissionListItemProps {
  mission: MissionListItem
  index: number
  missionsLength: number
  openIndex: number | null
  setOpenIndex: (index: number | null) => void
  user?: User
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

const MissionListItemUlam: React.FC<MissionListItemProps> = ({
  mission,
  index,
  openIndex,
  setOpenIndex,
  missionsLength,
  user
}) => {
  const navigate = useNavigate()
  const { getUrl } = useGlobalRoutes()
  const missionCrew = useUlamCrewForMissionList(mission.crew)
  const controlUnitResourcesText = useControlUnitResourceLabel(
    mission.controlUnits,
    mission.missionReportType,
    user?.controlUnitId
  )

  const isOpen = openIndex === index
  const listItemRef = useRef<HTMLDivElement>(null)

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

  const onClickRow = (e: MouseEvent) => {
    // Check if the event target is NOT the button or its children
    const isExportButton =
      (e.target as any).closest('button') &&
      (e.target as any).closest('button').getAttribute('data-testid') === 'export-btn'
    const isGoToMissionButton =
      (e.target as any).closest('button') &&
      (e.target as any).closest('button').getAttribute('data-testid') === 'go-to-mission-btn'
    if (isExportButton) {
      return // Ignore clicks on the export button
    } else if (isGoToMissionButton) {
      goToMission(mission)
    }

    setOpenIndex(isOpen ? null : index) // Toggle open state
  }

  const goToMission = (mission: MissionListItem) => {
    const id = mission?.id ?? mission.idUUID
    if (id) navigate(`${getUrl(OwnerType.MISSION)}/${mission?.id ?? mission.idUUID}`)
  }

  return (
    <ListItemWithHover ref={listItemRef} onClick={onClickRow} data-testid="mission-list-item-with-hover">
      <FlexboxGrid align="middle" style={{ height: '100%', padding: '0.5rem 1rem', marginBottom: '4px' }}>
        <FlexboxGrid.Item colspan={1} style={{ paddingTop: '8px' }} data-testid={'mission-list-item-icon'}>
          <MissionIconUlam missionReportType={mission.missionReportType} />
        </FlexboxGrid.Item>

        <FlexboxGrid.Item colspan={3} data-testid={'mission-list-item-mission_number'}>
          <Text weight={'bold'} as={'h2'}>
            {`${mission.missionNameUlam}/${missionsLength - index}`}
          </Text>
        </FlexboxGrid.Item>

        <FlexboxGrid.Item colspan={3} data-testid={'mission-list-item-open_by'} style={{ padding: '0 4px' }}>
          <MissionSourceTag missionSource={mission.missionSource} isFake={mission.openBy === 'fake'} />
        </FlexboxGrid.Item>

        <FlexboxGrid.Item colspan={2} data-testid={'mission-list-item-start_date'}>
          <Text as={'h3'}>{mission.startDateTimeUtcText}</Text>
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

        <FlexboxGrid.Item colspan={3} data-testid={'mission-list-item-mission_status'}>
          <MissionStatusTag status={mission?.status} />
        </FlexboxGrid.Item>

        <FlexboxGrid.Item colspan={3} data-testid={'mission-list-item-completeness'}>
          <MissionCompletenessForStatsTag
            completenessForStats={mission?.completenessForStats}
            missionStatus={mission?.status}
          />
        </FlexboxGrid.Item>

        <FlexboxGrid.Item colspan={1} data-testid={'mission-list-item-icon-edit'} style={{ textAlign: 'right' }}>
          <Stack spacing={'1rem'} justifyContent={'flex-end'}>
            <Stack.Item>
              <IconButton
                Icon={Icon.Chevron}
                accent={Accent.TERTIARY}
                data-testid={'expand-collapse-btn'}
                style={{ transform: isOpen ? 'rotate(180deg)' : '' }}
              />
            </Stack.Item>
            <Stack.Item>
              <IconButton
                Icon={Icon.Edit}
                accent={Accent.TERTIARY}
                data-testid={'go-to-mission-btn'}
                onClick={e => goToMission(mission)}
              />
            </Stack.Item>
          </Stack>
        </FlexboxGrid.Item>

        {isOpen && (
          <FlexboxGrid.Item colspan={24} data-testid={'mission-list-item-more'}>
            <Divider
              style={{
                backgroundColor: THEME.color.charcoal
              }}
            />
            <FlexboxGrid justify="space-between" style={{ width: '100%', marginBottom: '1rem' }}>
              <FlexboxGrid.Item style={{ maxWidth: '60%', overflowWrap: 'break-word' }}>
                <p
                  style={{
                    color: THEME.color.gunMetal,
                    fontSize: '13px',
                    paddingBottom: '15px'
                  }}
                >
                  {mission.observationsByUnit}
                </p>
              </FlexboxGrid.Item>
              <FlexboxGrid.Item></FlexboxGrid.Item>
            </FlexboxGrid>
          </FlexboxGrid.Item>
        )}
      </FlexboxGrid>
    </ListItemWithHover>
  )
}

export default MissionListItemUlam
