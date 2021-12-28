import axios from "axios"
const base = "http://10.60.43.53:8081/statistics/hive"

export const hgetByTime = params => {
    return axios.get(`${base}/time`, {
        params
    })
}

export const hgetByScore = params => {
    return axios.get(`${base}/score`, {
        params
    })
}

export const hstatisticsByScore = () => {
    return axios.get(`${base}/score-all`)
}

export const hstatisticsByDirector = params => {
    return axios.get(`${base}/director-all`, {
        params
    })
}

export const hstatisticsByActor = params => {
    return axios.get(`${base}/actor-all`, {
        params
    })
}

export const hstatisticsByLabel = params => {
    return axios.get(`${base}/label-all`, {
        params
    })
}
