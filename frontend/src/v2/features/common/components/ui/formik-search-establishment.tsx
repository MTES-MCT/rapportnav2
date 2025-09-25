import { FormikSearchProps, Icon, TextInput } from '@mtes-mct/monitor-ui'
import { FieldProps } from 'formik'
import { useEffect, useState } from 'react'
import { Dropdown, Stack } from 'rsuite'
import styled from 'styled-components'
import { useEstablishmentListQuery, useLazyEstablishmentQuery } from '../../services/use-etablishments-service'
import { Establishment } from '../../types/etablishment'

type FormikSearchEstablishmentProps = {
  name: string
  fieldFormik: FieldProps<string>
}

export const FormikSearchEstablishment = styled(
  ({ name, fieldFormik, ...props }: Omit<FormikSearchProps, 'options'> & FormikSearchEstablishmentProps) => {
    const [open, setOpen] = useState<boolean>()
    const [siren, setSiren] = useState<string>()
    const [search, setSearch] = useState<string>()

    const { data: establishment } = useLazyEstablishmentQuery(siren)
    const { data: establishments } = useEstablishmentListQuery(search)

    const getName = (establishment: Establishment) =>
      `${establishment.name} | n°${establishment.siret} | ${establishment.address}`

    const onSelect = (eventKey?: string, event: React.SyntheticEvent) => {
      const value = establishments?.find(item => item.id === eventKey)
      event.stopPropagation()
      if (!value) return

      setOpen(false)
      setSiren(eventKey)
      setSearch(getName(value))
      fieldFormik.form.setFieldValue(name, value.id)
    }

    useEffect(() => {
      setOpen(!!establishments?.length)
    }, [establishments])

    useEffect(() => {
      if (!establishment) return
      setSearch(getName(establishment))
    }, [establishment])

    useEffect(() => {
      if (!fieldFormik.field.value) return
      setSiren(fieldFormik.field.value)
    }, [fieldFormik])

    return (
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
              {establishments?.map(item => (
                <Dropdown.Item eventKey={item.id} style={{ maxWidth: '100%' }}>
                  {getName(item)}
                </Dropdown.Item>
              ))}
            </Dropdown.Menu>
          )}
        </Stack.Item>
      </Stack>
    )
  }
)(() => ({}))
