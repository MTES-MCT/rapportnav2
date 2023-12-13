import React from 'react'
import {CoordinatesFormat, CoordinatesInput, DateRangePicker, Icon, Label, THEME} from '@mtes-mct/monitor-ui'
import Divider from 'rsuite/Divider'
import {Stack} from 'rsuite'
import Text from '../../../ui/text'
import {formatDateTimeForFrenchHumans} from '../../../dates'
import ControlsToCompleteTag from '../controls/controls-to-complete-tag'
import EnvControlForm from '../controls/env-control-form'
import {Action} from '../../../types/action-types'
import {ControlType} from '../../../types/control-types'
import {EnvActionControl, actionTargetTypeLabels, vehicleTypeLabels, EnvAction} from '../../../types/env-mission-types'
import {useParams} from 'react-router-dom'
import EnvInfractionNewTarget from '../infractions/env-infraction-new-target'
import EnvInfractionExistingTarget from '../infractions/env-infraction-existing-target'
import useActionById from "./use-action-by-id.tsx";

interface ActionControlPropsEnv {
    action: Action
}

const ActionControlEnv: React.FC<ActionControlPropsEnv> = ({action}) => {
    const {missionId, actionId} = useParams()

    const {
        data: envAction,
        loading,
        error,
    } = useActionById(actionId, missionId, action.source, action.type)

    if (loading) {
        return (
            <div>loading</div>
        )
    }
    if (error) {
        return (
            <div>error</div>
        )
    }
    if (envAction) {
        const actionData: EnvActionControl = envAction.data
        return (
            <Stack direction="column" spacing={'2rem'} alignItems="flex-start" style={{width: '100%'}}>
                <Stack.Item style={{width: '100%'}}>
                    <Stack direction="row" spacing={'0.5rem'} style={{width: '100%'}}>
                        <Stack.Item alignSelf="baseline">
                            <Icon.ControlUnit color={THEME.color.charcoal} size={20}/>
                        </Stack.Item>
                        <Stack.Item grow={2}>
                            <Text as="h2" weight="bold">
                                Contrôles {envAction.startDateTimeUtc && `(${formatDateTimeForFrenchHumans(envAction.startDateTimeUtc)})`}
                            </Text>
                        </Stack.Item>
                    </Stack>
                </Stack.Item>
                <Stack.Item style={{width: '100%'}}>
                    <Label>Thématique de contrôle</Label>
                    <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                        {actionData?.themes[0].theme ?? 'inconnue'}
                    </Text>
                </Stack.Item>
                <Stack.Item style={{width: '100%'}}>
                    <Label>Sous-thématiques de contrôle</Label>
                    <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                        {actionData?.themes[0].subThemes?.length
                            ? actionData?.themes[0].subThemes?.join(', ')
                            : 'inconnues'}
                    </Text>
                </Stack.Item>
                <Stack.Item style={{width: '100%'}}>
                    <Label>Date et heure de début et de fin</Label>
                    <DateRangePicker
                        defaultValue={[envAction.startDateTimeUtc || new Date(), envAction.endDateTimeUtc || new Date()]}
                        // label="Date et heure de début et de fin"
                        withTime={true}
                        isCompact={true}
                        isLight={true}
                        readOnly={true}
                        disabled={true}
                    />
                </Stack.Item>
                <Stack.Item>
                    <Label>Lieu du contrôle</Label>
                    <CoordinatesInput
                        defaultValue={[
                            actionData?.latitude as any,
                            actionData?.longitude as any
                        ]}
                        coordinatesFormat={CoordinatesFormat.DECIMAL_DEGREES}
                        // label="Lieu du contrôle"
                        // isLight={true}
                        disabled={true}
                    />
                </Stack.Item>
                <Stack.Item style={{width: '100%'}}>
                    <Divider style={{backgroundColor: THEME.color.charcoal}}/>
                </Stack.Item>
                <Stack.Item style={{width: '100%'}}>
                    <Stack direction="column" alignItems="flex-start" spacing={'2rem'} style={{width: '100%'}}>
                        <Stack.Item style={{width: '100%'}}>
                            <Stack direction="row" alignItems="flex-start" spacing={'2rem'} style={{width: '100%'}}>
                                <Stack.Item style={{width: '33%'}}>
                                    <Stack direction="column" spacing={'1rem'} alignItems="flex-start">
                                        <Stack.Item>
                                            <Label>Nbre total de contrôles</Label>
                                            <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                                                {actionData?.actionNumberOfControls ?? 'inconnu'}
                                            </Text>
                                        </Stack.Item>
                                        <Stack.Item>
                                            <Label>Type de cible</Label>
                                            <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                                                {!!actionData?.actionTargetType
                                                    ? actionTargetTypeLabels[actionData?.actionTargetType].libelle
                                                    : 'inconnu'}
                                            </Text>
                                        </Stack.Item>
                                        <Stack.Item>
                                            <Label>Type de véhicule</Label>
                                            <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                                                {!!actionData?.vehicleType ? vehicleTypeLabels[actionData?.vehicleType].libelle : 'inconnu'}
                                            </Text>
                                        </Stack.Item>
                                        <Stack.Item>
                                            <Label>Observations</Label>
                                            <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                                                {actionData?.observations ?? 'aucunes'}
                                            </Text>
                                        </Stack.Item>
                                    </Stack>
                                </Stack.Item>

                                <Stack.Item style={{width: '67%'}}>
                                    <Stack direction="column" alignItems="flex-start" spacing={'1.5rem'}
                                           style={{width: '100%'}}>
                                        {(actionData?.controlsToComplete?.length || 0) > 0 && (
                                            <Stack.Item alignSelf="flex-end">
                                                <ControlsToCompleteTag
                                                    amountOfControlsToComplete={
                                                        actionData?.controlsToComplete?.length
                                                    }
                                                    isLight={true}
                                                />
                                            </Stack.Item>
                                        )}

                                        <Stack.Item>
                                            <Text as="h3" weight="bold" color={THEME.color.gunMetal}>
                                                dont...
                                            </Text>
                                        </Stack.Item>
                                        <Stack.Item style={{width: '100%'}}>
                                            <EnvControlForm
                                                controlType={ControlType.ADMINISTRATIVE}
                                                data={actionData?.controlAdministrative}
                                                maxAmountOfControls={actionData?.actionNumberOfControls}
                                                shouldCompleteControl={
                                                    !!actionData?.controlsToComplete?.includes(
                                                        ControlType.ADMINISTRATIVE
                                                    )
                                                }
                                            />
                                        </Stack.Item>
                                        <Stack.Item style={{width: '100%'}}>
                                            <EnvControlForm
                                                controlType={ControlType.NAVIGATION}
                                                data={actionData?.controlNavigation}
                                                maxAmountOfControls={actionData?.actionNumberOfControls}
                                                shouldCompleteControl={
                                                    !!actionData?.controlsToComplete?.includes(ControlType.NAVIGATION)
                                                }
                                            />
                                        </Stack.Item>
                                        <Stack.Item style={{width: '100%'}}>
                                            <EnvControlForm
                                                controlType={ControlType.GENS_DE_MER}
                                                data={actionData?.controlGensDeMer}
                                                maxAmountOfControls={actionData?.actionNumberOfControls}
                                                shouldCompleteControl={
                                                    !!actionData?.controlsToComplete?.includes(
                                                        ControlType.GENS_DE_MER
                                                    )
                                                }
                                            />
                                        </Stack.Item>
                                        <Stack.Item style={{width: '100%'}}>
                                            <EnvControlForm
                                                controlType={ControlType.SECURITY}
                                                data={actionData?.controlSecurity}
                                                maxAmountOfControls={actionData?.actionNumberOfControls}
                                                shouldCompleteControl={
                                                    !!actionData?.controlsToComplete?.includes(ControlType.SECURITY)
                                                }
                                            />
                                        </Stack.Item>
                                    </Stack>
                                </Stack.Item>
                            </Stack>
                        </Stack.Item>
                        <Stack.Item style={{width: '100%'}}>
                            <EnvInfractionNewTarget
                                availableControlTypesForInfraction={actionData?.availableControlTypesForInfraction}
                            />
                        </Stack.Item>
                        <Stack.Item style={{width: '100%'}}>
                            <EnvInfractionExistingTarget
                                infractionsByTarget={actionData?.infractions}
                                availableControlTypesForInfraction={actionData?.availableControlTypesForInfraction}
                            />
                        </Stack.Item>
                    </Stack>
                </Stack.Item>
            </Stack>
        )
    }

    return null
}

export default ActionControlEnv
