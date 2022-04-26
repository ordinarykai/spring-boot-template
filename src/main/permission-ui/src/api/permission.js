import request from '../utils/request'

// 获取权限分页列表
export const page = (query) => {
  return request({
    url: '/permission',
    method: 'get',
    params: query
  })
}

// 获取权限详情
export const query = (permissionId) => {
  return request({
    url: '/permission/' + permissionId,
    method: 'get'
  })
}

// 新增或修改权限
export const addOrUpdate = (data) => {
  return request({
    url: '/permission',
    method: 'post',
    data: data
  })
}

// 删除权限
export const remove = (permissionIds) => {
  return request({
    url: '/permission/' + permissionIds,
    method: 'delete'
  })
}

// 根据权限等级查询权限列表
export const listByLevel = (level) => {
  return request({
    url: '/permission/level/' + level,
    method: 'get'
  })
}

// 查询权限树
export const tree = (query) => {
  return request({
    url: '/permission/tree',
    method: 'get',
    params: query
  })
}
