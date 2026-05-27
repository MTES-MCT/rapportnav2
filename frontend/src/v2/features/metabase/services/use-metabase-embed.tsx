import { useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'

interface MetabaseEmbedResponse {
  iframeUrl: string
}

const useMetabaseEmbedQuery = () => {
  const fetchMetabaseEmbedUrl = (): Promise<MetabaseEmbedResponse> =>
    axios.get('metabase/embed-url').then(response => response.data)

  return useQuery<MetabaseEmbedResponse>({
    queryKey: ['metabase-embed-url'],
    queryFn: fetchMetabaseEmbedUrl,
    staleTime: 0,
    gcTime: 0,
    refetchInterval: 8 * 60 * 1000, // token expires after 10min
    refetchOnWindowFocus: true,
    retry: 2
  })
}

export default useMetabaseEmbedQuery
