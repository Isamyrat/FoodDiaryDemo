import axios from 'axios'
import {config} from '../../Constants'
import {parseJwt} from './Helpers'

export const orderApi = {
    authenticate,
    getUsers,
    createCurator,
    createUser,
    deleteUserFromCurator,
    addUserToCurator,
    getFoodDiary,
    createFoodDiary,
    editFoodDiary,
    getSameProductName
}

function authenticate(username, password) {
    console.log('aga')
    console.log(config.url.API_BASE_URL)
    return instance.post('api/v1/auth/authenticate', {username, password}, {
        headers: {'Content-type': 'application/json'}
    })
}

function createCurator(username, password, user) {
    return instance.post('api/v1/auth/create-curator', {username, password}, {
        headers: {
            'Authorization': bearerAuth(user),
            'Content-type': 'application/json'
        }
    })
}

function createUser(username, password, user) {
    return instance.post('api/v1/auth/create-user', {username, password}, {
        headers: {
            'Authorization': bearerAuth(user),
            'Content-type': 'application/json'
        }
    })
}

function getUsers(user, url) {
    return instance.get(url, {
        headers: {'Authorization': bearerAuth(user)}
    })
}
function getSameProductName(value, user) {
    return instance.get(`/api/v1/food/diary/get/same/product/name?productName=${value}`, {
        headers: {'Authorization': bearerAuth(user)}
    })
}
function getFoodDiary(user, url) {
    return instance.get(url, {
        headers: {'Authorization': bearerAuth(user)}
    })
}

function deleteUserFromCurator(user, userId) {
    return instance.post(`/api/v1/user/removeUserFromCurator/${userId}`, {}, {
        headers: {
            'Authorization': bearerAuth(user),
            'Content-type': 'application/json'
        }
    })
}

function addUserToCurator(user, userId) {
    return instance.post(`/api/v1/user/addUserToCurator/${userId}`, {}, {
        headers: {
            'Authorization': bearerAuth(user),
            'Content-type': 'application/json'
        }
    })
}



function createFoodDiary(url, type, date, water, emotionalCondition, physicalState, product, width, user) {
    return instance.post(url, {type, water, emotionalCondition, physicalState, date, product, width}, {
        headers: {
            'Authorization': bearerAuth(user),
            'Content-type': 'application/json'
        }
    })
}

function editFoodDiary(foodDiaryId, type, date, water, emotionalCondition, physicalState, productDTOList, product, width, user) {
    return instance.post(`api/v1/food/diary/edit/${foodDiaryId}`, {
        type,
        water,
        emotionalCondition,
        physicalState,
        date,
        product,
        width,
        productDTOList
    }, {
        headers: {
            'Authorization': bearerAuth(user),
            'Content-type': 'application/json'
        }
    })
}

// -- Axios

const instance = axios.create({
    baseURL: config.url.API_BASE_URL
})

instance.interceptors.request.use(function (config) {
    // If token is expired, redirect user to login
    if (config.headers.Authorization) {
        const token = config.headers.Authorization.split(' ')[1]
        const data = parseJwt(token)
        if (Date.now() > data.exp * 1000) {
            window.location.href = "/login"
        }
    }
    return config
}, function (error) {
    return Promise.reject(error)
})

// -- Helper functions

function bearerAuth(user) {
    return `Bearer ${user.accessToken}`
}