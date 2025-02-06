import React from 'react'
import { useDelay } from '../../../common/hooks/use-delay.tsx'
import {
  Mission2, MissionGeneralInfo2,
} from '../../../common/types/mission-types.ts'
import { resetDebounceTime } from '../../../../store/slices/delay-query-reducer.ts'
import { useStore } from '@tanstack/react-store'
import { store } from '../../../../store'
import useCreateMissionMutation from '../../services/use-create-mission.tsx'
import MissionGeneralInformationForm from '../mission-general-information-form.tsx'

type MissionGeneralInformationUlamProps = {
  mission?: Mission2
}


const MissionGeneralInformationUlam: React.FC<MissionGeneralInformationUlamProps> = ({ mission}) => {


  const { handleExecuteOnDelay } = useDelay()
  const debounceTime = useStore(store, state => state.delayQuery.debounceTime)
  const mutation = useCreateMissionMutation();

  const onChange = async (newGeneralInfo: MissionGeneralInfo2): Promise<void> => {
    console.log(newGeneralInfo)
    await handleExecuteOnDelay(async () => {
     // await mutation.mutateAsync(newGeneralInfo)
      if (debounceTime !== undefined) resetDebounceTime()
    }, debounceTime)
  }


  return (
    <MissionGeneralInformationForm generalInfo2={mission?.generalInfos} onChange={onChange}/>
  )
}

export default MissionGeneralInformationUlam
