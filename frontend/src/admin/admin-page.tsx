import React, { useEffect } from 'react'
import PageWrapper from '../ui/page-wrapper.tsx'
import { useDispatch, useSelector } from 'react-redux'
import { AppDispatch, RootState } from '../redux/store.ts'
import { checkLoginStatus } from '../features/auth/auth.ts'

const AdminPage: React.FC = () => {

  const dispatch = useDispatch<AppDispatch>();
  const user = useSelector((state: RootState) => state.auth.user);
  useEffect(() => {
    dispatch(checkLoginStatus());
  }, [dispatch]);
  return (
    <PageWrapper>
      Hello {user?.userId}
    </PageWrapper>
  )
}

export default AdminPage
