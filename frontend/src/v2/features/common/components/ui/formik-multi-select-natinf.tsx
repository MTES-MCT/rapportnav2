import { FormikMultiSelect, FormikMultiSelectProps } from '@mtes-mct/monitor-ui'
import styled from 'styled-components'
import useNatinfListQuery from '../../services/use-natinf-service'

export const FormikMultiSelectNatinf = styled((props: Omit<FormikMultiSelectProps, 'label' | 'options'>) => {
  const { data: natinfs } = useNatinfListQuery()
  return (
    <FormikMultiSelect
      {...props}
      label="NATINF"
      placeholder=""
      isRequired={true}
      searchable={true}
      virtualized={true}
      options={natinfs?.map(n => ({ value: n.natinfCode, label: `${n.natinfCode} - ${n.infraction}` })) ?? []}
    />
  )
})(() => ({}))
