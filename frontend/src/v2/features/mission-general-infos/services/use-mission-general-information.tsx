import { useStore } from '@tanstack/react-store'
import { store } from '../../../store'
import useGetMissionQuery from '../../common/services/use-mission'
import useGetUserQuery from '../../common/services/use-user'
import { MissionGeneralInfo2 } from '../../common/types/mission-types'
import { useMissionGeneralInfos } from '../hooks/use-mission-general-infos'

type UseMissionGeneralInfoQueryResult<T, M> = {
  data: T
  error: M | null
  isError: boolean
  isPending: boolean
  isLoading: boolean
  isLoadingError: boolean
  isRefetchError: boolean
  isSuccess: boolean
}

const useGetMissionGeneralInformationQuery = (
  missionId?: string
): UseMissionGeneralInfoQueryResult<MissionGeneralInfo2, Error> => {
  const userId = useStore(store, state => state.user?.id)
  const { data: user } = useGetUserQuery(userId)
  const { getGeneralInfos } = useMissionGeneralInfos()
  const {
    error,
    isError,
    isPending,
    isLoading,
    isSuccess,
    data: mission,
    isLoadingError,
    isRefetchError
  } = useGetMissionQuery(missionId)
  return {
    error,
    isError,
    isPending,
    isLoading,
    isLoadingError,
    isRefetchError,
    isSuccess,
    data: getGeneralInfos(mission, user?.controlUnitId)
  }
}

export default useGetMissionGeneralInformationQuery
