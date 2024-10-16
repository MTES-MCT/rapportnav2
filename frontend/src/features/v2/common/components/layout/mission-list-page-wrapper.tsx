import useAuth from '@features/auth/hooks/use-auth'
import { ReactNode } from 'react'
import { Container, Content } from 'rsuite'

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
  const { isAuthenticated } = useAuth()
  return (
    <Container style={{ minHeight: '100vh', maxHeight: '100vh', overflow: 'hidden' }}>
      {header}
      <Container style={{}}>
        {isAuthenticated && <>{sidebar}</>}
        <Content
          style={{
            maxHeight: 'calc(100vh - 104px - 50px)', // full viewportHeight - headerHeight - extra margin
            overflow: 'auto',
            display: 'flex'
          }}
        >
          {children}
        </Content>
        {footer}
      </Container>
    </Container>
  )
}

export default MissionListPageWrapper
