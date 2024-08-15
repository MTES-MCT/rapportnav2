import { FC } from 'react'
import PatchableMonitorObservations from '../../forms/patchable-monitor-observations.tsx'
import { logSoftError } from '@mtes-mct/monitor-ui'
import usePatchActionFish from './use-patch-action-fish.tsx'

export interface ActionFishObservationsUnitProps {
  missionId?: string
  actionId?: string
  observationsByUnit?: string
  label: string
}

const ActionFishObservationsUnit: FC<ActionFishObservationsUnitProps> = ({
  missionId,
  actionId,
  observationsByUnit,
  label
}) => {
  const [patch, { error }] = usePatchActionFish()

  const handleSubmit = async (observationsByUnit: string) => {
    const output = await patch({
      variables: {
        action: {
          missionId,
          actionId,
          observationsByUnit
        }
      }
    })

    if (!output?.data?.patchActionFish || error) {
      logSoftError({
        isSideWindowError: false,
        message: `patchActionFish for action=${actionId} for observationsByUnit returns null`,
        userMessage: `Le champ observations n'a pas pu être sauvegardé. Réessayez ou contactez l'équipe RapportNav.`
      })
    }
  }

  return (
    <PatchableMonitorObservations
      label={label}
      onSubmit={handleSubmit}
      observationsByUnit={observationsByUnit}
      isLight={true}
    />
  )
}

export default ActionFishObservationsUnit
