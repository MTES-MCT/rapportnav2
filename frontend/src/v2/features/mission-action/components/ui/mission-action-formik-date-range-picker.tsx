import {
  FormikDatePicker, FormikDatePickerWithDateDateProps,
  Label, THEME,
} from '@mtes-mct/monitor-ui'
import styled from 'styled-components'
import {Stack} from "rsuite";
import Text from "@common/components/ui/text.tsx";

type MissionActionFormikDateRangePickerProps = FormikDatePickerWithDateDateProps & {
  errors: any //formik errors
}

export const MissionActionFormikDateRangePicker = styled(
  (props: MissionActionFormikDateRangePickerProps) => (
    <Stack direction={'column'} alignItems={'flex-start'}>
      <Stack.Item>
        <Label>{props.label || 'Date et heure de début et de fin'}</Label>
      </Stack.Item>
      <Stack.Item>
        <Stack direction={'row'} spacing={'0.5rem'}>
          <Stack.Item>
            <FormikDatePicker
              {...props}
              isErrorMessageHidden={true}
              label={''}
              name={'startDateTimeUtc'}
              isRequired={true}
              withTime={true}
              isCompact={true}

            />
          </Stack.Item>
          <Stack.Item alignSelf={'center'}>
            <Text as={'h4'}>au</Text>
          </Stack.Item>
          <Stack.Item>
            <FormikDatePicker
              {...props}
              isErrorMessageHidden={true}
              label={''}
              name={'endDateTimeUtc'}
              isRequired={true}
              withTime={true}
              isCompact={true}
            />
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{marginTop: '0.2rem'}}>
        <Text as={'h3'} color={THEME.color.maximumRed}>
          {props.errors?.endDateTimeUtc ?? props.errors?.startDateTimeUtc ?? ''}
        </Text>
      </Stack.Item>
    </Stack>
  )
)({})
