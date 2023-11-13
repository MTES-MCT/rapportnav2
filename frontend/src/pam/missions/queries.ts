import { gql } from '@apollo/client'

export const GET_MISSIONS = gql`
  query GetMissions {
    missions(userId: "book-1") {
      id
      missionSource
      startDateTimeUtc
      endDateTimeUtc
    }
  }
`
