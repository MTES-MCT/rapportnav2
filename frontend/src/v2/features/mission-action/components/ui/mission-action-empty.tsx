import Text from '@common/components/ui/text'
import { Stack } from 'rsuite'

const MissionActionEmpty: React.FC = () => {
  return (
    <Stack justifyContent={'center'} alignItems={'center'} style={{ height: '100%' }}>
      <Stack.Item alignSelf={'center'}>
        <Text as={'h3'}>Aucune action</Text>
      </Stack.Item>
    </Stack>
  )
}

export default MissionActionEmpty
