export function parseJwt(token) {
    if (!token) {
        return
    }
    const base64Url = token.split('.')[1]
    const base64 = base64Url.replace('-', '+').replace('_', '/')
    return JSON.parse(window.atob(base64))
}

export const handleLogError = (error) => {
    if (error.response) {
        console.log(error.response.data);
    } else if (error.request) {
        console.log(error.request);
    } else {
        console.log(error.message);
    }
}

export const initFoodDiaryType = (type) => {
    let typeSecond
    if (type === 'FIRST_SNACK') {
        typeSecond = 'Первый перекус'
    } else if (type === 'BREAKFAST') {
        typeSecond = 'Завтрак'
    } else if (type === 'LUNCH') {
        typeSecond = 'Обед'
    } else if (type === 'SECOND_SNACK') {
        typeSecond = 'Второй перекус'
    } else if (type === 'DINNER') {
        typeSecond = 'Ужин'
    }

    return typeSecond
}

export const initFoodDiaryCreatedDate = (date) => {
    let createdDate
    const dateFormat = new Date(date);

    createdDate = dateFormat.getFullYear() +
        "-" + (initDate(dateFormat.getMonth() + 1)) +
        "-" +  initDate(dateFormat.getDate()) +
        " " + initDate(dateFormat.getHours()) +
        ":" + initDate(dateFormat.getMinutes())
    return createdDate
}

export const initDate = (minutes) => {
    let minute
    if (minutes < 10) {
        minute = '0' + minutes
    }else {
        minute = minutes
    }
    return minute
}