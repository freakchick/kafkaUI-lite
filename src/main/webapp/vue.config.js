module.exports = {
    devServer: {
        proxy: "http://127.0.0.1:6666", //开发环境的跨域问题解决
        port: 6669
    }
}
