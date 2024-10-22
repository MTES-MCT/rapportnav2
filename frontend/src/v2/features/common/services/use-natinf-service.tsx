import { ApolloError, gql, useQuery } from '@apollo/client'
import { Natinf } from '@common/types/infraction-types'

export const GET_NATINFS = gql`
  query GetNatinfs {
    natinfs {
      infraction
      natinfCode
    }
  }
`

const useNatinfListQuery = (): { data?: Natinf[]; loading: boolean; error?: ApolloError } => {
  const { loading, error, data } = useQuery(GET_NATINFS, {
    // fetchPolicy: 'cache-only'
  })

  return { loading, error, data: data?.natinfs }
}

export default useNatinfListQuery
