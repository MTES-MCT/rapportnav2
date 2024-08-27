import React, { useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { useSelector } from 'react-redux';
import { RootState, AppDispatch } from '../redux/store';
import { checkLoginStatus } from '../features/auth/slice.ts';
import PageWrapper from '../ui/page-wrapper.tsx'

const AdminPage: React.FC = () => {
  const dispatch = useDispatch<AppDispatch>();
  const user = useSelector((state: RootState) => state.auth.user);

  useEffect(() => {
    dispatch(checkLoginStatus());
  }, [dispatch]);

  return (
    <PageWrapper>
      Hello admin
    </PageWrapper>
  );
};

export default AdminPage;
