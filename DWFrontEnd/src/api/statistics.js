import axios from "axios"
const base = "http://10.60.43.53:8004/statistics/mysql"

export const getByTime = params => {
    return axios.get(`${base}/time`, {
        params
    })
}

export const getByScore = params => {
    return axios.get(`${base}/score`, {
        params
    })
}

export const statisticsByScore = () => {
    return axios.get(`${base}/score-all`)
}

export const statisticsByDirector = params => {
    return axios.get(`${base}/director-all`, {
        params
    })
}

export const statisticsByActor = params => {
    return axios.get(`${base}/actor-all`, {
        params
    })
}

export const statisticsByLabel = params => {
    return axios.get(`${base}/label-all`, {
        params
    })
}
