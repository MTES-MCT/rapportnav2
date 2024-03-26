import { ControlResult } from '../../../types/control-types'
import { Option } from "@mtes-mct/monitor-ui";


const defaultOptions: Option[] = [
    {
        label: 'Oui',
        value: ControlResult.YES
    },
    {
        label: 'Non',
        value: ControlResult.NO
    },
    {
        label: 'Non contrôlé',
        value: ControlResult.NOT_CONTROLLED
    }
]

export enum ControlResultExtraOptions {
    'NOT_CONCERNED' = 'NOT_CONCERNED'
}

const extraOptions = {
    [ControlResultExtraOptions.NOT_CONCERNED]: {
        label: 'Non concerné',
        value: ControlResult.NOT_CONCERNED
    } as Option
}

export const controlResultOptions: (availableExtraOptions?: ControlResultExtraOptions[]) => Option[] = (
    availableExtraOptions = []
) => [...defaultOptions, ...(availableExtraOptions.map(option => extraOptions[option]) || [])]
