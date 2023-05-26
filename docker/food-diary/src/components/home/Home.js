import React, {Component} from 'react'
import {Container, Dimmer, Loader, Segment} from 'semantic-ui-react'

class Home extends Component {
    state = {
        isLoading: false,
    }

    render() {
        const {isLoading} = this.state
        if (isLoading) {
            return (
                <Segment basic style={{marginTop: window.innerHeight / 2}}>
                    <Dimmer active inverted>
                        <Loader inverted size='huge'>Loading</Loader>
                    </Dimmer>
                </Segment>
            )
        } else {
            return (
                <Container >

                </Container>
            )
        }
    }
}

export default Home