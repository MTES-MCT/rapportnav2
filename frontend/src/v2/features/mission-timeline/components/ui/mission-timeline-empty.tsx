import Text from '@common/components/ui/text'
import { Stack } from 'rsuite'

interface MissionTimelineEmptyProps {
  message?: string
}

const MissionTimelineEmpty: React.FC<MissionTimelineEmptyProps> = ({ message }) => {
  return (
    <Stack justifyContent={'center'} alignItems={'center'} style={{ height: '100%' }}>
      <Stack.Item alignSelf={'center'}>
        <Text as={'h3'}>{message ?? "Aucune action n'est ajoutée pour le moment"}</Text>
      </Stack.Item>
    </Stack>
  )
}

export default MissionTimelineEmpty
