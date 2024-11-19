import {
  ControlAdministrative,
  ControlGensDeMer,
  ControlNavigation,
  ControlSecurity,
  ControlType
} from '@common/types/control-types'
import { Label } from '@mtes-mct/monitor-ui'
import { Field, FieldProps } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import MissionControlAdministrativeForm from './mission-control-administrative-form'
import MissionControlGensDeMerForm from './mission-control-gens-de-mer-form'
import MissionControlModelForm from './mission-control-model-form'

type MissionControlNavFormProps = {
  label: string
  hideGensDeMer?: boolean
  controlsToComplete?: ControlType[]
}

const MissionControlNavForm: FC<MissionControlNavFormProps> = ({ label, hideGensDeMer, controlsToComplete }) => {
  return (
    <Stack direction="column" spacing="0.5rem" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Label>{label}</Label>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Field name="controlAdministrative">
          {(field: FieldProps<ControlAdministrative>) => (
            <MissionControlAdministrativeForm
              fieldFormik={field}
              name="controlAdministrative"
              isToComplete={controlsToComplete?.includes(ControlType.ADMINISTRATIVE)}
            />
          )}
        </Field>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Field name="controlNavigation">
          {(field: FieldProps<ControlNavigation>) => (
            <MissionControlModelForm
              fieldFormik={field}
              name="controlNavigation"
              controlType={ControlType.NAVIGATION}
              isToComplete={controlsToComplete?.includes(ControlType.NAVIGATION)}
            />
          )}
        </Field>
      </Stack.Item>
      {!hideGensDeMer && (
        <Stack.Item style={{ width: '100%' }}>
          <Field name="controlGensDeMer">
            {(field: FieldProps<ControlGensDeMer>) => (
              <MissionControlGensDeMerForm
                fieldFormik={field}
                name="controlGensDeMer"
                isToComplete={controlsToComplete?.includes(ControlType.GENS_DE_MER)}
              />
            )}
          </Field>
        </Stack.Item>
      )}
      <Stack.Item style={{ width: '100%' }}>
        <Field name="controlSecurity">
          {(field: FieldProps<ControlSecurity>) => (
            <MissionControlModelForm
              name="controlSecurity"
              fieldFormik={field}
              controlType={ControlType.SECURITY}
              isToComplete={controlsToComplete?.includes(ControlType.SECURITY)}
            />
          )}
        </Field>
      </Stack.Item>
    </Stack>
  )
}

export default MissionControlNavForm
