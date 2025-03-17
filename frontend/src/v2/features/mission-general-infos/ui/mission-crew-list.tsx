import Text, { TextProps } from '@common/components/ui/text'
import {
  Accent,
  Button,
  ButtonProps,
  Icon,
  IconButton,
  IconButtonProps,
  Label,
  LabelProps,
  Size
} from '@mtes-mct/monitor-ui'
import { List, ListItemProps, Stack, StackProps } from 'rsuite'
import styled from 'styled-components'
import { Agent } from '../../common/types/crew-type'

export const MissionCrewListStyled = styled(List)({
  width: '100%',
  border: 'none',
  boxShadow: 'none'
})

export const MissionCrewListItemStyled = styled((props: ListItemProps & { index: number; length: number }) => (
  <List.Item {...props} />
))(({ theme, index, length }) => ({
  backgroundColor: 'inherit',
  paddingTop: 2,
  paddingBottom: 2,
  boxShadow: 'none',
  borderBottom: index === length - 1 ? 'none' : `1px solid ${theme.color.lightGray}`
}))

export const MissionCrewEditIconButton = styled((props: Omit<IconButtonProps, 'Icon'>) => (
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

export const MissionCrewDeleteIconButton = styled((props: Omit<IconButtonProps, 'Icon'>) => (
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

export const MissionCrewMemberText = styled(
  ({ agent, ...props }: Omit<TextProps, 'as' | 'children'> & { agent?: Agent }) => (
    <Text
      {...props}
      as="h3"
      truncate="true"
      data-testid="crew-member-agent"
    >{`${agent?.firstName} ${agent?.lastName}`}</Text>
  )
)(({ theme }) => ({
  color: theme.color.gunMetal
}))

export const MissionCrewTitleLabel = styled((props: LabelProps) => <Label {...props} />)(({ theme }) => ({
  fontWeight: 'bold',
  color: theme.color.gunMetal
}))

export const MissionCrewUnderlineStack = styled((props: StackProps) => (
  <Stack {...props} direction="row" alignItems="center" />
))(({ theme }) => ({
  paddingBottom: 4,
  borderBottom: `0.5px solid ${theme.color.lightGray}`,
  marginBottom: 8
}))

export const MissionCrewAddMemberButton = styled((props: ButtonProps) => (
  <Button
    Icon={Icon.Plus}
    size={Size.SMALL}
    isFullWidth={true}
    accent={Accent.SECONDARY}
    role="add-crew-member-button"
    data-testid="add-crew-member-button"
    {...props}
  />
))(({ theme }) => ({
  color: theme.color.charcoal
}))

export const MissionCrewStack = styled((props: StackProps) => (
  <Stack {...props} direction="column" alignItems="flex-start" spacing="0.25rem" />
))({
  width: '100%'
})
