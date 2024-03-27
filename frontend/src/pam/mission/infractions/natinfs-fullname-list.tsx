import React from 'react'
import Text from '../../../ui/text.tsx'
import useNatinfs from './use-natinfs.tsx'
import { Natinf } from '../../../types/infraction-types.ts'
import { Stack } from 'rsuite'

interface InfractionTagProps {
  natinfs?: string[]
}

const NatinfsFullNameList: React.FC<InfractionTagProps> = ({ natinfs }) => {
  const { data: allNatinfs, loading, error } = useNatinfs()
  if (loading || error || !natinfs) {
    return
  }

  function filterNatinfByCode(natinfCode: string, natinfs: Natinf[] | undefined = undefined): Natinf | undefined {
    return natinfs?.find(natinf => natinf.natinfCode.toString() === natinfCode.toString())
  }

  function createFilteredList(natinfs: string[]): Natinf[] {
    return natinfs
      .map(natinfCode => filterNatinfByCode(natinfCode, allNatinfs))
      .filter((natinf): natinf is Natinf => !!natinf)
  }

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

export default NatinfsFullNameList
