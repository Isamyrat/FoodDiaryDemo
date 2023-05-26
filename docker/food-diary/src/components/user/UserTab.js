import React from 'react'
import { Tab } from 'semantic-ui-react'
import UserFoodDiaryTable from "./foodDiary/UserFoodDiaryTable";
import CreateNewFoodDiaryRecord from "./foodDiary/CreateNewFoodDiaryRecord";

function UserTab() {
    const panes = [
        {
            menuItem: { key: 'users', icon: 'tasks', content: 'Дневник питания' },
            render: () => (
                <Tab.Pane>
                    <UserFoodDiaryTable />

                </Tab.Pane>
            )
        },
        {
            menuItem: { key: 'users', icon: 'tasks', content: 'Добавить новую запись' },
            render: () => (
                <Tab.Pane>
                    <CreateNewFoodDiaryRecord />

                </Tab.Pane>
            )
        },
    ]

    return (
        <Tab menu={{ attached: 'top'}} panes={panes} />
    )
}

export default UserTab