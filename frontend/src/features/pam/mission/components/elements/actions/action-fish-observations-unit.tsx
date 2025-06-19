import { FC } from 'react'
import PatchableMonitorObservations from '../../../../../common/components/elements/patchable-monitor-observations.tsx'
import { logSoftError } from '@mtes-mct/monitor-ui'
import usePatchActionFish from '../../../hooks/use-patch-action-fish.tsx'

export interface ActionFishObservationsUnitProps {
  missionId?: string
  actionId?: string
  startDateTimeUtc?: string
  endDateTimeUtc?: string
  observationsByUnit?: string
  label: string
}

const ActionFishObservationsUnit: FC<ActionFishObservationsUnitProps> = ({
  missionId,
  actionId,
  startDateTimeUtc,
  endDateTimeUtc,
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
          startDateTimeUtc,
          endDateTimeUtc,
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
