import Text from '@common/components/ui/text'
import { Stack } from 'rsuite'

interface MissionTimelineEmptyProps {
  isReinforcementTime?: boolean
}

const MissionTimelineEmpty: React.FC<MissionTimelineEmptyProps> = ({isReinforcementTime}) => {
  return (
    <Stack justifyContent={'center'} alignItems={'center'} style={{ height: '100%' }}>
      <Stack.Item alignSelf={'center'}>
        <Text as={'h3'}>{isReinforcementTime ? 'Aucune détail de mission ne vous est demandé lors de renfort extérieur' : "Aucune action n'est ajoutée pour le moment"}</Text>
      </Stack.Item>
    </Stack>
  )
}

export default MissionTimelineEmpty
