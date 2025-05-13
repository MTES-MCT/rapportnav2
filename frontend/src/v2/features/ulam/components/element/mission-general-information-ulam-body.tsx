import { useStore } from '@tanstack/react-store'
import React from 'react'
import { store } from '../../../../store/index.ts'
import { resetDebounceTime } from '../../../../store/slices/delay-query-reducer.ts'
import { useDelay } from '../../../common/hooks/use-delay.tsx'
import useUpdateGeneralInfo from '../../../common/services/use-update-generalInfo.tsx'
import { MissionGeneralInfo2 } from '../../../common/types/mission-types.ts'
import MissionGeneralInformationForm from './mission-general-information-ulam-form.tsx'

type MissionGeneralInformationUlamBodyProps = {
  missionId?: string
  generalInfos?: MissionGeneralInfo2
}

const MissionGeneralInformationUlamBody: React.FC<MissionGeneralInformationUlamBodyProps> = ({
  missionId,
  generalInfos
}) => {
  const { handleExecuteOnDelay } = useDelay()
  const mutation = useUpdateGeneralInfo(missionId)
  const debounceTime = useStore(store, state => state.delayQuery.debounceTime)

  const onChange = async (newGeneralInfo: MissionGeneralInfo2): Promise<void> => {
    await handleExecuteOnDelay(async () => {
      console.log(newGeneralInfo)
      await mutation.mutateAsync(newGeneralInfo)
      if (debounceTime !== undefined) resetDebounceTime()
    }, debounceTime)
  }

  if (!generalInfos) return
  return <MissionGeneralInformationForm generalInfo2={generalInfos} onChange={onChange} />
}

export default MissionGeneralInformationUlamBody
