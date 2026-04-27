import { Select } from '@mtes-mct/monitor-ui'
import { useField } from 'formik'
import { useMemo } from 'react'
import styled from 'styled-components'
import { usePortListAllQuery } from '../../services/use-port-service'

type FormikSearchPortProps = {
  name: string
  label: string
  isLight?: boolean
  className?: string
}

export const FormikSearchPort = styled(({ ...props }: FormikSearchPortProps) => {
  const [field, meta, helpers] = useField<string | undefined>(props.name)
  const { data: ports } = usePortListAllQuery()

  const options = useMemo(() => {
    if (!ports) return []
    return ports.map(p => ({
      value: p.locode,
      label: `${p.name} (${p.locode})`
    }))
  }, [ports])

  const handleChange = (nextValue: string | undefined) => {
    helpers.setValue(nextValue)
  }

  debugger

  return (
    <Select<string>
      isLight={true}
      isRequired={true}
      isErrorMessageHidden={true}
      {...props}
      searchable={true}
      value={field.value}
      error={meta.error}
      onChange={handleChange}
      options={options}
      data-testid="search-port"
    />
  )
})({})
