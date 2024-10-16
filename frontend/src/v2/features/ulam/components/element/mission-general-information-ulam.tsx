import { TextInput } from '@mtes-mct/monitor-ui'
import React, { useState } from 'react'
import { useDelay } from '../../../../features/common/hooks/use-delay'

type MissionGeneralInformationUlamProps = {}

const MissionGeneralInformationUlam: React.FC<MissionGeneralInformationUlamProps> = () => {
  const { handleExecuteOnDelay } = useDelay()
  const [value, setValue] = useState<string>()
  const handleChange = (nextValue?: string) => {
    setValue(nextValue)
    handleExecuteOnDelay(() => console.log(nextValue))
  }

  return (
    <TextInput
      label="Test gloabla delay"
      name="testGlobalDealy"
      value={value}
      isErrorMessageHidden={true}
      onChange={handleChange}
    />
  )
}

export default MissionGeneralInformationUlam
