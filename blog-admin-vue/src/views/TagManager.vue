<template>
  <div>
    <el-button icon="el-icon-refresh" @click="listTag"></el-button>
    <el-button type="primary" icon="el-icon-plus" style="margin-right: 20px" @click="addTag">添加新标签</el-button>
    <div style="display:flex;flex-wrap: wrap">
      <div v-for="(tag,index) in tags" :key="tag.tagId" style="display:flex; margin-top: 20px">
        <el-input size="small" placeholder="请输入标签名字" v-model="tag.name" :disabled="!tagsEditing[index]" style="width: 200px; margin-right:10px">
        </el-input>
        <div style="display:flex; margin-right: 40px">
          <div v-if="tagsEditing[index]">
            <el-button size="small" type="primary" @click="updateTag(tag);tagsEditing.splice(index,1,false)" icon="el-icon-check"></el-button>
            <el-button size="small" @click="tagsEditing.splice(index,1,false)" icon="el-icon-close"></el-button>
          </div>
          <div v-else style="display:flex;">
            <el-button size="small" type="success" icon="el-icon-edit" @click="tagsEditing.splice(index,1,true)" plain></el-button>
            <el-button size="small" type="danger" icon="el-icon-delete" @click="deleteTag(tag.tagId)" plain></el-button>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import { listTag, addTag, updateTag, deleteTag } from "@/api/tag";
export default {
  data() {
    return {
      tags: [],
      tagsEditing: [],
      typeArr: ["", "success", "info", "warning", "danger"]
    };
  },
  methods: {
    listTag() {
      listTag().then(res => {
        this.tags = res.data.data;
        this.tagsEditing = new Array(this.tags.length).fill(false);
      });
    },
    addTag() {
      this.$prompt("请输入标签名称", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消"
      })
        .then(({value}) => { //这个地方需要加花括号，因为参数是一个object，含有value属性，即输入的字符串
          addTag({ name: value }).then(res => {
            this.$message.success("添加成功");
            this.listTag();
          });
        })
        .catch(() => {});
    },
    deleteTag(tagId) {
      this.$confirm(
        "此操作不可恢复，会同时删除文章中对应的标签，是否继续？",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      ).then(() => {
        deleteTag(tagId).then(res => {
          this.$message.success("删除成功");
          this.listTag();
        });
      });
    },
    updateTag(tag) {
      updateTag(tag).then(res => {
        this.$message.success("修改成功");
        // this.listTag();
      });
    }
  },
  created() {
    this.listTag();
  }
};
</script>
