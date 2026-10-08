import Api from "./Api"

export const getAllProjects = () => {
  return Api.get('/project')
}

export const getProjectById = (id) => {
  return Api.get(`/project/${id}`)
}

export const createProject = (data) => {
  return Api.post('/project', data)
}

export const updateProject = (id, data) => {
  return Api.put(`/project/${id}`, data)
}

export const deleteProject = (id) => {
  return Api.delete(`/project/${id}`)
}