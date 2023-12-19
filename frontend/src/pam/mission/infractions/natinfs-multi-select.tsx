import {Dropdown, Icon, MultiSelect, OptionValue} from '@mtes-mct/monitor-ui'
import {ActionTypeEnum} from '../../../types/env-mission-types'
import React from "react";
import useNatinfs from "./use-natinfs.tsx";
import {Natinf} from "../../../types/infraction-types.ts";

interface NatinfsMultiSelectProps {
    selectedNatinfs?: string[]
    onChange: (field: string, value: any) => void
}

const NatinfsMultiSelect: React.FC<NatinfsMultiSelectProps> = ({selectedNatinfs, onChange}) => {
    const {data: natinfs, loading, error} = useNatinfs()

    if (loading || error) {
        return
    }
    return (
        <MultiSelect
            error=""
            label="NATINF"
            name="natinfs"
            value={selectedNatinfs || []}
            onChange={(nextValue?: OptionValue[]) => onChange("natinfs", nextValue)}
            options={natinfs?.map(({natinfCode, infraction}) => ({
                value: natinfCode,
                label: `${natinfCode} - ${infraction}`
            })) as any}
            placeholder=""
            searchable={true}
            virtualized={true}
        />
    )
}

export default NatinfsMultiSelect
