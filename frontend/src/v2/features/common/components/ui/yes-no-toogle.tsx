import { Accent, Button, Icon, Size, THEME } from '@mtes-mct/monitor-ui'
import { isNil } from 'lodash'
import { useEffect, useState } from 'react'
import { Stack } from 'rsuite'

const activeStyle = {
  color: 'white',
  borderColor: THEME.color.blueGray,
  backgroundColor: THEME.color.blueGray
}

const inactiveStyle = {
  color: THEME.color.charcoal,
  borderColor: THEME.color.blueGray,
  backgroundColor: THEME.color.blueGray25
}

type YesNoToogleProps = {
  initValue?: boolean
  onSubmit?: (response: boolean) => void
}

const YesNoToogle: React.FC<YesNoToogleProps> = ({ initValue, onSubmit }) => {
  const [value, setValue] = useState<boolean>()

  const handleResponse = (response: boolean) => {
    setValue(response)
    if (onSubmit) onSubmit(response)
  }

  useEffect(() => {
    if (isNil(value)) return
    setValue(initValue)
  }, [initValue])

  return (
    <Stack direction={'row'}>
      <Button
        Icon={Icon.Confirm}
        size={Size.NORMAL}
        accent={Accent.PRIMARY}
        onClick={() => handleResponse(true)}
        style={value === true ? activeStyle : inactiveStyle}
      >
        Oui
      </Button>
      <Button
        Icon={Icon.Reject}
        size={Size.NORMAL}
        accent={Accent.PRIMARY}
        onClick={() => handleResponse(false)}
        style={{ borderLeft: 'none', ...(value === false ? activeStyle : inactiveStyle) }}
      >
        Non
      </Button>
    </Stack>
  )
}

export default YesNoToogle