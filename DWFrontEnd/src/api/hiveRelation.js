import axios from "axios"
const base = "http://10.60.43.53:8081/relationships/hive"

export const hgetActorByDirector = params => {
    return axios.get(`${base}/actor-director`, {
        params
    })
}

export const hgetActorByActor = params => {
    return axios.get(`${base}/actor-actor`, {
        params
    })
}

export const hgetDirectorByDirector = params => {
    return axios.get(`${base}/director-director`, {
        params
    })
}

export const hgetDirectorByActor = params => {
    return axios.get(`${base}/director-actor`, {
        params
    })
}
