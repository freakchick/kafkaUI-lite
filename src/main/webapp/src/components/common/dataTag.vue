<template>


  <div class="mytag" :title="title">
    <span class="left">{{ left }}</span>
    <span class="right">{{ right }}</span>
  </div>

</template>

<script>
  export default {
    name: "dataTag",
    props: ["left", "right","title"]
  }
</script>

<style scoped>
  .mytag {
    margin-right: 4px;
    display: inline-block;

  }

  .mytag span {

    display: inline-block;
    font-size: 12px;
    line-height: 12px;
    color: cornsilk;
  }

  .left {
    padding: 4px 4px 4px 6px;
    background-color: #40485b;
    border-radius: 4px 0 0 4px;

  }

  .right {
    padding: 4px 6px 4px 4px;
    background-color: #409eff;
    border-radius: 0 4px 4px 0;
  }
</style>
