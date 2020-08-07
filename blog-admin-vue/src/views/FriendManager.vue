<template>
  <div>
    <div>
      <el-button icon="el-icon-refresh" @click="listFriend"></el-button>
      <el-button
        type="primary"
        icon="el-icon-plus"
        style="margin-right: 20px"
        @click="dialogVisible = true"
      >添加友链</el-button>
    </div>
    <el-table :data="friends" stripe>
      <el-table-column label="编号" prop="id"></el-table-column>
      <el-table-column label="标题" prop="name"></el-table-column>
      <el-table-column label="链接">
        <template slot-scope="scope">
          <a type="primary" target="_blank" :href="scope.row.link">{{scope.row.link}}</a>
        </template>
      </el-table-column>
      <el-table-column label="备注" prop="remark"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <!-- 先stringfy再parse，实现深拷贝 -->
          <el-button
            size="medium"
            plain
            icon="el-icon-edit"
            type="success"
            @click="tempRow= JSON.parse(JSON.stringify(scope.row)); dialogVisible = true;"
          >编辑</el-button>
          <el-button
            size="medium"
            plain
            icon="el-icon-delete"
            type="danger"
            @click="deleteFriend(scope.row.id)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="tempRow.id?'编辑友链':'添加友链'" :visible.sync="dialogVisible" width="30%" @close="tempRow={}">
      <el-form label-width="80px" :model="tempRow" :rules="rules">
        <el-form-item label="友链名称" prop="name">
          <el-input v-model="tempRow.name"></el-input>
        </el-form-item>
        <el-form-item label="地址" prop="link">
          <el-input v-model="tempRow.link"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="tempRow.remark"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="tempRow.id?updateFriend():addFriend()">确 定</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import {
  addFriend,
  updateFriend,
  deleteFriend,
  listFriend
} from "@/api/friend";
export default {
  data() {
    return {
      friends: [],
      // 当前正在编辑的行
      tempRow: {},
      dialogVisible: false,
      rules: {
        name: [{ required: true, message: "请输入名称", trigger: "blur" }],
        link: [{ required: true, message: "请输入链接地址", trigger: "blur" }],
      }
    };
  },
  methods: {
    listFriend() {
      // 取json数据
      listFriend().then(res => {
        this.friends = res.data.data;
      });
    },
    addFriend() {
      if (!this.tempRow.link.startsWith("http")) {
        this.$message.error("请输入http/https前缀");
        return;
      }
      addFriend(this.tempRow).then(res => {
        this.$message.success("成功");
        this.dialogVisible = false;
        this.listFriend();
      });
    },
    deleteFriend(friendId) {
      this.$confirm("此操作不可恢复，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteFriend(friendId).then(res => {
          this.$message.success("删除成功");
          this.listFriend();
        });
      });
    },
    updateFriend() {
      if (!this.tempRow.link.startsWith("http")) {
        this.$message.error("请输入http/https前缀");
        return;
      }
      updateFriend(this.tempRow).then(res => {
        this.$message.success("成功");
        this.dialogVisible = false;
        this.listFriend();
      });
    }
  },
  created() {
    this.listFriend();
  }
};
</script>
