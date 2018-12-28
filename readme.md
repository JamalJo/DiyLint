# 自定义Lint：

主要代码
---
lintjar模块 - lint代码
app模块 - 用来测试的代码
lintaar模块 - 用来生成aar，没有实际作用


NewThreadDetector 
---
检查业务代码中，直接new Thread的情况。


ModelSerializedNameDetector
---
检查JavaBean中，类中各field，是否添加了SerializedName注解，
添加SerializedName注解后，可以解决JavaBean中field name被混淆后，
不能正常反解析的问题。



ModelInitialDetector
---
检查JavaBean中，类中各field，是否在构造函数中初始化，避免空指针。
