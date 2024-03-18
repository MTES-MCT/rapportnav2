import React from 'react'
import styled from 'styled-components'
import { FlexboxGrid, Stack } from 'rsuite'
import { Accent, Button, Icon, IconButton, Size, TagGroup, THEME } from '@mtes-mct/monitor-ui'
import Text from '../../ui/text'
import MissionOpenByTag from './mission-open-by-tag.tsx'
import GearIcon from '@rsuite/icons/Gear'
import { useFlag } from '@unleash/proxy-client-react'
import MissionStatusTag from './mission-status-tag.tsx'
import { Mission, MissionStatusEnum } from '../../types/mission-types.ts'
import { formatMissionName } from './utils.ts'
import MissionReportStatusTag from './mission-report-status-tag.tsx'
import MissionPageHeaderBanner from './page-header-banner.tsx'

const StyledHeader = styled.div`
  height: 60px;
  background: #3b4559 0 0 no-repeat padding-box;
  opacity: 1;
  padding: 0 2rem;
`

interface MissionPageHeaderProps {
  mission?: Mission
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
  const exportRapportEnabled = useFlag('export_rapport')

  return (
    <>
      <StyledHeader>
        <FlexboxGrid justify="space-between" align="middle" style={{ height: '100%' }}>
          <FlexboxGrid.Item>
            <Stack direction="row" spacing={'1rem'}>
              <Stack.Item>
                <Text as="h1" weight="bold" color={THEME.color.gainsboro}>
                  {formatMissionName(mission?.startDateTimeUtc)}
                </Text>
              </Stack.Item>

              <Stack.Item>
                <TagGroup>
                  <MissionOpenByTag missionSource={mission?.missionSource} />
                  <MissionStatusTag status={mission?.status} />
                  <MissionReportStatusTag
                    missionStatus={mission?.status}
                    reportStatus={mission?.reportStatus?.status}
                  />
                </TagGroup>
              </Stack.Item>
            </Stack>
          </FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={6}>
            <FlexboxGrid justify="end" align="middle" style={{ height: '100%' }}>
              <Stack direction={'row'} alignItems={'center'} spacing={'2rem'}>
                <Stack.Item>
                  <Button
                    Icon={exportLoading ? LoadingIcon : Icon.Download}
                    accent={Accent.PRIMARY}
                    size={Size.NORMAL}
                    onClick={onClickExport}
                    role={'dl-mission-export'}
                    style={{
                      display:
                        !!exportRapportEnabled || mission?.status === MissionStatusEnum.ENDED ? 'visible' : 'none'
                    }}
                  >
                    Exporter le rapport de la mission
                  </Button>
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
      {/*{mission?.status === MissionStatusEnum.ENDED && <MissionPageHeaderBanner reportStatus={mission.reportStatus} />}*/}
    </>
  )
}

export default MissionPageHeader
