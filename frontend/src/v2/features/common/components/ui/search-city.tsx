import { Icon, TextInput } from '@mtes-mct/monitor-ui'
import { useEffect, useRef, useState } from 'react'
import { Dropdown, Stack } from 'rsuite'
import styled from 'styled-components'
import { useAddressListQuery } from '../../services/use-address-service'
import { FieldProps } from 'formik'

type SearchCityValue = {
  zipCode?: string
  city?: string
}

type SearchAddressProps = {
  name: string
  value?: SearchCityValue
  isLight?: boolean
  label?: string
  onChange: (value?: SearchCityValue) => void
  fieldFormik: FieldProps<string>
}

export const SearchCity = styled(({ value, isLight, label, onChange, fieldFormik, ...props }: SearchAddressProps) => {
  const userTyping = useRef<boolean>(false)
  const [open, setOpen] = useState<boolean>(false)
  const [search, setSearch] = useState<string>('')
  const { data: addresses } = useAddressListQuery(search)

  const getDisplayName = (city?: string, zipCode?: string) =>
    city && zipCode ? `${city} (${zipCode})` : (city ?? zipCode ?? '')

  // Display from stored values on load
  useEffect(() => {
    if (!userTyping.current && value?.city) {
      setSearch(getDisplayName(value.city, value.zipCode))
    }
  }, [value?.city, value?.zipCode])

  // Only open dropdown when user is actively typing
  useEffect(() => {
    if (userTyping.current) {
      setOpen(!!addresses?.length)
    }
  }, [addresses])

  const onSelect = (eventKey?: string, event?: React.SyntheticEvent) => {
    const selected = addresses?.find(item => item.town === eventKey)
    event?.stopPropagation()
    if (!selected) return

    userTyping.current = false
    setOpen(false)
    setSearch(getDisplayName(selected.town, selected.zipcode))
    onChange({ zipCode: selected.zipcode, city: selected.town })
  }

  return (
    <Stack direction="column" spacing="0.5rem" data-testid="search-city">
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="column">
          <Stack.Item style={{ width: '100%' }}>
            <TextInput
              {...props}
              label={label ?? ''}
              placeholder=""
              value={search}
              isLight={isLight}
              isRequired={true}
              Icon={Icon.Search}
              isErrorMessageHidden={true}
              onChange={newValue => {
                userTyping.current = true
                setSearch(newValue ?? '')
                if (!newValue) {
                  onChange(undefined)
                }
              }}
              error={fieldFormik?.meta?.error}
            />
          </Stack.Item>
          <Stack.Item style={{ width: '100%', overflow: 'hidden' }}>
            {open && (
              <Dropdown.Menu style={{ overflow: 'scroll', minHeight: 0 }} onSelect={onSelect}>
                {addresses?.map(item => (
                  <Dropdown.Item key={item.town} eventKey={item.town} style={{ maxWidth: '100%' }}>
                    {getDisplayName(item.town, item.zipcode)}
                  </Dropdown.Item>
                ))}
              </Dropdown.Menu>
            )}
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </Stack>
  )
})(() => ({}))
