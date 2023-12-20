import React from 'react'
import {Mission} from '../../types/env-mission-types'
import {Col, FlexboxGrid, Loader, Stack} from 'rsuite'
import {Accent, Button, Icon, Size} from '@mtes-mct/monitor-ui'
import MissionsList from './missions-list'
import {Link} from 'react-router-dom'
import {useQuery} from '@apollo/client/react'
import {GET_MISSIONS} from './queries'
import {GET_MISSION_BY_ID} from '../mission/queries'

const Missions: React.FC = () => {
    const {loading, error, data, client} = useQuery(GET_MISSIONS)

    const prefetchMission = async (missionId: string) => {
        await client.query({
            query: GET_MISSION_BY_ID,
            variables: {missionId}
        })
    }

    if (data) {
        const missions: Mission[] = data.missions as Mission[]
        return (
            <FlexboxGrid
                // align="middle"
                justify="center"
                style={{padding: '4rem 2rem', display: 'flex', flex: 1}}
            >
                <FlexboxGrid.Item as={Col} colspan={24} xxl={16}>
                    <Stack direction="column" alignItems="flex-start">
                        <Stack.Item>
                            <h3>Missions</h3>
                        </Stack.Item>

                        <Stack.Item style={{paddingTop: '2rem'}}>
                            <h4>Mes rapports de mission</h4>
                        </Stack.Item>

                        <Stack.Item alignSelf="flex-end" style={{paddingTop: '2rem'}}>
                            <Link to={`/pam/missions/0`}>
                                <Button accent={Accent.PRIMARY} Icon={Icon.Plus} size={Size.NORMAL} disabled={true}>
                                    Créer un rapport de mission
                                </Button>
                            </Link>
                        </Stack.Item>

                        <Stack.Item style={{paddingTop: '2rem', width: '100%'}}>
                            {loading ? (
                                <Stack justifyContent="center" style={{marginTop: '5rem'}}>
                                    <Stack.Item>
                                        <Loader/>
                                    </Stack.Item>
                                </Stack>
                            ) : (
                                <MissionsList missions={missions} prefetchMission={prefetchMission}/>
                            )}
                        </Stack.Item>
                    </Stack>
                </FlexboxGrid.Item>
            </FlexboxGrid>
        )
    }

    return null
}

export default Missions
