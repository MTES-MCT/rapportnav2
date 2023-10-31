import React from 'react'
import { THEME, Icon, Tag, Accent } from '@mtes-mct/monitor-ui'
import { ActionTypeEnum, EnvAction, EnvActionControl, MissionSourceEnum } from '../../env-mission-types'
import { FlexboxGrid, Stack } from 'rsuite'
import { Action, NavAction } from '../../mission-types'
import { FishAction } from '../../fish-mission-types'
import { StatusColorTag } from '../status/status-selection-dropdown'
import { mapStatusToText } from '../status/utils'
import { controlMethodToHumanString, vesselTypeToHumanString } from '../controls/utils'
import ControlsToCompleteTag from '../controls/controls-to-complete-tag'
import Title from '../../../ui/title'

interface MissionTimelineItemProps {
  action: Action
  onClick: (action: Action) => void
}

const Wrapper: React.FC<{ action: Action; onClick: any; children: any }> = ({ action, onClick, children }) => {
  return (
    <div onClick={onClick}>
      <FlexboxGrid style={{ width: '100%', padding: '0.5rem' }} justify="start">
        {children}
      </FlexboxGrid>
    </div>
  )
}

const ActionEnvControl: React.FC<{ action: EnvAction | EnvActionControl; onClick: any }> = ({ action, onClick }) => {
  return (
    <Wrapper action={action as EnvActionControl} onClick={onClick}>
      <FlexboxGrid.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing="1rem">
          <Stack.Item alignSelf="flex-start" style={{ paddingTop: '0.2rem' }}>
            <Icon.Control color={THEME.color.charcoal} size={20} />
          </Stack.Item>
          <Stack.Item alignSelf="flex-start" style={{ width: '100%' }}>
            <Stack direction="column" alignItems="flex-start" style={{ width: '100%' }}>
              <Stack.Item>
                Contrôles <b>{action && 'themes' in action && action.themes[0].theme ? action.themes[0].theme : ''}</b>
              </Stack.Item>
              <Stack.Item>
                <b>
                  {action && 'actionNumberOfControls' in action && action.actionNumberOfControls
                    ? `${action.actionNumberOfControls} ${action.actionNumberOfControls > 1 ? 'contrôles' : 'contrôle'}`
                    : 'Nombre de contrôles inconnu'}{' '}
                  &nbsp;
                </b>
                {action &&
                'actionNumberOfControls' in action &&
                action.actionNumberOfControls &&
                action.actionNumberOfControls > 1
                  ? 'réalisés'
                  : 'réalisé'}{' '}
                sur des cibles de type&nbsp;
                <b>
                  {action && 'actionTargetType' in action && action.actionTargetType
                    ? action.actionTargetType?.toLowerCase()
                    : 'inconnu'}
                </b>
              </Stack.Item>
              <Stack.Item>infrations...</Stack.Item>

              <Stack.Item alignSelf="flex-start" style={{ width: '100%' }}>
                <Stack direction="row" spacing="1rem" style={{ width: '100%' }}>
                  <Stack.Item style={{ width: '100%' }}>
                    {(action as any as EnvActionControl)?.controlsToComplete !== undefined &&
                    (action as any as EnvActionControl)?.controlsToComplete?.length > 0 ? (
                      <ControlsToCompleteTag
                        amountOfControlsToComplete={(action as any as EnvActionControl)?.controlsToComplete?.length}
                      />
                    ) : (
                      <div />
                    )}
                  </Stack.Item>
                  <Stack.Item alignSelf="flex-end" style={{ width: '100%' }}>
                    <Stack direction="column" alignItems="flex-end">
                      <Stack.Item alignSelf="flex-end">
                        <Title as="h4" color={THEME.color.slateGray}>
                          ajouté par CACEM
                        </Title>
                      </Stack.Item>
                    </Stack>
                  </Stack.Item>
                </Stack>
              </Stack.Item>
            </Stack>
          </Stack.Item>
        </Stack>
      </FlexboxGrid.Item>
    </Wrapper>
  )
}
const ActionFishControl: React.FC<{ action: FishAction; onClick: any }> = ({ action, onClick }) => {
  return (
    <Wrapper action={action as FishAction} onClick={onClick}>
      <FlexboxGrid.Item style={{ width: '100%' }}>
        <Stack direction="column" alignItems="flex-start" spacing="1rem">
          <Stack.Item alignSelf="flex-start" style={{ paddingTop: '0.2rem' }}>
            <Stack direction="row" spacing="1rem">
              <Stack.Item alignSelf="flex-start" style={{ paddingTop: '0.2rem' }}>
                <Icon.Control color={THEME.color.charcoal} size={20} />
              </Stack.Item>
              <Stack.Item alignSelf="flex-start">
                <Stack direction="column" alignItems="flex-start">
                  <Stack.Item>Contrôles CNSP</Stack.Item>
                </Stack>
              </Stack.Item>
            </Stack>
          </Stack.Item>

          <Stack.Item alignSelf="flex-start" style={{ width: '100%' }}>
            <Stack direction="row" spacing="1rem" style={{ width: '100%' }}>
              <Stack.Item style={{ width: '100%' }}>
                {(action as any as FishAction)?.controlsToComplete !== undefined &&
                  (action as any as FishAction)?.controlsToComplete?.length > 0 && (
                    <ControlsToCompleteTag
                      amountOfControlsToComplete={(action as any as FishAction)?.controlsToComplete?.length}
                    />
                  )}
              </Stack.Item>
              <Stack.Item alignSelf="flex-end" style={{ width: '100%' }}>
                <Stack direction="column" alignItems="flex-end">
                  <Stack.Item alignSelf="flex-end">
                    <Title as="h4" color={THEME.color.slateGray}>
                      ajouté par CNSP
                    </Title>
                  </Stack.Item>
                </Stack>
              </Stack.Item>
            </Stack>
          </Stack.Item>
        </Stack>
      </FlexboxGrid.Item>
    </Wrapper>
  )
}

const ActionNavControl: React.FC<{ action: NavAction; onClick: any }> = ({ action, onClick }) => {
  return (
    <Wrapper action={action as FishAction} onClick={onClick}>
      <FlexboxGrid.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing="1rem">
          <Stack.Item alignSelf="flex-start" style={{ paddingTop: '0.2rem' }}>
            <Icon.Control color={THEME.color.charcoal} size={20} />
          </Stack.Item>
          <Stack.Item alignSelf="flex-start">
            <Stack direction="column" alignItems="flex-start">
              <Stack.Item>
                Contrôles <b>{controlMethodToHumanString(action.controlMethod)}</b> -{' '}
                <b>{vesselTypeToHumanString(action.vesselType)}</b>
              </Stack.Item>
            </Stack>
          </Stack.Item>
        </Stack>
      </FlexboxGrid.Item>
    </Wrapper>
  )
}

const ActionSurveillance: React.FC = () => {
  // Implementation for ActionSurveillance
  return null
}

const ActionNote: React.FC = () => {
  // Implementation for ActionNote
  return null
}

const ActionOther: React.FC = () => {
  // Implementation for ActionOther
  return null
}

const ActionStatus: React.FC<{ action: NavAction; onClick: any }> = ({ action, onClick }) => {
  return (
    <Wrapper action={action as any} onClick={onClick}>
      <Stack alignItems="center" spacing="0.5rem">
        <Stack.Item>
          <StatusColorTag status={action.status} />
        </Stack.Item>
        <Stack.Item>
          <b>{`${mapStatusToText(action.status)} - ${action.isStart ? 'début' : 'fin'}`}</b>
        </Stack.Item>
        <Stack.Item>
          <Icon.EditUnbordered size={20} />
        </Stack.Item>
      </Stack>
    </Wrapper>
  )
}

const ActionContact: React.FC = () => {
  // Implementation for ActionContact
  return null
}

const getActionComponent = (action: Action) => {
  if (action.source === MissionSourceEnum.MONITORENV) {
    if (action.type === ActionTypeEnum.CONTROL) {
      return ActionEnvControl
    }
  } else if (action.source === MissionSourceEnum.MONITORFISH) {
    if (action.type === ActionTypeEnum.CONTROL) {
      return ActionFishControl
    }
  } else if (action.source === MissionSourceEnum.RAPPORTNAV) {
    switch (action.type) {
      case ActionTypeEnum.CONTROL:
        return ActionNavControl
      case ActionTypeEnum.STATUS:
        return ActionStatus
      default:
        return null
    }
  }
  return null
}

const MissionTimelineItem: React.FC<MissionTimelineItemProps> = ({
  action,
  onClick
  // componentMap = ActionComponentMap
}) => {
  const Component = getActionComponent(action)
  // const Component = componentMap[action.actionType]

  if (!Component) {
    return null
  }

  return <Component action={action.data as any} onClick={onClick} />
}

export default MissionTimelineItem
