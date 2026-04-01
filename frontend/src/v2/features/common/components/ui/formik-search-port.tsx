import { FormikSearchProps, Icon, TextInput } from '@mtes-mct/monitor-ui'
import { FieldProps } from 'formik'
import { useEffect, useRef, useState } from 'react'
import { Dropdown, Stack } from 'rsuite'
import styled from 'styled-components'
import { usePortListQuery } from '../../services/use-port-service'
import { Port } from '../../types/port-type'

type FormikSearchPortProps = {
  name: string
  fieldFormik: FieldProps<string>
}

export const FormikSearchPort = styled(
  ({ name, fieldFormik, ...props }: Omit<FormikSearchProps, 'options'> & FormikSearchPortProps) => {
    const initialized = useRef(false)
    const [open, setOpen] = useState<boolean>(false)
    const [search, setSearch] = useState<string>('')
    const locode = fieldFormik?.field?.value
    const { data: ports } = usePortListQuery(!initialized.current && locode ? locode : search)

    const getName = (p: Port) => `${p?.name} (${p?.locode})`

    // When ports load, resolve locode to display name (or open dropdown after init)
    useEffect(() => {
      if (!initialized.current && locode && ports?.length) {
        const found = ports.find(p => p.locode === locode)
        if (found) {
          setSearch(getName(found))
          initialized.current = true
        }
      } else if (initialized.current) {
        setOpen(!!ports?.length)
      }
    }, [ports, locode])

    const onSelect = (eventKey: string | undefined, event: React.SyntheticEvent) => {
      event.stopPropagation()
      if (!eventKey) return
      const value = ports?.find(item => item.locode === eventKey)
      if (!value) return
      initialized.current = true
      setOpen(false)
      setSearch(getName(value))
      fieldFormik.form.setFieldValue(name, value.locode)
    }

    return (
      <Stack.Item style={{ width: '100%' }} data-testid="search-port">
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
              onChange={value => {
                initialized.current = true
                setSearch(value ?? '')
              }}
              error={fieldFormik?.meta?.error}
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
