import Api from "./Api"

export const getUserProfile = () => {
  return Api.get('/user/profile')
}

export const updateUserProfile = (data) => {
  return Api.put('/user/profile', data)
}

export const getAllUsers =()=>{
  return Api.get('/user')
}