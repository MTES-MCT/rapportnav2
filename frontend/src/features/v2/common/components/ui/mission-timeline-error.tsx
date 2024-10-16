import { THEME } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import Text from '../../../../common/components/ui/text'

type MissionTimelineErrorProps = {
  error: { message: string } & unknown
}
const MissionTimelineError: React.FC<MissionTimelineErrorProps> = ({ error }) => {
  return (
    <Stack justifyContent={'center'} alignItems={'center'} style={{ height: '100%' }} data-testid={'timeline-error'}>
      <Stack.Item alignSelf={'center'}>
        <Text as={'h3'} color={THEME.color.maximumRed}>
          Une erreur s'est produite lors du chargement de la timeline.
          <br />
          Si le problème persiste, veuillez contacter l'équipe RapportNav.
        </Text>
        <Text as={'h3'} color={THEME.color.lightGray}>
          Erreur: {error.message}
        </Text>
      </Stack.Item>
    </Stack>
  )
}

export default MissionTimelineError
