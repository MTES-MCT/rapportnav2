import useAuth from '@features/auth/hooks/use-auth'
import { ReactNode } from 'react'
import { Container, Content } from 'rsuite'

interface MissionListPageWrapperProps {
  sidebar: JSX.Element
  header: JSX.Element
  footer: JSX.Element
  children: ReactNode
}

const MissionListPageWrapper: React.FC<MissionListPageWrapperProps> = ({ children, header, footer, sidebar }) => {
  const { isAuthenticated } = useAuth()
  return (
    <Container style={{ minHeight: '100vh', maxHeight: '100vh', overflow: 'hidden' }}>
      {header}
      <Container>
        {isAuthenticated && <>{sidebar}</>}
        <Content
          style={{
            maxHeight: 'calc(100vh - 104px - 50px)',
            overflow: 'hidden',
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
