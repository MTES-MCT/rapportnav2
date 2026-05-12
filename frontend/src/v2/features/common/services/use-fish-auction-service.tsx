import { useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'

export interface FishAuction {
  id: number
  name: string
  facade: string
}

export const useFishAuctionListQuery = () => {
  const fetchFishAuctions = (): Promise<FishAuction[]> =>
    axios.get('fish_auctions').then(response => response.data)

  return useQuery<FishAuction[]>({
    queryKey: ['fish-auctions'],
    queryFn: fetchFishAuctions,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2
  })
}
