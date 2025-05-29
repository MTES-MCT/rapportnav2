import { FormikSearch, FormikSearchProps } from '@mtes-mct/monitor-ui'
import styled from 'styled-components'
import { Vessel } from '../../types/vessel-type'

export const FormikSearchVessel = styled(
  ({ vessels, ...props }: Omit<FormikSearchProps, 'options'> & { vessels: Vessel[] }) => {
    return (
      <FormikSearch
        {...props}
        placeholder=""
        isRequired={true}
        isSearchIconHidden={false}
        isErrorMessageHidden={true}
        options={
          vessels?.map(v => ({
            value: v.vesselId,
            label: `${v.vesselName} - ${v.externalReferenceNumber}`
          })) ?? []
        }
      />
    )
  }
)(() => ({}))
