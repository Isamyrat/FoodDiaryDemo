import React from 'react'
import { Tab } from 'semantic-ui-react'
import UserTable from './UserTable'
import AddCurator from './AddCurator'
import AddUser from "./AddUser";
import "../../assets/css/tabs.css"
function UsersTab() {
    const panes = [
        {
            menuItem: { key: 'users', icon: 'users', content: 'Все пользователи' },
            render: () => (
                <Tab.Pane >
                    <UserTable />

                </Tab.Pane>
            )
        },
        {
            menuItem: { key: 'addCurator', icon: 'users', content: 'Добавить куратора' },
            render: () => (
                <Tab.Pane>
                    <AddCurator />

                </Tab.Pane>
            )
        },
        {
            menuItem: { key: 'addUser', icon: 'users', content: 'Добавить пользователя' },
            render: () => (
                <Tab.Pane>
                    <AddUser />

                </Tab.Pane>
            )
        }
    ]

    return (
        <Tab menu={{ attached: 'top', tabular: true, className: 'tabFlex'}} panes={panes} />
    )
}

export default UsersTab