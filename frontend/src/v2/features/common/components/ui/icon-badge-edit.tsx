import { Icon, IconProps } from '@mtes-mct/monitor-ui'
import styled from 'styled-components'

export const IconBadgeEdit = styled((props: Omit<IconProps, 'label' | 'options'>) => {
  return (
    <div
      style={{
        width: 20,
        height: 20,
        color: 'white',
        display: 'flex',
        borderRadius: '50%',
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: '#5697D2'
      }}
    >
      <Icon.EditUnbordered size={15} {...props} />
    </div>
  )
})(() => ({}))
