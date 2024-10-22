import { ControlAdministrative, ControlType } from '@common/types/control-types.ts'
import { FormikEffect, FormikTextarea, THEME } from '@mtes-mct/monitor-ui'
import { FieldProps, Formik } from 'formik'
import { FC } from 'react'
import { Panel, Stack } from 'rsuite'
import { MissionActionFormikToogleUnitShouldConfirm } from '../../../mission-action/components/ui/mission-action-formik-toogle-unit-has-confirm'
import { ControlAdministrativeInput, useControlAdministrative } from '../../hooks/use-control-administrative'
import MissionActionControlFormikMultiRadio from '../ui/mission-control-fomik-multi-radio'
import { MissionControlFormikCheckBoxTitle } from '../ui/mission-control-title-checkbox'

type MissionControlAdministrativeFormProps = {
  name: string
  unitShouldConfirm?: boolean
  shouldCompleteControl?: boolean
  fieldFormik: FieldProps<ControlAdministrative>
}

const MissionControlAdministrativeForm: FC<MissionControlAdministrativeFormProps> = ({
  name,
  fieldFormik,
  unitShouldConfirm,
  shouldCompleteControl
}) => {
  const { radios, controlTypeLabel, initValue, handleSubmit } = useControlAdministrative(name, fieldFormik)

  return (
    <Panel
      header={
        <MissionControlFormikCheckBoxTitle
          text={controlTypeLabel}
          shouldComplete={true}
          name={`unitHasConfirmed`}
          //shouldComplete={isRequired}
          //checked={controlIsChecked}
          //onChange={(isChecked: boolean) => console.log('handleToggleControl')}
        />
      }
      style={{ backgroundColor: THEME.color.white, borderRadius: 0 }}
    >
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit}>
          <>
            <FormikEffect onChange={nextValues => handleSubmit(nextValues as ControlAdministrativeInput)} />
            <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%' }}>
              <Stack.Item style={{ width: '100%' }}>
                <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%' }}>
                  <Stack.Item style={{ width: '100%' }}>
                    <MissionActionFormikToogleUnitShouldConfirm name={`unitHasConfirmed`} />
                  </Stack.Item>
                  <Stack.Item style={{ width: '100%' }}>
                    <MissionActionControlFormikMultiRadio control={ControlType.ADMINISTRATIVE} radios={radios ?? []} />
                  </Stack.Item>
                  <Stack.Item style={{ width: '100%', paddingTop: 4 }}>
                    <FormikTextarea
                      style={{ width: '100%' }}
                      name={`observations`}
                      label="Observations (hors infraction) sur les pièces administratives"
                    />
                  </Stack.Item>
                </Stack>
              </Stack.Item>
            </Stack>
          </>
        </Formik>
      )}
    </Panel>
  )
}

export default MissionControlAdministrativeForm

/**
 * <Stack.Item style={{ width: '100%' }}>
     <ControlInfraction
      controlId={data?.id}
      infractions={data?.infractions}
      controlType={ControlType.ADMINISTRATIVE}
      />
</Stack.Item>
 */
