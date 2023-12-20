import {THEME, Checkbox} from '@mtes-mct/monitor-ui'
import {Stack} from 'rsuite'
import Text from '../../../ui/text'
import {ControlType} from '../../../types/control-types'
import {controlTitle} from './utils'

export interface ControlTitleCheckboxProps {
    controlType: ControlType
    checked?: boolean
    shouldCompleteControl?: boolean
    onChange?: (isChecked: boolean) => void
}

const ControlTitleCheckbox: React.FC<ControlTitleCheckboxProps> = ({
                                                                       controlType,
                                                                       checked,
                                                                       shouldCompleteControl,
                                                                       onChange
                                                                   }) => {
    return (
        <Stack direction="row" alignItems="center" spacing={'0.2rem'}>
            <Stack.Item alignSelf="baseline">
                <Checkbox
                    error=""
                    label=""
                    name="control"
                    checked={!!checked}
                    onChange={(isChecked: boolean) => (onChange ? onChange(isChecked) : undefined)}
                />
            </Stack.Item>
            <Stack.Item>
                <Text as="h3" color={THEME.color.gunMetal} weight="bold">
                    {controlTitle(controlType)}
                </Text>
            </Stack.Item>
            {!!shouldCompleteControl && (
                <Stack.Item>
                    <p style={{color: THEME.color.maximumRed}}>●</p>
                </Stack.Item>
            )}
        </Stack>
    )
}

export default ControlTitleCheckbox
