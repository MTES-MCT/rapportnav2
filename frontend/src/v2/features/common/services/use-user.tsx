import { useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios'
import { User } from '../types/user'

const useGetUserQuery = (userId?: number) => {
  const fetchUser = (): Promise<User> => axios.get(`users/${userId}`).then(response => response.data)

  const query = useQuery<User>({
    queryKey: ['user', userId],
    enabled: !!userId,
    queryFn: fetchUser,
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2
  })
  return query
}

export default useGetUserQuery
