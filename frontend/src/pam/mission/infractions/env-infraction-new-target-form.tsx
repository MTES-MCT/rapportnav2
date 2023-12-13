import React from 'react'
import {Stack} from 'rsuite'
import {Select, TextInput} from '@mtes-mct/monitor-ui'
import {ControlType} from '../../../types/control-types'
import {InfractionEnvNewTarget} from '../../../types/infraction-types'
import InfractionForm from './infraction-form'
import {
    VESSEL_SIZE_OPTIONS,
    VESSEL_TYPE_OPTIONS,
    CONTROL_TYPE_OPTIONS,
    getDisabledControlTypes
} from '../controls/utils'

interface EnvInfractionNewTargetFormProps {
    infraction?: InfractionEnvNewTarget
    availableControlTypesForInfraction?: ControlType[]
    availableNatinfs?: string[]
    onChange: (field: string, value: any) => void
    onCancel: () => void
}

const EnvInfractionNewTargetForm: React.FC<EnvInfractionNewTargetFormProps> = ({
                                                                                   infraction,
                                                                                   availableControlTypesForInfraction,
                                                                                   availableNatinfs,
                                                                                   onChange,
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
                                    value={infraction?.vesselSize}
                                    name="vesselSize"
                                    onChange={(nextValue: OptionValue) => onChange('vesselSize', nextValue)}
                                />
                            </Stack.Item>
                            <Stack.Item style={{width: '60%'}}>
                                <Select
                                    label="Type de navire"
                                    options={VESSEL_TYPE_OPTIONS}
                                    value={infraction?.vesselType}
                                    name="vesselType"
                                    onChange={(nextValue: OptionValue) => onChange('vesselType', nextValue)}
                                />
                            </Stack.Item>
                        </Stack>
                    </Stack.Item>
                    <Stack.Item style={{width: '100%'}}>
                        <Stack direction="row" alignItems="baseline" spacing={'0.5rem'}>
                            <Stack.Item style={{width: '40%'}}>
                                <TextInput
                                    label="Immatriculation"
                                    value={infraction?.vesselIdentifier}
                                    name="vesselIdentifier"
                                    onChange={(nextValue?: string) => onChange('vesselIdentifier', nextValue)}
                                />
                            </Stack.Item>
                            <Stack.Item style={{width: '60%'}}>
                                <TextInput
                                    label="Identité de la personne contrôlée"
                                    value={infraction?.identityControlledPerson}
                                    name="identityControlledPerson"
                                    onChange={(nextValue?: string) => onChange('identityControlledPerson', nextValue)}
                                />
                            </Stack.Item>
                        </Stack>
                    </Stack.Item>
                </Stack>
            </Stack.Item>
            <Stack.Item style={{width: '100%'}}>
                <InfractionForm
                    infraction={infraction}
                    availableNatinfs={availableNatinfs}
                    onChange={onChange}
                    onCancel={onCancel}
                />
            </Stack.Item>
        </Stack>
    )
}

export default EnvInfractionNewTargetForm
