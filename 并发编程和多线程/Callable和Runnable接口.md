`Callable` �� `Runnable` �� Java �����ڶ�������Ľӿڣ���������ʹ�ó����͹������������������������ǵ���ϸ�Աȣ�

------

### 1. **����ֵ**

- **Runnable**:

    - `Runnable` �ӿڵ� `run()` ����û�з���ֵ����������Ϊ `void`����
    - �����ڲ���Ҫ���ؽ��������


  ```
  public interface Runnable {
      void run();
  }
  ```

- **Callable**:

    - `Callable` �ӿڵ� `call()` �������Է���һ���������������Ϊ `V`�����ͣ���
    - ��������Ҫ���ؽ��������


  ```
  public interface Callable<V> {
      V call() throws Exception;
  }
  ```

------

### 2. **�쳣����**

- **Runnable**:
    - `run()` ���������׳��ܼ��쳣��checked exception����ֻ��ͨ�� try-catch �鴦���쳣��
    - ��������з����쳣���޷�ֱ�Ӵ��ݸ������ߡ�
- **Callable**:
    - `call()` ���������׳��ܼ��쳣��`throws Exception`���������߿���ͨ�� `Future.get()` �����쳣��
    - ���ʺ���Ҫ�����쳣������

------

### 3. **ʹ�ó���**

- **Runnable**:

    - �����ڼ򵥵��첽����������߳�ִ���޷���ֵ������
    - ������ `Thread` ����̳߳أ�`ExecutorService`���С�

  ```
  Runnable task = () -> {
      System.out.println("Running a Runnable task");
  };
  new Thread(task).start();
  ```

- **Callable**:

    - ��������Ҫ���ؽ�������쳣������
    - ֻ��ͨ���̳߳أ�`ExecutorService`���ύ���񣬷���һ�� `Future` ����


  ```
  Callable<Integer> task = () -> {
      return 42;
  };
  ExecutorService executor = Executors.newSingleThreadExecutor();
  Future<Integer> future = executor.submit(task);
  System.out.println("Result: " + future.get());
  ```

------

### 4. **���̳߳صĽ��**

- **Runnable**:

    - ����ͨ�� `ExecutorService.submit(Runnable)` �ύ���񣬷���һ�� `Future` ���󣬵� `Future.get()` ���Ƿ��� `null`��
    - Ҳ����ͨ�� `ExecutorService.execute(Runnable)` �ύ���񣬵��������κν����


  ```
  ExecutorService executor = Executors.newSingleThreadExecutor();
  executor.submit(() -> {
      System.out.println("Running a Runnable task");
  });
  ```

- **Callable**:

    - ֻ��ͨ�� `ExecutorService.submit(Callable)` �ύ���񣬷���һ�� `Future` ���󣬿���ͨ�� `Future.get()` ��ȡ��������

  ```
  ExecutorService executor = Executors.newSingleThreadExecutor();
  Future<Integer> future = executor.submit(() -> {
      return 42;
  });
  System.out.println("Result: " + future.get());
  ```

------

### 5. **���ܶԱ��ܽ�**

| ����            | Runnable                      | Callable                   |
| :-------------- | :---------------------------- | :------------------------- |
| ����ֵ          | �޷���ֵ (`void`)             | �з���ֵ (`V`)             |
| �쳣����        | �����׳��ܼ��쳣              | �����׳��ܼ��쳣           |
| ʹ�ó���        | ���������践��ֵ          | ��Ҫ����ֵ���쳣��������� |
| ���̳߳صĽ��  | `execute()` �� `submit()`     | ֻ��ͨ�� `submit()`        |
| ���ص� `Future` | `Future`��`get()` ���� `null` | `Future`��`get()` ���ؽ�� |

------

### 6. **���ѡ��**

- ���������Ҫ���ؽ���Ҳ��漰���ӵ��쳣����ʹ�� **Runnable**��
- ���������Ҫ���ؽ������Ҫ�����ܼ��쳣��ʹ�� **Callable**��

------

### ʾ������

#### Runnable ʾ��


```
Runnable task = () -> {
    System.out.println("Running a Runnable task");
};
new Thread(task).start();
```

#### Callable ʾ��



```
Callable<Integer> task = () -> {
    return 42;
};
ExecutorService executor = Executors.newSingleThreadExecutor();
Future<Integer> future = executor.submit(task);
System.out.println("Result: " + future.get());
executor.shutdown();
```

## �ܽ�2

Callable��Runnable�ӿڶ���������Java��ʵ�ֶ��̵߳Ľӿڣ�������֮����һЩ����:

1. ����ֵ:
Runnable�ӿڵ�run0����û�з���ֵ����ͨ������ִ��һЩ����Ҫ���ؽ��������
Callable�ӿڵ�cal(0)�������Է���һ��������������߳�ִ�����񲢷��ؽ��������ͨ��Future�����
ȡ��
2. �׳��쳣:
Runnable�ӿڵ�run0���������׳��ܼ��쳣��ֻ���׳�δ����쳣��
Callable�ӿڵ�cal10���������׳��ܼ��쳣
3. ����:
Callable�ӿ�ʹ���˷��ͣ�����ָ��call0)�����ķ�������
Runnable�ӿ�û��ʹ�÷��͡�
4. �̳߳�֧��:
Callable�ӿ�ͨ����Executor���һ��ʹ�ã������ύ��ExecutorService���̳߳�ִ��
Runnable�ӿ�ͬ��������Executor���һ��ʹ�ã�����Callable�ӿ��ṩ�˸��ḻ�Ĺ��ܣ���ȡ����
�񡢻�ȡִ�н���ȡ�
