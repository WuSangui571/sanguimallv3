<template>
  <el-tree
      style="max-width: 600px"
      :data="dataSource"
      show-checkbox
      node-key="id"
      default-expand-all
      :expand-on-click-node="false"
      :render-content="renderContent"
  />
</template>

<script>
import {defineComponent} from 'vue'
import {doGet} from "../../http/HttpRequest.js";
export default defineComponent({
  name: "DashboardView",
  data(){
    return {
      datasource:{
        id:0,
        label:"",
        children:{
          id:0,
          label:"",
          children:{
            id:0,
            label:"",
          }
        }
      }
    }
  },

  mounted() {
    this.getData();
  },
  methods: {
    getData(){
      doGet("/api/product/category/list/tree",{}).then(resp => {
        if (resp.data.code === 200){
          console.log(resp)

          this.datasource = resp.data.data.list;
        }
      })
    },
  },
})
</script>

<style scoped>

</style>