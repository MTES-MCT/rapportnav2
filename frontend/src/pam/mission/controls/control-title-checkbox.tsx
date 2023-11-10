import { THEME, Checkbox } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import Title from '../../../ui/title'
import { ControlType } from '../../mission-types'
import { controlTitle } from './utils'

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
    <Stack direction="row" alignItems="center">
      <Stack.Item alignSelf="baseline">
        <Checkbox
          error=""
          label=""
          name="control"
          disabled={!!!onChange && !checked}
          checked={!!checked}
          onChange={(isChecked: boolean) => (onChange ? onChange(isChecked) : undefined)}
        />
      </Stack.Item>
      <Stack.Item>
        <Title as="h3" color={THEME.color.gunMetal} weight="bold">
          {controlTitle(controlType)}
        </Title>
      </Stack.Item>
      <Stack.Item>&nbsp;&nbsp;</Stack.Item>
      {!!shouldCompleteControl && (
        <Stack.Item>
          <p style={{ color: THEME.color.maximumRed }}>●</p>
        </Stack.Item>
      )}
    </Stack>
  )
}

export default ControlTitleCheckbox
