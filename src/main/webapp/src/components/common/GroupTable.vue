<template>
  <div>
    <vxe-table
        border
        resizable
        :span-method="mergeRowMethod"
        :data="data">
      <!--      <vxe-table-column type="seq" width="60"></vxe-table-column>-->
      <vxe-table-column field="topic" title="topic" align="center"></vxe-table-column>
      <vxe-table-column field="partition" title="分区号" align="center"></vxe-table-column>
      <vxe-table-column field="offset" title="消费偏移量" align="center"></vxe-table-column>
      <vxe-table-column field="lag" title="未消费消息条数" align="center"></vxe-table-column>
    </vxe-table>
  </div>
</template>

<script>
export default {
  name: "GroupTablele",
  props: ['data'],
  methods: {
    mergeRowMethod({row, _rowIndex, column, visibleData}) {
      const fields = ['topic']
      const cellValue = row[column.property]
      if (cellValue && fields.includes(column.property)) {
        const prevRow = visibleData[_rowIndex - 1]
        let nextRow = visibleData[_rowIndex + 1]
        if (prevRow && prevRow[column.property] === cellValue) {
          return {rowspan: 0, colspan: 0}
        } else {
          let countRowspan = 1
          while (nextRow && nextRow[column.property] === cellValue) {
            nextRow = visibleData[++countRowspan + _rowIndex]
          }
          if (countRowspan > 1) {
            return {rowspan: countRowspan, colspan: 1}
          }
        }
      }
    }
  }
}
</script>

<style scoped>

</style>