import Text from '@common/components/ui/text.tsx'
import { FormikDatePicker, FormikDatePickerWithDateDateProps, FormikEffect, Label, THEME } from '@mtes-mct/monitor-ui'
import { FieldProps, Formik, FormikErrors } from 'formik'
import { isEmpty, isEqual } from 'lodash'
import { useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import styled from 'styled-components'
import { simpleDateRangeValidationSchema } from '../../validation-schema/date-validation'

type DateInput = {
  startDateTimeUtc: Date
  endDateTimeUtc: Date
}

type MissionActionFormikDateRangePickerProps = FormikDatePickerWithDateDateProps & {
  name: string
  validateOnSubmit?: boolean
  fieldFormik: FieldProps<Date[]>
}

export const MissionActionFormikDateRangePicker = styled(
  ({ name, fieldFormik, validateOnSubmit, ...props }: MissionActionFormikDateRangePickerProps) => {
    const [initValue, setInitValue] = useState<DateInput>()

    useEffect(() => {
      if (!fieldFormik?.field?.value) return
      const startDateTimeUtc = fieldFormik.field.value[0]
      const endDateTimeUtc = fieldFormik.field.value[1]
      setInitValue({ startDateTimeUtc, endDateTimeUtc })
    }, [fieldFormik])

    const handleSubmit = async (value: DateInput, errors: FormikErrors<DateInput>) => {
      if (isEqual(value, initValue)) return
      if (!isEmpty(errors)) {
        if (validateOnSubmit) fieldFormik.form.setErrors(errors)
        return
      }
      await fieldFormik.form.setFieldValue(name, [value.startDateTimeUtc, value.endDateTimeUtc])
    }

    return (
      <>
        {initValue && (
          <Formik
            initialValues={initValue}
            onSubmit={handleSubmit}
            enableReinitialize
            validateOnChange={true}
            validationSchema={simpleDateRangeValidationSchema}
          >
            {({ errors, validateForm }) => (
              <>
                <FormikEffect
                  onChange={async nextValue => {
                    validateForm(nextValue).then(errors => handleSubmit(nextValue as DateInput, errors))
                  }}
                />
                <Stack direction={'column'} alignItems={'flex-start'}>
                  <Stack.Item>
                    <Stack direction="row">
                      <Stack.Item>
                        <Label>{props.label || 'Date et heure de début et de fin'}</Label>
                      </Stack.Item>
                      <Stack.Item>
                        <Label
                          as={'h3'}
                          style={{ marginLeft: 2, color: THEME.color.maximumRed, fontWeight: 'initial' }}
                        >
                          *
                        </Label>
                      </Stack.Item>
                    </Stack>
                  </Stack.Item>
                  <Stack.Item>
                    <Stack direction={'row'} spacing={'0.5rem'}>
                      <Stack.Item>
                        <FormikDatePicker
                          {...props}
                          label={''}
                          isRequired={true}
                          withTime={true}
                          isCompact={true}
                          name={'startDateTimeUtc'}
                          isErrorMessageHidden={true}
                        />
                      </Stack.Item>
                      <Stack.Item alignSelf={'center'}>
                        <Text as={'h4'}>au</Text>
                      </Stack.Item>
                      <Stack.Item>
                        <FormikDatePicker
                          {...props}
                          label={''}
                          isRequired={true}
                          withTime={true}
                          isCompact={true}
                          name={'endDateTimeUtc'}
                          isErrorMessageHidden={true}
                        />
                      </Stack.Item>
                    </Stack>
                  </Stack.Item>
                  <Stack.Item style={{ marginTop: '0.2rem' }}>
                    <Text as={'h3'} color={THEME.color.maximumRed}>
                      {<>{errors?.endDateTimeUtc ?? errors?.startDateTimeUtc ?? ''}</>}
                    </Text>
                  </Stack.Item>
                </Stack>
              </>
            )}
          </Formik>
        )}
      </>
    )
  }
)({})
