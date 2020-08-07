<template>
  <el-container id="app" style="height:100%">
    <el-aside width="210px" class="sidebar">
      <el-menu
        class="menu"
        :default-active="$route.path"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409eff"
      >
        <el-menu-item index="/">
          <i class="el-icon-document"></i>
          <span>文章管理</span>
        </el-menu-item>
        <el-menu-item index="/archives">
          <i class="el-icon-folder-opened"></i>
          <span>目录管理</span>
        </el-menu-item>
        <el-menu-item index="/tags">
          <i class="el-icon-collection-tag"></i>
          <span>标签管理</span>
        </el-menu-item>
        <el-menu-item index="/friend">
          <i class="el-icon-link"></i>
          <span>友情链接</span>
        </el-menu-item>
        <el-menu-item index="/param">
          <i class="el-icon-s-custom"></i>
          <span>参数管理</span>
        </el-menu-item>
        <el-menu-item index="/upload">
          <i class="el-icon-upload"></i>
          <span>上传文件</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container id="main">
      <el-header class="header">
        <h3>博客后台管理系统</h3>
        <div>
          <el-dropdown @command="handleCommand">
            <div>
              <el-image :src="headImgUrl" class="head-img"></el-image>
              <i class="el-icon-caret-bottom"></i>
            </div>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="resetPwd">修改密码</el-dropdown-item>
              <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>

    <el-dialog title="修改密码" :visible.sync="resetPwdDialogVisible" width="400px">
      <el-input type="password" placeholder="请输入旧密码" class="my-input" v-model="oldPassword"></el-input>
      <el-input type="password" placeholder="请输入新密码" class="my-input" v-model="newPassword"></el-input>
      <el-input type="password" placeholder="请确认密码" class="my-input" v-model="newPassword2"></el-input>
      <el-button
        type="primary"
        :disabled="oldPassword.length <= 0 || newPassword <= 0 || newPassword2 <= 0"
        @click="resetPwd"
        style="width: 100%"
      >确认修改</el-button>
    </el-dialog>
  </el-container>
</template>

<script>
import { resetPwd, logout } from "@/api/admin";
import { getValue } from "@/api/param";

export default {
  data() {
    return {
      resetPwdDialogVisible: false,
      oldPassword: "",
      newPassword: "",
      newPassword2: "",
      blogInfo: {},
      headImgUrl: "",
      nickname: ""
    };
  },
  methods: {
    getAdminInfo() {
      getValue("headImgUrl").then(res => {
        this.headImgUrl = res.data.data;
      });
      getValue("nickname").then(res => {
        this.nickname = res.data.data;
      });
    },
    logout() {
      this.$confirm("是否确认退出登录", "提示")
        .then(() => {
          logout();
          this.$router.push("login");
        })
        .catch(() => {});
    },
    resetPwd() {
      if (this.newPassword !== this.newPassword2) {
        this.$message.error("两次密码输入不一致 ");
        this.newPassword = this.newPassword2 = "";
        return;
      }
      resetPwd(this.oldPassword, this.newPassword).then(res => {
        this.$message.success("修改密码成功，下次登录生效");
      });
    },
    handleCommand(command) {
      switch (command) {
        case "resetPwd":
          this.resetPwdDialogVisible = true;
          break;
        case "logout":
          this.logout();
          break;
      }
    }
  },
  created() {
    this.getAdminInfo();
  },
  mounted() {}
};
</script>

<style lang="scss" scoped>
.my-input {
  margin-bottom: 20px;
}

.sidebar {
  height: 100%;
  position: fixed;
  top: 0;
  bottom: 0;
  overflow: hidden;
}

.sidebar .menu {
  height: 100% !important;
}

#main {
  margin-left: 210px;
}

.header {
  border-bottom: 1px solid #dcdfe6;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.head-img {
  width: 40px;
  height: 40px;
  border-radius: 30%;
}
</style>