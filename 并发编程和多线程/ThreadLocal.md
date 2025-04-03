## ���߳��ܷ��ʸ��׵�threadlocal��

�� Java �`ThreadLocal` ������Ϊÿ��ʹ�������̶߳������洢һ�ݱ��������Ļ��ƣ��ӽ��̺͸��������ڲ�ͬ�Ľ��̣����� `ThreadLocal` �Ǻ��̰߳󶨵ģ������ӽ����޷�ֱ�ӷ��ʸ������� `ThreadLocal` ��ֵ������Ӳ�ͬ������ϸ���ͣ�

### 1. `ThreadLocal` �Ĺ���ԭ��

`ThreadLocal` Ϊÿ��ʹ�������߳�ά��һ�������ı���������ÿ���̶߳����Զ����ظı��Լ��ĸ�����������Ӱ�������߳�����Ӧ�ĸ�����`ThreadLocal` �������Ǵ洢�� `Thread` �����ڲ��� `ThreadLocalMap` ���;��������������߳�˽�еġ�

������һ���򵥵� `ThreadLocal` ʹ��ʾ����





```java
public class ThreadLocalExample {
    private static final ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        // ���õ�ǰ�̣߳����̣߳��� ThreadLocal ֵ
        threadLocal.set(10);

        // ��ӡ���̵߳� ThreadLocal ֵ
        System.out.println("���̵߳� ThreadLocal ֵ: " + threadLocal.get());

        // ����һ�����߳�
        Thread newThread = new Thread(() -> {
            // ���̻߳�ȡ ThreadLocal ֵ�����������̣߳�û�����ù�������Ϊ null
            System.out.println("���̵߳� ThreadLocal ֵ: " + threadLocal.get());
            // Ϊ���߳����� ThreadLocal ֵ
            threadLocal.set(20);
            // ��ӡ���̵߳� ThreadLocal ֵ
            System.out.println("���߳����ú� ThreadLocal ֵ: " + threadLocal.get());
        });
        newThread.start();

        try {
            newThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // �ٴδ�ӡ���̵߳� ThreadLocal ֵ���������߳�Ӱ��
        System.out.println("���̵߳� ThreadLocal ֵ: " + threadLocal.get());
    }
}
```

�����������У���ͬ�̶߳� `ThreadLocal` �Ĳ������໥�����ģ�һ���߳����õ�ֵ����Ӱ�������̡߳�

### 2. ���̺��̵߳Ĺ�ϵ

�����ǳ����ڲ���ϵͳ�е�һ��ִ�й��̣���ϵͳ������Դ����͵��ȵĻ�����λ���߳��ǽ����е�һ��ִ�е�Ԫ���� CPU ���Ⱥͷ��ɵĻ�����λ���ӽ������ɸ�����ͨ��ϵͳ���ã��� Java �е� `ProcessBuilder` �� `Runtime.getRuntime().exec()`���������½��̣���ӵ���Լ��������ڴ�ռ��ϵͳ��Դ��

### 3. �ӽ����޷����ʸ����� `ThreadLocal` ֵ��ԭ��

���� `ThreadLocal` �������Ǻ��̰߳󶨵ģ��洢��ÿ���̵߳� `ThreadLocalMap` �У����ӽ��̺͸������ǲ�ͬ�Ľ��̣�ӵ�и��Զ������ڴ�ռ���̡߳��ӽ������Լ����߳���ϵ������̳и��������̵߳� `ThreadLocal` ���ݡ�

### 4. ��Ҫ�ڸ��ӽ��̼䴫������

�����Ҫ�ڸ��ӽ��̼䴫�����ݣ�����ʹ��һЩ���̼�ͨ�ţ�IPC�����ƣ�����ܵ��������ڴ桢��Ϣ���еȡ��� Java ����Խ��� `ProcessBuilder` �����ӽ��̣���ͨ����׼����������������ݴ��ݡ�




```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class ProcessCommunicationExample {
    public static void main(String[] args) {
        try {
            // ����һ���ӽ���
            ProcessBuilder processBuilder = new ProcessBuilder("java", "ChildProcess");
            Process process = processBuilder.start();

            // ���ӽ��̵ı�׼������д������
            OutputStream outputStream = process.getOutputStream();
            String data = "Hello from parent process!";
            outputStream.write(data.getBytes());
            outputStream.close();

            // ��ȡ�ӽ��̵ı�׼�����
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("���ӽ��̽��յ�: " + line);
            }
            reader.close();

            // �ȴ��ӽ���ִ�����
            int exitCode = process.waitFor();
            System.out.println("�ӽ����˳���: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```





�����ʾ���У�������ͨ����׼�����������ݴ��ݸ��ӽ��̣��ӽ��̿��Զ�ȡ��Щ���ݡ�


