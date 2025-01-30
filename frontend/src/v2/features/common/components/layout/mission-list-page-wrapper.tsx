import useAuth from '@features/auth/hooks/use-auth'
import {ReactNode} from 'react'
import {Container, Content, Stack} from 'rsuite'

interface MissionListPageWrapperProps {
  sidebar: JSX.Element
  header: JSX.Element
  footer: JSX.Element
  showMenu?: boolean
  children: ReactNode
}

const MissionListPageWrapper: React.FC<MissionListPageWrapperProps> = ({
                                                                         children,
                                                                         header,
                                                                         footer,
                                                                         showMenu,
                                                                         sidebar
                                                                       }) => {
  const {isAuthenticated} = useAuth()
  return (
    <Container style={{minHeight: '100vh', maxHeight: '100vh', overflow: 'hidden'}}>
      <Stack direction={'column'} style={{width: '100%'}}>
        <Stack.Item style={{width: '100%'}}>
          {header}

        </Stack.Item>
        <Stack.Item style={{width: '100%'}}>
          {footer}

        </Stack.Item>
        <Stack.Item style={{width: '100%'}}>
          <Container style={{}}>
            {isAuthenticated && <>{sidebar}</>}
            <Content
              style={{
                maxHeight: 'calc(100vh - 104px - 50px)', // full viewportHeight - headerHeight - extra margin
                overflow: 'hidden',
                display: 'flex'
              }}
            >
              {children}
            </Content>
          </Container>
        </Stack.Item>

      </Stack>

    </Container>
  )
}

export default MissionListPageWrapper
