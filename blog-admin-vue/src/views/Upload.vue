<template>
  <div>
    <div
      ref="paste"
      type="text"
      contenteditable="true"
      @paste.stop.prevent="pasteImg($event)"
    ></div>
    <el-input
      id="input"
      type="textarea"
      placeholder="请粘贴图片"
      class="text"
    ></el-input>
    <span>预览</span>
    <el-image
      fit="contain"
      class="image"
      :src="base64"
      :preview-src-list="[base64]"
    ></el-image>
    <el-button type="primary" @click="upload">开始上传</el-button>
    <p>{{ "七牛外链: " + url }}</p>
  </div>
</template>

<script>
import { upload } from "@/api/upload";
import { Message, MessageBox } from "element-ui";
import ClipboardJS from "clipboard";

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
          this.$message.error("上传错误：" + error.response.status);
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
    pasteImg(e) {
      console.log("获得剪切板的内容", e);
      const cbd = e.clipboardData;
      const ua = window.navigator.userAgent;
      // 如果是 Safari 直接 return
      if (!(e.clipboardData && e.clipboardData.items)) {
        return;
      }
      if (
        cbd.items &&
        cbd.items.length === 2 &&
        cbd.items[0].kind === "string" &&
        cbd.items[1].kind === "file" &&
        cbd.types &&
        cbd.types.length === 2 &&
        cbd.types[0] === "text/plain" &&
        cbd.types[1] === "Files" &&
        ua.match(/Macintosh/i) &&
        Number(ua.match(/Chrome\/(\d{2})/i)[1]) < 49
      ) {
        return;
      }
      for (let i = 0; i < cbd.items.length; i++) {
        let item = cbd.items[i];
        if (item.kind == "file") {
          // blob 就是从剪切板获得的文件，可以进行上传或其他操作
          const blob = item.getAsFile();
          console.log("获得blob", blob);
          if (blob.size === 0) {
            return;
          }
          const reader = new FileReader();
          const imgs = new Image();
          imgs.file = blob;
          reader.onload = (function (aImg) {
            return function (e) {
              console.log("获得粘贴的结果", e.target.result);
              aImg.src = e.target.result;
            };
          })(imgs);
          reader.readAsDataURL(blob);
          this.$refs.paste.appendChild(imgs);
        }
      }
    },
  },
  mounted() {
    document.getElementById("input").addEventListener("paste", (event) => {
      console.log("paste event");
      var clipdata = event.clipboardData || window.clipboardData;
      window.c = clipdata;
      console.log(clipdata);

      var clipboard = new ClipboardJS("#input");

      clipboard.on("success", function (e) {
        console.info("Action:", e.action);
        console.info("Text:", e.text);
        console.info("Trigger:", e.trigger);
        console.log(e);
        e.clearSelection();
      });

      clipboard.on("error", function (e) {
        console.error("Action:", e.action);
        console.error("Trigger:", e.trigger);
      });
      return;

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
