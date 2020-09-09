<template>
  <div>
    <el-input id="input" type="textarea" placeholder="请粘贴图片" class="text"></el-input>
    <span>预览</span>
    <el-image fit="contain" class="image" :src="base64" :preview-src-list="[base64]"></el-image>
    <el-button type="primary" @click="upload">开始上传</el-button>
    <p>{{'七牛外链: '+url}}</p>
  </div>
</template>

<script>
import { upload } from "@/api/upload";
import { Message, MessageBox } from "element-ui";

export default {
  data() {
    return {
      base64: "",
      file: "",
      url: "",
    };
  },
  methods: {
    upload() {
      upload(this.file)
        .then((res) => {
          // 上传完成后，显示url
          this.url = res.data.url;
        })
        .catch((error) => {
          this.$message.error("上传错误："+error.response.status);
        });
    },
    readFile(file) {
      var reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        if (reader.result) {
          //预览文件
          this.base64 = reader.result;
        }
      };
    },
  },
  mounted() {
    document.getElementById("input").addEventListener("paste", (event) => {
      if (!(event.clipboardData && event.clipboardData.items)) {
        return;
      }
      var item = event.clipboardData.items[0];

      if (item.kind === "string") {
        item.getAsString((str) => {
          alert("paste string?");
        });
      } else if (item.kind === "file") {
        this.file = item.getAsFile();
        this.readFile(this.file);
      }
    });
  },
};
</script>

<style scoped>
.text {
  margin-bottom: 20px;
  max-width: 500px;
  display: block;
}

.image {
  margin-top: 5px;
  width: 150px;
  height: 100px;
  display: block;
  margin-bottom: 20px;
}
</style>
