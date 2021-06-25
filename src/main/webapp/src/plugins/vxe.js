import Vue from 'vue'
import XEUtils from 'xe-utils'
import {
  VXETable,
  Column,
  Header,
  Table
} from 'vxe-table'
import zhCN from 'vxe-table/lib/locale/lang/zh-CN'

// 按需加载的方式默认是不带国际化的，自定义国际化需要自行解析占位符 '{0}'，例如：
VXETable.setup({
  i18n: (key, args) => XEUtils.toFormatString(XEUtils.get(zhCN, key), args)
})


Vue.use(Column)
Vue.use(Header)
Vue.use(Table)