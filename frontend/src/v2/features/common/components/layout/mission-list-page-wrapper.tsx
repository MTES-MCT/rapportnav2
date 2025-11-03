import useAuth from '@features/auth/hooks/use-auth'
import { THEME } from '@mtes-mct/monitor-ui'
import { ReactNode } from 'react'
import { Container, Content, Stack } from 'rsuite'

interface MissionListPageWrapperProps {
  sidebar: JSX.Element
  header: JSX.Element
  footer?: JSX.Element
  children: ReactNode
}

const MissionListPageWrapper: React.FC<MissionListPageWrapperProps> = ({ children, header, footer, sidebar }) => {
  const { isAuthenticated } = useAuth()
  return (
    <Container style={{ minHeight: '100vh', maxHeight: '100vh', overflow: 'hidden' }}>
      <Stack direction={'column'} style={{ width: '100%', height: '100vh' }}>
        <Stack.Item style={{ width: '100%' }}>{header}</Stack.Item>

        <Stack.Item style={{ width: '100%' }} flex={1}>
          <Container style={{ height: '100%' }}>
            {isAuthenticated && <>{sidebar}</>}
            <Content
              style={{
                maxHeight: 'calc(100vh - 104px - 50px)', // full viewportHeight - headerHeight - extra margin
                overflow: 'scroll',
                display: 'flex'
              }}
            >
              {children}
            </Content>
          </Container>
        </Stack.Item>

        {footer && (
          <Stack.Item style={{ width: '100%', minHeight: '50px', borderTop: `2px solid ${THEME.color.lightGray}` }}>
            {footer}
          </Stack.Item>
        )}
      </Stack>
    </Container>
  )
}

export default MissionListPageWrapper
