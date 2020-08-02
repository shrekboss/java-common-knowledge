## 文件IO：实现高效正确的文件读写并非易事
### 文件读写需要确保字符编码一致：badencodingissue
- FileReader 是以当前机器的默认字符集来读取文件的，如果希望指定字符集的话，需要直接使用 
InputStreamReader 和 FileInputStream。

Files.readAllLines() 这种方式有个问题是，读取超出内存大小的大文件时会出现 OOM
- readAllLines 读取文件所有内容后，放到一个 List 中返回，如果内存无法容纳这个 List，就会 OOM

### 使用Files类静态方法进行文件操作注意释放文件句柄：filestreamoperationneedclose
与 Files.readAllLines 方法返回 List 不同，lines 方法返回的是 Stream。这在需要时可以不断读取、使用文
件中的内容，而不是一次性地把所有内容都读取到内存中，因此避免了 OOM。
lines 方法源码可以发现，Stream 的 close 注册了一个回调，来关闭 BufferedReader 进行资源释放。

> 案例：程序在生产上运行一段时间后就会出现 too many files 的错误，我们想当然地认为是 OS 设置的最大
> 文件句柄太小了，就让运维放开这个限制，但放开后还是会出现这样的问题。经排查发现，其实是文件句柄没
> 有释放导致的，问题就出在 Files.lines 方法上。

**一个很容易被忽略的严重问题:** 读取完文件后没有关闭。通常会认为静态方法的调用不涉及资源释放，因
为方法调用结束自然代表资源使用完成，由 API 释放资源，但对于 Files 类的一些返回 Stream 的方法并不是
这样。

`
java.nio.file.FileSystemException: demo.txt: Too many open files
at sun.nio.fs.UnixException.translateToIOException(UnixException.java:91)
at sun.nio.fs.UnixException.rethrowAsIOException(UnixException.java:102)
at sun.nio.fs.UnixException.rethrowAsIOException(UnixException.java:107)`

`
lsof -p 63937
...
java    63902 crayzer *238r   REG                1,4      370         12934160647 /Users/crayzer/Documents/common-mistakes/demo.txt
java    63902 crayzer *239r   REG                1,4      370         12934160647 /Users/crayzer/Documents/common-mistakes/demo.txt
...
lsof -p 63937 | grep demo.txt | wc -l
10007`

### 注意读写文件要考虑设置缓冲区：filebufferperformance

