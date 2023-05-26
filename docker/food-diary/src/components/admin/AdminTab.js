import React from 'react'
import {Tab} from 'semantic-ui-react'
import UsersTab from './user/UsersTab'


function AdminTab() {

    const panes = [
        {
            menuItem: {key: 'users', icon: 'users', content: 'Пользователи'},
            render: () => (
                <Tab.Pane>
                    <UsersTab />

                </Tab.Pane>
            )

        }
    ]

    return (
        <Tab menu={{attached: 'top', fluid: true, vertical: true}} panes={panes}/>
    )
}

export default AdminTab