import { Button, Size } from '@mtes-mct/monitor-ui'
import { useNavigate, useRouteError } from 'react-router-dom'
import { Stack } from 'rsuite'
import Text from '../features/common/components/ui/text.tsx'

export default function ErrorPage() {
  const navigate = useNavigate()
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
          {error.status === 404 && (
            <Text as={'h1'} fontStyle="normal" weight="bold">
              PAGE NON TROUVÉE
            </Text>
          )}
          {error.status === 403 && <Text as={'h1'}> PAGE INTERDITE</Text>}
          {![403, 404].includes(error.status) && (
            <Text as={'h1'}>
              <Text as={'h3'}>
                Une erreur est survenue. Essayez de recharger la page. Si l'erreur persiste, veuillez contacter l'équipe
                RapportNav avec une capture d'écran.
              </Text>
            </Text>
          )}
        </Stack.Item>
        <Stack.Item>
          <Button size={Size.LARGE} onClick={() => navigate('/', { replace: true })} style={{ marginTop: 24 }}>
            {`Retour à l'acceuil`}
          </Button>
        </Stack.Item>
      </Stack>
    </div>
  )
}
