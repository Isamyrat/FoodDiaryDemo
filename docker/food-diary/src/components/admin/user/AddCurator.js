import React, {Component} from 'react'
import {Button, Form, Grid, Message, Segment} from 'semantic-ui-react'

import AuthContext from '../../context/AuthContext'
import {orderApi} from '../../misc/OrderApi'
import {handleLogError} from '../../misc/Helpers'
import {Navigate} from "react-router-dom";


class AddCurator extends Component {
    static contextType = AuthContext

    state = {
        username: '',
        password: '',
        isError: false,
        isAccess: false,
    }

    handleInputChange = (e, {name, value}) => {
        this.setState({[name]: value})
    }
    handleSubmit = (e) => {
        e.preventDefault()
        const Auth = this.context
        const user = Auth.getUser()
        const { username, password } = this.state

        if (!(username && password)) {
            this.setState({ isError: true })
            return
        }

        orderApi.createCurator(username, password, user)
            .then(response => {
                console.log(response)
                let isError
                let isAccess

                if(response.data.successful){
                    isError = false
                    isAccess = true
                }else {
                    isError = true
                    isAccess = false
                }

                this.setState({
                    isError: isError,
                    isAccess: isAccess,
                    username: '',
                    password: ''
                })
            })
            .catch(error => {
                handleLogError(error)
                this.setState({ isError: true })
            })
    }
    render() {
        const { isLoggedIn, isError, isAccess, username, password } = this.state
        if (isLoggedIn) {
            return <Navigate to={'/'} />
        } else {
            return (
                <Grid textAlign='center'>
                    <Grid.Column style={{ maxWidth: 450 }}>
                        <Form size='large' onSubmit={this.handleSubmit}>
                            <Segment>
                                <Form.Input
                                    fluid
                                    autoFocus
                                    value={username}
                                    name='username'
                                    icon='user'
                                    iconPosition='left'
                                    placeholder='Username'
                                    onChange={this.handleInputChange}
                                />
                                <Form.Input
                                    fluid
                                    name='password'
                                    value={password}
                                    icon='lock'
                                    iconPosition='left'
                                    placeholder='Password'
                                    type='password'
                                    onChange={this.handleInputChange}
                                />
                                <Button color='black' fluid size='large'>Create curator</Button>
                            </Segment>
                        </Form>
                        {isAccess && <Message positive>Curator is created!</Message>}
                        {isError && <Message negative>The username or password curator are incorrect!</Message>}
                    </Grid.Column>
                </Grid>
            )
        }
    }
}

export default AddCurator