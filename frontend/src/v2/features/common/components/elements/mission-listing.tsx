import React, { FC, useState } from 'react'
import { Stack } from 'rsuite'
import MissionListItem from './mission-list-item.tsx'
import MissionListHeader from './mission-list-header.tsx'
import { Mission } from '@common/types/mission-types.ts'

interface MissionListingProps {
  missions?: Mission[]
  isUlam: boolean
}


const MissionListing: FC<MissionListingProps> = ({missions, isUlam}) => {
  const [openIndex, setOpenIndex] = useState<number | null>(null);
  return (
    <Stack direction="column" alignItems="flex-start" spacing="0.5rem" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <MissionListHeader isUlam={isUlam} />
      </Stack.Item>
      <Stack.Item style={{ width: '100%', height: '100%' }} >
        {missions?.map((mission, index) => (
            <MissionListItem
              isUlam={isUlam}
              mission={mission}
              index={index}
              openIndex={openIndex}
              setOpenIndex={setOpenIndex}
            />
          ))
        }
      </Stack.Item>
    </Stack>
  )
}

export default MissionListing
