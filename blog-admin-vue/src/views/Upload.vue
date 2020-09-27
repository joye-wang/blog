<template>
  <div>
    <div class="file-attr">
      <span>请指定目录</span
      ><el-input style="width: 100px" v-model="dir" placeholder="请指定目录" />
      <span>请指定文件名</span
      ><el-input
        style="width: 100px"
        v-model="fileName"
        placeholder="请指定文件名"
      />
    </div>
    <div class="paste-div" type="text" @paste="pasteImg($event)">
      先点击此处，再粘贴图片
    </div>
    <span>预览</span>
    <el-image
      fit="contain"
      class="image"
      :src="base64"
      :preview-src-list="[base64]"
    ></el-image>
    <el-button type="primary" @click="upload">开始上传</el-button>
    <p>{{ "七牛外链: " + url }}</p>
    <el-button v-show="url">复制</el-button>
  </div>
</template>

<script>
import { upload } from "@/api/upload";
import { Message, MessageBox } from "element-ui";
import Cookies from "js-cookie";

export default {
  data() {
    return {
      base64: "",
      file: "",
      url: "",
      dir: "",
      fileName: "",
    };
  },
  methods: {
    upload() {
      if (!this.dir) {
        this.$message.error("请指定目录");
        return;
      }
      if (!this.fileName) {
        this.$message.error("请指定文件名");
        return;
      }
      Cookies.set("dir", this.dir);
      upload(this.file, this.dir + "/" + this.fileName).then((res) => {
        // 上传完成后，显示url
        this.url = res.data.url;
      });
    },
    pasteImg(event) {
      console.debug("paste event:", event);
      if (!(event.clipboardData && event.clipboardData.items)) {
        return;
      }
      var item = event.clipboardData.items[0];

      if (item.kind === "string") {
        item.getAsString((str) => {
          this.$message.error("无法粘贴文本");
          console.log("paste string:", str);
        });
      } else if (item.kind === "file") {
        this.file = item.getAsFile();
        var reader = new FileReader();
        reader.readAsDataURL(this.file);
        reader.onload = () => {
          if (reader.result) {
            //预览文件
            this.base64 = reader.result;
          }
        };
      }
    },
  },
  created() {
    this.dir = Cookies.get("dir");
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
  width: 200px;
  height: 160px;
  display: block;
  margin-bottom: 20px;
}

.paste-div {
  margin: 20px 0;
  color: #c0c4cc;
  width: 200px;
  height: 160px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 15px;
  background: #f5f7fa;
}

.file-attr > * {
    margin-right: 20px;
}
</style>
