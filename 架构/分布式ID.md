

## 雪花算法的缺点

由于雪花算法是强依赖于时间的，在分布式环境下，如果发生时钟回拨，很可能会引起 ID 重复、 ID 乱序、服务会处于不可用状态等问题。

## 解决方法

- 将 ID 生成交给少量服务器，并关闭时钟同步。
- 直接报错，交给上层业务处理。
- 如果回拨时间较短，在耗时要求内，比如 5ms，那么等待回拨时长后再进行生成。
- 如果回拨时间很长，那么无法等待，可以匀出少量位（1~2 位）作为回拨位，一旦时钟回拨，将回拨位加 1，可得到不一样的 ID， 2 位回拨位允许标记 3 次时钟回拨，基本够使用。如果超出了，可以再选择抛出异常。



https://mp.weixin.qq.com/s?__biz=MzIxMDU5OTU1Mw==&mid=2247486267&idx=1&sn=af379ed1917adfff980c3ad163c8d9d8&scene=21#wechat_redirect
https://blog.csdn.net/qq_50487743/article/details/145473041
https://mp.weixin.qq.com/s?__biz=MzU0OTE4MzYzMw==&mid=2247547792&idx=2&sn=91a10823ceab0cb9db26e22783343deb&chksm=fbb1b26eccc63b784879f90540c8ab1731e635b30e5f4fd41de67f87a4fe055473039206f09d&scene=27