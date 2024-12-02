import { Mission } from '@common/types/mission-types.ts'
import React, { useEffect, useRef } from 'react'
import { Checkbox, Icon, THEME } from '@mtes-mct/monitor-ui'
import { Divider, FlexboxGrid } from 'rsuite'
import { Link } from 'react-router-dom'
import styled from 'styled-components'
import { formatDateForFrenchHumans, formatDateForMissionName } from '@common/utils/dates-for-humans.ts'
import MissionCompletenessForStatsTag from '../../../common/components/elements/mission-completeness-for-stats-tag.tsx'
import MissionStatusTag from '../../../common/components/elements/mission-status-tag.tsx'
import MissionOpenByTag from '@features/pam/mission/components/elements/mission-open-by-tag.tsx'
import Text from '@common/components/ui/text.tsx'
import { formatMissionName } from '@features/pam/mission/utils/utils.ts'

interface MissionListItemProps {
  mission?: Mission
  isSelected: boolean
  onToggle: (index?: number, isChecked?: boolean) => void
}

const ListItem = styled.div`
  height: inherit;
  background-color: ${THEME.color.cultured};
`

const MissionListItemPam: React.FC<MissionListItemProps> = ({ mission, isSelected, onToggle }) => {
  const crewNumber = !mission?.generalInfo?.serviceId ? '--' : mission?.generalInfo.serviceId % 2 === 0 ? 'B' : 'A'

  return (
    <ListItem data-testid="mission-list-item-with-hover">
      <FlexboxGrid align="middle" style={{ height: '64px', padding: '0.5rem 2rem', marginBottom: '4px' }}>
        <FlexboxGrid.Item colspan={1} data-testid={'mission-list-item-icon'}>
          <Checkbox
            label={''}
            checked={isSelected}
            style={{ marginBottom: '16px' }}
            onChange={(isChecked?: boolean) => onToggle(mission?.id, isChecked)}
          />
        </FlexboxGrid.Item>

        <FlexboxGrid.Item colspan={4} data-testid={'mission-list-item-mission_number'}>
          <Text weight={'bold'} as={'h2'}>
            {formatMissionName(mission?.startDateTimeUtc)}
          </Text>
        </FlexboxGrid.Item>

        <FlexboxGrid.Item colspan={4} data-testid={'mission-list-item-open_by'}>
          <MissionOpenByTag missionSource={mission?.missionSource} isFake={mission?.openBy === 'fake'} />
        </FlexboxGrid.Item>

        <FlexboxGrid.Item colspan={2} data-testid={'mission-list-item-start_date'}>
          <Text as={'h3'}>{formatDateForFrenchHumans(mission?.startDateTimeUtc)}</Text>
        </FlexboxGrid.Item>

        <FlexboxGrid.Item colspan={2} data-testid={'mission-list-item-end_date'}>
          <Text as={'h3'}>{formatDateForFrenchHumans(mission?.endDateTimeUtc)}</Text>
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
            {crewNumber}
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
            to={`/pam/missions/${mission?.id}`}
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
