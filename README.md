Name | Academy | score 
-- | -- | ---
Harry Potter | Gryffindor| 90 
Hermione Granger | Gryffindor | 100 
Draco Malfoy | Slytherin | 90

``` kotlin
        val filename = "/Users/guxichang/code-src/dda-kt/src/main/resources/dda.cfg"

        InitDDA.initDDA(filename)

        log.info(InitDDA.rootPath)

        val interval = TimeUnit.SECONDS.toMillis(1)
        val observer = FileAlterationObserver(File(InitDDA.rootPath))
        observer.addListener(FileDetect())
        //创建文件变化监听器
        val monitor = FileAlterationMonitor(interval, observer)
        // 开始监控
        monitor.start()

        for (key in InitDDA.regQuene.keys) {
            Executors.newSingleThreadExecutor().submit({
                val abq = InitDDA.regQuene[key]

                //每一个队列一个线程，开启循环模式
                while (true) {
                    val fp = abq!!.take()
                    val path = fp.path
                    val clazz = fp.clazz
                    /**
                     * 反射插件
                     * 文件现文件本地化
                     * 失败回放
                     */
                    if (key is KafkaSink) {
                        key.bootServr
                        key.topic
                    } else if (key is HdfsSink) {
                        key.path
                    } else if (key is FtpSink) {
                        key.ip
                    }
                }
            })
        }