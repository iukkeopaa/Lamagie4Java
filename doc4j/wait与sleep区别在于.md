1. wait会释放所有锁而sleep不会释放锁资源.
2. wait只能在同步方法和同步块中使用，而sleep任何地方都可以
3. . wait无需捕捉异常，而sleep需要
4. sleep是Thread的方法，而wait是Object类的方法
5. sleep
   方法调用的时候必须指定时间