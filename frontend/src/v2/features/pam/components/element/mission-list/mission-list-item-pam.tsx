import Text from '@common/components/ui/text.tsx'
import MissionOpenByTag from '@features/pam/mission/components/elements/mission-open-by-tag.tsx'
import { Checkbox, Icon, THEME } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Link } from 'react-router-dom'
import { FlexboxGrid, Stack } from 'rsuite'
import styled from 'styled-components'
import MissionCompletenessForStatsTag from '../../../../common/components/elements/mission-completeness-for-stats-tag.tsx'
import MissionStatusTag from '../../../../common/components/ui/mission-status-tag.tsx'
import { MissionListItem } from '../../../../common/types/mission-types.ts'
import { PAM_V2_HOME_PATH } from '@router/routes.tsx'
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
              <MissionOpenByTag missionSource={mission.missionSource} isFake={mission.openBy === 'fake'} />
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
          <Link
            to={`${PAM_V2_HOME_PATH}/${mission?.id}`}
            style={{
              textDecoration: 'none',
              textAlign: 'right',
              display: 'block'
            }}
          >
            <Icon.Edit size={20} style={{ color: THEME.color.charcoal }} />
          </Link>
        </FlexboxGrid.Item>
      </FlexboxGrid>
    </ListItem>
  )
}

export default MissionListItemPam
