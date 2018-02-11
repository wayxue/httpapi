## 统一 http 接口的实现
由于原始的MVC 模式下，大量API 接口会导致代码量大，而且很有可能接口风格乱七八糟，调用起来很麻烦，容易出错。
## 原始的MVC 模式 ##
![](/image/MVC.png)
## 实现统一接口 ##
![](/image/MVC2.png)
具体实现：
![](/image/MVC3.png)
## 需要注意的问题 ##
get请求时路径上的参数不能出现{}，是一个400 bad request 的错误。
可以通过转义的方式暂时解决，{：%7B，}：%7D。
如： 
localhost:8080/ser?method=123&params={}                                                                                                   
localhost:8080/ser?method=123&params=%7B%7D
