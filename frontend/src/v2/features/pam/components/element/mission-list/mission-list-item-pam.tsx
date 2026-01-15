import Text from '@common/components/ui/text.tsx'
import { Checkbox, Icon, THEME } from '@mtes-mct/monitor-ui'
import { useGlobalRoutes } from '@router/use-global-routes.tsx'
import { FC } from 'react'
import { useNavigate } from 'react-router-dom'
import { FlexboxGrid, Stack } from 'rsuite'
import styled from 'styled-components'
import MissionCompletenessForStatsTag from '../../../../common/components/elements/mission-completeness-for-stats-tag.tsx'
import MissionSourceTag from '../../../../common/components/ui/mission-source-tag.tsx'
import MissionStatusTag from '../../../../common/components/ui/mission-status-tag.tsx'
import { MissionListItem } from '../../../../common/types/mission-types.ts'
import { OwnerType } from '../../../../common/types/owner-type.ts'
import MissionInterServicesTag from '../../ui/mission-interservices-tag.tsx'

interface MissionListItemProps {
  mission: MissionListItem
  isSelected: boolean
  onToggle: (index?: number, isChecked?: boolean) => void
}

const ListItem = styled.div`
  height: inherit;
  background-color: ${THEME.color.cultured};
`

const MissionListItemPam: FC<MissionListItemProps> = ({ mission, isSelected, onToggle }) => {
  const navigate = useNavigate()
  const { getUrl } = useGlobalRoutes()
  const goToMission = (mission: MissionListItem) => {
    const id = mission?.id ?? mission.idUUID
    if (id) navigate(`${getUrl(OwnerType.MISSION)}/${mission?.id}`)
  }

  return (
    <ListItem data-testid={'mission-list-item'}>
      <FlexboxGrid align="middle" style={{ height: '64px', padding: '0.5rem 2rem', marginBottom: '4px' }}>
        <FlexboxGrid.Item colspan={1} data-testid={'mission-list-item-icon'}>
          <Checkbox
            label={''}
            checked={isSelected}
            style={{ marginBottom: '16px' }}
            onChange={(isChecked?: boolean) => onToggle(mission?.id, isChecked)}
            title={'Sélectionner cette mission'}
          />
        </FlexboxGrid.Item>

        <FlexboxGrid.Item colspan={4} data-testid={'mission-list-item-mission_number'}>
          <Text weight={'bold'} as={'h2'}>
            {mission.missionNamePam}
          </Text>
        </FlexboxGrid.Item>

        <FlexboxGrid.Item colspan={4} data-testid={'mission-list-item-open_by'}>
          <Stack direction={'column'} alignItems={'flex-start'}>
            <Stack.Item>
              <MissionSourceTag missionSource={mission.missionSource} isFake={mission.openBy === 'fake'} />
            </Stack.Item>

            {(mission?.controlUnits || []).length > 1 && (
              <Stack.Item>
                <MissionInterServicesTag />
              </Stack.Item>
            )}
          </Stack>
        </FlexboxGrid.Item>

        <FlexboxGrid.Item colspan={2} data-testid={'mission-list-item-start_date'}>
          <Text as={'h3'}>{mission.startDateTimeUtcText}</Text>
        </FlexboxGrid.Item>

        <FlexboxGrid.Item colspan={2} data-testid={'mission-list-item-end_date'}>
          <Text as={'h3'}>{mission.endDateTimeUtcText}</Text>
        </FlexboxGrid.Item>

        <FlexboxGrid.Item colspan={2} data-testid={'mission-list-item-crew'}>
          <Text
            as={'h3'}
            weight={'bold'}
            style={{
              textAlign: 'center',
              border: `1px solid ${THEME.color.lightGray}`,
              borderTop: 0,
              borderBottom: 0
            }}
          >
            {mission.crewNumber}
          </Text>
        </FlexboxGrid.Item>

        <FlexboxGrid.Item colspan={1}>
          <Text as={'h3'} style={{ textAlign: 'center' }}></Text>
        </FlexboxGrid.Item>

        <FlexboxGrid.Item colspan={3} data-testid={'mission-list-item-mission_status'}>
          <MissionStatusTag status={mission?.status} />
        </FlexboxGrid.Item>

        <FlexboxGrid.Item colspan={3} data-testid={'mission-list-item-mission_completeness'}>
          <MissionCompletenessForStatsTag
            completenessForStats={mission?.completenessForStats}
            missionStatus={mission?.status}
          />
        </FlexboxGrid.Item>

        <FlexboxGrid.Item colspan={1}>
          <Text as={'h3'} style={{ textAlign: 'center' }}></Text>
        </FlexboxGrid.Item>

        <FlexboxGrid.Item colspan={1} data-testid={'mission-list-item-icon-edit'}>
          <Icon.Edit size={20} style={{ color: THEME.color.charcoal }} onClick={e => goToMission(mission)} />
        </FlexboxGrid.Item>
      </FlexboxGrid>
    </ListItem>
  )
}

export default MissionListItemPam
