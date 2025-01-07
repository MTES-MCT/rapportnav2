import { Mission } from '@common/types/mission-types.ts'
import React, { MouseEvent, useEffect, useRef } from 'react'
import { Accent, Icon, IconButton, THEME } from '@mtes-mct/monitor-ui'
import { Divider, FlexboxGrid, Stack } from 'rsuite'
import { redirect } from 'react-router-dom'
import styled from 'styled-components'
import { useControlUnitResourceLabel } from '../../../hooks/use-ulam-home-unit-resources.tsx'
import { formatDateForFrenchHumans } from '@common/utils/dates-for-humans.ts'
import MissionCompletenessForStatsTag from '../../../../common/components/elements/mission-completeness-for-stats-tag.tsx'
import MissionStatusTag from '../../../../common/components/elements/mission-status-tag.tsx'
import MissionOpenByTag from '@features/pam/mission/components/elements/mission-open-by-tag.tsx'
import { useCrewForMissionList } from '../../../hooks/use-crew-for-mission-list.tsx'
import { ULAM_V2_HOME_PATH } from '@router/router.tsx'
import Text from '@common/components/ui/text.tsx'
import { formatMissionName } from '@features/pam/mission/utils/utils.ts'
import MissionIconUlam from '../../ui/mission-icon-ulam.tsx'
import { useMissionReportExport } from '../../../../common/hooks/use-mission-report-export.tsx'
import { ExportMode, ExportReportType } from '../../../../common/types/mission-export-types.ts'
import ExportFileButton from '../../../../common/components/elements/export-file-button.tsx'
import { isCompleteForStats } from '../../../../common/hooks/use-mission-completeness-for-stats.tsx'

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
  const missionName = formatMissionName(mission?.startDateTimeUtc)
  const missionDate = formatDateForFrenchHumans(mission?.startDateTimeUtc)
  const missionCrew = useCrewForMissionList(mission?.crew)

  const { exportMissionReport, loading: exportIsLoading } = useMissionReportExport()
  const exportAEM = async (mission?: Mission, event?: React.MouseEvent) => {
    // Stop event propagation to prevent row from collapsing
    event?.preventDefault()
    event?.stopPropagation()

    mission &&
      (await exportMissionReport({
        missionIds: [mission].map((m: Mission) => m.id),
        exportMode: ExportMode.INDIVIDUAL_MISSION,
        reportType: ExportReportType.AEM
      }))
  }

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
      redirect(`${ULAM_V2_HOME_PATH}/${mission?.id}`)
    }

    setOpenIndex(isOpen ? null : index) // Toggle open state
  }

  return (
    <ListItemWithHover ref={listItemRef} onClick={onClickRow} data-testid="mission-list-item-with-hover">
      <FlexboxGrid align="middle" style={{ height: '100%', padding: '0.5rem 2rem', marginBottom: '4px' }}>
        <FlexboxGrid.Item colspan={1} style={{ paddingTop: '8px' }} data-testid={'mission-list-item-icon'}>
          <MissionIconUlam missionSource={mission?.missionSource} />
        </FlexboxGrid.Item>

        <FlexboxGrid.Item colspan={3} data-testid={'mission-list-item-mission_number'}>
          <Text weight={'bold'} as={'h2'}>
            {missionName}
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
              <IconButton //
                Icon={Icon.Chevron}
                accent={Accent.TERTIARY}
                data-testid={'expand-collapse-btn'}
                style={{ transform: isOpen ? 'rotate(180deg)' : '' }}
              />
            </Stack.Item>
            <Stack.Item>
              <IconButton //
                Icon={Icon.Edit}
                accent={Accent.TERTIARY}
                data-testid={'go-to-mission-btn'}
                onMouseDown={(e: React.MouseEvent) => {
                  e.preventDefault() // Prevents button focus behavior
                  e.stopPropagation() // Prevents parent `onClick` from firing
                }}
                onClick={e => {
                  redirect(`${ULAM_V2_HOME_PATH}/${mission?.id}`)
                }}
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
                  {' '}
                  {mission?.observationsByUnit}
                </p>
              </FlexboxGrid.Item>
              <FlexboxGrid.Item>
                <ExportFileButton
                  isLoading={exportIsLoading}
                  accent={Accent.SECONDARY}
                  disabled={!isCompleteForStats(mission?.completenessForStats)}
                  title={
                    isCompleteForStats(mission?.completenessForStats)
                      ? 'Exporter les tableaux de mission'
                      : "Export indisponible tant que la mission n'est pas terminée et complétée."
                  }
                  onMouseDown={(e: React.MouseEvent) => {
                    e.preventDefault() // Prevents button focus behavior
                    e.stopPropagation() // Prevents parent `onClick` from firing
                  }}
                  onClick={async (e: React.MouseEvent) => {
                    await exportAEM(mission, e)
                  }}
                >
                  Exporter les tableaux de mission
                </ExportFileButton>
              </FlexboxGrid.Item>
            </FlexboxGrid>
          </FlexboxGrid.Item>
        )}
      </FlexboxGrid>
    </ListItemWithHover>
  )
}

export default MissionListItemUlam
