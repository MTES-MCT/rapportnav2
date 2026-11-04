import Text from '@common/components/ui/text'
import { Icon, Message, SearchProps, TextInput, THEME } from '@mtes-mct/monitor-ui'
import { SyntheticEvent, useEffect, useState } from 'react'
import { Dropdown, Stack } from 'rsuite'
import styled from 'styled-components'
import { useEstablishmentListQuery } from '../../services/use-etablishments-service'
import { Establishment } from '../../types/etablishment'

type EstablishmentSearchProps = {
  establishment?: Establishment
  handleSubmit: (value?: Establishment) => void
}

export const EstablishmentSearch = styled(
  ({ establishment, handleSubmit, ...props }: Omit<SearchProps, 'options'> & EstablishmentSearchProps) => {
    const [open, setOpen] = useState<boolean>()
    const [search, setSearch] = useState<string>()
    const { data: establishments } = useEstablishmentListQuery(search)

    const getName = (value: Establishment) => {
      const siret = value.siret ? ` | SIRET=${value.siret}` : ''
      const headquarters = value.isHeadquarter ? '(siège) ' : ''
      const address = value.address ? ` | ${value.address}` : ''
      return `${value.name} ${headquarters}${siret}${address}`
    }

    const onSelect = (eventKey?: string, event?: SyntheticEvent) => {
      const value = establishments?.find(item => item.id === eventKey)
      event?.stopPropagation()
      if (!value) return

      setOpen(false)
      setSearch(getName(value))
      handleSubmit({ id: establishment?.id, ...value })
    }

    useEffect(() => {
      setOpen(!!establishments?.length)
    }, [establishments])

    useEffect(() => {
      if (!establishment?.siren) {
        setSearch(undefined)
        return
      }
      setSearch(getName(establishment))
    }, [establishment])

    return (
      <Stack direction="column" spacing="0.5rem" data-testid="search-establishment">
        <Stack.Item style={{ width: '100%' }}>
          <Message level="INFO">
            <Text as="h3" color={THEME.color.blueYonder}>
              <strong>Recherche dans l’annuaire entreprise de l’Etat.</strong>
            </Text>
            <span>
              <strong>
                <u>Le SIRET fait foi</u>
              </strong>{' '}
              afin de garantir <u>l'exhaustivité</u> des résultats et <u>la qualité/justesse</u> de la recherche <br />
              <em>(sélection de l’établissement contrôlé et non d’une société mère par exemple)</em>
            </span>
          </Message>
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Stack direction="column">
            <Stack.Item style={{ width: '100%' }}>
              <TextInput
                {...props}
                name={'search'}
                placeholder=""
                value={search}
                isRequired={true}
                Icon={Icon.Search}
                isErrorMessageHidden={true}
                onChange={value => setSearch(value)}
              />
            </Stack.Item>
            <Stack.Item style={{ width: '100%', position: 'relative' }}>
              {open && (
                <Dropdown.Menu
                  style={{
                    position: 'absolute',
                    zIndex: 10,
                    width: '100%',
                    overflow: 'scroll',
                    maxHeight: 200,
                    minHeight: 0
                  }}
                  onSelect={onSelect}
                >
                  {establishments?.map(item => (
                    <Dropdown.Item key={item.id} eventKey={item.id} style={{ maxWidth: '100%' }}>
                      {getName(item)}
                    </Dropdown.Item>
                  ))}
                </Dropdown.Menu>
              )}
            </Stack.Item>
          </Stack>
        </Stack.Item>
      </Stack>
    )
  }
)(() => ({}))
