import React, { FC } from 'react'
import { Stack } from 'rsuite'
import MissionListItem from './mission-list-item.tsx'
import MissionListHeader from './mission-list-header.tsx'
import { Mission } from '@common/types/mission-types.ts'

interface MissionListingProps {
  missions?: Mission[]
}


const MissionListing: FC<MissionListingProps> = ({missions}) => {
  return (
    <Stack direction="column" alignItems="flex-start" spacing="0.5rem" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <MissionListHeader isUlam={true} />
      </Stack.Item>
      <Stack.Item style={{ width: '100%', height: '100%' }} >
        <MissionListItem isUlam={true} />
      </Stack.Item>
    </Stack>
  )
}

export default MissionListing
