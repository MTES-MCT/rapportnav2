import React from 'react'
import { Accent, Icon, Tag, THEME } from '@mtes-mct/monitor-ui'
import { actionTargetTypeLabels, EnvActionControl } from '../../../../types/env-mission-types'
import { FlexboxGrid, Stack } from 'rsuite'
import { Action } from '../../../../types/action-types'
import ControlsToCompleteTag from '../../controls/controls-to-complete-tag'
import Text from '../../../../ui/text'
import { useParams } from 'react-router-dom'
import { TimelineItemWrapper } from "./timeline-item.tsx";


const ActionEnvControl: React.FC<{ action: Action; onClick: any }> = ({action, onClick}) => {
  const {actionId} = useParams()
  const actionData = action.data as unknown as EnvActionControl
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
            <Icon.ControlUnit color={THEME.color.charcoal} size={20}/>
          </Stack.Item>
          <Stack.Item alignSelf="flex-start" style={{width: '100%'}}>
            <Stack direction="column" spacing="0.5rem" alignItems="flex-start" style={{width: '100%'}}>
              <Stack.Item>
                <Stack direction="row" spacing="0.25rem">
                  <Stack.Item>
                    <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                      Contrôle
                    </Text>
                  </Stack.Item>
                  <Stack.Item>
                    <Text as="h3" weight="bold" color={THEME.color.gunMetal} data-testid={"theme"}>
                      {actionData && 'themes' in actionData && actionData?.themes[0]?.theme ? actionData?.themes[0]?.theme : ''}
                    </Text>
                  </Stack.Item>
                </Stack>
              </Stack.Item>
              <Stack.Item>
                <Text as="h3" weight="normal" color={THEME.color.slateGray}>
                  <b>
                    {actionData && 'actionNumberOfControls' in actionData && actionData.actionNumberOfControls
                      ? `${actionData.actionNumberOfControls} ${
                        actionData.actionNumberOfControls > 1 ? 'contrôles' : 'contrôle'
                      }`
                      : 'Nombre de contrôles inconnu'}
                  </b>
                  &nbsp;
                  {actionData &&
                  'actionNumberOfControls' in actionData &&
                  actionData.actionNumberOfControls &&
                  actionData.actionNumberOfControls > 1
                    ? 'réalisés'
                    : 'réalisé'}{' '}
                  sur des cibles de type&nbsp;
                  <b>
                    {actionData && 'actionTargetType' in actionData && actionData.actionTargetType
                      ? actionTargetTypeLabels[actionData.actionTargetType]?.libelle?.toLowerCase()
                      : 'inconnu'}
                  </b>
                </Text>
              </Stack.Item>

              <Stack.Item alignSelf="flex-start" style={{width: '100%'}}>
                <Stack direction="row" spacing="1rem" style={{width: '100%'}}>
                  <Stack.Item style={{width: '70%'}}>
                    {actionData?.controlsToComplete !== undefined &&
                    actionData?.controlsToComplete?.length > 0 ? (
                      <ControlsToCompleteTag
                        amountOfControlsToComplete={actionData?.controlsToComplete?.length}
                      />
                    ) : (
                      <>
                        {
                          action.summaryTags?.map((tag: string) => (
                            <Tag key={tag}
                                 accent={Accent.PRIMARY}
                                 style={{marginRight: '0.5rem'}}>{tag}</Tag>
                          ))
                        }
                      </>
                    )}
                  </Stack.Item>
                  <Stack.Item alignSelf="flex-end" style={{width: '30%'}}>
                    <Stack direction="column" alignItems="flex-end">
                      <Stack.Item alignSelf="flex-end">
                        <Text as="h4" color={THEME.color.slateGray} fontStyle={"italic"}>
                          ajouté par CACEM
                        </Text>
                      </Stack.Item>
                    </Stack>
                  </Stack.Item>
                </Stack>
              </Stack.Item>
            </Stack>
          </Stack.Item>
        </Stack>
      </FlexboxGrid.Item>
    </TimelineItemWrapper>
  )
}

export default ActionEnvControl
