import { MissionStatusEnum } from '@common/types/mission-types.ts'

import Text from '@common/components/ui/text.tsx'
import { Accent, Icon, IconButton, Size, TagGroup, THEME } from '@mtes-mct/monitor-ui'
import React, { useEffect } from 'react'
import { FlexboxGrid, Stack } from 'rsuite'
import styled from 'styled-components'
import { setMissionStatus } from '../../../../store/slices/mission-reducer.ts'
import { useDate } from '../../hooks/use-date.tsx'
import { Mission2 } from '../../types/mission-types.ts'
import MissionCompletenessForStatsTag from '../elements/mission-completeness-for-stats-tag.tsx'
import MissionPageHeaderBanner from './mission-page-header-banner.tsx'
import MissionSourceTag from './mission-source-tag.tsx'
import MissionStatusTag from './mission-status-tag.tsx'
import MissionPageHeaderTimeConversion from './mission-page-header-time-conversion.tsx'

const StyledHeader = styled.div`
  height: 60px;
  background: #3b4559 0 0 no-repeat padding-box;
  opacity: 1;
  padding: 0 2rem;
`

interface MissionPageHeaderProps {
  mission?: Mission2
  onClickClose: () => void
}

const MissionPageHeader: React.FC<MissionPageHeaderProps> = ({ mission, onClickClose }) => {
  const { formatMissionName } = useDate()
  useEffect(() => {
    setMissionStatus(mission?.status)
  }, [mission])

  return (
    <>
      <StyledHeader>
        <FlexboxGrid justify="space-between" align="middle" style={{ height: '100%' }}>
          <FlexboxGrid.Item>
            <Stack direction="row" spacing={'1rem'}>
              <Stack.Item>
                <Text as="h1" weight="bold" color={THEME.color.gainsboro}>
                  {formatMissionName(mission?.envData?.startDateTimeUtc)}
                </Text>
              </Stack.Item>

              <Stack.Item>
                <TagGroup>
                  <MissionSourceTag missionSource={mission?.envData?.missionSource} />
                  <MissionStatusTag status={mission?.status} />
                  <MissionCompletenessForStatsTag
                    missionStatus={mission?.status}
                    completenessForStats={mission?.completenessForStats}
                  />
                </TagGroup>
              </Stack.Item>
            </Stack>
          </FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={6}>
            <FlexboxGrid justify="end" align="middle" style={{ height: '100%' }}>
              <Stack direction={'row'} alignItems={'center'} spacing={'2rem'}>
                <Stack.Item>
                  <MissionPageHeaderTimeConversion />
                </Stack.Item>

                <Stack.Item>
                  <IconButton
                    Icon={Icon.Close}
                    accent={Accent.TERTIARY}
                    size={Size.NORMAL}
                    color={THEME.color.gainsboro}
                    onClick={onClickClose}
                    role={'quit-mission-cross'}
                    style={{ marginTop: '4px' }}
                  />
                </Stack.Item>
              </Stack>
            </FlexboxGrid>
          </FlexboxGrid.Item>
        </FlexboxGrid>
      </StyledHeader>
      {mission?.status === MissionStatusEnum.ENDED && (
        <MissionPageHeaderBanner completenessForStats={mission?.completenessForStats} />
      )}
    </>
  )
}

export default MissionPageHeader
