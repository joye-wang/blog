<template>
  <div>
    <div>
      <el-button icon="el-icon-refresh" @click="listParam"></el-button>
      <el-button
        type="primary"
        icon="el-icon-plus"
        style="margin-right: 20px"
        @click="dialogVisible = true"
      >添加参数</el-button>
    </div>
    <el-table :data="params" stripe>
      <el-table-column prop="id" width="100" label="编号"></el-table-column>
      <el-table-column label="key" prop="k"></el-table-column>
      <el-table-column prop="val" label="参数值"></el-table-column>
      <el-table-column prop="remark" label="备注"></el-table-column>
      <el-table-column label="创建时间" prop="createTime"></el-table-column>
      <el-table-column label="预览">
        <template slot-scope="scope">
          <el-image
            style="width: 100px; height: 100px"
            v-if="scope.row.type=='img'"
            :src="scope.row.val"
            :preview-src-list="[scope.row.val]"
          ></el-image>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            size="medium"
            plain
            icon="el-icon-edit"
            type="success"
            @click="paramForm = JSON.parse(JSON.stringify(scope.row)); dialogVisible = true"
          >编辑</el-button>
          <el-button
            size="medium"
            plain
            icon="el-icon-delete"
            type="danger"
            @click="deleteParam(scope.row.id)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :visible.sync="dialogVisible" @close="paramForm={}">
      <el-form :model="paramForm" ref="paramForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="键" prop="k" required>
          <el-input v-model="paramForm.k"></el-input>
        </el-form-item>
        <el-form-item label="值" prop="val" required>
          <el-input v-model="paramForm.val"></el-input>
        </el-form-item>
        <el-form-item label="类型">
          <el-input v-model="paramForm.type"></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="paramForm.remark" type="textarea"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submit">提交</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { listParam, deleteParam, updateParam, createParam } from "@/api/param";
import { upload } from "@/api/upload";
export default {
  data() {
    return {
      params: [],
      paramForm: {},
      dialogVisible: false,
      previewImgUrl: ""
    };
  },
  methods: {
    listParam() {
      listParam().then(res => {
        this.params = res.data.data;
      });
    },
    updateParam() {
      updateParam(this.paramForm).then(res => {
        this.$message.success("更新成功");
        this.dialogVisible = false;
        this.listParam();
      });
    },
    createParam() {
      createParam(this.paramForm).then(res => {
        this.$message.success("添加成功");
        this.dialogVisible = false;
        this.listParam();
      });
    },
    deleteParam(id) {
      this.$confirm("此操作不可恢复，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteParam(id).then(res => {
          this.$message.success("删除成功");
          this.listParam();
        });
      });
    },
    preview(url) {
      this.previewDialogVisible = true;
      this.previewImgUrl = url;
    },
    submit() {
      if (this.paramForm.id) {
        this.updateParam();
      } else {
        this.createParam();
      }
    }
  },
  mounted() {
    this.listParam();
  }
};
</script>

<style >
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.modify-button {
  float: right;
}
.user-info {
  margin: auto;
}
</style>