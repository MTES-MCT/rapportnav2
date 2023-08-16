import { Icon, SideMenu } from '@mtes-mct/monitor-ui'
import { ReactNode } from 'react'
import useAuth from '../auth/use-auth'
import { Header as CustomHeader } from './header'
import { Container, Content, Footer, Sidebar, Header } from 'rsuite'

interface PageWrapperProps {
  header?: ReactNode
  children: ReactNode
}

const PageWrapper: React.FC<PageWrapperProps> = ({ children, header }) => {
  const { isAuthenticated } = useAuth()
  return (
    <Container>
      <Header>{!!header && <>{header}</>}</Header>
      <Container>
        {isAuthenticated && (
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
        <Content>{children}</Content>
      </Container>
      <Footer></Footer>
    </Container>
  )
}

PageWrapper.defaultProps = {
  header: <CustomHeader />
}

export default PageWrapper
