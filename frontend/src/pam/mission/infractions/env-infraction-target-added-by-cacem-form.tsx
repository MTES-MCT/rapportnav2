import React from 'react'
import { Stack } from 'rsuite'
import { Checkbox, Legend, MultiRadio, Select, THEME } from '@mtes-mct/monitor-ui'
import { ControlType } from '../../../types/control-types'
import { Infraction, InfractionByTarget } from '../../../types/infraction-types'
import InfractionForm from './infraction-form'
import {
    controlTitle,
    getDisabledControlTypes,
    vesselSizeToHumanString,
    vesselTypeToHumanString
} from '../controls/utils'
import Text from '../../../ui/text'
import { formalNoticeLabels, infractionTypeLabels, transformFormatToOptions } from '../../../types/env-mission-types'
import NatinfsFullNameList from "./natinfs-fullname-list.tsx";

const CONTROL_TYPE_OPTIONS = [
    {
        label: `Infraction - ${controlTitle(ControlType.ADMINISTRATIVE)}`,
        value: ControlType.ADMINISTRATIVE
    },
    {
        label: `Infraction - ${controlTitle(ControlType.SECURITY)}`,
        value: ControlType.SECURITY
    },
    {
        label: `Infraction - ${controlTitle(ControlType.NAVIGATION)}`,
        value: ControlType.NAVIGATION
    },
    {
        label: `Infraction - ${controlTitle(ControlType.GENS_DE_MER)}`,
        value: ControlType.GENS_DE_MER
    }
]

interface EnvInfractionNewTargetFormProps {
    infraction?: InfractionByTarget
    formData?: Infraction
    availableControlTypesForInfraction?: ControlType[]
    onChange: (field: string, value: any) => void
    onCancel: () => void
}

const EnvInfractionTargetAddedByCacemForm: React.FC<EnvInfractionNewTargetFormProps> = ({
                                                                                            infraction,
                                                                                            formData,
                                                                                            availableControlTypesForInfraction,
                                                                                            onChange,
                                                                                            onCancel
                                                                                        }) => {

    return (
        <Stack direction="column" spacing={'2rem'} style={{width: '100%', padding: '1rem'}}>
            <Stack.Item style={{width: '100%'}}>
                <Stack direction="row" spacing={'2rem'} style={{width: '100%'}}>
                    <Stack.Item style={{width: '100%'}}>
                        <Legend>Immatriculation</Legend>
                        <Text as="h3" weight="medium">
                            {formData?.target?.vesselIdentifier}
                        </Text>
                    </Stack.Item>
                    <Stack.Item style={{width: '100%'}}>
                        <Legend>Type de navire</Legend>
                        <Text as="h3" weight="medium">
                            {vesselTypeToHumanString(formData?.target?.vesselType)}
                        </Text>
                    </Stack.Item>
                    <Stack.Item style={{width: '100%'}}>
                        <Legend>Taille du navire</Legend>
                        <Text as="h3" weight="medium">
                            {vesselSizeToHumanString(formData?.target?.vesselSize)}
                        </Text>
                    </Stack.Item>
                </Stack>
            </Stack.Item>
            <Stack.Item style={{width: '100%'}}>
                <Legend>Identité de la personne contrôlée</Legend>
                <Text as="h3" weight="medium">
                    {formData?.target?.identityControlledPerson}
                </Text>
            </Stack.Item>
            <Stack.Item style={{width: '100%'}}>
                <MultiRadio
                    isReadOnly={true}
                    isInline
                    value={formData?.target?.infractionType}
                    label="Type d'infraction"
                    name="infractionType"
                    options={transformFormatToOptions(infractionTypeLabels)}
                />
            </Stack.Item>
            <Stack.Item style={{width: '100%'}}>
                <MultiRadio
                    isReadOnly={true}
                    isInline
                    value={formData?.target?.formalNotice}
                    label="Mise en demeure"
                    name="formalNotice"
                    options={transformFormatToOptions(formalNoticeLabels)}
                />
            </Stack.Item>
            <Stack.Item style={{width: '100%'}}>
                <Legend>NATINF</Legend>
                <NatinfsFullNameList natinfs={infraction?.infractions[0]?.natinfs}/>
            </Stack.Item>
            <Stack.Item style={{width: '100%'}}>
                <Stack direction="row" spacing={'2rem'}>
                    <Stack.Item>
                        <Legend>Tribunal compétent</Legend>
                        <Text as="h3" weight="medium">
                            {formData?.target?.relevantCourt || '--'}
                        </Text>
                    </Stack.Item>
                    <Stack.Item>
                        <Legend>&nbsp;</Legend>
                        <Checkbox value={!!formData?.target?.toProcess} label="À traiter"/>
                    </Stack.Item>
                </Stack>
            </Stack.Item>
            <Stack.Item style={{width: '100%'}}>
                <Legend>Observations</Legend>
                <Text as="h3" weight="medium">
                    {formData?.target?.observations ?? '--'}
                </Text>
            </Stack.Item>
            <Stack.Item style={{width: '100%'}}>
                <Legend>Ajout d’une infraction pour cette cible</Legend>
                <Stack
                    direction="column"
                    spacing={'2rem'}
                    style={{width: '100%', backgroundColor: THEME.color.cultured, padding: '2rem'}}
                >
                    <Stack.Item style={{width: '100%'}}>
                        <Select
                            label="Type de contrôle avec infraction"
                            options={CONTROL_TYPE_OPTIONS}
                            disabledItemValues={getDisabledControlTypes(availableControlTypesForInfraction)}
                            value={formData?.controlType}
                            name="controlType"
                            onChange={(nextValue: OptionValue) => onChange('controlType', nextValue)}
                        />
                    </Stack.Item>
                    <Stack.Item style={{width: '100%'}}>
                        <InfractionForm
                            infraction={formData}
                            onChange={onChange}
                            onCancel={onCancel}
                        />
                    </Stack.Item>
                </Stack>
            </Stack.Item>
        </Stack>
    )
}

export default EnvInfractionTargetAddedByCacemForm
