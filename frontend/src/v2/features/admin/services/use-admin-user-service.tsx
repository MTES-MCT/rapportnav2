import { useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import { User } from '../../common/types/user.ts'

const useUserListQuery = () => {
  const fetchUsers = (): Promise<User[]> => axios.get(`admin/users`).then(response => response.data)

  const query = useQuery<User[]>({
    queryKey: ['admin-users'],
    queryFn: fetchUsers,
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2
  })
  return query
}

export default useUserListQuery
