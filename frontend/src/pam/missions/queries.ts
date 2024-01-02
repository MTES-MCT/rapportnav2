import { gql } from '@apollo/client'

export const GET_MISSIONS = gql`
  query GetMissions {
    missions {
      id
      missionSource
      startDateTimeUtc
      endDateTimeUtc
      openBy
    }
  }
`
