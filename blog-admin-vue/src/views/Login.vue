<template>
  <div style="margin: 200px auto; text-align:center; width:300px">
    <h2>博客管理系统登录</h2>
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules">
      <el-form-item prop="username">
        <el-input v-model="loginForm.username" prefix-icon="el-icon-user" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          @keyup.enter.native="submit"
          prefix-icon="el-icon-lock"
          placeholder="请输入密码"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button style="width:100%" type="primary" @click="submit">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { login } from "@/api/admin";
import Cookies from "js-cookie";
import md5 from 'js-md5';

export default {
  data() {
    return {
      loginForm: {},
      loginRules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" }
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 6, message: "密码长度不能小于6位", trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    submit() {
      let isValid = true;
      this.$refs["loginForm"].validate((valid, object) => {
        if (!valid) {
          isValid = false;
          return;
        }
      });
      if (!isValid) return;
      login(this.loginForm.username, md5(this.loginForm.password)).then(res => {
        Cookies.set(window.LOGIN_TIME, new Date().getTime(), {expires: window.LOGIN_VALID_DAY});
        this.$message.success("登录成功");
        this.$router.push("/");
      });
    }
  }
};
</script>
