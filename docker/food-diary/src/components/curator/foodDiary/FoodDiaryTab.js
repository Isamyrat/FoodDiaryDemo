import React from 'react'
import { Tab } from 'semantic-ui-react'
import FoodDiaryTable from "./FoodDiaryTable";
import {useParams} from "react-router-dom";

function FoodDiaryTab() {
    const {userId} = useParams();
    const panes = [
        {
            menuItem: { key: 'users', icon: 'tasks', content: 'Дневник питания' },
            render: () => (
                <Tab.Pane>
                    <FoodDiaryTable
                        userId={userId}
                    />

                </Tab.Pane>
            )
        }
    ]

    return (
        <Tab menu={{ attached: 'top'}} panes={panes} />
    )
}

export default FoodDiaryTab