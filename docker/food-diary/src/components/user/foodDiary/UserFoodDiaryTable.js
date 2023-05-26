import React, {Component} from 'react'
import {Button, Container, Icon, Table} from 'semantic-ui-react'
import AuthContext from "../../context/AuthContext";
import {orderApi} from "../../misc/OrderApi";
import {handleLogError, initFoodDiaryCreatedDate, initFoodDiaryType} from "../../misc/Helpers";
import "../../assets/css/tableFooter.css"
import {Link} from "react-router-dom";

class UserFoodDiaryTable extends Component {
    static contextType = AuthContext

    constructor(props) {
        super(props);
        this.state = {
            foodDiaryList: [],
            userUsernameSearch: '',
            hasPrev: false,
            hasMore: false,
            prev: 0,
            next: 0,
            size: 0,
        }
    }

    componentDidMount() {
        this.handleGetFoodDiary()
    }

    handleGetFoodDiary = () => {
        const Auth = this.context
        const user = Auth.getUser()

        const url = '/api/v1/food/diary/show/all'

        this.getFoodDiary(user, url);
    }


    prevPage = () => {
        const Auth = this.context
        const user = Auth.getUser()

        let url = `/api/v1/food/diary/show/all?page=${this.state.prev}&size=${this.state.size}`

        this.getFoodDiary(user, url);
    }

    nextPage = () => {
        const Auth = this.context
        const user = Auth.getUser()
        let url = `/api/v1/food/diary/show/all?page=${this.state.next}&size=${this.state.size}`

        this.getFoodDiary(user, url);
    }

    getFoodDiary(user, url) {
        orderApi.getFoodDiary(user, url)
            .then(response => {
                this.init(response)
            })
            .catch(error => {
                handleLogError(error)
            })
    }

    handleAddProduct = (userId) => {
        const Auth = this.context
        const user = Auth.getUser()

        orderApi.addProduct(user, userId)
            .then(() => {
                this.handleGetUsers()
            })
            .catch(error => {
                handleLogError(error)
            })
    }
    init = (response) => {
        this.setState({
            foodDiaryList: response.data.foodDiaryDTOList,
            hasPrev: response.data.hasPrev,
            hasMore: response.data.hasMore,
            prev: response.data.prev,
            next: response.data.next,
            size: response.data.size
        })
    }
    initFoodDiary = () => {
        let foodDairy
        if (this.state.foodDiaryList.length === 0) {
            foodDairy = (
                <Table.Row key='foodDiaryEmpty'>
                    <Table.Cell textAlign='center' colSpan='6'>food diary is empty</Table.Cell>
                </Table.Row>
            )
        } else {
            foodDairy = this.state.foodDiaryList.map(foodDiary => {
                return (
                    <Table.Row key={foodDiary.id}>
                        <Table.Cell>
                            <Link to={`/edit-food-diary/${foodDiary.id}`} key={foodDiary.id}>
                                <Button
                                    compact
                                    circular
                                    color='orange'
                                    size='small'
                                    icon='edit'
                                />
                            </Link>
                        </Table.Cell>
                        <Table.Cell>{initFoodDiaryType(foodDiary.type)}</Table.Cell>
                        <Table.Cell>{initFoodDiaryCreatedDate(foodDiary.createdDate)}</Table.Cell>
                        <Table.Cell>{this.initProductList(foodDiary.productDTOList)}</Table.Cell>
                        <Table.Cell>{foodDiary.water} ml</Table.Cell>
                        <Table.Cell>{foodDiary.emotionalCondition}</Table.Cell>
                        <Table.Cell>{foodDiary.physicalState}</Table.Cell>


                    </Table.Row>
                )
            })
        }
        return foodDairy;
    }

    initProductList = (productDTOList) => {
        let productList
        if (productDTOList.length === 0) {
            productList = (
                'пусто'
            )
        } else if (productDTOList.length === 1) {
            productList = productDTOList.map(products => {
                return (<>
                        {products.product} : {products.width} кл
                        <br/>
                    </>
                )
            })
        } else {
            productList = productDTOList.map(products => {
                return (
                    <>
                        {products.product} : {products.width} кл
                        <br/>
                    </>
                )
            })
        }
        return productList;
    }

    render() {
        const {
            hasPrev,
            hasMore
        } = this.state
        return (
            <Container>
                <Table compact striped selectable textAlign={"center"}>
                    <Table.Header>
                        <Table.Row>
                            <Table.HeaderCell width={2}>Изменить дневник</Table.HeaderCell>
                            <Table.HeaderCell width={3}>Тип</Table.HeaderCell>
                            <Table.HeaderCell width={3}>Дата</Table.HeaderCell>
                            <Table.HeaderCell width={5} textAlign={"center"}>Список продуктов
                                <br/>
                                Продукт : вес
                            </Table.HeaderCell>
                            <Table.HeaderCell width={1}>Количество воды</Table.HeaderCell>
                            <Table.HeaderCell width={2}>Эмоциональное состояние</Table.HeaderCell>
                            <Table.HeaderCell width={2}>Психологическое состояние</Table.HeaderCell>
                        </Table.Row>
                    </Table.Header>
                    <Table.Body>
                        {this.initFoodDiary()}
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

export default UserFoodDiaryTable