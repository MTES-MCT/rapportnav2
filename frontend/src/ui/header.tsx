import React from 'react';
import { useDispatch, useSelector } from 'react-redux'
import styled from 'styled-components';
import { FlexboxGrid } from 'rsuite';
import { Accent, Button } from '@mtes-mct/monitor-ui';
import { performLogout } from '../features/auth/slice'
import republique from '../assets/images/republique.svg';
import { useApolloClient } from '@apollo/client'
import { useNavigate } from 'react-router-dom'
import { RootState } from '../redux/store.ts'

interface HeaderProps {}

const StyledHeader = styled.div`
  height: 104px;
  background: var(--white-ffffff-) 0% 0% no-repeat padding-box;
  background: #ffffff 0% 0% no-repeat padding-box;
  box-shadow: 0px -3px 10px #00000033;
  opacity: 1;
  padding: 0.5rem;
`;

export const Header: React.FC<HeaderProps> = () => {
  const isLoggedIn = useSelector((state: RootState) => state.auth.user);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const apolloClient = useApolloClient();

  const handleLogout = () => {
    dispatch(performLogout({ navigate, apolloClient }));
  };


  return (
    <StyledHeader>
      <FlexboxGrid justify="space-between" align="middle" style={{ height: '100%' }}>
        <FlexboxGrid.Item>
          <FlexboxGrid justify="space-between" align="middle" style={{ height: '100%' }}>
            <FlexboxGrid.Item>
              <img src={republique} alt="marianne logo" height={80} />
            </FlexboxGrid.Item>
            <FlexboxGrid.Item style={{ marginLeft: '1rem' }}>
              <h3>Rapport Nav</h3>
            </FlexboxGrid.Item>
          </FlexboxGrid>
        </FlexboxGrid.Item>
        {isLoggedIn && (
          <>
            {/* <FlexboxGrid.Item colspan={2}>
              <a target="_blank" href="http://localhost:3000/">
                admin panel
              </a>
            </FlexboxGrid.Item> */}
            <FlexboxGrid.Item colspan={2}>
              <Button accent={Accent.SECONDARY} onClick={handleLogout}>
                Logout
              </Button>
            </FlexboxGrid.Item>
          </>
        )}
      </FlexboxGrid>
    </StyledHeader>
  );
};

export default Header;
