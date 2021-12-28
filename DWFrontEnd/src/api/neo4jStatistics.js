import axios from "axios"
const base = "http://10.60.43.53:8081/statistics/neo4j"

export const ngetByTime = params => {
    return axios.get(`${base}/time`, {
        params
    })
}

export const ngetByScore = params => {
    return axios.get(`${base}/score`, {
        params
    })
}

export const nstatisticsByScore = () => {
    return axios.get(`${base}/score-all`)
}

export const nstatisticsByLabel = params => {
    return axios.get(`${base}/label-all`, {
        params
    })
}

export const nstatisticsByActor = params => {
    return axios.get(`${base}/actor-all`, {
        params
    })
}

export const nstatisticsByDirector = params => {
    return axios.get(`${base}/director-all`, {
        params
    })
}
