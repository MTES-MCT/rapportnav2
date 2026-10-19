import { FormikEffect } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import MissionBoundFormikDateRangePicker from '../../../common/components/elements/mission-bound-formik-date-range-picker.tsx'
import StyledTabs, { StyledTabItem } from '../../../common/components/ui/styled-tab.tsx'
import { useFormValidationReporter } from '../../../common/hooks/use-form-validation-reporter.tsx'
import { useMissionFinished } from '../../../common/hooks/use-mission-finished.tsx'
import { useOnlineManager } from '../../../common/hooks/use-online-manager.tsx'
import { MissionAction } from '../../../common/types/mission-action'
import FishingSectorControlConclusion from '../../../sati/components/elements/fishing-sector-control-conclusion.tsx'
import FishingSectorControlFiles from '../../../sati/components/elements/fishing-sector-control-files.tsx'
import FishingSectorControlInfos from '../../../sati/components/elements/fishing-sector-control-infos.tsx'
import FishingSectorControlProduct from '../../../sati/components/elements/fishing-sector-control-product.tsx'
import { useSati } from '../../../sati/hooks/use-sati.tsx'
import { useMissionActionGenericControl } from '../../hooks/use-mission-action-generic-control.tsx'
import { ActionControlInput } from '../../types/action-type.ts'
import { getControlSectorFishingSchema } from '../../validation-schema/control-sector-fishing.ts'

const HIDE_ITEMS = ['propeche', 'files']

const ITEMS: StyledTabItem[] = [
  {
    key: 'infos',
    title: `Infos sur l'établissement`,
    component: FishingSectorControlInfos
  },
  {
    key: 'propeche',
    title: 'Produits de la pêche',
    component: FishingSectorControlProduct
  },
  {
    key: 'files',
    title: 'Obligations déclaratives',
    component: FishingSectorControlFiles
  },
  {
    key: 'conclusion',
    title: 'Conclusions',
    component: FishingSectorControlConclusion
  }
]

const MissionActionItemSectorFishingControl: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  const { isSatiEnabled } = useSati()
  const isMissionFinished = useMissionFinished(action.ownerId)

  const { isOnline } = useOnlineManager()
  const { onFormError } = useFormValidationReporter()
  const { initValue, handleSubmit, validationSchema } = useMissionActionGenericControl(
    action,
    onChange,
    getControlSectorFishingSchema(isMissionFinished),
    false,
    [
      'incidentDuringOperation',
      'isControlDuringSecurityDay',
      'isWithinDepartment',
      'hasDivingDuringOperation',
      'isSeizureSleepingFishingGear',
      'signature',
      'isOutOfUnit'
    ]
  )

  return (
    <form style={{ width: '100%' }}>
      {initValue && (
        <Formik
          validateOnMount={true}
          onSubmit={handleSubmit}
          validateOnChange={true}
          initialValues={initValue}
          enableReinitialize={true}
          validationSchema={validationSchema}
        >
          {formik => (
            <>
              <FormikEffect
                onError={onFormError}
                onChange={async nextValues => handleSubmit(nextValues as ActionControlInput)}
              />
              <Stack
                direction="column"
                spacing="1rem"
                alignItems="flex-start"
                style={{ width: '100%' }}
                data-testid={'action-generic-control'}
              >
                <Stack.Item grow={1}>
                  <MissionBoundFormikDateRangePicker
                    isLight={true}
                    disabled={!isOnline}
                    missionId={action.ownerId}
                    title={
                      isOnline
                        ? ''
                        : "Non disponible hors ligne, il est nécessaire d'être synchronisé avec les centres pour saisir/modifier cette donnée."
                    }
                  />
                </Stack.Item>

                <Stack.Item style={{ width: '100%' }}>
                  <StyledTabs
                    defaultActiveKey={ITEMS[0].key}
                    items={ITEMS.filter(i => isSatiEnabled || !HIDE_ITEMS.includes(i.key))}
                    params={{ controlsToComplete: action.controlsToComplete, values: formik.values, formik }}
                  />
                </Stack.Item>
              </Stack>
            </>
          )}
        </Formik>
      )}
    </form>
  )
}
export default MissionActionItemSectorFishingControl
