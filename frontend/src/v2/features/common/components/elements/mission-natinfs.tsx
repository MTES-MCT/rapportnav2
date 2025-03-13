import Text from '@common/components/ui/text'
import { Natinf } from '@common/types/infraction-types.ts'
import React from 'react'
import { Stack } from 'rsuite'
import useNatinfListQuery from '../../services/use-natinf-service'

interface MissionNatinfsProps {
  natinfs?: string[]
}

const MissionNatinfs: React.FC<MissionNatinfsProps> = ({ natinfs }) => {
  const { data: allNatinfs, isLoading, error } = useNatinfListQuery()
  if (isLoading || error || !natinfs) {
    return
  }

  const filterNatinfByCode = (natinfCode: string, natinfs?: Natinf[]): Natinf | undefined =>
    natinfs?.find(natinf => natinf.natinfCode.toString() === natinfCode.toString())

  const createFilteredList = (natinfs: string[]): Natinf[] =>
    natinfs.map(natinfCode => filterNatinfByCode(natinfCode, allNatinfs)).filter((natinf): natinf is Natinf => !!natinf)

  const natinfsWithName: Natinf[] = createFilteredList(natinfs)

  return (
    <Stack direction={'column'} alignItems={'flex-start'} spacing={'0.2rem'}>
      {!natinfsWithName?.length
        ? '--'
        : natinfsWithName?.map((natinf: Natinf) => (
            <Stack.Item key={natinf.natinfCode}>
              <Text as={'h3'} weight={'medium'}>
                {`${natinf.natinfCode} - ${natinf.infraction}`}
              </Text>
            </Stack.Item>
          ))}
    </Stack>
  )
}

export default MissionNatinfs
