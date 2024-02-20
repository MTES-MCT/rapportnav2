import React from 'react'
import { useParams } from "react-router-dom";
import { MissionTimelineItemProps, TimelineItemWrapper } from "./timeline-item.tsx";
import { FlexboxGrid, Stack } from "rsuite";
import { Icon, THEME } from "@mtes-mct/monitor-ui";
import Text from "../../../../ui/text.tsx";
import { ActionFreeNote } from "../../../../types/action-types.ts";
import { capitalize } from "lodash";


const ActionNote: React.FC<MissionTimelineItemProps> = ({action, onClick}) => {
    const {actionId} = useParams()
    const actionData = action.data as unknown as ActionFreeNote

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
                    <Stack.Item alignSelf="flex-start" style={{width: '100%'}}>
                        <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                            {capitalize(actionData?.observations) || 'Note libre'}
                        </Text>
                    </Stack.Item>
                </Stack>
            </FlexboxGrid.Item>
        </TimelineItemWrapper>
    )
}

export default ActionNote
