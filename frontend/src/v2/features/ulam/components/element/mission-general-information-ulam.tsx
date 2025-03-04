import { useStore } from '@tanstack/react-store'
import React from 'react'
import { store } from '../../../../store'
import { resetDebounceTime } from '../../../../store/slices/delay-query-reducer.ts'
import { useDelay } from '../../../common/hooks/use-delay.tsx'
import useUpdateGeneralInfo from '../../../common/services/use-update-generalInfo.tsx'
import { Mission2, MissionGeneralInfo2 } from '../../../common/types/mission-types.ts'
import MissionGeneralInformationForm from './mission-general-information-form-ulam.tsx'

type MissionGeneralInformationUlamProps = {
  mission?: Mission2
}

const MissionGeneralInformationUlam: React.FC<MissionGeneralInformationUlamProps> = ({ mission }) => {
  const { handleExecuteOnDelay } = useDelay()
  const debounceTime = useStore(store, state => state.delayQuery.debounceTime)
  const mutation = useUpdateGeneralInfo(mission?.id)

  const onChange = async (newGeneralInfo: MissionGeneralInfo2): Promise<void> => {
    await handleExecuteOnDelay(async () => {
      await mutation.mutateAsync(newGeneralInfo)
      if (debounceTime !== undefined) resetDebounceTime()
    }, debounceTime)
  }
  if (!mission?.generalInfos) return
  return <MissionGeneralInformationForm generalInfo2={mission.generalInfos} onChange={onChange} />
}

export default MissionGeneralInformationUlam
