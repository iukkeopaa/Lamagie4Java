`CompletableFuture` �� Java 8 �����һ��ǿ����첽��̹��ߣ�����ʵ���첽���š�����������������ʽ�ķ�ʽ�����첽����������϶���첽���������������������ϵ���쳣����ȡ�����ӻ������ܡ����÷�����ʾ���ȷ�����ϸ���� `CompletableFuture` ���첽���š�

### ��������



`CompletableFuture` ʵ���� `Future` �ӿں� `CompletionStage` �ӿڡ�`Future` �ӿ��ṩ��һ���첽��ȡ�������ķ�ʽ���� `CompletionStage` �ӿڶ�����һϵ�������첽������ŵķ�����ʹ�ö���첽������԰���һ����˳����߼�������ϡ�

### ���÷�����ʾ��

#### 1. ���� `CompletableFuture`



- **`runAsync`**������ִ��û�з���ֵ���첽����











```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureExample {
    public static void main(String[] args) {
        // ʹ��Ĭ���̳߳�
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("�첽���� 1 ִ�����");
        });

        // ʹ���Զ����̳߳�
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("�첽���� 2 ִ�����");
        }, executor);
        executor.shutdown();
    }
}
```



- **`supplyAsync`**������ִ���з���ֵ���첽����









```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SupplyAsyncExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "�첽������";
        });
        String result = future.get();
        System.out.println(result);
    }
}
```

#### 2. ������ɺ�Ĵ���



- **`thenApply`**�����ڶ� `CompletableFuture` �Ľ������ת����










```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ThenApplyExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 2)
               .thenApply(num -> num * 2);
        Integer result = future.get();
        System.out.println(result);
    }
}
```



- **`thenAccept`**���������� `CompletableFuture` �Ľ����û�з���ֵ��













```java
import java.util.concurrent.CompletableFuture;

public class ThenAcceptExample {
    public static void main(String[] args) {
        CompletableFuture.supplyAsync(() -> "Hello")
               .thenAccept(str -> System.out.println("���յ��Ľ��: " + str));
    }
}
```



- **`thenRun`**���� `CompletableFuture` ��ɺ�ִ��һ���޲����Ĳ�����












```java
import java.util.concurrent.CompletableFuture;

public class ThenRunExample {
    public static void main(String[] args) {
        CompletableFuture.supplyAsync(() -> "Task completed")
               .thenRun(() -> System.out.println("��������ִ��"));
    }
}
```

#### 3. ��϶�� `CompletableFuture`



- **`thenCombine`**��������� `CompletableFuture` �Ľ����














```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ThenCombineExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> 2);
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> 3);
        CompletableFuture<Integer> combinedFuture = future1.thenCombine(future2, (num1, num2) -> num1 + num2);
        Integer result = combinedFuture.get();
        System.out.println(result);
    }
}
```



- **`thenCompose`**�����ڽ�һ�� `CompletableFuture` �Ľ��ת��Ϊ��һ�� `CompletableFuture`��










```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ThenComposeExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 2)
               .thenCompose(num -> CompletableFuture.supplyAsync(() -> num * 2));
        Integer result = future.get();
        System.out.println(result);
    }
}
```

#### 4. �����쳣



- **`exceptionally`**���� `CompletableFuture` �����쳣ʱ���ṩһ��Ĭ�ϵĽ����





```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ExceptionallyExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            if (Math.random() < 0.5) {
                throw new RuntimeException("ģ���쳣");
            }
            return 2;
        }).exceptionally(ex -> {
            System.out.println("�����쳣: " + ex.getMessage());
            return 0;
        });
        Integer result = future.get();
        System.out.println(result);
    }
}
```





### �ܽ�



`CompletableFuture` �ṩ�˷ḻ�ķ�����ʵ���첽����ı��ţ�ͨ����Щ�������Է���ش��������������ϵ����϶�����񡢴����쳣�ȣ����������첽��̵�Ч�ʺͿ�ά���ԡ�


## CompletableFuture���쳣�������

�� Java �У�`CompletableFuture` �ṩ�˷ḻ���쳣������ƣ�����Ӧ���첽����ִ�й����п��ܳ��ֵ��쳣�����������ϸ���� `CompletableFuture` �е��쳣������Ƽ���ʹ�÷�ʽ��

### 1. �쳣�����ĳ���



��ʹ�� `CompletableFuture` ʱ���쳣���������¼��ֳ����в�����



- **����ִ�й������׳��쳣**���� `CompletableFuture` ��װ�������� `Runnable` �� `Callable`���ڲ������ܻ���Ϊ����ԭ���׳��쳣�������ָ���쳣�������쳣�ȡ�
- **����ȡ��**�������� `CompletableFuture` �� `cancel` ����ȡ������ʱ�������������ִ�У����ܻ��׳��쳣��
- **��ʱ�쳣**����ʹ�ô��г�ʱ������ `get` ������ȡ������ʱ�����������ָ��ʱ����δ��ɣ����׳� `TimeoutException`��

### 2. �쳣������

#### `exceptionally` ����



`exceptionally` �������ڴ��� `CompletableFuture` ����ִ�й������׳����쳣��������һ�� `Function` ���͵Ĳ�����������ִ�й����г����쳣ʱ������øú��������쳣����������һ��Ĭ��ֵ��



**ʾ������**��











```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ExceptionallyExample {
    public static void main(String[] args) {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            if (Math.random() < 0.5) {
                throw new RuntimeException("ģ���쳣");
            }
            return 10;
        }).exceptionally(ex -> {
            System.out.println("�����쳣: " + ex.getMessage());
            return -1;
        });

        try {
            Integer result = future.get();
            System.out.println("���ս��: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
```



**�������**��



- `supplyAsync` ������װ�������У��� 50% �ĸ����׳� `RuntimeException`��
- `exceptionally` ���������쳣������쳣��Ϣ��������Ĭ��ֵ -1��
- ���ͨ�� `get` ������ȡ��������

#### `handle` ����



`handle` �������������Ƿ�������ɣ�����ִ�С�������һ�� `BiFunction` ���͵Ĳ��������е�һ�����������������������������������ɣ����ڶ�������������ִ�й������׳����쳣��������쳣����



**ʾ������**��








```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class HandleExample {
    public static void main(String[] args) {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            if (Math.random() < 0.5) {
                throw new RuntimeException("ģ���쳣");
            }
            return 10;
        }).handle((result, ex) -> {
            if (ex != null) {
                System.out.println("�����쳣: " + ex.getMessage());
                return -1;
            }
            return result;
        });

        try {
            Integer finalResult = future.get();
            System.out.println("���ս��: " + finalResult);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
```



**�������**��



- `handle` ��������������Ƿ�������ɣ��ֱ�������������쳣�����
- �������������ɣ�`ex` ����Ϊ `null`��ֱ�ӷ��ؽ������������׳��쳣��`ex` ���������쳣��Ϣ������쳣��Ϣ������Ĭ��ֵ -1��

#### `whenComplete` ����



`whenComplete` ������ `handle` �������ƣ�Ҳ�����������Ƿ�������ɶ���ִ�С������������µĽ����ֻ�Ƕ�����Ľ�����쳣���д���



**ʾ������**��










```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class WhenCompleteExample {
    public static void main(String[] args) {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            if (Math.random() < 0.5) {
                throw new RuntimeException("ģ���쳣");
            }
            return 10;
        }).whenComplete((result, ex) -> {
            if (ex != null) {
                System.out.println("�����쳣: " + ex.getMessage());
            } else {
                System.out.println("����������ɣ����Ϊ: " + result);
            }
        });

        try {
            Integer finalResult = future.get();
            System.out.println("���ս��: " + finalResult);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
```



**�������**��



- `whenComplete` �������������Ƿ�������ɣ������Ӧ����Ϣ��
- ������ı�����Ľ�������ս����Ȼͨ�� `get` ������ȡ��

### 3. ����ʽ�����д����쳣



��ʹ�� `CompletableFuture` ������ʽ����ʱ���쳣����ͬ����Ҫ����������ʽ���õ�����λ������쳣��������



**ʾ������**��










```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ChainedExceptionHandling {
    public static void main(String[] args) {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            if (Math.random() < 0.5) {
                throw new RuntimeException("ģ���쳣");
            }
            return 10;
        }).thenApply(result -> result * 2)
          .exceptionally(ex -> {
                System.out.println("�����쳣: " + ex.getMessage());
                return -1;
            });

        try {
            Integer result = future.get();
            System.out.println("���ս��: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
```







**�������**��



- ����ʽ�����У�`supplyAsync` ������װ��������ܻ��׳��쳣��
- `exceptionally` �������Բ�����쳣����������Ӧ�Ĵ���



ͨ�����ϵ��쳣��������������Ч�ش��� `CompletableFuture` ����ִ�й����г��ֵ��쳣����߳���Ľ�׳�ԡ�

## ʵ�ֲ���������

�� Java �У�`CompletableFuture` ��һ��ǿ��Ĺ��ߣ�������ʵ�ֲ������������½���ϸ�������ʹ�� `CompletableFuture` ʵ�ֲ�����������������˼·��ʾ�����뼰������͡�

### ����˼·



ʵ�ֲ���������ͨ����ѭ���²��裺



1. **�������񼯺�**������Ҫ���������ת��Ϊһϵ�е� `CompletableFuture` ����
2. **����ִ������**������ `CompletableFuture` ���첽ִ�����ԣ�����Щ���񲢷�ִ�С�
3. **�ϲ�������**���ȴ�����������ɺ󣬽�������кϲ��ʹ���
4. **�쳣����**��������ִ�й����п��ܳ��ֵ��쳣���в���ʹ���

### ʾ������



����



java









```java
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class CompletableFutureConcurrentStreamProcessing {

    public static void main(String[] args) {
        // ģ����Ҫ����������б�
        List<Integer> dataList = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // ���� CompletableFuture �����б�
        List<CompletableFuture<Integer>> futures = dataList.stream()
               .map(data -> CompletableFuture.supplyAsync(() -> processData(data)))
               .collect(Collectors.toList());

        // ������� CompletableFuture ����
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        // �ȴ�����������ɣ�����ȡ���
        CompletableFuture<List<Integer>> allResults = allFutures.thenApply(v ->
                futures.stream()
                       .map(future -> {
                            try {
                                return future.get();
                            } catch (InterruptedException | ExecutionException e) {
                                // �����쳣
                                Thread.currentThread().interrupt();
                                System.err.println("����ִ�г���: " + e.getMessage());
                                return null;
                            }
                        })
                       .collect(Collectors.toList())
        );

        try {
            // ��ȡ���ս��
            List<Integer> results = allResults.get();
            System.out.println("����������: " + results);
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            System.err.println("��ȡ�������: " + e.getMessage());
        }
    }

    // ģ�����ݴ�����
    private static Integer processData(Integer data) {
        try {
            // ģ���ʱ����
            Thread.sleep((long) (Math.random() * 1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("���ݴ����ж�: " + e.getMessage());
        }
        return data * 2;
    }
}
```

### �������



1. **�������񼯺�**��
    - ����һ��������������� `dataList` ��Ϊ��Ҫ��������ݡ�
    - ʹ�� Java 8 �� Stream API �� `dataList` �е�ÿ��Ԫ��ӳ��Ϊһ�� `CompletableFuture` ����ÿ��������� `processData` ���������ݽ��д���
    - ����Щ `CompletableFuture` �����ռ��� `futures` �б��С�
2. **����ִ������**��
    - ʹ�� `CompletableFuture.allOf` ������ `futures` �б��е����� `CompletableFuture` ������ϳ�һ���µ� `CompletableFuture` ���� `allFutures`��
    - `allFutures` ����������������ɺ�Ż���ɣ��Ӷ�ʵ��������Ĳ���ִ�С�
3. **�ϲ�������**��
    - �� `allFutures` ��ɺ�ʹ�� `thenApply` �����Խ�����д���
    - ͨ�� Stream API ���� `futures` �б�����ÿ�� `CompletableFuture` �� `get` ������ȡ�������������ռ���һ���µ��б��С�
4. **�쳣����**��
    - �� `processData` �����У����� `InterruptedException` �쳣�����д���
    - �ڻ�ȡ `CompletableFuture` ���ʱ������ `InterruptedException` �� `ExecutionException` �쳣����������Ӧ�Ĵ���

### ��������˵��



- **�Զ����̳߳�**��Ĭ������£�`CompletableFuture` ʹ�� `ForkJoinPool.commonPool()` ��ִ�����������Ҫ����ϸ�ؿ����̳߳صĴ�С����Ϊ�������Զ����̳߳ز����䴫�ݸ� `supplyAsync` �ȷ��������磺



����



java









```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

ExecutorService executor = Executors.newFixedThreadPool(5);
CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> processData(data), executor);
```







- **������������ʽ����**��`CompletableFuture` ֧������֮���������ϵ����ʽ���á�����ʹ�� `thenApply`��`thenCompose`��`thenCombine` �ȷ���ʵ�ָ����ӵĲ����������߼������磺



����



java









```java
CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> 10);
CompletableFuture<Integer> future2 = future1.thenApply(result -> result * 2);
```







ͨ�����ϲ���ͷ����������ʹ�� `CompletableFuture` ʵ�ָ�Ч�Ĳ���������


## ʵ�ֲ�����
�� Java �У�`CompletableFuture` ��������ʵ�ֲ��������������������첽�ͷ������ķ�ʽ����ִ�ж�����񣬲��������ǵĽ����������ʹ�� `CompletableFuture` ʵ�ֲ������������ϸ�����ʾ�����롣

### 1. ����˼·



Ҫʹ�� `CompletableFuture` ʵ�ֲ���������ͨ����Ҫ���¼������裺



1. **���������б�**������Ҫ����������װ�� `CompletableFuture` ����
2. **����ִ������**��ʹ�� `CompletableFuture.allOf` ��������������ִ����Щ����
3. **����������**���ȴ�����������ɺ󣬻�ȡ������ÿ������Ľ����

### 2. ʾ������

#### ʾ�� 1���򵥵Ĳ���������





```java
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureParallelProcessing {
    public static void main(String[] args) {
        // ģ����Ҫ����������б�
        List<Integer> dataList = List.of(1, 2, 3, 4, 5);

        // ���� CompletableFuture �����б�
        List<CompletableFuture<Integer>> futures = new ArrayList<>();
        for (Integer data : dataList) {
            CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> processData(data));
            futures.add(future);
        }

        // ������ CompletableFuture ��ϳ�һ���µ� CompletableFuture
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        // �ȴ�����������ɣ�����ȡ���
        CompletableFuture<List<Integer>> allResults = allFutures.thenApply(v -> {
            return futures.stream()
                   .map(future -> {
                        try {
                            return future.get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                   .toList();
        });

        try {
            // ��ȡ���ս��
            List<Integer> results = allResults.get();
            System.out.println("������: " + results);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    // ģ�����ݴ�����
    private static Integer processData(Integer data) {
        try {
            // ģ���ʱ����
            Thread.sleep((long) (Math.random() * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return data * 2;
    }
}
```



**�������**��



- ���ȣ�������һ�������������б� `dataList`����ʾ��Ҫ��������ݡ�
- Ȼ�󣬱��� `dataList`����ÿ�����ݷ�װ��һ�� `CompletableFuture` ���󣬲���ӵ� `futures` �б��С�ÿ�� `CompletableFuture` �����첽ִ�� `processData` ������
- ʹ�� `CompletableFuture.allOf` ���������� `CompletableFuture` ��ϳ�һ���µ� `CompletableFuture`���� `CompletableFuture` ��������������ɺ���ɡ�
- ͨ�� `thenApply` ����������������ɺ󣬻�ȡÿ������Ľ�����������ռ���һ���µ��б��С�
- ��󣬵��� `get` ������ȡ���յĽ���б������

#### ʾ�� 2�������쳣�Ĳ���������











```java
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureParallelWithException {
    public static void main(String[] args) {
        List<Integer> dataList = List.of(1, 2, 3, 4, 5);
        List<CompletableFuture<Integer>> futures = new ArrayList<>();

        for (Integer data : dataList) {
            CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
                if (data == 3) {
                    throw new RuntimeException("ģ���쳣");
                }
                return processData(data);
            }).exceptionally(ex -> {
                System.out.println("�������� " + data + " ʱ�����쳣: " + ex.getMessage());
                return -1;
            });
            futures.add(future);
        }

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        CompletableFuture<List<Integer>> allResults = allFutures.thenApply(v -> {
            return futures.stream()
                   .map(future -> {
                        try {
                            return future.get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                   .toList();
        });

        try {
            List<Integer> results = allResults.get();
            System.out.println("������: " + results);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static Integer processData(Integer data) {
        try {
            Thread.sleep((long) (Math.random() * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return data * 2;
    }
}
```







**�������**��



- ��ʾ�� 1 ���ƣ����� `supplyAsync` ������ģ����һ���쳣�������������Ϊ 3 ʱ���׳� `RuntimeException`��
- ʹ�� `exceptionally` ���������쳣���������쳣ʱ������쳣��Ϣ������ -1 ��ΪĬ�Ͻ����
- ����������ʾ�� 1 ��ͬ�����ջ�ȡ�������������

### 3. �ܽ�



ͨ��ʹ�� `CompletableFuture`�����Է����ʵ�ֲ���������`CompletableFuture` �ṩ�˷ḻ�ķ����������첽���񣬰����������ϡ��쳣����ȣ��ܹ�������ָ��ӵĲ��д�������

## ʵ���첽��

�� Java �У�`CompletableFuture` ��������ʵ���첽���������������첽�ͷ������ķ�ʽ����һϵ���������潫�Ӽ���������ϸ�������ʹ�� `CompletableFuture` �����첽��������������Ӧ��ʾ�����롣

### 1. �����첽����ִ��



���ȣ������������ʹ�� `CompletableFuture` ִ�е����첽���񡣿���ʹ�� `supplyAsync` ������ִ���з���ֵ������ʹ�� `runAsync` ������ִ���޷���ֵ������








```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class BasicAsyncTask {
    public static void main(String[] args) {
        // ִ���з���ֵ���첽����
        CompletableFuture<String> futureWithResult = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task result";
        });

        // ִ���޷���ֵ���첽����
        CompletableFuture<Void> futureWithoutResult = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Void task completed");
        });

        try {
            // ��ȡ�з���ֵ����Ľ��
            String result = futureWithResult.get();
            System.out.println("Result: " + result);
            // �ȴ��޷���ֵ�������
            futureWithoutResult.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
```

### 2. ��ʽ�첽������



`CompletableFuture` ֧����ʽ���ã���������һ��������ɺ����ִ����һ�����񡣿���ʹ�� `thenApply`��`thenCompose` �ȷ���ʵ�֡�

#### `thenApply` ����



`thenApply` �������ڶ�ǰһ������Ľ������ת����











```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ChainedAsyncTasksThenApply {
    public static void main(String[] args) {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 5)
               .thenApply(num -> num * 2)
               .thenApply(result -> result + 3);

        try {
            Integer finalResult = future.get();
            System.out.println("Final result: " + finalResult);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
```

#### `thenCompose` ����



`thenCompose` �������ڽ�һ�� `CompletableFuture` �Ľ�����ݸ���һ������ `CompletableFuture` �ĺ�����ʵ�ָ����ӵ���ʽ�첽������







```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ChainedAsyncTasksThenCompose {
    public static void main(String[] args) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello")
               .thenCompose(result -> CompletableFuture.supplyAsync(() -> result + " World"));

        try {
            String finalResult = future.get();
            System.out.println("Final result: " + finalResult);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
```

### 3. �����첽������



����ʹ�� `CompletableFuture.allOf` �� `CompletableFuture.anyOf` ���������д������첽����

#### `CompletableFuture.allOf`



`allOf` �������ڲ���ִ�ж�� `CompletableFuture` ���񣬲��ȴ�����������ɡ�






```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ParallelAsyncTasksAllOf {
    public static void main(String[] args) {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Result 1";
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Result 2";
        });

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(future1, future2);

        CompletableFuture<String> combinedResult = allFutures.thenApply(v -> {
            try {
                return future1.get() + " | " + future2.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return null;
            }
        });

        try {
            String result = combinedResult.get();
            System.out.println("Combined result: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
```

#### `CompletableFuture.anyOf`



`anyOf` �������ڲ���ִ�ж�� `CompletableFuture` ����ֻҪ��һ��������ɾͻ��������������












```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ParallelAsyncTasksAnyOf {
    public static void main(String[] args) {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Result 1";
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Result 2";
        });

        CompletableFuture<Object> anyFuture = CompletableFuture.anyOf(future1, future2);

        anyFuture.thenAccept(result -> {
            if (result instanceof String) {
                System.out.println("First completed result: " + result);
            }
        });

        try {
            anyFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
```

### 4. �쳣����



���첽�������У���Ҫ�Կ��ܳ��ֵ��쳣���д�������ʹ�� `exceptionally` �����������쳣��







```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AsyncExceptionHandling {
    public static void main(String[] args) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            if (Math.random() < 0.5) {
                throw new RuntimeException("Simulated exception");
            }
            return "Task result";
        }).exceptionally(ex -> {
            System.out.println("Exception occurred: " + ex.getMessage());
            return "Default result";
        });

        try {
            String result = future.get();
            System.out.println("Final result: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
```







ͨ�����ϼ��ַ�ʽ�������ʹ�� `CompletableFuture` ʵ�ָ��ӵ��첽��������߳�������ܺ���Ӧ�ԡ�

## ���CompletableFuture

�� Java �У�`CompletableFuture` �ṩ�˶��ַ���������� `CompletableFuture` ��ϳ�һ���������㲻ͬ��ҵ����������Ϊ����ϸ���ܼ��ֳ�������Ϸ�ʽ��

### 1. �������



������Ҫ��˳������ִ�ж�� `CompletableFuture` ���񣬼�һ��������ɺ���ִ����һ������ʱ������ʹ�� `thenCompose` �� `thenApply` ������

#### `thenCompose` ����



`thenCompose` �������ڽ�һ�� `CompletableFuture` �Ľ�����ݸ���һ������ `CompletableFuture` �ĺ������Ӷ�ʵ�ִ�����ϡ�








```java
import java.util.concurrent.CompletableFuture;

public class CompletableFutureSerialCombination {
    public static void main(String[] args) {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> combinedFuture = future1.thenCompose(result1 ->
                CompletableFuture.supplyAsync(() -> result1 + " World")
        );

        combinedFuture.thenAccept(System.out::println);

        // �ȴ��������
        try {
            combinedFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```



�����������У�`future1` ��ִ�в����ؽ�� `"Hello"`��Ȼ�����������ݸ� `thenCompose` �еĺ������ú�������һ���µ� `CompletableFuture`��������ϳ�һ���µ� `CompletableFuture` �������� `"Hello World"`��

#### `thenApply` ����



`thenApply` ������ `thenCompose` ���ƣ��������ܵĺ������ص���һ����ͨ����������� `CompletableFuture`��









```java
import java.util.concurrent.CompletableFuture;

public class CompletableFutureThenApplyCombination {
    public static void main(String[] args) {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> combinedFuture = future1.thenApply(result1 -> result1 + " World");

        combinedFuture.thenAccept(System.out::println);

        try {
            combinedFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### 2. ������ϲ��ȴ������������



������Ҫ����ִ�ж�� `CompletableFuture` ���񣬲�������������ɺ���к�������ʱ������ʹ�� `CompletableFuture.allOf` ������










```java
import java.util.concurrent.CompletableFuture;

public class CompletableFutureAllOfCombination {
    public static void main(String[] args) {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Result 1";
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Result 2";
        });

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(future1, future2);

        CompletableFuture<String> combinedFuture = allFutures.thenApply(v -> {
            try {
                return future1.get() + " " + future2.get();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });

        combinedFuture.thenAccept(System.out::println);

        try {
            combinedFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```



�����������У�`future1` �� `future2` ����ִ�У�`CompletableFuture.allOf` �����᷵��һ���µ� `CompletableFuture`�������д���� `CompletableFuture` �����ʱ������µ� `CompletableFuture` �Ż���ɡ�Ȼ��ͨ�� `thenApply` ������ȡÿ������Ľ������ϡ�

### 3. ������ϲ��ȴ�����һ���������



����ֻ��Ҫ�ڶ������ִ�е� `CompletableFuture` ��������һ�����ʱ�ͽ��к�������������ʹ�� `CompletableFuture.anyOf` ������












```java
import java.util.concurrent.CompletableFuture;

public class CompletableFutureAnyOfCombination {
    public static void main(String[] args) {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Result 1";
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Result 2";
        });

        CompletableFuture<Object> anyFuture = CompletableFuture.anyOf(future1, future2);

        anyFuture.thenAccept(result -> System.out.println("��һ����ɵĽ��: " + result));

        try {
            anyFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```



�����������У�`future1` �� `future2` ����ִ�У�`CompletableFuture.anyOf` �����᷵��һ���µ� `CompletableFuture`��������һ������� `CompletableFuture` ���ʱ������µ� `CompletableFuture` �ͻ���ɣ������Ի�ȡ����һ����ɵ�����Ľ����

### 4. �ϲ����� `CompletableFuture` �Ľ��



�������Ҫ������ `CompletableFuture` �Ľ���ϲ�������ʹ�� `thenCombine` ������











```java
import java.util.concurrent.CompletableFuture;

public class CompletableFutureThenCombineCombination {
    public static void main(String[] args) {
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> 10);
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> 20);

        CompletableFuture<Integer> combinedFuture = future1.thenCombine(future2, (result1, result2) -> result1 + result2);

        combinedFuture.thenAccept(System.out::println);

        try {
            combinedFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```







�����������У�`future1` �� `future2` ����ִ�У�`thenCombine` ������������������ɺ󣬽����ǵĽ�����ݸ�һ����Ԫ�������кϲ������յõ�һ���µ� `CompletableFuture` �����ϲ���Ľ����