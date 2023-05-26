import React from 'react'
import { Tab } from 'semantic-ui-react'
import UserTable from './user/UserTable'
import NotCuratorUsersTable from "./user/NotCuratorUsersTable";

function CuratorTab() {
    const panes = [
        {
            menuItem: { key: 'users', icon: 'users', content: 'Все пользователи' },
            render: () => (
                <Tab.Pane>
                    <UserTable />

                </Tab.Pane>
            )
        },
        {
            menuItem: { key: 'addUsers', icon: 'users', content: 'Добавить новых пользователей' },
            render: () => (
                <Tab.Pane>
                    <NotCuratorUsersTable />

                </Tab.Pane>
            )
        }
    ]

    return (
        <Tab menu={{ attached: 'top'}} panes={panes} />
    )
}

export default CuratorTab