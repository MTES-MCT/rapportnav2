import Text from '@common/components/ui/text'
import { FormikCheckbox, FormikCheckboxProps, THEME } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import styled from 'styled-components'

// const { getControlType } = useControl()

type MissionControlTitle = {
  text?: string
  shouldComplete: boolean
}

export const MissionControlFormikCheckBoxTitle = styled(
  ({ text, shouldComplete, ...props }: Omit<FormikCheckboxProps, 'label'> & MissionControlTitle) => (
    <Stack direction="row" alignItems="center" spacing={'0.2rem'}>
      <Stack.Item alignSelf="baseline">
        <FormikCheckbox label="" {...props} />
      </Stack.Item>
      <Stack.Item>
        <Text as="h3" color={THEME.color.gunMetal} weight="bold">
          {text}
        </Text>
      </Stack.Item>
      {!!shouldComplete && (
        <Stack.Item>
          <p data-testid="control-title-required-control" style={{ color: THEME.color.maximumRed }}>
            ●
          </p>
        </Stack.Item>
      )}
    </Stack>
  )
)({})
