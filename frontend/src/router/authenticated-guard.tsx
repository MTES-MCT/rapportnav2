import React, { ReactNode, useEffect, useState } from 'react'
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom'
import { RootState, AppDispatch } from '../redux/store';
import { checkLoginStatus } from '../features/auth/slice.ts';
import * as Sentry from '@sentry/react'

interface AuthenticatedGuardProps {
  children: ReactNode;
  adminOnly?: boolean;

}

const AuthenticatedGuard: React.FC<AuthenticatedGuardProps> = ({ children, adminOnly = false }) => {
  const dispatch = useDispatch<AppDispatch>();
  const user = useSelector((state: RootState) => state.auth.user);
  const [loading, setLoading] = useState(true);
  let navigate = useNavigate()

  useEffect(() => {
    const verifyLoginStatus = async () => {
      dispatch(checkLoginStatus())
      setLoading(false);
    };
    verifyLoginStatus();
  }, [dispatch]);

  if ( loading ) {
    return <div>Chargement...</div>;
  }
  if (user && adminOnly) {
    if (user?.roles.includes('ADMIN')) {
      return <>{children}</>;
    }

    const error = new Error("Access denied: User does not have the necessary admin privileges.");
    Sentry.captureException(error)
    throw error;
  }

  if (user && !adminOnly) {
    return <>{children}</>;
  }

  navigate('/login', { replace: true })
};

export default AuthenticatedGuard;
