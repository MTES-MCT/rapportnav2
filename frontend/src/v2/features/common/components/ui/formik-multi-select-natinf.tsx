import { Natinf } from '@common/types/infraction-types'
import { FormikMultiSelect, FormikMultiSelectProps } from '@mtes-mct/monitor-ui'
import styled from 'styled-components'

interface NatinfsMultiSelectProps {
  natinfOptions: Natinf[]
}

export const FormikMultiSelectNatinf = styled(
  ({ natinfOptions, ...props }: Omit<FormikMultiSelectProps, 'label' | 'options'> & NatinfsMultiSelectProps) => (
    <FormikMultiSelect
      {...props}
      label="NATINF"
      placeholder=""
      isRequired={true}
      searchable={true}
      virtualized={true}
      options={natinfOptions.map(n => ({ value: n.natinfCode, label: `${n.natinfCode} - ${n.infraction}` }))}
    />
  )
)(() => ({}))
