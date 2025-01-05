1. 有一个共享变量 abc，在一个线程里设置了 abc 的值 abc=3，你思考一下，有哪些办法可以让其他线程能够看到abc==3？
> 1. 使用volatile关键字修饰
> 2. 使用synchronized对修改这个变量的代码块进行加锁
> 3. 使用localhost进行处理