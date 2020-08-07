<template>
  <div>
    <div class="container">
      <h2>编写文章</h2>
      <el-input v-model="post.title" placeholder="请输入标题"></el-input>
      <div style="margin: 20px 0">
        <el-select v-model="post.categoryId" filterable placeholder="请选择文章目录" clearable>
          <el-option
            v-for="category in categories"
            :key="category.id"
            :label="category.name"
            :value="category.id"
          ></el-option>
        </el-select>
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="medium"
          style="margin: 0 20px"
          @click="addCategoryDialogVisible=true"
        >添加目录</el-button>
        <el-select
          v-model="post.tagIds"
          filterable
          clearable
          multiple
          :multiple-limit="5"
          style="width:400px"
          placeholder="请选择标签，最多5个"
        >
          <el-option v-for="tag in tags" :key="tag.tagId" :label="tag.name" :value="tag.tagId"></el-option>
        </el-select>
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="medium"
          style="margin: 0 20px"
          @click="addTagDialogVisible=true"
        >创建标签</el-button>
      </div>

      <mavon-editor v-model="post.mdContent" ref="editor"/>
    </div>

    <div class="bottom-menu">
      <div style="max-width:1260px; margin: auto">
        <span style="margin-right:10px">展示</span>
        <el-switch v-model="isShow"></el-switch>
        <el-button
          type="success"
          style="float: right; margin-right: 20px"
          :disabled="submitDisabled"
          @click="submit"
        >{{submitText}}</el-button>
      </div>
    </div>

    <el-dialog title="添加文章目录" :visible.sync="addCategoryDialogVisible" width="30%">
      <el-input v-model="category.name" placeholder="请输入目录名称"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addCategory" :disabled="!category.name">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="添加标签" :visible.sync="addTagDialogVisible" width="30%">
      <el-input v-model="tag.name" placeholder="请输入标签名称"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addTag" :disabled="!tag.name">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import mavonEditor from "mavon-editor";
import "mavon-editor/dist/css/index.css";
import { getPost, publishPost, editPost } from "@/api/post";
import { getUrlParam } from "@/util/url";
import { listCategory, addCategory } from "@/api/category";
import { listTag, addTag } from "@/api/tag";
import { upload } from "@/api/upload";

export default {
  data() {
    return {
      postId: 0,
      post: { mdContent: "" },
      addCategoryDialogVisible: false,
      addTagDialogVisible: false,
      category: {},
      tag: {},
      tags: [],
      isShow: true,
      categories: []
    };
  },
  computed: {
    submitDisabled() {
      return !this.post.title || !this.post.mdContent;
    },
    submitText() {
      return this.postId ? "确认编辑" : "确认发表";
    }
  },
  methods: {
    getPost() {
      getPost(this.postId).then(res => {
        this.post = res.data.data;
        this.status = this.post.status == window.ARTICLE.SHOW;
        this.$set(
          this.post,
          "tagIds",
          this.post.postTags.map(tag => {
            return tag.tagId;
          })
        );
      });
    },
    listCategory() {
      listCategory().then(res => {
        this.categories = res.data.data;
      });
    },
    listTag() {
      listTag().then(res => {
        this.tags = res.data.data;
      });
    },
    addCategory() {
      addCategory(this.category).then(res => {
        this.addCategoryDialogVisible = false;
        this.$message.success("添加成功");
        this.listCategory();
      });
    },
    addTag() {
      addTag(this.tag).then(res => {
        this.addTagDialogVisible = false;
        this.$message.success("添加成功");
        this.listTag();
      });
    },
    submit() {
      delete this.post.postTags;
      this.post.status = this.status ? window.ARTICLE.SHOW : window.ARTICLE.HIDDEN;
      // 获取html 内容
      this.post.content = this.$refs.editor.d_render;
      if (this.postId) {
        editPost(this.post).then(res => {
          this.$message.success("更新成功");
          // update post after edit
          this.getPost();
        });
      } else {
        publishPost(this.post).then(res => {
          this.$message.success("发布成功，请继续编辑");
          // 发布完成之后，跳转编辑页面
          this.postId = res.data.data;
          this.post.postId = res.data.data;
          this.$router.push("edit?postId=" + res.data.data);
        });
      }
    }
  },
  created() {
    this.listTag();
    this.listCategory();
    this.postId = getUrlParam("postId");
    if (this.postId) {
      this.getPost();
    }
  },
  components: {
    "mavon-editor": mavonEditor.mavonEditor
  }
};
</script>

<style scoped>
@import "~vditor/dist/index.css";

.container {
  max-width: 1260px;
  margin: 20px auto 50px auto;
}

.bottom-menu {
  width: 100%;
  position: fixed;
  bottom: 0;
  height: 50px;
  border-top: solid 1px #dcdfe6;
  z-index: 1550;
  padding-top: 10px;
  background: white;
}
</style>