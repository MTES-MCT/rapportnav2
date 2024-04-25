import React from 'react'
import { useParams } from "react-router-dom";
import { MissionTimelineItemProps, TimelineItemWrapper } from "./timeline-item.tsx";
import { FlexboxGrid, Stack } from "rsuite";
import { Icon, THEME } from "@mtes-mct/monitor-ui";
import Text from "../../../../ui/text.tsx";
import { capitalize } from "lodash";
import { ActionAntiPollution as NavActionAntiPollution } from '../../../../types/action-types.ts'


const ActionAntiPollution: React.FC<MissionTimelineItemProps> = ({action, onClick}) => {
  const {actionId} = useParams()
  const actionData = action.data as unknown as NavActionAntiPollution

  return (
    <TimelineItemWrapper onClick={onClick} borderWhenSelected={action.id === actionId}>
      <FlexboxGrid.Item
        style={{
          width: '100%',
          padding: '1rem'
        }}
      >
        <Stack direction="row" spacing="0.5rem">
          <Stack.Item alignSelf="flex-start">
            <Icon.Note color={THEME.color.charcoal} size={20}/>
          </Stack.Item>
          <Stack.Item alignSelf="flex-start" style={{maxWidth: 'calc(100% - 3rem)'}}>
            <Text as="h3" weight="medium" color={THEME.color.gunMetal} style={{
              whiteSpace: 'nowrap',
              overflow: 'hidden',
              textOverflow: 'ellipsis',
            }}>
              {capitalize(actionData?.observations) || 'Opération de lutte anti-pollution'}
            </Text>

          </Stack.Item>
        </Stack>
      </FlexboxGrid.Item>
    </TimelineItemWrapper>
  )
}

export default ActionAntiPollution
