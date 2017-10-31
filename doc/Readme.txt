Server - 解耦通信（目前是Netty）
InvokerWrapper - 解耦？？目前抽离出来是受dubbo设计影响(例如 failover、cluster、mock等)
Mapper - 解耦(URL-方法&实现)
InvokerHolder - 解耦调用方式？

多个加载器怎么办？