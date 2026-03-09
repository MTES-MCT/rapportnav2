import { useQuery, UseQueryResult } from '@tanstack/react-query'
import axios from '../../../../query-client/axios'
import { MissionExport } from '../types/mission-export-types'

const fetchDummyPdf = async (): Promise<MissionExport> => {
  const response = await axios.get('missions/export/dummy-pdf')
  return response.data
}

const useDummyPdfExport = (): UseQueryResult<MissionExport, Error> => {
  return useQuery({
    queryKey: ['dummy-pdf-export'],
    queryFn: fetchDummyPdf,
    enabled: false
  })
}

export default useDummyPdfExport
