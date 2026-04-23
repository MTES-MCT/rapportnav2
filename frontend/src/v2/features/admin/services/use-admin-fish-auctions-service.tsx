import { useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import { AdminFishAuction } from '../types/admin-fish-auction-types.ts'

const useAdminFishAuctionListQuery = () => {
  const fetchAdminFishAuctions = (): Promise<AdminFishAuction[]> =>
    axios.get(`admin/fish_auctions`).then(response => response.data)

  const query = useQuery<AdminFishAuction[]>({
    queryKey: ['admin-fish-auctions'],
    queryFn: fetchAdminFishAuctions,
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2
  })
  return query
}

export default useAdminFishAuctionListQuery
