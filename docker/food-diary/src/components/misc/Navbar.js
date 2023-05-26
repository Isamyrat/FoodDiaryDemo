import React from 'react'
import {Link} from 'react-router-dom'
import {Container, Menu} from 'semantic-ui-react'
import {useAuth} from '../context/AuthContext'

function Navbar() {
    const {getUser, userIsAuthenticated, userLogout} = useAuth()

    const logout = () => {
        userLogout()
    }

    const enterMenuStyle = () => {
        return userIsAuthenticated() ? {"display": "none"} : {"display": "block"}
    }

    const logoutMenuStyle = () => {
        return userIsAuthenticated() ? {"display": "block"} : {"display": "none"}
    }

    const adminPageStyle = () => {
        const user = getUser()
        return user && user.data.rol[0] === 'ROLE_ADMIN' ? {"display": "block"} : {"display": "none"}
    }

    const userPageStyle = () => {
        const user = getUser()
        return user && user.data.rol[0] === 'ROLE_USER' ? {"display": "block"} : {"display": "none"}
    }

    const curatorPageStyle = () => {
        const user = getUser()
        return user && user.data.rol[0] === 'ROLE_CURATOR' ? {"display": "block"} : {"display": "none"}
    }

    const getUserName = () => {
        const user = getUser()
        return user ? user.data.preferred_username : ''
    }

    return (
            <Menu inverted  size='massive' stackable style={{borderRadius: 0}}>
                <Container fluid>
                    <Menu.Item header>Food diary</Menu.Item>
                    <Menu.Item as={Link} exact='true' to="/">Home</Menu.Item>
                    <Menu.Item as={Link} to="/adminPage" style={adminPageStyle()}>AdminPage</Menu.Item>
                    <Menu.Item as={Link} to="/userPage" style={userPageStyle()}>UserPage</Menu.Item>
                    <Menu.Item as={Link} to="/curatorPage" style={curatorPageStyle()}>CuratorPage</Menu.Item>
                    <Menu.Menu position='right'>
                        <Menu.Item as={Link} to="/login" style={enterMenuStyle()}>Login</Menu.Item>
                        <Menu.Item header style={logoutMenuStyle()}>{`Hi ${getUserName()}`}</Menu.Item>
                        <Menu.Item as={Link} to="/" style={logoutMenuStyle()} onClick={logout}>Logout</Menu.Item>
                    </Menu.Menu>
                </Container>
            </Menu>
    )
}

export default Navbar
