import { useRouteError } from 'react-router-dom'
import Text from '../features/common/components/ui/text.tsx'
import { Stack } from 'rsuite'
import { THEME } from '@mtes-mct/monitor-ui'

export default function ErrorPage() {
  const error: any = useRouteError()
  console.error(error)

  return (
    <div style={{ width: '100%', height: '100vh', margin: '0 auto' }}>
      <Stack
        direction={'column'}
        justifyContent={'center'}
        alignItems={'center'}
        spacing={'1rem'}
        style={{ width: '50%', height: '100vh', margin: '0 auto' }}
      >
        <Stack.Item alignSelf={'center'}>
          <Text as={'h3'}>
            Une erreur est survenue. Essayez de recharger la page. Si l'erreur persiste, veuillez contacter l'équipe
            RapportNav avec une capture d'écran.
          </Text>
        </Stack.Item>
        <Stack.Item alignSelf={'center'}>
          <Text as={'h4'} color={THEME.color.lightGray}>
            {error ? `Unhandled Exception: ${error}.` : ''}
          </Text>
        </Stack.Item>
      </Stack>
    </div>
  )
}
