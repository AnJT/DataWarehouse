import axios from "axios"
const base = "http://10.60.43.53:8081/query/neo4j"

export const ngetByTitle = params => {
    return axios.get(`${base}/title`, {
        params
    })
}

export const ngetByActor = params => {
    return axios.get(`${base}/actor`, {
        params
    })
}

export const ngetByDirector = params => {
    return axios.get(`${base}/director`, {
        params
    })
}

export const ngetByLabel = params => {
    return axios.get(`${base}/label`, {
        params
    })
}

export const ngetByScore = params => {
    return axios.get(`${base}/score`, {
        params
    })
}

export const ngetByEmotion = params => {
    return axios.get(`${base}/emotion`, {
        params
    })
}
