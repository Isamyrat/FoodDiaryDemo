import React, {Component} from 'react'
import {Button, Container, Form, Icon, Input, Table} from 'semantic-ui-react'
import AuthContext from "../../context/AuthContext";
import {orderApi} from "../../misc/OrderApi";
import {handleLogError} from "../../misc/Helpers";
import "../../assets/css/tableFooter.css"
import {Link} from "react-router-dom";

class UserTable extends Component {
    static contextType = AuthContext

    state = {
        users: [],
        userUsernameSearch: '',
        hasPrev: false,
        hasMore: false,
        prev: 0,
        next: 0,
        data: '',
        size: 0,
    }

    componentDidMount() {
        this.handleGetUsers()
    }

    handleInputChange = (e, {name, value}) => {
        this.setState({[name]: value})
    }

    handleGetUsers = () => {
        const Auth = this.context
        const user = Auth.getUser()

        const url = '/api/v1/user/show/all/curator/users'

        this.getUsers(user, url);
    }

    resetSearchUser = () => {
        const Auth = this.context
        const user = Auth.getUser()

        this.setState({userUsernameSearch: ''})
        const url = '/api/v1/user/show/all/curator/users'

        this.getUsers(user, url);
    }

    handleSearchUser = () => {
        const Auth = this.context
        const user = Auth.getUser()

        const username = this.state.userUsernameSearch
        const url = username ? `/api/v1/user/show/all/curator/users?data=${username}` : '/api/v1/user/show/all/curator/users'

        orderApi.getUsers(user, url)
            .then(response => {
                this.init(response)
            })
            .catch(error => {
                handleLogError(error)
                this.setState({users: []})
            })
    }

    prevPage = () => {
        const Auth = this.context
        const user = Auth.getUser()

        let url = `/api/v1/user/show/all/curator/users?page=${this.state.prev}&size=${this.state.size}`

        if (this.data !== '') {
            url += `&data=${this.state.data}`
            this.setState({userUsernameSearch: this.state.data})
        }

        this.getUsers(user, url);
    }

    nextPage = () => {
        const Auth = this.context
        const user = Auth.getUser()
        let url = `/api/v1/user/show/all/curator/users?page=${this.state.next}&size=${this.state.size}`

        if (this.data !== '') {
            url += `&data=${this.state.data}`
            this.setState({userUsernameSearch: this.state.data})
        }
        this.getUsers(user, url);
    }

    getUsers(user, url) {
        orderApi.getUsers(user, url)
            .then(response => {
                this.init(response)
            })
            .catch(error => {
                handleLogError(error)
            })
    }

    init = (response) => {
        this.setState({
            users: response.data.userList,
            hasPrev: response.data.hasPrev,
            hasMore: response.data.hasMore,
            prev: response.data.prev,
            next: response.data.next,
            size: response.data.size,
            data: response.data.data
        })
    }
    handleDeleteUser = (userId) => {
        const url = '/api/v1/user/show/all/curator/users'

        const Auth = this.context
        const user = Auth.getUser()

        orderApi.deleteUserFromCurator(user, userId)
            .then(() => {
                this.getUsers(user, url);
            })
            .catch(error => {
                handleLogError(error)
            })
    }

    initUserList = () => {
        let userList
        if (this.state.users.length === 0) {
            userList = (
                <Table.Row key='no-user'>
                    <Table.Cell textAlign='center' colSpan='6'>No user</Table.Cell>
                </Table.Row>
            )

        } else {
            userList = this.state.users.map(user => {
                return (
                    <Table.Row key={user.id}>
                        <Table.Cell>{user.id}</Table.Cell>
                        <Table.Cell>{user.username}</Table.Cell>
                        <Table.Cell >
                                <Link to={`/get/food/diary/${user.id}`} className="btn btn-dark">check</Link>
                        </Table.Cell>
                        <Table.Cell>
                            <Button
                                compact
                                circular
                                color='red'
                                size='small'
                                icon='trash'
                                disabled={user.username === 'admin'}
                                onClick={() => this.handleDeleteUser(user.id)}
                            />
                        </Table.Cell>
                    </Table.Row>
                )
            })
        }
        return userList;
    }

    render() {
        const {
            userUsernameSearch,
            hasPrev,
            hasMore
        } = this.state
        return (
            <Container>
                <Form>
                    <Input type='text' placeholder='Поищем...' name='userUsernameSearch' value={userUsernameSearch}
                           onChange={this.handleInputChange} action>
                        <input/>

                        <Button
                            circular
                            color='white'
                            size='mini'
                            icon='search'
                            disabled={userUsernameSearch === null || userUsernameSearch === ''}
                            onClick={() => this.handleSearchUser()}
                        />
                        <Button
                            circular
                            color='black'
                            size='mini'
                            icon='times'
                            disabled={userUsernameSearch === null || userUsernameSearch === ''}
                            onClick={() => this.resetSearchUser()}
                        />
                    </Input>
                </Form>

                <Table compact striped selectable textAlign={"center"}>
                    <Table.Header>
                        <Table.Row>
                            <Table.HeaderCell width={1}>ID</Table.HeaderCell>
                            <Table.HeaderCell width={3}>Username</Table.HeaderCell>
                            <Table.HeaderCell width={3}>Посмотреть дневник питания</Table.HeaderCell>
                            <Table.HeaderCell width={1}>Удалить</Table.HeaderCell>
                        </Table.Row>
                    </Table.Header>
                    <Table.Body>
                        {this.initUserList()}
                    </Table.Body>
                </Table>
                <div className="rightAlign">
                    <Button.Group>
                        <Button animated
                                disabled={hasPrev === false}
                                onClick={() => this.prevPage()}>
                            <Button.Content visible>Prev</Button.Content>
                            <Button.Content hidden>
                                <Icon name='arrow left'/>
                            </Button.Content>
                        </Button>
                        <Button animated
                                disabled={hasMore === false}
                                onClick={() => this.nextPage()}>
                            <Button.Content visible>Next</Button.Content>
                            <Button.Content hidden>
                                <Icon name='arrow right'/>
                            </Button.Content>
                        </Button>
                    </Button.Group>
                </div>
            </Container>

        )
    }
}

export default UserTable