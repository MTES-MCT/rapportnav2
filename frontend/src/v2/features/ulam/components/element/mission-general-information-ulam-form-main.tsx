import { FormikMultiCheckbox, FormikSelect } from '@mtes-mct/monitor-ui'
import { Field, FieldProps } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { FormikDateRangePicker } from '../../../common/components/ui/formik-date-range-picker.tsx'
import { FormikNumberInput } from '../../../common/components/ui/formik-number-input.tsx'
import { useMissionType } from '../../../common/hooks/use-mission-type.tsx'
import { MissionGeneralInfoInput } from '../../hooks/use-ulam-mission-general-information-form.tsx'

export interface MissionGeneralInformationUlamFormMainProps {
  isCreation?: boolean
  values: MissionGeneralInfoInput
}

const MissionGeneralInformationUlamFormMain: FC<MissionGeneralInformationUlamFormMainProps> = ({
  values,
  isCreation
}) => {
  const {
    missionTypeOptions,
    reportTypeOptions,
    reinforcementTypeOptions,
    isExternalReinforcementTime,
    isMissionTypeSea,
    isEnvMission
  } = useMissionType()

  return (
    <Stack direction="column">
      <Stack.Item style={{ width: '100%', marginBottom: '1em' }}>
        <FormikSelect
          isRequired={true}
          isLight={isCreation}
          disabled={!isCreation}
          name="missionReportType"
          label={'Type de rapport'}
          options={reportTypeOptions}
        />
      </Stack.Item>

      {isEnvMission(values.missionReportType) && (
        <Stack.Item style={{ width: '100%', marginBottom: '1em', textAlign: 'left' }}>
          <FormikMultiCheckbox
            isInline
            isLight
            isRequired={true}
            name="missionTypes"
            label={'Type de mission'}
            options={missionTypeOptions}
          />
        </Stack.Item>
      )}

      {isExternalReinforcementTime(values.missionReportType) && (
        <Stack.Item style={{ width: '100%', marginBottom: '1em' }}>
          <Stack direction="row" spacing={10}>
            <Stack.Item style={{ width: '55%' }}>
              <FormikSelect
                isLight={isCreation}
                label="Nature du renfort"
                name="reinforcementType"
                options={reinforcementTypeOptions}
              />
            </Stack.Item>
          </Stack>
        </Stack.Item>
      )}

      <Stack.Item style={{ width: '100%', marginBottom: '1em', textAlign: 'left' }}>
        <Stack direction="row">
          <Stack.Item style={{ width: '70%' }}>
            <Field name="dates">
              {(field: FieldProps<Date[]>) => (
                <FormikDateRangePicker
                  label=""
                  name="dates"
                  fieldFormik={field}
                  isLight={isCreation}
                  validateOnSubmit={isCreation}
                />
              )}
            </Field>
          </Stack.Item>
          <Stack.Item style={{ width: '30%' }}>
            {!isCreation && isMissionTypeSea(values.missionTypes) && (
              <FormikNumberInput isLight={false} label={"Nb d'heures en mer"} name="nbHourAtSea" />
            )}
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </Stack>
  )
}

export default MissionGeneralInformationUlamFormMain
