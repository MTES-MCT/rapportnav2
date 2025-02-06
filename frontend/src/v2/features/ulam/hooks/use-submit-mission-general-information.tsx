import { Mission2, MissionGeneralInfo2 } from '../../common/types/mission-types.ts'
import { resetDebounceTime } from '../../../store/slices/delay-query-reducer.ts'
import { useDelay } from '../../common/hooks/use-delay.tsx'
import { useStore } from '@tanstack/react-store'
import { store } from '../../../store'
import useCreateMissionMutation from '../services/use-create-mission.tsx'


const useHandleSubmitMissionGeneralInfoHook = (missionId?: string) => {

  const { handleExecuteOnDelay } = useDelay()
  const debounceTime = useStore(store, state => state.delayQuery.debounceTime)
  const mutation = useCreateMissionMutation();

   const handleSubmit = (values, mission: Mission2) => {
    const generalInfos: MissionGeneralInfo2 = {
      id: mission?.generalInfos.id,
      nbHourAtSea: values.initial.nbHourAtSea,
      reinforcementType: values.initial.reinforcementType,
      missionReportType: values.initial.missionReportType,
      isMissionArmed: values.extended.isMissionArmed,
      isAllAgentsParticipating: values.extended.isAllAgentsParticipating,
      isWithInterMinisterialService: values.extended.isWithInterMinisterialService,
      missionTypes: values.initial.missionTypes,
      missionId: Number(missionId),
      serviceId: mission?.generalInfos.serviceId,
      startDateTimeUtc: values.initial.startDateTimeUtc,
      endDateTimeUtc: values.initial.endDateTimeUtc
    }

    handleExecuteOnDelay(async () => {
      await mutation.mutateAsync(generalInfos)
      if (debounceTime !== undefined) resetDebounceTime()
    }, debounceTime)
  }

  return {handleSubmit}
}

export default useHandleSubmitMissionGeneralInfoHook

