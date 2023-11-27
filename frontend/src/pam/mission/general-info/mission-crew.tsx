import React, { useState } from 'react'
import { FlexboxGrid, Panel, Stack } from 'rsuite'
import {
  THEME,
  DateRangePicker,
  Label,
  TextInput,
  Select,
  Icon,
  Button,
  Accent,
  Size,
  Dropdown
} from '@mtes-mct/monitor-ui'
import { MissionGeneralInfo } from '../../types/mission-types'
import { MUTATION_ADD_OR_UPDATE_DISTANCE_CONSUMPTION } from '../queries'
import { useMutation } from '@apollo/client'
import omit from 'lodash/omit'
import { useParams } from 'react-router-dom'
import InputPicker from '../../../ui/input-picker'
import { ItemDataType } from 'rsuite/esm/@types/common'
import { AgentCrew } from '../../../types/crew-types'
import Text from '../../../ui/text'

interface MissionCrewProps {
  crew?: AgentCrew[]
}

const MissionCrew: React.FC<MissionCrewProps> = ({ crew }) => {
  const { missionId } = useParams()

  const [crewList, setCrewList] = useState<AgentCrew[] | undefined>(crew)

  const [mutate, { statusData, statusLoading, statusError }] = useMutation(
    MUTATION_ADD_OR_UPDATE_DISTANCE_CONSUMPTION,
    {
      // refetchQueries: [GET_MISSION_BY_ID]
    }
  )

  const onClickAddCrewMember = () => setCrewList([...(crewList || []), {} as AgentCrew])

  const onChange = async (field?: string, value?: any) => {
    // let updatedData = {
    //   ...omit(info, '__typename', 'infractions'),
    //   id: info?.id,
    //   missionId: missionId
    // }
    // if (!!field && !!value) {
    //   updatedData = {
    //     ...updatedData,
    //     [field]: value
    //   }
    // }
    // await mutate({ variables: { info: updatedData } })
  }

  const onCreateCrewMember = async (value: string) => {
    debugger
  }

  return (
    <>
      <Label>Equipage à bord</Label>
      <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%' }}>
        {!!!crewList || !crewList.length ? (
          <Stack.Item style={{ width: '100%', backgroundColor: THEME.color.gainsboro, padding: '0.5rem' }}>
            <Text as="h3">Aucun membre d'équipage renseigné</Text>
          </Stack.Item>
        ) : (
          crewList?.map((crew: AgentCrew) => (
            <Stack.Item key={crew.id} style={{ width: '100%', backgroundColor: THEME.color.gainsboro }}>
              <Stack
                direction="row"
                alignItems="flex-start"
                spacing="1rem"
                style={{ width: '100%', backgroundColor: THEME.color.gainsboro, padding: '0.5rem' }}
              >
                <Stack.Item style={{ flex: 1 }}>
                  <Label>Identité</Label>
                  <InputPicker
                    name="identity"
                    placeholder=""
                    // isLight={true}
                    data={[]}
                    value={crew?.agent?.firstName}
                    onChange={(nextValue?: string) => onChange('identity', nextValue)}
                    onCreate={(value: string, item: ItemDataType, event) => onCreateCrewMember(value)}
                  />
                </Stack.Item>
                <Stack.Item style={{ flex: 1 }}>
                  <Select
                    label="Fonction"
                    name="function"
                    isLight={true}
                    value={crew?.role?.title}
                    options={[]}
                    // disabled={!crew?.agent}
                    onChange={(nextValue?: string) => onChange('identity', nextValue)}
                  />
                </Stack.Item>
                <Stack.Item style={{ flex: 0 }} alignSelf="center">
                  <Label>&nbsp;</Label> {/*  fake label to align the icon with the fields */}
                  {/* <Icon.More /> */}
                  <Dropdown
                    Icon={Icon.More}
                    accent={Accent.SECONDARY}
                    onSelect={function noRefCheck() {}}
                    // open
                    title=""
                    placement="bottomEnd"
                  >
                    {/* <Dropdown.Item Icon={Icon.More} accent="SECONDARY" eventKey="ARCHIVE" /> */}
                    <Dropdown.Item eventKey="DELETE">Supprimer le membre</Dropdown.Item>
                  </Dropdown>
                </Stack.Item>
              </Stack>
            </Stack.Item>
          ))
        )}
        <Stack.Item>
          <Button
            onClick={() => onClickAddCrewMember()}
            accent={Accent.SECONDARY}
            size={Size.NORMAL}
            Icon={Icon.Plus}
            role="add-crew-member-button"
            isFullWidth={false}
          >
            Ajouter un membre d’équipage
          </Button>
        </Stack.Item>
      </Stack>
    </>
  )
}

export default MissionCrew
