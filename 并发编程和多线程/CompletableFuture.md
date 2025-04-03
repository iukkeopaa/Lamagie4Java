`CompletableFuture` 是 Java 8 引入的一个强大的异步编程工具，用于实现异步编排。它可以让你以声明式的方式处理异步任务，轻松组合多个异步操作，处理任务的依赖关系、异常情况等。下面从基本介绍、常用方法及示例等方面详细介绍 `CompletableFuture` 的异步编排。

### 基本介绍



`CompletableFuture` 实现了 `Future` 接口和 `CompletionStage` 接口。`Future` 接口提供了一种异步获取任务结果的方式，而 `CompletionStage` 接口定义了一系列用于异步任务编排的方法，使得多个异步任务可以按照一定的顺序和逻辑进行组合。

### 常用方法及示例

#### 1. 创建 `CompletableFuture`



- **`runAsync`**：用于执行没有返回值的异步任务。











```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureExample {
    public static void main(String[] args) {
        // 使用默认线程池
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("异步任务 1 执行完毕");
        });

        // 使用自定义线程池
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("异步任务 2 执行完毕");
        }, executor);
        executor.shutdown();
    }
}
```



- **`supplyAsync`**：用于执行有返回值的异步任务。









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
            return "异步任务结果";
        });
        String result = future.get();
        System.out.println(result);
    }
}
```

#### 2. 任务完成后的处理



- **`thenApply`**：用于对 `CompletableFuture` 的结果进行转换。










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



- **`thenAccept`**：用于消费 `CompletableFuture` 的结果，没有返回值。













```java
import java.util.concurrent.CompletableFuture;

public class ThenAcceptExample {
    public static void main(String[] args) {
        CompletableFuture.supplyAsync(() -> "Hello")
               .thenAccept(str -> System.out.println("接收到的结果: " + str));
    }
}
```



- **`thenRun`**：在 `CompletableFuture` 完成后执行一个无参数的操作。












```java
import java.util.concurrent.CompletableFuture;

public class ThenRunExample {
    public static void main(String[] args) {
        CompletableFuture.supplyAsync(() -> "Task completed")
               .thenRun(() -> System.out.println("后续操作执行"));
    }
}
```

#### 3. 组合多个 `CompletableFuture`



- **`thenCombine`**：组合两个 `CompletableFuture` 的结果。














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



- **`thenCompose`**：用于将一个 `CompletableFuture` 的结果转换为另一个 `CompletableFuture`。










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

#### 4. 处理异常



- **`exceptionally`**：当 `CompletableFuture` 出现异常时，提供一个默认的结果。





```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ExceptionallyExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            if (Math.random() < 0.5) {
                throw new RuntimeException("模拟异常");
            }
            return 2;
        }).exceptionally(ex -> {
            System.out.println("出现异常: " + ex.getMessage());
            return 0;
        });
        Integer result = future.get();
        System.out.println(result);
    }
}
```





### 总结



`CompletableFuture` 提供了丰富的方法来实现异步任务的编排，通过这些方法可以方便地处理任务的依赖关系、组合多个任务、处理异常等，大大提高了异步编程的效率和可维护性。


## CompletableFuture的异常处理机制

在 Java 中，`CompletableFuture` 提供了丰富的异常处理机制，用于应对异步任务执行过程中可能出现的异常情况。以下详细介绍 `CompletableFuture` 中的异常处理机制及其使用方式。

### 1. 异常产生的场景



在使用 `CompletableFuture` 时，异常可能在以下几种场景中产生：



- **任务执行过程中抛出异常**：在 `CompletableFuture` 封装的任务（如 `Runnable` 或 `Callable`）内部，可能会因为各种原因抛出异常，例如空指针异常、算术异常等。
- **任务被取消**：当调用 `CompletableFuture` 的 `cancel` 方法取消任务时，如果任务正在执行，可能会抛出异常。
- **超时异常**：当使用带有超时参数的 `get` 方法获取任务结果时，如果任务在指定时间内未完成，会抛出 `TimeoutException`。

### 2. 异常处理方法

#### `exceptionally` 方法



`exceptionally` 方法用于处理 `CompletableFuture` 任务执行过程中抛出的异常。它接受一个 `Function` 类型的参数，当任务执行过程中出现异常时，会调用该函数进行异常处理，并返回一个默认值。



**示例代码**：











```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ExceptionallyExample {
    public static void main(String[] args) {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            if (Math.random() < 0.5) {
                throw new RuntimeException("模拟异常");
            }
            return 10;
        }).exceptionally(ex -> {
            System.out.println("捕获到异常: " + ex.getMessage());
            return -1;
        });

        try {
            Integer result = future.get();
            System.out.println("最终结果: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
```



**代码解释**：



- `supplyAsync` 方法封装的任务中，有 50% 的概率抛出 `RuntimeException`。
- `exceptionally` 方法捕获到异常后，输出异常信息，并返回默认值 -1。
- 最后通过 `get` 方法获取任务结果。

#### `handle` 方法



`handle` 方法无论任务是否正常完成，都会执行。它接受一个 `BiFunction` 类型的参数，其中第一个参数是任务的正常结果（如果任务正常完成），第二个参数是任务执行过程中抛出的异常（如果有异常）。



**示例代码**：








```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class HandleExample {
    public static void main(String[] args) {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            if (Math.random() < 0.5) {
                throw new RuntimeException("模拟异常");
            }
            return 10;
        }).handle((result, ex) -> {
            if (ex != null) {
                System.out.println("捕获到异常: " + ex.getMessage());
                return -1;
            }
            return result;
        });

        try {
            Integer finalResult = future.get();
            System.out.println("最终结果: " + finalResult);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
```



**代码解释**：



- `handle` 方法会根据任务是否正常完成，分别处理正常结果和异常情况。
- 如果任务正常完成，`ex` 参数为 `null`，直接返回结果；如果任务抛出异常，`ex` 参数包含异常信息，输出异常信息并返回默认值 -1。

#### `whenComplete` 方法



`whenComplete` 方法与 `handle` 方法类似，也是无论任务是否正常完成都会执行。但它不返回新的结果，只是对任务的结果或异常进行处理。



**示例代码**：










```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class WhenCompleteExample {
    public static void main(String[] args) {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            if (Math.random() < 0.5) {
                throw new RuntimeException("模拟异常");
            }
            return 10;
        }).whenComplete((result, ex) -> {
            if (ex != null) {
                System.out.println("捕获到异常: " + ex.getMessage());
            } else {
                System.out.println("任务正常完成，结果为: " + result);
            }
        });

        try {
            Integer finalResult = future.get();
            System.out.println("最终结果: " + finalResult);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
```



**代码解释**：



- `whenComplete` 方法根据任务是否正常完成，输出相应的信息。
- 它不会改变任务的结果，最终结果仍然通过 `get` 方法获取。

### 3. 在链式调用中处理异常



在使用 `CompletableFuture` 进行链式调用时，异常处理同样重要。可以在链式调用的任意位置添加异常处理方法。



**示例代码**：










```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ChainedExceptionHandling {
    public static void main(String[] args) {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            if (Math.random() < 0.5) {
                throw new RuntimeException("模拟异常");
            }
            return 10;
        }).thenApply(result -> result * 2)
          .exceptionally(ex -> {
                System.out.println("捕获到异常: " + ex.getMessage());
                return -1;
            });

        try {
            Integer result = future.get();
            System.out.println("最终结果: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
```







**代码解释**：



- 在链式调用中，`supplyAsync` 方法封装的任务可能会抛出异常。
- `exceptionally` 方法可以捕获该异常，并进行相应的处理。



通过以上的异常处理方法，可以有效地处理 `CompletableFuture` 任务执行过程中出现的异常，提高程序的健壮性。

## 实现并发流处理

在 Java 中，`CompletableFuture` 是一个强大的工具，可用于实现并发流处理。以下将详细介绍如何使用 `CompletableFuture` 实现并发流处理，包括基本思路、示例代码及代码解释。

### 基本思路



实现并发流处理通常遵循以下步骤：



1. **创建任务集合**：将需要处理的数据转换为一系列的 `CompletableFuture` 任务。
2. **并行执行任务**：利用 `CompletableFuture` 的异步执行特性，让这些任务并发执行。
3. **合并任务结果**：等待所有任务完成后，将结果进行合并和处理。
4. **异常处理**：对任务执行过程中可能出现的异常进行捕获和处理。

### 示例代码



收起



java









```java
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class CompletableFutureConcurrentStreamProcessing {

    public static void main(String[] args) {
        // 模拟需要处理的数据列表
        List<Integer> dataList = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // 创建 CompletableFuture 任务列表
        List<CompletableFuture<Integer>> futures = dataList.stream()
               .map(data -> CompletableFuture.supplyAsync(() -> processData(data)))
               .collect(Collectors.toList());

        // 组合所有 CompletableFuture 任务
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        // 等待所有任务完成，并获取结果
        CompletableFuture<List<Integer>> allResults = allFutures.thenApply(v ->
                futures.stream()
                       .map(future -> {
                            try {
                                return future.get();
                            } catch (InterruptedException | ExecutionException e) {
                                // 处理异常
                                Thread.currentThread().interrupt();
                                System.err.println("任务执行出错: " + e.getMessage());
                                return null;
                            }
                        })
                       .collect(Collectors.toList())
        );

        try {
            // 获取最终结果
            List<Integer> results = allResults.get();
            System.out.println("并发处理结果: " + results);
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            System.err.println("获取结果出错: " + e.getMessage());
        }
    }

    // 模拟数据处理方法
    private static Integer processData(Integer data) {
        try {
            // 模拟耗时操作
            Thread.sleep((long) (Math.random() * 1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("数据处理被中断: " + e.getMessage());
        }
        return data * 2;
    }
}
```

### 代码解释



1. **创建任务集合**：
    - 定义一个包含多个整数的 `dataList` 作为需要处理的数据。
    - 使用 Java 8 的 Stream API 将 `dataList` 中的每个元素映射为一个 `CompletableFuture` 任务，每个任务调用 `processData` 方法对数据进行处理。
    - 将这些 `CompletableFuture` 任务收集到 `futures` 列表中。
2. **并行执行任务**：
    - 使用 `CompletableFuture.allOf` 方法将 `futures` 列表中的所有 `CompletableFuture` 任务组合成一个新的 `CompletableFuture` 对象 `allFutures`。
    - `allFutures` 会在所有子任务都完成后才会完成，从而实现了任务的并行执行。
3. **合并任务结果**：
    - 在 `allFutures` 完成后，使用 `thenApply` 方法对结果进行处理。
    - 通过 Stream API 遍历 `futures` 列表，调用每个 `CompletableFuture` 的 `get` 方法获取结果，并将结果收集到一个新的列表中。
4. **异常处理**：
    - 在 `processData` 方法中，捕获 `InterruptedException` 异常并进行处理。
    - 在获取 `CompletableFuture` 结果时，捕获 `InterruptedException` 和 `ExecutionException` 异常，并进行相应的处理。

### 其他补充说明



- **自定义线程池**：默认情况下，`CompletableFuture` 使用 `ForkJoinPool.commonPool()` 来执行任务。如果需要更精细地控制线程池的大小和行为，可以自定义线程池并将其传递给 `supplyAsync` 等方法。例如：



收起



java









```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

ExecutorService executor = Executors.newFixedThreadPool(5);
CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> processData(data), executor);
```







- **任务依赖和链式调用**：`CompletableFuture` 支持任务之间的依赖关系和链式调用。可以使用 `thenApply`、`thenCompose`、`thenCombine` 等方法实现更复杂的并发流处理逻辑。例如：



收起



java









```java
CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> 10);
CompletableFuture<Integer> future2 = future1.thenApply(result -> result * 2);
```







通过以上步骤和方法，你可以使用 `CompletableFuture` 实现高效的并发流处理。


## 实现并行流
在 Java 中，`CompletableFuture` 可以用于实现并行流处理，它允许你以异步和非阻塞的方式并行执行多个任务，并处理它们的结果。以下是使用 `CompletableFuture` 实现并行流处理的详细步骤和示例代码。

### 1. 基本思路



要使用 `CompletableFuture` 实现并行流处理，通常需要以下几个步骤：



1. **创建任务列表**：将需要处理的任务封装成 `CompletableFuture` 对象。
2. **并行执行任务**：使用 `CompletableFuture.allOf` 或其他方法并行执行这些任务。
3. **处理任务结果**：等待所有任务完成后，获取并处理每个任务的结果。

### 2. 示例代码

#### 示例 1：简单的并行任务处理





```java
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureParallelProcessing {
    public static void main(String[] args) {
        // 模拟需要处理的数据列表
        List<Integer> dataList = List.of(1, 2, 3, 4, 5);

        // 创建 CompletableFuture 任务列表
        List<CompletableFuture<Integer>> futures = new ArrayList<>();
        for (Integer data : dataList) {
            CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> processData(data));
            futures.add(future);
        }

        // 将所有 CompletableFuture 组合成一个新的 CompletableFuture
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        // 等待所有任务完成，并获取结果
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
            // 获取最终结果
            List<Integer> results = allResults.get();
            System.out.println("处理结果: " + results);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    // 模拟数据处理方法
    private static Integer processData(Integer data) {
        try {
            // 模拟耗时操作
            Thread.sleep((long) (Math.random() * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return data * 2;
    }
}
```



**代码解释**：



- 首先，创建了一个包含整数的列表 `dataList`，表示需要处理的数据。
- 然后，遍历 `dataList`，将每个数据封装成一个 `CompletableFuture` 对象，并添加到 `futures` 列表中。每个 `CompletableFuture` 都会异步执行 `processData` 方法。
- 使用 `CompletableFuture.allOf` 方法将所有 `CompletableFuture` 组合成一个新的 `CompletableFuture`，该 `CompletableFuture` 会在所有任务完成后完成。
- 通过 `thenApply` 方法在所有任务完成后，获取每个任务的结果，并将其收集到一个新的列表中。
- 最后，调用 `get` 方法获取最终的结果列表并输出。

#### 示例 2：处理异常的并行任务处理











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
                    throw new RuntimeException("模拟异常");
                }
                return processData(data);
            }).exceptionally(ex -> {
                System.out.println("处理数据 " + data + " 时出现异常: " + ex.getMessage());
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
            System.out.println("处理结果: " + results);
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







**代码解释**：



- 与示例 1 类似，但在 `supplyAsync` 方法中模拟了一个异常，当处理的数据为 3 时，抛出 `RuntimeException`。
- 使用 `exceptionally` 方法处理异常，当出现异常时，输出异常信息并返回 -1 作为默认结果。
- 后续步骤与示例 1 相同，最终获取并输出处理结果。

### 3. 总结



通过使用 `CompletableFuture`，可以方便地实现并行流处理。`CompletableFuture` 提供了丰富的方法来处理异步任务，包括任务的组合、异常处理等，能够满足各种复杂的并行处理需求。

## 实现异步流

在 Java 中，`CompletableFuture` 可以用于实现异步流处理，允许你以异步和非阻塞的方式处理一系列任务。下面将从几个方面详细介绍如何使用 `CompletableFuture` 进行异步流处理，并给出相应的示例代码。

### 1. 基本异步任务执行



首先，我们来看如何使用 `CompletableFuture` 执行单个异步任务。可以使用 `supplyAsync` 方法来执行有返回值的任务，使用 `runAsync` 方法来执行无返回值的任务。








```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class BasicAsyncTask {
    public static void main(String[] args) {
        // 执行有返回值的异步任务
        CompletableFuture<String> futureWithResult = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task result";
        });

        // 执行无返回值的异步任务
        CompletableFuture<Void> futureWithoutResult = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Void task completed");
        });

        try {
            // 获取有返回值任务的结果
            String result = futureWithResult.get();
            System.out.println("Result: " + result);
            // 等待无返回值任务完成
            futureWithoutResult.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
```

### 2. 链式异步任务处理



`CompletableFuture` 支持链式调用，允许你在一个任务完成后接着执行另一个任务。可以使用 `thenApply`、`thenCompose` 等方法实现。

#### `thenApply` 方法



`thenApply` 方法用于对前一个任务的结果进行转换。











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

#### `thenCompose` 方法



`thenCompose` 方法用于将一个 `CompletableFuture` 的结果传递给另一个返回 `CompletableFuture` 的函数，实现更复杂的链式异步操作。







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

### 3. 并行异步任务处理



可以使用 `CompletableFuture.allOf` 和 `CompletableFuture.anyOf` 方法来并行处理多个异步任务。

#### `CompletableFuture.allOf`



`allOf` 方法用于并行执行多个 `CompletableFuture` 任务，并等待所有任务完成。






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



`anyOf` 方法用于并行执行多个 `CompletableFuture` 任务，只要有一个任务完成就会继续后续操作。












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

### 4. 异常处理



在异步流处理中，需要对可能出现的异常进行处理。可以使用 `exceptionally` 方法来处理异常。







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







通过以上几种方式，你可以使用 `CompletableFuture` 实现复杂的异步流处理，提高程序的性能和响应性。

## 组合CompletableFuture

在 Java 中，`CompletableFuture` 提供了多种方法来将多个 `CompletableFuture` 组合成一个，以满足不同的业务需求。下面为你详细介绍几种常见的组合方式。

### 1. 串行组合



当你需要按顺序依次执行多个 `CompletableFuture` 任务，即一个任务完成后再执行下一个任务时，可以使用 `thenCompose` 和 `thenApply` 方法。

#### `thenCompose` 方法



`thenCompose` 方法用于将一个 `CompletableFuture` 的结果传递给另一个返回 `CompletableFuture` 的函数，从而实现串行组合。








```java
import java.util.concurrent.CompletableFuture;

public class CompletableFutureSerialCombination {
    public static void main(String[] args) {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> combinedFuture = future1.thenCompose(result1 ->
                CompletableFuture.supplyAsync(() -> result1 + " World")
        );

        combinedFuture.thenAccept(System.out::println);

        // 等待任务完成
        try {
            combinedFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```



在上述代码中，`future1` 先执行并返回结果 `"Hello"`，然后将这个结果传递给 `thenCompose` 中的函数，该函数返回一个新的 `CompletableFuture`，最终组合成一个新的 `CompletableFuture` 并输出结果 `"Hello World"`。

#### `thenApply` 方法



`thenApply` 方法与 `thenCompose` 类似，但它接受的函数返回的是一个普通结果，而不是 `CompletableFuture`。









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

### 2. 并行组合并等待所有任务完成



当你需要并行执行多个 `CompletableFuture` 任务，并在所有任务都完成后进行后续操作时，可以使用 `CompletableFuture.allOf` 方法。










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



在上述代码中，`future1` 和 `future2` 并行执行，`CompletableFuture.allOf` 方法会返回一个新的 `CompletableFuture`，当所有传入的 `CompletableFuture` 都完成时，这个新的 `CompletableFuture` 才会完成。然后通过 `thenApply` 方法获取每个任务的结果并组合。

### 3. 并行组合并等待任意一个任务完成



当你只需要在多个并行执行的 `CompletableFuture` 任务中有一个完成时就进行后续操作，可以使用 `CompletableFuture.anyOf` 方法。












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

        anyFuture.thenAccept(result -> System.out.println("第一个完成的结果: " + result));

        try {
            anyFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```



在上述代码中，`future1` 和 `future2` 并行执行，`CompletableFuture.anyOf` 方法会返回一个新的 `CompletableFuture`，当任意一个传入的 `CompletableFuture` 完成时，这个新的 `CompletableFuture` 就会完成，并可以获取到第一个完成的任务的结果。

### 4. 合并两个 `CompletableFuture` 的结果



如果你需要将两个 `CompletableFuture` 的结果合并，可以使用 `thenCombine` 方法。











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







在上述代码中，`future1` 和 `future2` 并行执行，`thenCombine` 方法会在两个任务都完成后，将它们的结果传递给一个二元函数进行合并，最终得到一个新的 `CompletableFuture` 包含合并后的结果。