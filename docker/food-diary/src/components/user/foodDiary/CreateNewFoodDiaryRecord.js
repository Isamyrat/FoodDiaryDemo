import React, {Component} from 'react'

import AuthContext from '../../context/AuthContext'
import {orderApi} from "../../misc/OrderApi";
import {handleLogError, initFoodDiaryCreatedDate} from "../../misc/Helpers";
import {Button, Form, Grid, Icon, Input, Message, Segment} from "semantic-ui-react";
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
import {DateTimeInput} from 'semantic-ui-calendar-react'
import "../../assets/css/button.css"

const options = [{key: 'b', text: 'Завтрак', value: 'BREAKFAST'}, {
    key: 'f',
    text: 'Первый перекус',
    value: 'FIRST_SNACK'
}, {key: 'l', text: 'Обед', value: 'LUNCH'}, {key: 's', text: 'Второй перекус', value: 'SECOND_SNACK'}, {
    key: 'd',
    text: 'Ужин',
    value: 'DINNER'
}]

class CreateNewFoodDiaryRecord extends Component {
    static contextType = AuthContext

    state = {
        id: 0,
        type: '',
        water: 0,
        emotionalCondition: '',
        physicalState: '',
        createdDate: '',
        product: '',
        width: 0,
        isError: false,
        isAccess: false,
        results: [],
        tag: '',
        productDTOList: [],
    }


    handleInputChange = (e, {name, value}) => {
        this.setState({[name]: value})
    }

    handleDropdownChange(e, data) {
        this.setState({type: data.value})
    }
    handleInputChangeProductSecond(mm, id, productName) {
        let str
        if (mm !== null) {
            str = mm.target.value
        } else {
            str = productName
        }
        if (str === 0) {
            str = productName
        }
        if (str === undefined) {
            str = ''
            this.setState({
                results: [],
            })
            this.handlerChangesProduct(str, id)
            return;
        }
        if (str.length < 2) {
            this.setState({
                results: [],
            })
            this.handlerChangesProduct(str, id)
            return;
        }

        const Auth = this.context
        const user = Auth.getUser()

        orderApi.getSameProductName(str, user)
            .then(response => {
                this.setState({
                    results: response.data.productDTOList,
                })
                this.handlerChangesProduct(str, id)
            })
            .catch(error => {
                handleLogError(error)
                this.setState({isError: true})
            })
    };

    handleInputChangeProduct = (value) => {
        const str = value.target.value
        if (str.length < 2) {
            this.setState({
                results: [],
                product: str
            })

            return;
        }

        const Auth = this.context
        const user = Auth.getUser()

        orderApi.getSameProductName(str, user)
            .then(response => {
                this.setState({
                    results: response.data.productDTOList,
                    product: str,
                })
            })
            .catch(error => {
                handleLogError(error)
                this.setState({isError: true})
            })
    };
    handlerChangesProduct = (name, id) => {
        const productsList = this.state.productDTOList

        if (name !== '' || name !== 0 || name !== null) {
            const newState = productsList.map(products => {
                if (products.id === id && products.lastProductName !== name) {
                    return {...products, lastProductName: name, product: name};
                } else if (products.id === id && products.lastProductName === '') {
                    return {...products, lastProductName: name, product: name};
                }
                return products;
            })

            this.setState({productDTOList: newState},
                function () {
                    console.log(this.state.productDTOList, 'productDTOList');
                });
        }
    }
    handlerChangesProductFlag = (name, id) => {
        const productsList = this.state.productDTOList

        if (name !== '' || name !== 0 || name !== null) {
            const newState = productsList.map(products => {
                if (products.id === id) {
                    return {...products, product: name};
                }
                return products;
            })

            const newState2 = newState.map(products => {
                if (products.id === id) {
                    return {...products, flag: true};
                }
                return products;
            })

            this.setState({productDTOList: newState2},
                function () {
                    console.log(this.state.productDTOList, 'productDTOList');
                });

        }
    }
    handlerChangesWidth = (name, id) => {
        const productsList = this.state.productDTOList

        const newState = productsList.map(products => {
            if (products.id === id) {
                return {...products, width: name};
            }
            return products;
        })

        this.setState({
            productDTOList: newState,
        })
    }
    handleSubmit = (e) => {
        e.preventDefault()
        const Auth = this.context
        const user = Auth.getUser()

        const {id, type, water, emotionalCondition, physicalState, createdDate, product, width, tag, productDTOList} = this.state
        let productName
        if (tag !== '') {
            productName = tag
        } else if (product !== 0 || product !== '') {
            productName = product
        }

        if (!(type && water && emotionalCondition && physicalState && createdDate && productName && width)) {
            this.setState({
                isError: true,
                isAccess: false,
            })
            return
        }

        let url
        if (id === 0 || id === null) {
            url = 'api/v1/food/diary/create'
            orderApi.createFoodDiary(url, type, createdDate, water, emotionalCondition, physicalState, productName, width, user)
                .then(response => {
                    this.init(response);
                })
                .catch(error => {
                    handleLogError(error)
                    this.setState({isError: true})
                })
        } else {
            let flag = false;

            productDTOList.forEach((products) => {
                console.log(products.product)
                if (products.product === '' || products.product === 0) {
                    this.setState({
                        isError: true,
                        isAccess: false,
                    })
                    return flag = true;
                }
            });
            if (flag){
                return
            }
            orderApi.editFoodDiary(id, type, createdDate, water, emotionalCondition, physicalState, productDTOList, productName, width, user)
                .then(response => {
                    this.init(response)
                })
                .catch(error => {
                    handleLogError(error)
                    this.setState({isError: true})
                })
        }
    }

    init(response) {
        let isError
        let isAccess

        if (response.data.successful) {
            isError = false
            isAccess = true
        } else {
            isError = true
            isAccess = false
        }

        const dateFormat = new Date(response.data.foodDiaryDTO.createdDate);
        this.setState({
            isError: isError,
            isAccess: isAccess,
            id: response.data.foodDiaryDTO.id,
            type: response.data.foodDiaryDTO.type,
            water: response.data.foodDiaryDTO.water,
            emotionalCondition: response.data.foodDiaryDTO.emotionalCondition,
            physicalState: response.data.foodDiaryDTO.physicalState,
            createdDate: initFoodDiaryCreatedDate(dateFormat),
            productDTOList: response.data.foodDiaryDTO.productDTOList,
            product: '',
            width: 0,
            results: [],
            tag: '',
        })
    }

    onTagsChange = (event, values) => {
        this.setState({
            tag: values.product
        }, () => {
            console.log(this.state.tag);
        });
    }
    onProductChange(event, values) {
        setTimeout(() => {
            const id = values
            let productName
            if (event.target.textContent !== '') {
                productName = event.target.textContent
                this.handlerChangesProductFlag(productName, id)
                this.setState({
                    results: []
                });
            }
        }, 7)
    }
    initNewProduct = () => {
        const product = this.state.product
        const width = this.state.width
        const results = this.state.results
        return (
            <Form.Group widths='equal'>
                <label className="dateLabelInCreateNewRecordFoodDiary">
                    <Autocomplete
                        autoFocus
                        freeSolo
                        id="combo-box-demo"
                        options={results}
                        onChange={this.onTagsChange}
                        onInputChange={async (
                            value: string,
                        ) => {
                            this.handleInputChangeProduct(value);
                        }}
                        disableClearable
                        getOptionLabel={(option) => option.product}
                        style={{width: 350}}
                        clearOnEscape
                        renderInput={(params) => <
                            TextField
                            autoFocus
                            value={product}
                            label={"Название продукта"}
                            {...params}
                            variant="outlined"/>}
                    />
                </label>
                <Form.Field
                    fluid="true"
                    autoFocus
                    control={Input}
                    label='Вес (в гр)'
                    value={width}
                    name='width'
                    icon='balance scale'
                    iconPosition='left'
                    placeholder='Вес (в гр)'
                    type="number"
                    min="1"
                    onChange={this.handleInputChange}
                    required
                />
                <label className="dateLabelInCreateNewRecordFoodDiary">
                    Add
                    <Button
                        autoFocus
                        circular
                        color={"green"}
                        size={"medium"}
                        icon={'plus'}
                        type={"submit"}
                    />
                </label>
            </Form.Group>)
    }
    initProductList = () => {
        let productList
        const productsList = this.state.productDTOList
        const results = this.state.results
        if (productsList.length === 0) {
            productList = this.initNewProduct()
        } else {
            productList = productsList.map(products => {
                return (
                    <Form.Group widths='equal'>
                        <label className="dateLabelInCreateNewRecordFoodDiary">
                            <Autocomplete
                                autoFocus
                                freeSolo
                                id={products.id}
                                options={results}
                                value={products}
                                onChange={(e) => this.onProductChange(e, products.id)}
                                onInputChange={(
                                    mm: string,
                                ) => {
                                    this.handleInputChangeProductSecond(mm, products.id, products.product);
                                }}
                                getOptionLabel={(option) => option.product}
                                style={{width: 350}}
                                clearOnEscape
                                renderInput={(params) => <
                                    TextField
                                    autoFocus
                                    //   value={products}
                                    label={"Название продукта"}
                                    {...params}
                                    variant="outlined"/>}
                            />
                        </label>
                        <Form.Field
                            fluid
                            autoFocus
                            control={Input}
                            label='Вес (в гр)'
                            value={products.width}
                            name='data'
                            icon='balance scale'
                            iconPosition='left'
                            placeholder='Вес (в гр)'
                            type="number"
                            min="1"
                            onChange={(e) => this.handlerChangesWidth(e.target.value, products.id)}
                            required
                        />
                    </Form.Group>)
            })
        }
        return productList
    }

    render() {
        const {
            isError, type, isAccess, water, emotionalCondition, physicalState, createdDate,
        } = this.state
        return (<Grid textAlign='center'>
            <Grid.Column style={{maxWidth: 750}}>
                <Form size='large' onSubmit={this.handleSubmit}>
                    <Segment>
                        <Form.Group widths='equal'>
                            <Form.Field
                                fluid="true"
                                autoFocus
                            >
                                Дата
                                <DateTimeInput
                                    dateTimeFormat="YYYY-MM-DD HH:mm"
                                    name="createdDate"
                                    placeholder="Date Time"
                                    value={createdDate}
                                    iconPosition="left"
                                    onChange={this.handleInputChange}
                                    inlineLabel={true}
                                    closable={true}
                                    clearable={true}
                                    clearIcon={<Icon name="remove" color="red"/>}

                                />
                            </Form.Field>
                            <Form.Field
                                fluid="true"
                                autoFocus
                                control={Input}
                                label='Количество воды (в мл)'
                                value={water}
                                name='water'
                                icon='tint'
                                iconPosition='left'
                                placeholder='Количество воды'
                                type="number"
                                min="1"
                                onChange={this.handleInputChange}
                                required
                            />
                            <Form.Select
                                fluid="true"
                                id='date'
                                label='Тип'
                                options={options}
                                value={type}
                                placeholder='Тип'
                                onChange={(e, data) => this.handleDropdownChange(e, data)}
                                required
                            />
                        </Form.Group>
                        <Form.Group widths='equal'>
                            <Form.TextArea
                                fluid="true"
                                autoFocus
                                label='Эмоциональное состояние'
                                value={emotionalCondition}
                                name='emotionalCondition'
                                placeholder='Tell about emotional condition'
                                onChange={this.handleInputChange}
                                required
                            />

                            <Form.TextArea
                                fluid="true"
                                autoFocus
                                label='Физическое состояние'
                                value={physicalState}
                                name='physicalState'
                                placeholder='Tell about emotional condition'
                                onChange={this.handleInputChange}
                                required
                            />
                        </Form.Group>
                        {this.initProductList()}
                        {this.state.productDTOList.length !== 0 ? this.initNewProduct() : ''}
                    </Segment>
                </Form>
                {isAccess && <Message positive>Product is created!</Message>}
                {isError && <Message negative>Something is wrong are incorrect!</Message>}
            </Grid.Column>
        </Grid>)
    }
}

export default CreateNewFoodDiaryRecord