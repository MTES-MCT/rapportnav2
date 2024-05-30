import { Accent, Icon, IconButton, IconButtonProps, IconProps, Size } from '@mtes-mct/monitor-ui'
import { FlexboxGrid, List, ListItemProps, Stack } from 'rsuite'
import styled from 'styled-components'
import { Agent, MissionCrew as MissionCrewModel } from '../../../types/crew-types'
import Text, { TextProps } from '../../../ui/text'

const CrewList = styled(List)({
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

const CommentIcon = styled((props: IconProps & { comment?: string }) => (
  <Icon.Comment data-testid="crew-member-comment-icon" {...props} size={14} />
))(({ theme, comment }) => ({
  marginRight: 13,
  color: `${!comment || comment === '' ? theme.color.lightGray : theme.color.charcoal}`
}))

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

const TruncateCommentText = styled((props: Omit<TextProps, 'as'>) => <Text {...props} as="h3" truncate />)(
  ({ theme }) => ({
    fontStyle: 'italic',
    color: theme.color.charcoal
  })
)

const CrewText = styled((props: Omit<TextProps, 'as'>) => <Text {...props} as="h3" truncate />)(({ theme }) => ({
  color: theme.color.gunMetal
}))

const CrewAgentText = styled(({ agent, ...props }: Omit<TextProps, 'as' | 'children'> & { agent?: Agent }) => (
  <Text {...props} as="h3" truncate data-testid="crew-member-agent">{`${agent?.firstName}, ${agent?.lastName}`}</Text>
))(({ theme }) => ({
  color: theme.color.gunMetal
}))

interface CrewListProps {
  crewList: MissionCrewModel[]
  handleEditCrew: (id: string) => void
  handleDeleteCrew: (id: string) => void
}

const MissionCrewMemberList: React.FC<CrewListProps> = ({ crewList, handleEditCrew, handleDeleteCrew }) => {
  return (
    <CrewList>
      {crewList?.map((crew: MissionCrewModel, index) => (
        <CrewListItem key={crew.id} index={index} length={crewList.length}>
          <FlexboxGrid align={'middle'}>
            <FlexboxGrid.Item colspan={7}>{<CrewAgentText agent={crew?.agent} />}</FlexboxGrid.Item>
            <FlexboxGrid.Item colspan={7}>
              <CrewText>{crew.role?.title}</CrewText>
            </FlexboxGrid.Item>
            <FlexboxGrid.Item colspan={7}>
              <FlexboxGrid align={'middle'} justify="start">
                <FlexboxGrid.Item colspan={4}>
                  <CommentIcon comment={crew.comment} />
                </FlexboxGrid.Item>
                <FlexboxGrid.Item colspan={20}>
                  <TruncateCommentText>{crew.comment}</TruncateCommentText>
                </FlexboxGrid.Item>
              </FlexboxGrid>
            </FlexboxGrid.Item>
            <FlexboxGrid.Item colspan={3}>
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
