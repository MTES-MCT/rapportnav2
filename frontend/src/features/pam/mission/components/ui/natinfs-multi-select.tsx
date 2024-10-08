import { MultiSelect, OptionValue } from '@mtes-mct/monitor-ui'
import React from 'react'
import useNatinfs from '../../hooks/use-natinfs.tsx'

interface NatinfsMultiSelectProps {
  selectedNatinfs?: string[]
  onChange: (field: string, value: any) => void
}

const NatinfsMultiSelect: React.FC<NatinfsMultiSelectProps> = ({ selectedNatinfs, onChange }) => {
  const { data: natinfs, loading, error } = useNatinfs()

  if (loading || error) {
    return
  }
  return (
    <MultiSelect
      error=""
      label="NATINF"
      name="natinfs"
      isRequired={true}
      value={selectedNatinfs || []}
      onChange={(nextValue?: OptionValue[]) => onChange('natinfs', nextValue)}
      options={
        natinfs?.map(({ natinfCode, infraction }) => ({
          value: natinfCode,
          label: `${natinfCode} - ${infraction}`
        })) as any
      }
      placeholder=""
      searchable={true}
      virtualized={true}
    />
  )
}

export default NatinfsMultiSelect
