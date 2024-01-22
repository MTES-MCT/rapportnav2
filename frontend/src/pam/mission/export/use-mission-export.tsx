import { ApolloError, gql, useQuery } from '@apollo/client'
import { MissionExport } from "../../../types/mission-types.ts";

export const GET_MISSION_EXPORT = gql`
  query GetMissionExport($missionId: ID) {
    missionExport(missionId: $missionId) {
      fileName
      fileContent
    }
  }
`


const useMissionExport = (missionId?: string): { data?: MissionExport; loading: boolean; error?: ApolloError } => {
  const {loading, error, data} = useQuery(GET_MISSION_EXPORT, {
    variables: {missionId}
    // fetchPolicy: 'cache-only'
  })

  // Access the base64-encoded file content

  if (data) {
    debugger
    const fileContent = data.missionExport;
  }
  if (error) {
    debugger
  }


  return {loading, error, data: data?.missionExport}
}

export default useMissionExport
