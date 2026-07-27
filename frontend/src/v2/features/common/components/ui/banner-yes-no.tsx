import Text from '@common/components/ui/text'
import { THEME } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import YesNoToogle from './yes-no-toogle'

type BannerYesNoProps = {
  title?: string
  message?: string
  value?: boolean
  onSubmit: (response: boolean) => void
}

const BannerYesNo: React.FC<BannerYesNoProps> = ({ title, value, message, onSubmit }) => {
  const handleSubmit = (response: boolean) => {
    onSubmit(response)
  }
  return (
    <Stack direction={'row'} style={{ padding: 16, backgroundColor: THEME.color.blueGray25 }}>
      <Stack.Item style={{ width: '50%' }}>
        <Stack direction={'column'} style={{ alignItems: 'start' }}>
          <Stack.Item>
            <Text as="h3" color={THEME.color.blueGray} weight="bold">
              {title}
            </Text>
          </Stack.Item>
          <Stack.Item>
            <Text as="h3" color={THEME.color.charcoal} weight="normal">
              {message}
            </Text>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '50%' }}>
        <Stack direction={'row'} style={{ justifyContent: 'end' }}>
          <YesNoToogle initValue={value} onSubmit={handleSubmit} />
        </Stack>
      </Stack.Item>
    </Stack>
  )
}

export default BannerYesNo
