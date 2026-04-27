import { Select } from '@mtes-mct/monitor-ui'
import { useField } from 'formik'
import { useMemo } from 'react'
import styled from 'styled-components'
import { FishAuction, useFishAuctionListQuery } from '../../services/use-fish-auction-service'

type FormikSelectFishAuctionProps = {
  name: string
  label: string
  isLight?: boolean
  className?: string
}

export const FormikSelectFishAuction = styled(({ ...props }: FormikSelectFishAuctionProps) => {
  const [field, meta, helpers] = useField<FishAuction | undefined>(props.name)
  const { data: fishAuctions } = useFishAuctionListQuery()

  const options = useMemo(() => {
    if (!fishAuctions) return []
    return fishAuctions.map(fa => ({
      value: fa.id,
      label: `${fa.name} (${fa.facade})`
    }))
  }, [fishAuctions])

  const handleChange = (nextValue: number | undefined) => {
    if (!nextValue) {
      helpers.setValue(undefined)
      return
    }
    const selected = fishAuctions?.find(fa => fa.id === nextValue)
    helpers.setValue(selected)
  }

  return (
    <Select<number>
      isLight={true}
      isRequired={true}
      isErrorMessageHidden={true}
      {...props}
      value={field.value?.id}
      error={meta.touched ? meta.error : undefined}
      onChange={handleChange}
      options={options}
      data-testid="select-fish-auction"
    />
  )
})({})
