import { Accent, Icon, IconButton, IconButtonProps, Size } from '@mtes-mct/monitor-ui'
import { FlexboxGrid, List, ListItemProps, Stack } from 'rsuite'
import styled from 'styled-components'
import Text, { TextProps } from '@common/components/ui/text.tsx'
import { Agent, MissionCrew as MissionCrewModel } from '../../../../../common/types/crew-types.ts'


const CrewList = styled(List)({
  width: '100%',
  border: 'none',
  boxShadow: 'none'
})

const CrewListItem = styled((props: ListItemProps & { index: number; length: number }) => <List.Item {...props} />)(
  ({ theme, index, length }) => ({
    backgroundColor: 'inherit',
    paddingTop: 2,
    paddingBottom: 2,
    boxShadow: 'none',
    borderBottom: index === length - 1 ? 'none' : `1px solid ${theme.color.lightGray}`
  })
)

const EditIconButton = styled((props: Omit<IconButtonProps, 'Icon'>) => (
  <IconButton
    {...props}
    role="edit-crew"
    size={Size.NORMAL}
    accent={Accent.TERTIARY}
    Icon={Icon.EditUnbordered}
    data-testid="edit-crew-member-icon"
  />
))(({ theme }) => ({
  color: theme.color.charcoal
}))

const DeleteIconButton = styled((props: Omit<IconButtonProps, 'Icon'>) => (
  <IconButton
    {...props}
    role="delete-crew"
    size={Size.NORMAL}
    accent={Accent.TERTIARY}
    Icon={Icon.Delete}
    data-testid="delete-crew-member-icon"
  />
))(({ theme }) => ({
  color: theme.color.maximumRed
}))

const CrewAgentText = styled(({ agent, ...props }: Omit<TextProps, 'as' | 'children'> & { agent?: Agent }) => (
  <Text {...props} as="h3" truncate data-testid="crew-member-agent">{`${agent?.firstName} ${agent?.lastName}`}</Text>
))(({ theme }) => ({
  color: theme.color.gunMetal
}))

interface CrewListProps {
  crewList: Agent[]
  handleEditCrew: (id: string) => void
  handleDeleteCrew: (id: number) => void
}

const MissionCrewMemberList: React.FC<CrewListProps> = ({ crewList, handleEditCrew, handleDeleteCrew }) => {
  return (
    <CrewList>
      {crewList?.map((crew: MissionCrewModel, index) => (
        <CrewListItem key={crew.id} index={index} length={crewList.length}>
          <FlexboxGrid align={'middle'}>
            <FlexboxGrid.Item colspan={18}>{<CrewAgentText agent={crew?.agent} />}</FlexboxGrid.Item>
            <FlexboxGrid.Item colspan={6}>
              <Stack direction="row">
                <Stack.Item>
                  <EditIconButton onClick={() => handleEditCrew(crew.id)} />
                </Stack.Item>
                <Stack.Item>
                  <DeleteIconButton onClick={() => handleDeleteCrew(crew.id)} />
                </Stack.Item>
              </Stack>
            </FlexboxGrid.Item>
          </FlexboxGrid>
        </CrewListItem>
      ))}
    </CrewList>
  )
}

export default MissionCrewMemberList
