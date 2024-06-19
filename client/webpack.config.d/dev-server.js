config.devServer = Object.assign(
    {},
    config.devServer || {},
    {
        port: 8080,
        proxy: [
            {
                context: ["/api/**"],
                target: 'http://localhost:8081',
                logLevel: 'debug',
            },
            {
                context: ["/join/**"],
                target: 'http://localhost:8081',
                logLevel: 'debug',
                ws: true
            }
        ]
    }
)