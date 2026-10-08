import Api from "./Api"

export const getAllTasks = () => {
  return Api.get('/task')
}

export const getTaskById = (id) => {
  return Api.get(`/task/${id}`)
}

export const createTask = (data) => {
  return Api.post('/task', data)
}

export const updateTask = (id, data) => {
  return Api.put(`/task/${id}`, data)
}

export const deleteTask = (id) => {
  return Api.delete(`/task/${id}`)
}

export const updateTaskStatus = (id, status) => {
  return Api.patch(`/task/${id}/status`, { status })
}