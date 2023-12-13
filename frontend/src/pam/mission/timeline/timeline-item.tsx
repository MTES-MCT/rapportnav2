import React from 'react'
import {THEME, Icon, Tag, Accent} from '@mtes-mct/monitor-ui'
import {
    ActionTypeEnum,
    EnvAction,
    EnvActionControl,
    MissionSourceEnum,
    actionTargetTypeLabels
} from '../../../types/env-mission-types'
import {FlexboxGrid, Stack} from 'rsuite'
import {Action, ActionControl, ActionStatus as NavActionStatus} from '../../../types/action-types'
import {FishAction} from '../../../types/fish-mission-types'
import {StatusColorTag} from '../status/status-selection-dropdown'
import {mapStatusToText, statusReasonToHumanString} from '../status/utils'
import {controlMethodToHumanString, vesselTypeToHumanString} from '../controls/utils'
import ControlsToCompleteTag from '../controls/controls-to-complete-tag'
import Text from '../../../ui/text'
import {useParams} from 'react-router-dom'

interface MissionTimelineItemProps {
    action: Action
    onClick: (action: Action) => void
}

const Wrapper: React.FC<{ onClick: any; children: any; borderWhenSelected?: boolean }> = ({
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

const ActionEnvControl: React.FC<{ action: Action; onClick: any }> = ({action, onClick}) => {
    const {actionId} = useParams()
    const actionData = action.data as unknown as EnvActionControl
    return (
        <Wrapper onClick={onClick} borderWhenSelected={action.id === actionId}>
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
                                        <Text as="h3" weight="bold" color={THEME.color.gunMetal}>
                                            {actionData && 'themes' in actionData && actionData?.themes[0].theme ? actionData?.themes[0].theme : ''}
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
                                                        <Tag accent={Accent.PRIMARY}
                                                             style={{marginRight: '0.5rem'}}>{tag}</Tag>
                                                    ))
                                                }
                                            </>
                                        )}
                                    </Stack.Item>
                                    <Stack.Item alignSelf="flex-end" style={{width: '30%'}}>
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

const ActionFishControl: React.FC<{ action: Action; onClick: any }> = ({action, onClick}) => {
    const {actionId} = useParams()
    const actionData = action.data as unknown as FishAction
    return (
        <Wrapper onClick={onClick} borderWhenSelected={action.id === actionId}>
            <FlexboxGrid.Item style={{width: '100%', padding: '1rem'}}>
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
                                        <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                                            en mer - LE ZORBA
                                        </Text>
                                    </Stack.Item>
                                </Stack>
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
                                                        <Tag accent={Accent.PRIMARY}
                                                             style={{marginRight: '0.5rem'}}>{tag}</Tag>
                                                    ))
                                                }
                                            </>
                                        )}
                                    </Stack.Item>
                                    <Stack.Item alignSelf="flex-end" style={{width: '30%'}}>
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

const ActionNavControl: React.FC<{ action: Action; onClick: any }> = ({action, onClick}) => {
    const {actionId} = useParams()
    const actionData = action.data as unknown as ActionControl
    return (
        <Wrapper onClick={onClick} borderWhenSelected={action.id === actionId}>
            <FlexboxGrid.Item style={{width: '100%', padding: '1rem'}}>


                <Stack direction="row" spacing="0.5rem">
                    <Stack.Item alignSelf="flex-start" style={{paddingTop: '0.2rem'}}>
                        <Icon.ControlUnit color={THEME.color.charcoal} size={20}/>
                    </Stack.Item>
                    <Stack.Item>
                        <Stack direction="column" alignItems="flex-start" spacing="0.5rem">
                            <Stack.Item>
                                <Stack direction="row" spacing="0.5rem">
                                    <Stack.Item>
                                        <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                                            Contrôles
                                        </Text>
                                    </Stack.Item>
                                    <Stack.Item>
                                        <Text as="h3" weight="bold" color={THEME.color.gunMetal}>
                                            {`${controlMethodToHumanString(actionData?.controlMethod)} - ${vesselTypeToHumanString(
                                                actionData?.vesselType
                                            )}`}
                                        </Text>
                                    </Stack.Item>
                                </Stack>
                            </Stack.Item>
                            {
                                action.summaryTags?.length > 0 && (
                                    <Stack.Item>
                                        {
                                            action.summaryTags?.map((tag: string) => (
                                                <Tag accent={Accent.PRIMARY} style={{marginRight: '0.5rem'}}>{tag}</Tag>
                                            ))
                                        }
                                    </Stack.Item>
                                )
                            }
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

const ActionStatus: React.FC<{ action: Action; onClick: any }> = ({action, onClick}) => {
    const {actionId} = useParams()
    const isSelected = action.id === actionId
    const actionData = action.data as unknown as NavActionStatus
    return (
        <Wrapper onClick={onClick} borderWhenSelected={false}>
            <Stack alignItems="center" spacing="0.5rem">
                <Stack.Item>
                    <StatusColorTag status={actionData?.status}/>
                </Stack.Item>
                <Stack.Item>
                    <Text
                        as="h3"
                        weight="normal"
                        color={isSelected ? THEME.color.charcoal : THEME.color.slateGray}
                        decoration={isSelected ? 'underline' : 'normal'}
                    >
                        <p
                            style={{
                                whiteSpace: 'nowrap',
                                overflow: 'hidden',
                                textOverflow: 'ellipsis'
                            }}
                        >
                            <b>{`${mapStatusToText(actionData?.status)} - ${actionData?.isStart ? 'début' : 'fin'} ${
                                !!actionData?.reason ? ' - ' + statusReasonToHumanString(actionData?.reason) : ''
                            }`}</b>
                            {!!actionData?.observations ? ' - ' + actionData?.observations : ''}
                        </p>
                    </Text>
                </Stack.Item>
                <Stack.Item>
                    <Icon.EditUnbordered size={20} color={THEME.color.slateGray}/>
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

    return <Component action={action as any} onClick={onClick}/>
}

export default MissionTimelineItem
