import { useStore } from '@tanstack/react-store'
import React from 'react'
import { store } from '../../../../store'
import { resetDebounceTime } from '../../../../store/slices/delay-query-reducer.ts'
import { useDelay } from '../../../common/hooks/use-delay.tsx'
import useUpdateGeneralInfo from '../../../common/services/use-update-general-info.tsx'
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
  const mutation = useUpdateGeneralInfo()
  const debounceTime = useStore(store, state => state.delayQuery.debounceTime)

  const onChange = async (newGeneralInfo: MissionGeneralInfo2): Promise<void> => {
    await handleExecuteOnDelay(async () => {
      await mutation.mutateAsync({ missionId, generalInfo: newGeneralInfo })
      if (debounceTime !== undefined) resetDebounceTime()
    }, debounceTime)
  }

  if (!generalInfos) return
  return <MissionGeneralInformationForm generalInfo2={generalInfos} onChange={onChange} />
}

export default MissionGeneralInformationUlamBody
