import { FC } from 'react'
import usePatchActionEnv, { PatchActionEnvInput } from './use-patch-action-env.tsx'
import PatchableMonitorObservations from '../../forms/patchable-monitor-observations.tsx'
import { logSoftError } from '@mtes-mct/monitor-ui'

export interface ActionEnvObservationsUnitProps {
  missionId?: string
  actionId?: string
  observationsByUnit?: string
  label: string
}

const ActionEnvObservationsUnit: FC<ActionEnvObservationsUnitProps> = ({
  missionId,
  actionId,
  observationsByUnit,
  label
}) => {
  // const [patch] = usePatchActionEnv()
  const [patch, { error }] = usePatchActionEnv()

  const handleSubmit = async (observationsByUnit: string) => {
    const input: PatchActionEnvInput = {
      missionId,
      actionId,
      observationsByUnit
    }
    const output = await patch({
      variables: {
        action: input
      }
    })

    if (!output?.data?.patchActionEnv || error) {
      logSoftError({
        isSideWindowError: false,
        message: `patchActionEnv for action=${actionId} for observationsByUnit returns null`,
        userMessage: `Le champs observations n'a pas pu être sauvegardé. Réessayez ou contactez l'équipe RapportNav.`
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

export default ActionEnvObservationsUnit
