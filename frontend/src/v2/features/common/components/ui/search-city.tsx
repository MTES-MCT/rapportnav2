import { Search } from '@mtes-mct/monitor-ui'
import { useField, useFormikContext } from 'formik'
import { useEffect, useMemo, useState } from 'react'
import styled from 'styled-components'
import { useAddressListQuery } from '../../services/use-address-service'

type SearchCityProps = {
  name: string
  label: string
  isLight?: boolean
}

export const SearchCity = styled(({ ...props }: SearchCityProps) => {
  const [, meta, helpers] = useField<string | undefined>(props.name)
  const { values, setFieldValue } = useFormikContext<any>()
  const [search, setSearch] = useState(values.city ?? '')
  const { data: addresses } = useAddressListQuery(search)

  // Sync search with formik city value when it changes externally (e.g. after refetch)
  useEffect(() => {
    if (values.city && values.city !== search) {
      setSearch(values.city)
    }
  }, [values.city])

  const options = useMemo(() => {
    const apiOptions =
      addresses
        ?.filter(a => a.zipcode && a.town)
        .map(a => ({
          value: a.town!,
          label: `${a.town} (${a.zipcode})`
        })) ?? []

    // Seed option for prepopulation when no API results loaded yet
    if (values.city && values.zipCode && !apiOptions.some(o => o.value === values.city)) {
      apiOptions.unshift({ value: values.city, label: `${values.city} (${values.zipCode})` })
    }

    return apiOptions
  }, [addresses, values.city, values.zipCode])

  const handleQuery = (query: string | undefined) => {
    if (query) setSearch(query)
  }

  const handleChange = (nextValue: string | undefined) => {
    if (!nextValue) {
      helpers.setValue(undefined)
      setFieldValue('city', undefined)
      return
    }
    const selected = addresses?.find(a => a.town === nextValue)
    helpers.setValue(selected?.zipcode)
    setFieldValue('city', nextValue)
  }

  return (
    <Search<string>
      isLight={true}
      isRequired={true}
      isErrorMessageHidden={true}
      {...props}
      value={values.city}
      error={meta.error}
      onChange={handleChange}
      options={options}
      onQuery={handleQuery}
      filterBy={() => true}
      data-testid="search-city"
    />
  )
})({})
