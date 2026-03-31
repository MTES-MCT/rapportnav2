import { FormikSearchProps, Icon, TextInput } from '@mtes-mct/monitor-ui'
import { FieldProps } from 'formik'
import { useEffect, useState } from 'react'
import { Dropdown, Stack } from 'rsuite'
import styled from 'styled-components'
import { usePortListQuery } from '../../services/use-port-service'
import { Port } from '../../types/port-type'

type FormikSearchPortProps = {
  name: string
  port?: Port
  fieldFormik: FieldProps<string>
}

export const FormikSearchPort = styled(
  ({ name, port, fieldFormik, ...props }: Omit<FormikSearchProps, 'options'> & FormikSearchPortProps) => {
    const [open, setOpen] = useState<boolean>()
    const [search, setSearch] = useState<string>()
    const { data: ports } = usePortListQuery(search)

    const getName = (p: Port) => `${p?.name} - ${p?.locode}`
    const onSelect = (eventKey: string | undefined, event: React.SyntheticEvent) => {
      event.stopPropagation()
      if (!eventKey) return
      const value = ports?.find(item => item.locode === eventKey)
      if (!value) return
      setOpen(false)
      setSearch(getName(value))
      fieldFormik.form.setFieldValue(name, value.locode)
    }

    useEffect(() => {
      setOpen(!!ports?.length)
    }, [ports])

    useEffect(() => {
      if (port?.name) setSearch(getName(port))
    }, [port])

    return (
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="column">
          <Stack.Item style={{ width: '100%' }}>
            <TextInput
              {...props}
              name={name}
              placeholder=""
              value={search}
              isRequired={true}
              Icon={Icon.Search}
              isErrorMessageHidden={true}
              onChange={value => setSearch(value)}
            />
          </Stack.Item>
          <Stack.Item style={{ width: '100%', overflow: 'hidden' }}>
            {open && (
              <Dropdown.Menu style={{ overflow: 'scroll', minHeight: 0 }} onSelect={onSelect}>
                {ports?.map(item => (
                  <Dropdown.Item eventKey={item.locode} style={{ maxWidth: '100%' }}>
                    {getName(item)}
                  </Dropdown.Item>
                ))}
              </Dropdown.Menu>
            )}
          </Stack.Item>
        </Stack>
      </Stack.Item>
    )
  }
)(() => ({}))
