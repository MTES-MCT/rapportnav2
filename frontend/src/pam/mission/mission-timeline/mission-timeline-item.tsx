import React from 'react'
import { THEME, Icon } from '@mtes-mct/monitor-ui'
import { ActionTypeEnum, EnvAction, EnvActionControl } from '../../mission-types'
import { FlexboxGrid, Stack } from 'rsuite'

interface MissionTimelineItemProps {
  action: EnvAction
  onClick: (action: EnvAction) => void
  componentMap?: Record<ActionTypeEnum, React.FC<{ action: EnvAction; onClick: (action: EnvAction) => void }>>
}

const Action: React.FC<{ action: EnvAction; onClick: any; children: any }> = ({ action, onClick, children }) => {
  return (
    <div onClick={onClick}>
      <FlexboxGrid style={{ width: '100%', padding: '0.5rem' }} justify="start">
        {children}
      </FlexboxGrid>
    </div>
  )
}

const ActionControl: React.FC<{ action: EnvAction | EnvActionControl; onClick: any }> = ({ action, onClick }) => {
  return (
    <Action action={action as EnvAction} onClick={onClick}>
      <FlexboxGrid.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing="1rem">
          <Stack.Item alignSelf="flex-start" style={{ paddingTop: '0.2rem' }}>
            <Icon.Control color={THEME.color.charcoal} size={20} />
          </Stack.Item>
          <Stack.Item alignSelf="flex-start">
            <Stack direction="column" alignItems="flex-start">
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
              <Stack.Item>x types de contrôles à compléter</Stack.Item>
            </Stack>
          </Stack.Item>
        </Stack>
      </FlexboxGrid.Item>
    </Action>
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

const ActionStatus: React.FC = () => {
  // Implementation for ActionStatus
  return null
}

const ActionContact: React.FC = () => {
  // Implementation for ActionContact
  return null
}

const ActionComponentMap: Record<ActionTypeEnum, React.FC<{ action: EnvAction }>> = {
  [ActionTypeEnum.CONTROL]: ActionControl,
  [ActionTypeEnum.SURVEILLANCE]: ActionSurveillance,
  [ActionTypeEnum.NOTE]: ActionNote,
  [ActionTypeEnum.CONTACT]: ActionContact,
  [ActionTypeEnum.STATUS]: ActionStatus,
  [ActionTypeEnum.OTHER]: ActionOther
}

const MissionTimelineItem: React.FC<MissionTimelineItemProps> = ({
  action,
  onClick,
  componentMap = ActionComponentMap
}) => {
  const Component = componentMap[action.actionType]

  if (!Component) {
    return null
  }

  return <Component action={action} onClick={onClick} />
}

export default MissionTimelineItem
