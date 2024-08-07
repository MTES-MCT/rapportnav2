import { FC } from 'react'
import usePatchActionEnv from './use-patch-action-env.tsx'
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
    const output = await patch({
      variables: {
        action: {
          missionId,
          actionId,
          observationsByUnit
        }
      }
    })

    if (!output?.data?.patchActionEnv || error) {
      // if (!output.data.patchActionEnv || error) {
      console.log('DFDSFDSFDSFDSFSDFSDFSDFDSFSDFSD')
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
