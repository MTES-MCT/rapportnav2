import { Accent, Button, Icon, IconButton, Size } from '@mtes-mct/monitor-ui'
import { useSelector } from '@tanstack/react-store'
import { useNavigate } from 'react-router-dom'
import { FlexboxGrid, Stack } from 'rsuite'
import styled from 'styled-components'
import republique from '../../../../../assets/images/republique.svg'
import useAuth from '../../../../features/auth/hooks/use-auth'
import { store } from '../../../../store'
import { RoleType } from '../../types/role-type'

const StyledHeader = styled.div`
  height: 104px;
  background: var(--white-ffffff-) 0% 0% no-repeat padding-box;
  background: #ffffff 0% 0% no-repeat padding-box;
  box-shadow: 0px -3px 10px #00000033;
  opacity: 1;
  padding: 0.5rem;
`
type MissionListHeaderWrapperProps = {
  title: JSX.Element
  actions?: JSX.Element
}

export const MissionListPageHeaderWrapper: React.FC<MissionListHeaderWrapperProps> = ({
  title,
  actions
}: MissionListHeaderWrapperProps) => {
  const { hasRoles } = useAuth()
  const navigate = useNavigate()
  const user = useSelector(store, state => state.user)
  const { isAuthenticated, logout } = useAuth()
  const handleLogout = () => logout()

  return (
    <StyledHeader>
      <FlexboxGrid justify="space-between" align="middle" style={{ height: '100%', paddingLeft: 16, paddingRight: 16 }}>
        <FlexboxGrid.Item>
          <FlexboxGrid justify="space-between" align="middle" style={{ height: '100%' }}>
            <FlexboxGrid.Item>
              <img src={republique} alt="marianne logo" height={80} />
            </FlexboxGrid.Item>
            <FlexboxGrid.Item style={{ marginLeft: '1rem' }}>{title}</FlexboxGrid.Item>
          </FlexboxGrid>
        </FlexboxGrid.Item>
        <FlexboxGrid.Item>
          {isAuthenticated && (
            <Stack direction="row" style={{ height: '100%' }}>
              {actions}
              {hasRoles([RoleType.ADMIN]) && (
                <IconButton
                  size={Size.LARGE}
                  title="Admin Dashboard"
                  accent={Accent.TERTIARY}
                  Icon={Icon.GroupPerson}
                  onClick={() => navigate('/admin')}
                  style={{ marginLeft: 8, marginRight: 4 }}
                />
              )}
              {hasRoles([RoleType.MANAGER_PAM, RoleType.MANAGER_ULAM]) && (
                <Button
                  accent={Accent.SECONDARY}
                  onClick={() => navigate('/manage')}
                  Icon={Icon.Settings}
                  style={{ marginLeft: 8, marginRight: 4 }}
                >
                  Gestion de l'unité
                </Button>
              )}
              <Button
                accent={Accent.SECONDARY}
                onClick={handleLogout}
                Icon={Icon.Logout}
                style={{ marginLeft: 8, marginRight: 4 }}
              >
                Se déconnecter
              </Button>
            </Stack>
          )}
        </FlexboxGrid.Item>
      </FlexboxGrid>
    </StyledHeader>
  )
}

export default MissionListPageHeaderWrapper
