<template>
  <div id="post">
    <div>
      <el-button icon="el-icon-refresh" @click="reload" style="margin-right:10px"></el-button>
      <el-select v-model="condition.categoryId" clearable placeholder="请选择目录">
        <el-option
          v-for="category in categorys"
          :key="category.id"
          :label="category.name"
          :value="category.id"
        ></el-option>
      </el-select>
      <el-input
        clearable
        placeholder="请输入搜索内容"
        v-model="condition.search"
        style="width:300px; margin: 0 10px"
      ></el-input>
      <el-button
        type="primary"
        icon="el-icon-search"
        @click="listPost"
        :disabled="!condition.categoryId && !condition.search"
      ></el-button>
      <a href="#/publish" target="_blank" style="float:right">
        <el-button type="primary" icon="el-icon-plus">发布新文章</el-button>
      </a>
    </div>

    <el-table :data="posts" stripe>
      <el-table-column prop="id" label="编号" width="100"></el-table-column>
      <el-table-column label="标题">
        <template slot-scope="scope">
          <a class="link" :href="domain+'/posts/'+scope.row.id" target="_blank">{{scope.row.title}}</a>
        </template>
      </el-table-column>
      <el-table-column label="目录">
        <template slot-scope="scope">
          <span v-if="scope.row.categoryName">{{scope.row.categoryName}}</span>
          <span v-else style="color:#909399">暂无目录</span>
        </template>
      </el-table-column>
      <el-table-column prop="pageViews" label="浏览量"></el-table-column>
      <el-table-column label="是否展示">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-color="#13ce66"
            @change="togglePostShowStatus(scope.row.id, scope.row.status)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime"></el-table-column>
      <el-table-column label="修改时间" prop="updateTime"></el-table-column>
      <el-table-column label="操作" width="250">
        <template slot-scope="scope">
          <div>
            <a :href="'#/edit?postId='+scope.row.id" target="_blank" style="margin-right:1.25rem">
              <el-button plain size="medium" icon="el-icon-edit" type="primary">编辑</el-button>
            </a>
            <el-button
              plain
              size="medium"
              icon="el-icon-delete"
              type="danger"
              @click="deletePost(scope.row.id)"
            >删除</el-button>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="移动">
        <template slot-scope="scope">
          <el-select
            style="margin-top: 10px"
            size="medium"
            v-model="scope.row.categoryId"
            @change="(target) => movePost(scope.row.id, target)"
            placeholder="移动到"
          >
            <el-option
              v-for="category in categorys"
              :disabled="scope.row.categoryId == category.id"
              :key="category.id"
              :label="category.name"
              :value="category.id"
            ></el-option>
          </el-select>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      style="margin-top:20px"
      background
      @size-change="changePageSize"
      @current-change="changeCurrentPage"
      :current-page="condition.current"
      :page-sizes="[10, 20, 30, 50]"
      :page-size="condition.size"
      layout="->, total, sizes, prev, pager, next, jumper"
      :total="total"
    ></el-pagination>
  </div>
</template>

<script>
import { listPost, toggleStatus, movePost, deletePost } from "@/api/post";
import { getValue } from "@/api/param";
import { listCategory } from "@/api/category";
import { openUrl } from "@/util/url";

export default {
  data() {
    return {
      domain: "",
      targetCategoryId: "",
      total: 0,
      posts: [],
      categorys: [],
      // 搜索条件
      condition: {
        categoryId: "",
        search: "",
        current: 1,
        size: 10
      }
    };
  },
  methods: {
    reload() {
      this.condition.current = 1;
      this.listPost();
    },
    changePageSize(size) {
      this.condition.size = size;
      this.listPost();
    },
    changeCurrentPage(current) {
      this.condition.current = current;
      this.listPost();
    },
    // 切换文章展示状态
    togglePostShowStatus(postId, status) {
      toggleStatus(
        postId,
        status ? window.ARTICLE.SHOW : window.ARTICLE.HIDDEN
      ).then(res => {
        this.$message.success("成功");
      });
    },
    listCategory() {
      listCategory().then(res => {
        this.categorys = res.data.data;
      });
    },
    // 加载文章
    listPost() {
      listPost(this.condition).then(res => {
        this.posts = res.data.data.records.map((item, index, array) => {
          item.status = item.status === window.ARTICLE.SHOW;
          return item;
        });
        this.total = res.data.data.total;
      });
    },
    getDomain() {
      getValue("domain").then(res => {
        this.domain = res.data.data;
      });
    },
    movePost(postId, targetCategoryId) {
      movePost(postId, targetCategoryId).then(res => {
        this.$message.success("移动成功");
      });
    },
    deletePost(postId) {
      this.$confirm("此操作不可恢复，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deletePost(postId).then(res => {
          this.$message.success("删除成功");
          this.listPost();
        });
      });
    }
  },
  created() {
    this.listPost();
    this.listCategory();
    this.getDomain();
  }
};
</script>

<style scoped>
.link {
  font-weight: 600;
  color: #409eff;
  text-decoration: none;
}
</style>

