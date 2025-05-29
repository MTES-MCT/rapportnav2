import { FormikSelect, FormikSelectProps } from '@mtes-mct/monitor-ui'
import styled from 'styled-components'
import { CrossControl } from '../../types/crossed-control-type'

export const FormikSelectCrossControl = styled(
  ({ crossControls, ...props }: Omit<FormikSelectProps, 'options'> & { crossControls: CrossControl[] }) => {
    return (
      <FormikSelect
        {...props}
        placeholder=""
        isLight={true}
        isRequired={true}
        isErrorMessageHidden={true}
        options={
          crossControls.map(v => ({
            value: v.id ?? '',
            label: `${v.vesselName} | ${v?.vesselExternalReferenceNumber} | Ouvert le ${v.startDateTimeUtc} |${v.status}`
          })) ?? []
        }
      />
    )
  }
)(() => ({}))
