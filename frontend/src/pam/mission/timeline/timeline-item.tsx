import React from 'react'
import { THEME, Icon, Tag, Accent } from '@mtes-mct/monitor-ui'
import {
  ActionTypeEnum,
  EnvAction,
  EnvActionControl,
  MissionSourceEnum,
  actionTargetTypeLabels
} from '../../../types/env-mission-types'
import { FlexboxGrid, Stack } from 'rsuite'
import { Action, ActionStatus as NavActionStatus, statusReasonToHumanString } from '../../../types/action-types'
import { FishAction } from '../../../types/fish-mission-types'
import { StatusColorTag } from '../status/status-selection-dropdown'
import { mapStatusToText } from '../status/utils'
import { controlMethodToHumanString, vesselTypeToHumanString } from '../controls/utils'
import ControlsToCompleteTag from '../controls/controls-to-complete-tag'
import Text from '../../../ui/text'
import InfractionTag from '../infractions/infraction-tag'
import { useParams } from 'react-router-dom'

interface MissionTimelineItemProps {
  action: Action
  onClick: (action: Action) => void
}

const Wrapper: React.FC<{ action: Action; onClick: any; children: any; borderWhenSelected?: boolean }> = ({
  action,
  onClick,
  children,
  borderWhenSelected = null
}) => {
  return (
    <div onClick={onClick}>
      <FlexboxGrid
        style={{
          width: '100%',
          border: !!borderWhenSelected ? `3px solid ${THEME.color.blueGray}` : 'none',
          cursor: 'pointer'
        }}
        justify="start"
      >
        {children}
      </FlexboxGrid>
    </div>
  )
}

const ActionEnvControl: React.FC<{ action: EnvAction | EnvActionControl; onClick: any }> = ({ action, onClick }) => {
  const { actionId } = useParams()
  return (
    <Wrapper action={action as EnvActionControl} onClick={onClick} borderWhenSelected={action.id === actionId}>
      <FlexboxGrid.Item
        style={{
          width: '100%',
          padding: '1rem'
        }}
      >
        <Stack direction="row" spacing="0.5rem">
          <Stack.Item alignSelf="flex-start">
            <Icon.ControlUnit color={THEME.color.charcoal} size={20} />
          </Stack.Item>
          <Stack.Item alignSelf="flex-start" style={{ width: '100%' }}>
            <Stack direction="column" spacing="0.5rem" alignItems="flex-start" style={{ width: '100%' }}>
              <Stack.Item>
                <Stack direction="row" spacing="0.25rem">
                  <Stack.Item>
                    <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                      Contrôle
                    </Text>
                  </Stack.Item>
                  <Stack.Item>
                    <Text as="h3" weight="bold" color={THEME.color.gunMetal}>
                      {action && 'themes' in action && action.themes[0].theme ? action.themes[0].theme : ''}
                    </Text>
                  </Stack.Item>
                </Stack>
              </Stack.Item>
              <Stack.Item>
                <Text as="h3" weight="normal" color={THEME.color.slateGray}>
                  <b>
                    {action && 'actionNumberOfControls' in action && action.actionNumberOfControls
                      ? `${action.actionNumberOfControls} ${
                          action.actionNumberOfControls > 1 ? 'contrôles' : 'contrôle'
                        }`
                      : 'Nombre de contrôles inconnu'}
                  </b>
                  &nbsp;
                  {action &&
                  'actionNumberOfControls' in action &&
                  action.actionNumberOfControls &&
                  action.actionNumberOfControls > 1
                    ? 'réalisés'
                    : 'réalisé'}{' '}
                  sur des cibles de type&nbsp;
                  <b>
                    {action && 'actionTargetType' in action && action.actionTargetType
                      ? actionTargetTypeLabels[action.actionTargetType]?.libelle?.toLowerCase()
                      : 'inconnu'}
                  </b>
                </Text>
              </Stack.Item>

              <Stack.Item alignSelf="flex-start" style={{ width: '100%' }}>
                <Stack direction="row" spacing="1rem" style={{ width: '100%' }}>
                  <Stack.Item style={{ width: '70%' }}>
                    {(action as any as EnvActionControl)?.controlsToComplete !== undefined &&
                    (action as any as EnvActionControl)?.controlsToComplete?.length > 0 ? (
                      <ControlsToCompleteTag
                        amountOfControlsToComplete={(action as any as EnvActionControl)?.controlsToComplete?.length}
                      />
                    ) : (
                      <>
                        <InfractionTag text="5 PV" />
                        &nbsp;
                        <InfractionTag text="2 NATINF" />
                      </>
                    )}
                  </Stack.Item>
                  <Stack.Item alignSelf="flex-end" style={{ width: '30%' }}>
                    <Stack direction="column" alignItems="flex-end">
                      <Stack.Item alignSelf="flex-end">
                        <Text as="h4" color={THEME.color.slateGray} style="italic">
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
    </Wrapper>
  )
}

const ActionFishControl: React.FC<{ action: FishAction; onClick: any }> = ({ action, onClick }) => {
  const { actionId } = useParams()
  return (
    <Wrapper action={action as FishAction} onClick={onClick} borderWhenSelected={action.id === actionId}>
      <FlexboxGrid.Item style={{ width: '100%', padding: '1rem' }}>
        <Stack direction="row" spacing="0.5rem">
          <Stack.Item alignSelf="flex-start">
            <Icon.ControlUnit color={THEME.color.charcoal} size={20} />
          </Stack.Item>
          <Stack.Item alignSelf="flex-start" style={{ width: '100%' }}>
            <Stack direction="column" spacing="0.5rem" alignItems="flex-start" style={{ width: '100%' }}>
              <Stack.Item>
                <Stack direction="row" spacing="0.25rem">
                  <Stack.Item>
                    <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                      Contrôle
                    </Text>
                  </Stack.Item>
                  <Stack.Item>
                    <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                      en mer - LE ZORBA
                    </Text>
                  </Stack.Item>
                </Stack>
              </Stack.Item>

              <Stack.Item alignSelf="flex-start" style={{ width: '100%' }}>
                <Stack direction="row" spacing="1rem" style={{ width: '100%' }}>
                  <Stack.Item style={{ width: '70%' }}>
                    {(action as any as FishAction)?.controlsToComplete !== undefined &&
                    (action as any as FishAction)?.controlsToComplete?.length > 0 ? (
                      <ControlsToCompleteTag
                        amountOfControlsToComplete={(action as any as FishAction)?.controlsToComplete?.length}
                      />
                    ) : (
                      <>
                        <InfractionTag text="3 PV" />
                        &nbsp;
                        <InfractionTag text="2 NATINF" />
                      </>
                    )}
                  </Stack.Item>
                  <Stack.Item alignSelf="flex-end" style={{ width: '30%' }}>
                    <Stack direction="column" alignItems="flex-end">
                      <Stack.Item alignSelf="flex-end">
                        <Text as="h4" color={THEME.color.slateGray} style="italic">
                          ajouté par CNSP
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
    </Wrapper>
  )
}

const ActionNavControl: React.FC<{ action: Action; onClick: any }> = ({ action, onClick }) => {
  const { actionId } = useParams()
  return (
    <Wrapper action={action as FishAction} onClick={onClick} borderWhenSelected={action.id === actionId}>
      <FlexboxGrid.Item style={{ width: '100%', padding: '1rem' }}>
        <Stack direction="row" spacing="0.5rem">
          <Stack.Item alignSelf="flex-start" style={{ paddingTop: '0.2rem' }}>
            <Icon.ControlUnit color={THEME.color.charcoal} size={20} />
          </Stack.Item>
          <Stack.Item>
            <Stack direction="column" alignItems="flex-start">
              <Stack.Item>
                <Stack direction="row" spacing="0.25rem">
                  <Stack.Item>
                    <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                      Contrôles
                    </Text>
                  </Stack.Item>
                  <Stack.Item>
                    <Text as="h3" weight="bold" color={THEME.color.gunMetal}>
                      {`${controlMethodToHumanString(action.controlMethod)} - ${vesselTypeToHumanString(
                        action.vesselType
                      )}`}
                    </Text>
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

const ActionStatus: React.FC<{ action: NavActionStatus; onClick: any }> = ({ action, onClick }) => {
  const { actionId } = useParams()
  const isSelected = action.id === actionId
  return (
    <Wrapper action={action as any} onClick={onClick} borderWhenSelected={false}>
      <Stack alignItems="center" spacing="0.5rem">
        <Stack.Item>
          <StatusColorTag status={action.status} />
        </Stack.Item>
        <Stack.Item>
          <Text
            as="h3"
            weight="normal"
            color={isSelected ? THEME.color.charcoal : THEME.color.slateGray}
            decoration={isSelected ? 'underline' : 'normal'}
          >
            <b>{`${mapStatusToText(action.status)} - ${action.isStart ? 'début' : 'fin'} ${
              !!action.reason ? ' - ' + statusReasonToHumanString(action.reason) : ''
            }`}</b>
            {!!action.observations ? ' - ' + action.observations : ''}
          </Text>
        </Stack.Item>
        <Stack.Item>
          <Icon.EditUnbordered size={20} color={THEME.color.slateGray} />
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
