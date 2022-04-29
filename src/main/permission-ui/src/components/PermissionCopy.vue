<template>
  <div class="app-container">

    <el-form :model="queryParams" size="small" :inline="true" label-width="68px">
      <el-form-item label="权限名称">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入权限名称"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否关联">
        <el-select v-model="queryParams.associateStatus" placeholder="请选择是否关联">
          <el-option
          v-for="item in associatedList"
          :key="item.value"
          :label="item.label"
          :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="handeTree">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
    </el-row>

    <el-table :data="permissionList"
    row-key="permissionId"
    :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    @selection-change="handleSelectionChange"
    style="width: 100%">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="name" label="权限名称" />
      <el-table-column prop="parentName" label="父级权限名称" width="180" />
      <el-table-column prop="type" label="权限类型" />
      <el-table-column prop="uri" label="uri" />
      <el-table-column prop="icon" label="菜单图标" />
      <el-table-column prop="num" label="排序号" />
      <el-table-column prop="createTime" label="创建时间" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleAdd(scope.row)"
          >新增</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin-top: 20px">
      <el-pagination
        @current-change="handleQuery"
        :current-page.sync="queryParams.page"
        :page-size="queryParams.size"
        layout="prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>

    <!-- 添加或修改权限对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="权限名称">
          <el-input v-model="form.name" placeholder="请输入权限名称" />
        </el-form-item>
        <el-form-item label="权限类型">
            <el-select v-model="form.type" placeholder="请选择权限类型">
              <el-option
              v-for="item in typeList"
              :key="item.value"
              :label="item.label"
              :value="item.value">
              </el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="上级权限">
          <treeselect
          v-model="form.parentId"
          :options="permissionParentList"
          :show-count="true"
          placeholder="选择上级菜单"
          />
        </el-form-item>
         <el-form-item label="uri">
          <el-input v-model="form.uri" placeholder="请输入uri (菜单权限是页面的uri，按钮权限是method:uri)" />
        </el-form-item>
        <el-form-item label="菜单图标">
          <el-input v-model="form.icon" @focus="showIcon()" placeholder="请输入菜单图标 (菜单权限有效)" />
        </el-form-item>
        <el-form-item label="排序号">
          <el-input v-model="form.num" placeholder="请输入排序号" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 图标集合 -->
    <el-dialog :title="iconTitle" :visible.sync="iconOpen" width="1000px" append-to-body>
      <el-form>
      <el-form-item label="输入icon class搜索">
        <el-input
          v-model="iconClass"
          placeholder="输入icon class搜索"
          clearable
          style="width: 240px"
          @input="updateIconClass()"
        />
      </el-form-item>
      </el-form>
      <el-row :gutter="20">
        <el-col
        v-for="icon in iconList"
        :key="icon.class"
        :xs="3" :sm="3" :md="3" :lg="3" :xl="3">
          <div @click="changeIcon(icon.class)" style="margin-bottom: 30px; height: 51px; text-align:center">
            <li :class="icon.class" style="font-size: 32px"></li>
            <div>{{icon.class}}</div>
          </div>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import { page, addOrUpdate, remove, query, listByLevel, tree } from '../api/permission'
import { Message } from 'element-ui'
import Treeselect from '@riophae/vue-treeselect'
import iconJson from '../assets/icon.json'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: 'PermissionCopy',
  components: { Treeselect },
  data () {
    return {
      permissionList: [],
      queryParams: {
        page: 1,
        size: 10,
        name: ''
      },
      ids: [],
      typeList: [{
        label: '菜单',
        value: 1
      }, {
        label: '按钮',
        value: 2
      }],
      levelList: [{
        label: '一级',
        value: 1
      }, {
        label: '二级',
        value: 2
      }, {
        label: '三级',
        value: 3
      }],
      permissionParentList: [],
      title: '',
      // 是否显示弹出层
      open: false,
      // 新增修改权限数据
      form: {
        name: '',
        type: '',
        parentId: null,
        uri: '',
        icon: '',
        num: undefined
      },
      permissionId: undefined,
      permissionTree: undefined,
      treeTitle: '查看权限树',
      treeOpen: false,
      expandedKeys: [],
      total: undefined,
      associatedList: [{
        label: '全部',
        value: undefined
      }, {
        label: '是',
        value: 1
      }, {
        label: '否',
        value: 2
      }],
      iconList: [],
      iconTitle: '选择图标',
      iconOpen: false,
      iconClass: '',
      permissionTableTree: [],
      disabled: false
    }
  },
  created () {
    this.handleQuery()
  },
  methods: {
    handleQuery () {
      page(this.queryParams).then(response => {
        this.permissionList = response.data.records
        console.log(this.permissionList)
        this.total = response.data.total
      })
    },
    queryTree2 () {
      let permissionList = this.permissionList = this.permissionList.filter(permission => {
        return permission.name.indexOf(this.queryParams.name) != -1
      })
      this.permissionTableTree = permissionList.filter(permission => {
        return permission.parentId == 0
      })
      this.permissionTableTree.forEach((permission, index) => {
        permission.children = this.handeTree(permission.permissionId)
      })
      console.log(this.permissionTableTree)
    },
    handeTree (parentId) {
      let list = this.permissionList.filter(permission => {
        return permission.parentId == parentId
      })
      list.forEach((permission, index) => {
        permission.children = this.handeTree(permission.permissionId)
      })
      return list
    },
    resetQuery () {
      this.queryParams = {
        page: 1,
        size: 10
      }
      this.handleQuery()
    },
    handleAdd (row) {
      this.reset()
      if (row.level !== undefined) {
        this.form.level = row.level + 1
        this.form.parentId = row.permissionId + ''
        this.disabled = true
      } else {
        this.disabled = false
      }
      this.title = '新增权限'
      this.open = true
      this.getListByLevel()
    },
    handleUpdate (row) {
      this.reset()
      const permissionId = row.permissionId || this.ids
      query(permissionId).then(response => {
        this.form = response.data
        this.permissionId = response.data.permissionId
        this.open = true
        this.title = '修改权限'
        this.form.parentId = this.form.parentId + ''
        this.getListByLevel()
      })
    },
    handleDelete () {
      if (this.ids.length === 0) {
        Message({
          message: '请选择至少一行数据',
          type: 'error'
        })
        return
      }
      remove(this.ids).then(response => {
        this.handleQuery()
      })
    },
    cancel () {
      this.open = false
    },
    // 多选框选中数据
    handleSelectionChange (selection) {
      this.ids = selection.map(item => item.permissionId)
    },
    // 提交表单
    submitForm () {
      if (this.form.level === 1) {
        this.form.parentId = 0
      }
      addOrUpdate(this.form).then(response => {
        Message({
          message: response.message
        })
        this.open = false
        this.handleQuery()
      })
    },
    // 表单重置
    reset () {
      this.form = {
        permissionId: '',
        name: '',
        type: '',
        parentId: null,
        uri: '',
        icon: ''
      }
    },
    // 查询父级权限菜单
    getListByLevel () {
      listByLevel(1).then(response => {
        console.log(response.data)
        this.permissionParentList = response.data
      })
    },
    queryTree () {
      this.treeOpen = true
      tree().then(response => {
        this.permissionTree = response.data
        this.permissionTree.forEach(permission => {
          if (permission.children.length != 0) {
            this.expandedKeys.push(permission.value)
          }
        })
      })
    },
    showIcon () {
      this.iconOpen = true
      this.iconList = iconJson
    },
    changeIcon (icon) {
      this.form.icon = icon
      this.iconOpen = false
    },
    updateIconClass () {
      this.iconList = iconJson.filter(icon => icon.class.indexOf(this.iconClass) != -1)
    }
  }
}
</script>

<style scoped>

</style>
