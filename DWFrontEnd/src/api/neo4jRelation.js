import axios from "axios"
const base = "http://10.60.43.53:8081/relationships/neo4j"

export const ngetActorByDirector = params => {
    return axios.get(`${base}/director-actor`, {
        params
    })
}

export const ngetActorByActor = params => {
    return axios.get(`${base}/actor-actor`, {
        params
    })
}

export const ngetDirectorByDirector = params => {
    return axios.get(`${base}/director-director`, {
        params
    })
}

export const ngetDirectorByActor = params => {
    return axios.get(`${base}/actor-director`, {
        params
    })
}
