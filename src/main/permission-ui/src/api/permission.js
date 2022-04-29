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

// 查询权限下拉树
export const treeSelect = () => {
  return request({
    url: '/permission/tree/select',
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
