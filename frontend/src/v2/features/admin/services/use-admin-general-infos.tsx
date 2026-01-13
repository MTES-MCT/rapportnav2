import { useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import { User } from '../../common/types/user.ts'

const useGeneralInfosListQuery = () => {
  const fetchGeneralInfos = (): Promise<User[]> => axios.get(`admin/general-infos`).then(response => response.data)

  const query = useQuery<User[]>({
    queryKey: ['admin-general-infos'],
    queryFn: fetchGeneralInfos,
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2,
    staleTime: 0,
    gcTime: 0
  })
  return query
}

export default useGeneralInfosListQuery
