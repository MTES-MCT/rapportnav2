import { Icon, Message, SearchProps, TextInput } from '@mtes-mct/monitor-ui'
import { FieldProps, FormikErrors } from 'formik'
import { SyntheticEvent, useEffect, useState } from 'react'
import { Dropdown, Stack } from 'rsuite'
import styled from 'styled-components'
import { useEstablishmentListQuery } from '../../services/use-etablishments-service'
import { Establishment } from '../../types/etablishment'

type SearchEstablishmentProps = {
  establishment?: Establishment
  handleSubmit: (value?: Establishment, errors?: FormikErrors<Establishment>) => void
  fieldFormik: FieldProps<Establishment>
}

export const SearchEstablishment = styled(
  ({ establishment, handleSubmit, fieldFormik, ...props }: Omit<SearchProps, 'options'> & SearchEstablishmentProps) => {
    const [open, setOpen] = useState<boolean>()
    const [search, setSearch] = useState<string>()
    const { data: establishments } = useEstablishmentListQuery(search)

    const getName = (value: Establishment) => {
      const headquarters = value.isHeadquarter ? '(siège) ' : ''
      return `${value.name} ${headquarters}| SIRET=${value.siret} | ${value.address}`
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
            Recherche d'un établissement par son nom, <br />
            numéro de SIREN ou adresse dans le référentiel entreprises de l'état
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
                error={fieldFormik?.meta?.error?.name}
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
