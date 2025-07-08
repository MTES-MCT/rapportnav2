import { Icon, SideMenu } from '@mtes-mct/monitor-ui'
import { ReactNode } from 'react'
import useAuth from '../../../auth/hooks/use-auth.tsx'
import { Header as CustomHeader } from './header.tsx'
import { Container, Content, Footer, Header, Sidebar } from 'rsuite'

interface PageWrapperProps {
  header?: ReactNode
  footer?: ReactNode
  showMenu?: boolean
  children: ReactNode
}

const PageWrapper: React.FC<PageWrapperProps> = ({ children, header = <CustomHeader />, footer, showMenu = true }) => {
  const { isAuthenticated } = useAuth()

  return (
    <Container style={{ minHeight: '100vh', maxHeight: '100vh', overflow: 'hidden' }}>
      <Header>{!!header && <>{header}</>}</Header>
      <Container style={{}}>
        {isAuthenticated && (
          <>
            {showMenu && (
              <Sidebar style={{ flex: 0, width: '64px' }}>
                <SideMenu>
                  <SideMenu.Button
                    Icon={Icon.MissionAction}
                    isActive
                    // onClick={() => selectTab(generatePath(sideWindowPaths.MISSIONS))}
                    title="missions"
                  />
                </SideMenu>
              </Sidebar>
            )}
          </>
        )}
        <Content
          style={{
            maxHeight: 'calc(100vh - 104px - 50px)', // full viewportHeight - headerHeight - extra margin
            overflow: 'auto',
            display: 'flex'
          }}
        >
          {children}
        </Content>
        {footer && <Footer>{footer}</Footer>}
      </Container>
    </Container>
  )
}

export default PageWrapper
