import { Stack } from 'rsuite'
import Text from '../text'

const MissionTimelineEmpty: React.FC = () => {
  return (
    <Stack justifyContent={'center'} alignItems={'center'} style={{ height: '100%' }}>
      <Stack.Item alignSelf={'center'}>
        <Text as={'h3'}>Aucune action n'est ajoutée pour le moment</Text>
      </Stack.Item>
    </Stack>
  )
}

export default MissionTimelineEmpty
