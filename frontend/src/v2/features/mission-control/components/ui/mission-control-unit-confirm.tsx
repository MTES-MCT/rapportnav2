import Text from '@common/components/ui/text'
import { THEME } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import YesNoToogle from '../../../common/components/ui/yes-no-toogle'

type MissionControlUnitConfirmProps = {
  title?: string
  message?: string
  value?: boolean
  onSubmit: (response: boolean) => void
}

const MissionControlUnitConfirm: React.FC<MissionControlUnitConfirmProps> = ({ value, onSubmit }) => {
  return (
    <Stack direction={'row'} style={{ padding: 16, backgroundColor: THEME.color.blueGray25 }}>
      <Stack.Item style={{ width: '50%' }}>
        <Stack direction={'column'} style={{ alignItems: 'start' }}>
          <Stack.Item>
            <Text as="h3" color={THEME.color.blueGray} weight="bold">
              Validation de l'unité
            </Text>
          </Stack.Item>
          <Stack.Item>
            <Text as="h3" color={THEME.color.charcoal} weight="normal">
              Le contrôle a-t-il été éffectué?
            </Text>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '50%' }}>
        <Stack direction={'row'} style={{ justifyContent: 'end' }}>
          <YesNoToogle initValue={value} onSubmit={onSubmit} />
        </Stack>
      </Stack.Item>
    </Stack>
  )
}

export default MissionControlUnitConfirm
