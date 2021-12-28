import axios from "axios"
const base = "http://10.60.43.53:8004/query/mysql"

export const getByTitle = params => {
    return axios.get(`${base}/title`, {
        params
    })
}

export const getByActor = params => {
    return axios.get(`${base}/actor`, {
        params
    })
}

export const getByDirector = params => {
    return axios.get(`${base}/director`, {
        params
    })
}

export const getByLabel = params => {
    return axios.get(`${base}/label`, {
        params
    })
}

export const getByScore = params => {
    return axios.get(`${base}/score`, {
        params
    })
}

export const getByEmotion = params => {
    return axios.get(`${base}/emotion`, {
        params
    })
}

export const getByAsin = params => {
    return axios.get(`${base}/source`, {
        params
    })
}