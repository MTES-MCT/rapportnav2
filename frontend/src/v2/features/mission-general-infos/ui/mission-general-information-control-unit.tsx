import { FormikSelect, Select } from '@mtes-mct/monitor-ui'
import React, { useEffect, useState } from 'react'
import { ControlUnit } from '../../common/types/control-unit-types'

interface MissionGeneralInformationControlUnitProps {
  name: string
  open: boolean
  label: string
  isDisabled: boolean
  controlUnits: ControlUnit[]
}

const MissionGeneralInformationControlUnit: React.FC<MissionGeneralInformationControlUnitProps> = ({
  open,
  name,
  label,
  controlUnits,
  isDisabled
}) => {
  const [options, setOptions] = useState<{ value: number; label: string }[]>([])

  useEffect(() => {
    if (!controlUnits) return
    setOptions(controlUnits.map(c => ({ value: c.id, label: c.name })))
  }, [controlUnits])
  return (
    <>
      {open ? (
        <FormikSelect //TODO: add cleanup in @mtes-mct/monitor-ui component
          label={label}
          isRequired
          searchable
          isCleanable
          name={name}
          options={options}
          disabled={isDisabled}
        />
      ) : (
        <Select isRequired searchable isCleanable name="fake" label={label} options={[]} />
      )}
    </>
  )
}

export default MissionGeneralInformationControlUnit
