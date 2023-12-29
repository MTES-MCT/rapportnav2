import { Stack } from 'rsuite'
import {
    ControlAdministrative,
    ControlGensDeMer,
    ControlNavigation,
    ControlSecurity,
    ControlType
} from '../../../types/control-types'
import { NumberInput, TextInput } from '@mtes-mct/monitor-ui'
import { useMutation } from '@apollo/client'
import {
    DELETE_CONTROL_ADMINISTRATIVE,
    DELETE_CONTROL_GENS_DE_MER,
    DELETE_CONTROL_NAVIGATION,
    DELETE_CONTROL_SECURITY,
    MUTATION_ADD_OR_UPDATE_CONTROL_ADMINISTRATIVE,
    MUTATION_ADD_OR_UPDATE_CONTROL_GENS_DE_MER,
    MUTATION_ADD_OR_UPDATE_CONTROL_NAVIGATION,
    MUTATION_ADD_OR_UPDATE_CONTROL_SECURITY
} from '../queries'
import omit from 'lodash/omit'
import { useParams } from 'react-router-dom'
import ControlTitleCheckbox from './control-title-checkbox'
import { useEffect, useState } from 'react'
import { GET_MISSION_TIMELINE } from "../timeline/use-mission-timeline.tsx";
import { GET_ACTION_BY_ID } from "../actions/use-action-by-id.tsx";

export interface EnvControlFormProps {
    controlType: ControlType
    data?: ControlAdministrative | ControlSecurity | ControlNavigation | ControlGensDeMer
    maxAmountOfControls?: number
    shouldCompleteControl?: boolean
}

const EnvControlForm: React.FC<EnvControlFormProps> = ({
                                                           controlType,
                                                           data,
                                                           maxAmountOfControls,
                                                           shouldCompleteControl
                                                       }) => {
    const {missionId, actionId} = useParams()

    const [observationsValue, setObservationsValue] = useState<string | undefined>(data?.observations)

    const handleObservationsChange = (nextValue?: string) => {
        setObservationsValue(nextValue)
    }

    useEffect(() => {
        setObservationsValue(data?.observations)
    }, [data])

    const handleObservationsBlur = () => {
        onChange('observations', observationsValue)
    }

    const mutations = {
        [ControlType.ADMINISTRATIVE]: {
            addOrUpdate: MUTATION_ADD_OR_UPDATE_CONTROL_ADMINISTRATIVE,
            delete: DELETE_CONTROL_ADMINISTRATIVE
        },
        [ControlType.NAVIGATION]: {
            addOrUpdate: MUTATION_ADD_OR_UPDATE_CONTROL_NAVIGATION,
            delete: DELETE_CONTROL_NAVIGATION
        },
        [ControlType.SECURITY]: {
            addOrUpdate: MUTATION_ADD_OR_UPDATE_CONTROL_SECURITY,
            delete: DELETE_CONTROL_SECURITY
        },
        [ControlType.GENS_DE_MER]: {
            addOrUpdate: MUTATION_ADD_OR_UPDATE_CONTROL_GENS_DE_MER,
            delete: DELETE_CONTROL_GENS_DE_MER
        }
    }

    const [mutate] = useMutation(mutations[controlType].addOrUpdate, {
        refetchQueries: [GET_MISSION_TIMELINE, GET_ACTION_BY_ID]
    })

    const [deleteControl] = useMutation(mutations[controlType].delete, {
        refetchQueries: [GET_MISSION_TIMELINE, GET_ACTION_BY_ID],
    })

    const toggleControl = async (isChecked: boolean) =>
        isChecked
            ? onChange()
            : await deleteControl({
                variables: {
                    actionId
                }
            })

    const onChange = async (field?: string, value?: any) => {
        let updatedData = {
            ...omit(data, '__typename'),
            missionId: missionId,
            actionControlId: actionId,
            amountOfControls: data?.amountOfControls || 1
        }

        if (!!field && !!value) {
            updatedData = {
                ...updatedData,
                [field]: value
            }
        }
        await mutate({variables: {control: updatedData}})
    }

    return (
        <Stack direction="column" alignItems="flex-start" spacing={'0.5rem'} style={{width: '100%'}}>
            <Stack.Item style={{width: '100%'}}>
                <ControlTitleCheckbox
                    controlType={controlType}
                    checked={!!data || shouldCompleteControl}
                    shouldCompleteControl={!!shouldCompleteControl && !!!data}
                    onChange={(isChecked: boolean) => toggleControl(isChecked)}
                />
            </Stack.Item>
            <Stack.Item style={{width: '100%'}}>
                <Stack direction="row" style={{width: '100%'}} spacing={'0.5rem'}>
                    <Stack.Item style={{width: '33%'}}>
                        <NumberInput
                            label="Nb contrôles"
                            name="amountOfControls"
                            value={data?.amountOfControls}
                            max={maxAmountOfControls}
                            isLight={true}
                            onChange={(nextValue?: number) => onChange('amountOfControls', nextValue)}
                        />
                    </Stack.Item>
                    <Stack.Item style={{width: '67%'}}>
                        <TextInput
                            label="Observations (hors infraction)"
                            name="observations"
                            isLight={true}
                            value={observationsValue}
                            onChange={handleObservationsChange}
                            onBlur={handleObservationsBlur}
                        />
                    </Stack.Item>
                </Stack>
            </Stack.Item>
        </Stack>
    )
}

export default EnvControlForm
