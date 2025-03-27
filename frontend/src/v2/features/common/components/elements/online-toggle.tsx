import { FC } from 'react'
import styled from 'styled-components'
import { Icon, THEME, Toggle, ToggleProps } from '@mtes-mct/monitor-ui'
import { useOnlineManager } from '../../hooks/use-online-manager.tsx'
import { Stack } from 'rsuite'
import Text from '@common/components/ui/text.tsx'

export type OfflineToggleProps = {} & Omit<ToggleProps, 'label' | 'name'>

// Styled version of the Toggle component
const StyledToggle = styled(Toggle)`
  .rs-toggle-presentation {
    background-color: ${({ checked }) =>
      checked ? THEME.color.mediumSeaGreen + '!important' : THEME.color.maximumRed + '!important'};
    border: transparent;
    height: 28px;
    min-width: 50px;
    border-radius: 16px;

    // this is the toggle
    &::after {
      background-color: white;
      border-radius: 12px;
      height: 20px;
      width: 20px;
      margin-left: ${({ checked }) => (checked ? '-24px !important;' : '0 !important;')};
      margin-top: 2px;
    }

    &:hover {
      border: transparent;
      &::after {
        height: 22px;
        width: 22px;
        background-color: white;
        margin-left: ${({ checked }) => (checked ? '-24px !important;' : '0 !important;')};
        margin-top: 1px;
      }
    }

    .rs-toggle-inner {
      margin-left: ${({ checked }) => (checked ? '8px !important;' : '28px !important;')};
      margin-right: ${({ checked }) => (checked ? '28px !important;' : '8px !important;')};
      margin-top: 4px;
    }
  }
`

export const OnlineToggle: FC<OfflineToggleProps> = () => {
  const { isOnline, setOnline } = useOnlineManager()

  return (
    <Stack direction={'row'} alignItems={'center'} justifyContent={'center'} spacing={'0.5rem'}>
      <Stack.Item>
        <Text
          as={'h3'}
          color={isOnline ? THEME.color.lightGray : THEME.color.maximumRed}
          weight={isOnline ? 'normal' : 'bold'}
        >
          Hors ligne
        </Text>
      </Stack.Item>
      <Stack.Item>
        <StyledToggle
          label={''}
          name="online-toggle"
          checked={isOnline}
          checkedChildren={<Icon.Check color={THEME.color.white} size={14} />}
          unCheckedChildren={<Icon.Close color={THEME.color.white} size={14} />}
          onChange={(isChecked: boolean) => setOnline(isChecked)}
        />
      </Stack.Item>
      <Stack.Item>
        <Text
          as={'h3'}
          color={isOnline ? THEME.color.mediumSeaGreen : THEME.color.lightGray}
          weight={isOnline ? 'bold' : 'normal'}
        >
          En ligne
        </Text>
      </Stack.Item>
    </Stack>
  )
}

export default OnlineToggle
