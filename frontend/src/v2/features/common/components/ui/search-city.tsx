import { Icon, TextInput } from '@mtes-mct/monitor-ui'
import { useEffect, useRef, useState } from 'react'
import { Dropdown, Stack } from 'rsuite'
import styled from 'styled-components'
import { useAddressListQuery } from '../../services/use-address-service'
import { Address } from '../../types/address-type.ts'

type SearchAddressProps = {
  name: string
  value?: string
  isLight?: boolean
  label?: string
  onChange: (value?: string) => void
}

export const SearchCity = styled(({ value, isLight, label, onChange, ...props }: SearchAddressProps) => {
  const [open, setOpen] = useState<boolean>(false)
  const [search, setSearch] = useState<string>(value ?? '')
  const isUserTyping = useRef<boolean>(false)
  const { data: addresses } = useAddressListQuery(search)

  const getDisplayName = (address: Address) => `${address.town} (${address.zipcode})`

  const onSelect = (eventKey?: string, event?: React.SyntheticEvent) => {
    const selected = addresses?.find(item => item.town === eventKey)
    event?.stopPropagation()
    if (!selected) return

    isUserTyping.current = false
    setOpen(false)
    setSearch(getDisplayName(selected))
    onChange(getDisplayName(selected))
  }

  useEffect(() => {
    if (isUserTyping.current) {
      setOpen(!!addresses?.length)
    }
  }, [addresses])

  useEffect(() => {
    const newValue = value ?? ''
    if (newValue !== search) {
      setSearch(newValue)
    }
  }, [value])

  return (
    <Stack direction="column" spacing="0.5rem">
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="column">
          <Stack.Item style={{ width: '100%' }}>
            <TextInput
              {...props}
              name="search-address"
              label={label ?? ''}
              placeholder=""
              value={search}
              isLight={isLight}
              isRequired={true}
              Icon={Icon.Search}
              isErrorMessageHidden={true}
              onChange={newValue => {
                isUserTyping.current = true
                setSearch(newValue ?? '')
                if (!newValue) {
                  onChange(undefined)
                }
              }}
            />
          </Stack.Item>
          <Stack.Item style={{ width: '100%', overflow: 'hidden' }}>
            {open && (
              <Dropdown.Menu style={{ overflow: 'scroll', minHeight: 0 }} onSelect={onSelect}>
                {addresses?.map(item => (
                  <Dropdown.Item key={item.town} eventKey={item.town} style={{ maxWidth: '100%' }}>
                    {getDisplayName(item)}
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
