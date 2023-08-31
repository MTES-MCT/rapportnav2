import { Icon, SideMenu } from '@mtes-mct/monitor-ui'
import { ReactNode } from 'react'
import useAuth from '../auth/use-auth'
import { Header as CustomHeader } from './header'
import { Container, Content, Footer, Sidebar, Header } from 'rsuite'

interface PageWrapperProps {
  header?: ReactNode
  footer?: ReactNode
  showMenu?: boolean
  children: ReactNode
}

const PageWrapper: React.FC<PageWrapperProps> = ({ children, header, footer, showMenu }) => {
  const { isAuthenticated } = useAuth()
  return (
    <Container style={{ minHeight: '100vh' }}>
      <Header>{!!header && <>{header}</>}</Header>
      <Container>
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
        <Content style={{ display: 'flex' }}>{children}</Content>
        {footer && <Footer>{footer}</Footer>}
      </Container>
    </Container>
  )
}

PageWrapper.defaultProps = {
  header: <CustomHeader />,
  showMenu: true
}

export default PageWrapper
