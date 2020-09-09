<template>
  <div>
    <div>
      <el-button icon="el-icon-refresh" @click="listCategory"></el-button>
      <el-button
        type="primary"
        icon="el-icon-plus"
        style="margin-right: 20px"
        @click="addArchvieDialogVisible = true"
      >添加目录</el-button>
    </div>
    <el-table :data="categories" stripe>
      <el-table-column prop="id" width="100" label="编号"></el-table-column>
      <el-table-column label="标题" prop="name"></el-table-column>
      <el-table-column prop="postCount" width="100" label="文章数量"></el-table-column>
      <el-table-column label="创建时间">
        <template slot-scope="scope">
          <i class="el-icon-time"></i>
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            size="medium"
            plain
            icon="el-icon-edit"
            type="success"
            @click="tempRow = JSON.parse(JSON.stringify(scope.row)); editArchvieDialogVisible = true"
          >编辑</el-button>
          <el-button
            size="medium"
            plain
            icon="el-icon-delete"
            type="danger"
            @click="deleteCategory(scope.row.id)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="添加目录" :visible.sync="addArchvieDialogVisible" width="30%">
      <el-input v-model="newCategory.name" placeholder="请输入目录名字"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addArchvieDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addCategory">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="修改目录" :visible.sync="editArchvieDialogVisible" width="30%">
      <el-input v-model="tempRow.name"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editArchvieDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateCategory">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  listCategory,
  addCategory,
  updateCategory,
  deleteCategory
} from "@/api/category";
export default {
  data() {
    return {
      total: 0,
      categories: [],
      // 用来修改的category
      tempRow: {},
      // 用来添加的category
      newCategory: {
        name: ""
      },
      editArchvieDialogVisible: false,
      addArchvieDialogVisible: false
    };
  },
  methods: {
    addCategory() {
      addCategory(this.newCategory).then(res => {
        this.addArchvieDialogVisible = false;
        this.$message.success("添加成功");
        this.listCategory();

    
      });
    },
    updateCategory() {
      updateCategory(this.tempRow).then(res => {
        this.editArchvieDialogVisible = false;
        this.$message.success("修改成功");
        this.listCategory();
      });
    },
    listCategory() {
      listCategory().then(res => {
        this.categories = res.data.data;
      });
    },
    deleteCategory(id) {
      this.$confirm("此操作不可恢复，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteCategory(id).then(res => {
          this.$message.success("删除成功");
          this.listCategory();
        });
      });
    }
  },
  created() {
    this.listCategory();
  }
};
</script>
