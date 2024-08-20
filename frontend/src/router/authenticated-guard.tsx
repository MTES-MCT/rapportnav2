import React, { ReactNode, useEffect } from 'react'
import { useSelector, useDispatch } from 'react-redux';
import { Navigate } from 'react-router-dom';
import { RootState, AppDispatch } from '../redux/store';
import { checkLoginStatus } from '../features/auth/authSlice';
import { LOGIN_PATH, PAM_HOME_PATH } from './router';

interface AuthenticatedGuardProps {
  children: ReactNode
}

const AuthenticatedGuard: React.FC<AuthenticatedGuardProps> = ({ children }) => {
  const dispatch = useDispatch<AppDispatch>();
  const isLoggedIn = useSelector((state: RootState) => !!state.auth.user);

  useEffect(() => {
    dispatch(checkLoginStatus());
  }, [dispatch]);

  if (isLoggedIn) {
    return <Navigate to={PAM_HOME_PATH} />;
  }

  return children;
};

export default AuthenticatedGuard;
