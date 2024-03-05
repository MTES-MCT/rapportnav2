import React from 'react'
import styled from 'styled-components'
import { FlexboxGrid, Stack } from 'rsuite'
import { Accent, Button, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import Text from '../../ui/text'
import { MissionSourceEnum } from "../../types/env-mission-types.ts";
import MissionOpenByTag from "../missions/mission-open-by-tag.tsx";

const StyledHeader = styled.div`
  height: 60px;
  background: var(--charcoal-3b4559-) 0% 0% no-repeat padding-box;
  background: #3b4559 0% 0% no-repeat padding-box;
  opacity: 1;
  padding: 0 2rem;
`

interface MissionPageHeaderProps {
  missionName: string
  missionSource?: MissionSourceEnum
  onClickClose: () => void
  onClickExport: () => void
}

const MissionPageHeader: React.FC<MissionPageHeaderProps> = ({
                                                               missionName,
                                                               missionSource,
                                                               onClickClose,
                                                               onClickExport
                                                             }) => {
  return (
    <StyledHeader>
      <FlexboxGrid justify="space-between" align="middle" style={{height: '100%'}}>
        <FlexboxGrid.Item>
          <Stack direction="row" spacing={'1rem'}>
            <Stack.Item>
              <Text as="h1" weight="bold" color={THEME.color.gainsboro}>
                {missionName}
              </Text>
            </Stack.Item>
            {
              !!missionSource && (
                <Stack.Item>
                  {/*<Text as="h1" weight="bold" color={THEME.color.gainsboro}>*/}
                  <MissionOpenByTag missionSource={missionSource}/>
                  {/*</Text>*/}
                </Stack.Item>
              )
            }
          </Stack>
        </FlexboxGrid.Item>
        <FlexboxGrid.Item colspan={6}>
          <FlexboxGrid justify="end" align="middle" style={{height: '100%'}}>
            <Stack direction={"row"} alignItems={"center"} spacing={"2rem"}>
              <Stack.Item>
                {/*TODO uncomment following component when the time will be right*/}
                {/*<Button*/}
                {/*  Icon={Icon.Download}*/}
                {/*  accent={Accent.PRIMARY}*/}
                {/*  size={Size.NORMAL}*/}
                {/*  // color={THEME.color.gainsboro}*/}
                {/*  onClick={onClickExport}*/}
                {/*  role={'dl-mission-export'}*/}
                {/*>*/}
                {/*  Exporter le rapport de la mission*/}
                {/*</Button>*/}
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
  )
}

export default MissionPageHeader
