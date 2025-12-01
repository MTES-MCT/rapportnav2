import { FormikSearchProps, Icon, TextInput } from '@mtes-mct/monitor-ui'
import { FieldProps } from 'formik'
import { useEffect, useState } from 'react'
import { Dropdown, Stack } from 'rsuite'
import styled from 'styled-components'
import { useVesselListQuery } from '../../services/use-vessel-service'
import { Vessel } from '../../types/vessel-type'

type FormikSearchVesselProps = {
  name: string
  vessel?: Vessel
  fieldFormik: FieldProps<number>
}

export const FormikSearchVessel = styled(
  ({ name, vessel, fieldFormik, ...props }: Omit<FormikSearchProps, 'options'> & FormikSearchVesselProps) => {
    const [open, setOpen] = useState<boolean>()
    const [search, setSearch] = useState<string>()
    const { data: vessels } = useVesselListQuery(search)

    const getName = (v: Vessel) => `${v?.vesselName} - ${v?.flagState} - ${v?.externalReferenceNumber}`
    const onSelect = (eventKey: string | undefined, event: React.SyntheticEvent) => {
      event.stopPropagation()
      if (!eventKey) return
      const value = vessels?.find(item => item.vesselId === Number(eventKey))
      if (!value) return
      setOpen(false)
      setSearch(getName(value))
      fieldFormik.form.setFieldValue(name, value.vesselId)
    }

    useEffect(() => {
      setOpen(!!vessels?.length)
    }, [vessels])

    useEffect(() => {
      if (vessel?.vesselName) setSearch(getName(vessel))
    }, [vessel])

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
                {vessels?.map(item => (
                  <Dropdown.Item eventKey={item.vesselId} style={{ maxWidth: '100%' }}>
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
