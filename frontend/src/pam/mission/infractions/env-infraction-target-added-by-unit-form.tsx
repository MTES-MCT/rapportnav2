import React from 'react'
import { Stack } from 'rsuite'
import { OptionValue, Select, TextInput } from '@mtes-mct/monitor-ui'
import { ControlType } from '../../../types/control-types'
import { Infraction } from '../../../types/infraction-types'
import InfractionForm from './infraction-form'
import {
    CONTROL_TYPE_OPTIONS,
    getDisabledControlTypes,
    VESSEL_SIZE_OPTIONS,
    VESSEL_TYPE_OPTIONS
} from '../controls/utils'

interface EnvInfractionNewTargetFormProps {
    infraction?: Infraction
    availableControlTypesForInfraction?: ControlType[]
    onChange: (field: string, value: any) => void
    onChangeTarget: (field: string, value: any) => void
    onCancel: () => void
}

const EnvInfractionTargetAddedByUnitForm: React.FC<EnvInfractionNewTargetFormProps> = ({
                                                                                           infraction,
                                                                                           availableControlTypesForInfraction,
                                                                                           onChange,
                                                                                           onChangeTarget,
                                                                                           onCancel
                                                                                       }) => {

    return (
        <Stack direction="column" spacing={'2rem'} style={{width: '100%', padding: '1rem'}}>
            <Stack.Item style={{width: '100%'}}>
                <Select
                    label="Type de contrôle avec infraction"
                    options={CONTROL_TYPE_OPTIONS}
                    disabledItemValues={getDisabledControlTypes(availableControlTypesForInfraction)}
                    value={infraction?.controlType}
                    name="controlType"
                    onChange={(nextValue: OptionValue) => onChange('controlType', nextValue)}
                />
            </Stack.Item>
            <Stack.Item style={{width: '100%'}}>
                <Stack direction="column" alignItems="baseline" spacing={'0.5rem'}>
                    <Stack.Item style={{width: '100%'}}>
                        <Stack direction="row" alignItems="baseline" spacing={'0.5rem'}>
                            <Stack.Item style={{width: '40%'}}>
                                <Select
                                    label="Taille du navire"
                                    options={VESSEL_SIZE_OPTIONS}
                                    value={infraction?.target?.vesselSize}
                                    name="vesselSize"
                                    onChange={(nextValue: OptionValue) => onChangeTarget('vesselSize', nextValue)}
                                />
                            </Stack.Item>
                            <Stack.Item style={{width: '60%'}}>
                                <Select
                                    label="Type de navire"
                                    options={VESSEL_TYPE_OPTIONS}
                                    value={infraction?.target?.vesselType}
                                    name="vesselType"
                                    onChange={(nextValue: OptionValue) => onChangeTarget('vesselType', nextValue)}
                                />
                            </Stack.Item>
                        </Stack>
                    </Stack.Item>
                    <Stack.Item style={{width: '100%'}}>
                        <Stack direction="row" alignItems="baseline" spacing={'0.5rem'}>
                            <Stack.Item style={{width: '40%'}}>
                                <TextInput
                                    label="Immatriculation"
                                    value={infraction?.target?.vesselIdentifier}
                                    name="vesselIdentifier"
                                    role="vesselIdentifier"
                                    onChange={(nextValue?: string) => onChangeTarget('vesselIdentifier', nextValue)}
                                />
                            </Stack.Item>
                            <Stack.Item style={{width: '60%'}}>
                                <TextInput
                                    label="Identité de la personne contrôlée"
                                    value={infraction?.target?.identityControlledPerson}
                                    name="identityControlledPerson"
                                    role="identityControlledPerson"
                                    onChange={(nextValue?: string) => onChangeTarget('identityControlledPerson', nextValue)}
                                />
                            </Stack.Item>
                        </Stack>
                    </Stack.Item>
                </Stack>
            </Stack.Item>
            <Stack.Item style={{width: '100%'}}>
                <InfractionForm
                    infraction={infraction}
                    onChange={onChange}
                    onCancel={onCancel}
                />
            </Stack.Item>
        </Stack>
    )
}

export default EnvInfractionTargetAddedByUnitForm
