import { useStore } from '@tanstack/react-store'
import React from 'react'
import { store } from '../../../../../store'
import { resetDebounceTime } from '../../../../../store/slices/delay-query-reducer.ts'
import { useDelay } from '../../../../common/hooks/use-delay.tsx'
import { MissionGeneralInfo2 } from '../../../../common/types/mission-types.ts'
import MissionGeneralInformationFormPam from './mission-general-information-form-pam.tsx'
import useUpdateGeneralInfo from '../../../../common/services/use-update-generalInfo.tsx'

type MissionGeneralInformationPamProps = {
  missionId: number
  generalInfos?: MissionGeneralInfo2
}

const MissionGeneralInformationPamBody: React.FC<MissionGeneralInformationPamProps> = ({ missionId, generalInfos }) => {
  const { handleExecuteOnDelay } = useDelay()
  const debounceTime = useStore(store, state => state.delayQuery.debounceTime)
  const mutation = useUpdateGeneralInfo(missionId)

  const onChange = async (newGeneralInfo: MissionGeneralInfo2): Promise<void> => {
    await handleExecuteOnDelay(async () => {
      await mutation.mutateAsync(newGeneralInfo)
      if (debounceTime !== undefined) resetDebounceTime()
    }, debounceTime)
  }
  if (!generalInfos) return
  return <MissionGeneralInformationFormPam generalInfo2={generalInfos} onChange={onChange} />
}

export default MissionGeneralInformationPamBody
