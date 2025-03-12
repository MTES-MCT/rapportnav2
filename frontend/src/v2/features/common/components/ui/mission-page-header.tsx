import { MissionStatusEnum } from '@common/types/mission-types.ts'

import Text from '@common/components/ui/text.tsx'
import { Accent, Button, Icon, IconButton, Size, TagGroup, THEME } from '@mtes-mct/monitor-ui'
import GearIcon from '@rsuite/icons/Gear'
import React, { useEffect } from 'react'
import { FlexboxGrid, Stack } from 'rsuite'
import styled from 'styled-components'
import { setMissionStatus } from '../../../../store/slices/mission-reducer.ts'
import { useDate } from '../../hooks/use-date.tsx'
import { CompletenessForStatsStatusEnum, Mission2 } from '../../types/mission-types.ts'
import MissionCompletenessForStatsTag from '../elements/mission-completeness-for-stats-tag.tsx'
import MissionPageHeaderBanner from './mission-page-header-banner.tsx'
import MissionSourceTag from './mission-source-tag.tsx'
import MissionStatusTag from './mission-status-tag.tsx'

const StyledHeader = styled.div`
  height: 60px;
  background: #3b4559 0 0 no-repeat padding-box;
  opacity: 1;
  padding: 0 2rem;
`

interface MissionPageHeaderProps {
  mission?: Mission2
  onClickClose: () => void
  onClickExport: () => void
  exportLoading?: boolean
}

const LoadingIcon = () => (
  <GearIcon spin width={16} height={16} color={THEME.color.white} style={{ fontSize: '2em', marginRight: '0.5rem' }} />
)

const MissionPageHeader: React.FC<MissionPageHeaderProps> = ({
  mission,
  onClickClose,
  onClickExport,
  exportLoading
}) => {
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
                  {mission?.completenessForStats?.status === CompletenessForStatsStatusEnum.COMPLETE && (
                    <Button
                      Icon={exportLoading ? LoadingIcon : Icon.Download}
                      accent={Accent.PRIMARY}
                      size={Size.NORMAL}
                      onClick={onClickExport}
                      role={'dl-mission-export'}
                    >
                      Exporter le rapport de la mission
                    </Button>
                  )}
                </Stack.Item>

                <Stack.Item>
                  <IconButton
                    Icon={Icon.Close}
                    accent={Accent.TERTIARY}
                    size={Size.NORMAL}
                    color={THEME.color.gainsboro}
                    onClick={onClickClose}
                    role={'quit-mission-cross'}
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
