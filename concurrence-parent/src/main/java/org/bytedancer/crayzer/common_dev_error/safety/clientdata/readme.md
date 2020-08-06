## 数据源头：任何客户端的东西都不可信任
### 客户端的计算不可信：trustclientcalculation
### 客户端提交的参数需要校验：trustclientparameter
### 不能信任请求头里的任何内容：trustclientip
除了请求 Body 中的信息，请求头里的任何信息同样不能信任。要知道，来自请求头的 IP、Referer 和 
Cookie 都有被篡改的可能性，相关数据只能用来参考和记录，不能用作重要业务逻辑。

### 用户标识不能从客户端获取：trustclientuserid
如果接口面向外部用户，那么一定不能出现用户标识这样的参数，当前用户的标识一定来自服务端，只有经过
身份认证后的用户才会在服务端留下标识。如果你的接口现在面向内部其他服务，那么也要千万小心这样的接
口只能内部使用，还可能需要进一步考虑服务端调用方的授权问题。