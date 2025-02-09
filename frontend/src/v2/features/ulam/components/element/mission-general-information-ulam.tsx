import { useStore } from '@tanstack/react-store'
import React from 'react'
import { store } from '../../../../store'
import { resetDebounceTime } from '../../../../store/slices/delay-query-reducer.ts'
import { useDelay } from '../../../common/hooks/use-delay.tsx'
import { Mission2, MissionGeneralInfo2 } from '../../../common/types/mission-types.ts'
import useCreateMissionMutation from '../../services/use-create-mission.tsx'
import MissionGeneralInformationForm from './mission-general-information-form-ulam.tsx'

type MissionGeneralInformationUlamProps = {
  mission?: Mission2
}

const MissionGeneralInformationUlam: React.FC<MissionGeneralInformationUlamProps> = ({ mission }) => {
  const { handleExecuteOnDelay } = useDelay()
  const debounceTime = useStore(store, state => state.delayQuery.debounceTime)
  const mutation = useCreateMissionMutation()

  const onChange = async (newGeneralInfo: MissionGeneralInfo2): Promise<void> => {
    await handleExecuteOnDelay(async () => {
      // await mutation.mutateAsync(newGeneralInfo)
      console.log(newGeneralInfo)
      if (debounceTime !== undefined) resetDebounceTime()
    }, debounceTime)
  }
  if (!mission?.generalInfos) return
  return <MissionGeneralInformationForm generalInfo2={mission.generalInfos} onChange={onChange} />
}

export default MissionGeneralInformationUlam
