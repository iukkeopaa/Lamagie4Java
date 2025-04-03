![img_1.png](img_1.png)

## ����Ŀ¼��C:\\testDir1�����������ļ��У�dir1��dir2����һ���ļ�file1.txt,�г����Ŀ¼�µ�����Ŀ¼���ļ�


�� Java �У�����ʹ�� `java.io.File` ����� `java.nio.file` ���µ������г�ָ��Ŀ¼�µ�����Ŀ¼���ļ�������ֱ����������ʵ�ַ�ʽ��ʾ�����롣

### ʹ�� `java.io.File` ��








```java
import java.io.File;

public class ListFilesAndDirectories {
    public static void main(String[] args) {
        // ָ��Ŀ¼·��
        String directoryPath = "C:\\testDir1";
        File directory = new File(directoryPath);

        // ���Ŀ¼�Ƿ����
        if (directory.exists() && directory.isDirectory()) {
            // ��ȡĿ¼�µ������ļ����ļ���
            File[] filesAndDirs = directory.listFiles();
            if (filesAndDirs != null) {
                for (File file : filesAndDirs) {
                    if (file.isDirectory()) {
                        System.out.println("Ŀ¼: " + file.getName());
                    } else if (file.isFile()) {
                        System.out.println("�ļ�: " + file.getName());
                    }
                }
            }
        } else {
            System.out.println("ָ����Ŀ¼�����ڡ�");
        }
    }
}
```

### �������



1. **���� `File` ����**��ʹ��ָ����Ŀ¼·������һ�� `File` ����
2. **���Ŀ¼�Ƿ����**��ͨ�� `exists()` �� `isDirectory()` �������ָ����Ŀ¼�Ƿ������ΪĿ¼��
3. **��ȡĿ¼�µ������ļ����ļ���**��ʹ�� `listFiles()` ������ȡĿ¼�µ������ļ����ļ��У�����һ�� `File` ���顣
4. **�������鲢�����Ϣ**������ `File` ���飬ͨ�� `isDirectory()` �� `isFile()` �����ж�ÿ��Ԫ����Ŀ¼�����ļ����������Ӧ����Ϣ��

### ʹ�� `java.nio.file` ��












```java
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ListFilesAndDirectoriesNIO {
    public static void main(String[] args) {
        // ָ��Ŀ¼·��
        String directoryPath = "C:\\testDir1";
        Path directory = Paths.get(directoryPath);

        // ���Ŀ¼�Ƿ����
        if (Files.exists(directory) && Files.isDirectory(directory)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
                for (Path path : stream) {
                    if (Files.isDirectory(path)) {
                        System.out.println("Ŀ¼: " + path.getFileName());
                    } else if (Files.isRegularFile(path)) {
                        System.out.println("�ļ�: " + path.getFileName());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("ָ����Ŀ¼�����ڡ�");
        }
    }
}
```





### �������



1. **���� `Path` ����**��ʹ�� `Paths.get()` ��������ָ����Ŀ¼·������һ�� `Path` ����
2. **���Ŀ¼�Ƿ����**��ͨ�� `Files.exists()` �� `Files.isDirectory()` �������ָ����Ŀ¼�Ƿ������ΪĿ¼��
3. **��ȡĿ¼�µ������ļ����ļ���**��ʹ�� `Files.newDirectoryStream()` ������ȡĿ¼�µ������ļ����ļ��е� `DirectoryStream` ����
4. **�������������Ϣ**������ `DirectoryStream` ����ͨ�� `Files.isDirectory()` �� `Files.isRegularFile()` �����ж�ÿ��Ԫ����Ŀ¼�����ļ����������Ӧ����Ϣ��

### ע������



- ��ʹ�� `File` ��� `listFiles()` ����ʱ����Ҫע��÷������ܷ��� `null`�������ʹ�÷��ص�����֮ǰ��Ҫ���п�ֵ��顣
- ��ʹ�� `java.nio.file` ��ʱ��ʹ�� `try-with-resources` ���ȷ�� `DirectoryStream` ��ȷ�رգ�������Դй©��


## 1����дһ������,�� a.txt �ļ��еĵ����� b.txt �ļ��еĵ��ʽ���ϲ��� c.txt�ļ���,a.txt �ļ��еĵ����ûس����ָ�,b.txt �ļ����ûس���ո���зָ���


������ʹ�� Java ʵ�ֽ� `a.txt` �ļ��еĵ����� `b.txt` �ļ��еĵ��ʽ���ϲ��� `c.txt` �ļ��еĴ��룺











```java
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MergeFiles {
    public static void main(String[] args) {
        String fileAPath = "a.txt";
        String fileBPath = "b.txt";
        String fileCPath = "c.txt";

        // ��ȡ a.txt �ļ��еĵ���
        List<String> wordsA = readWordsFromFile(fileAPath, "\n");
        // ��ȡ b.txt �ļ��еĵ���
        List<String> wordsB = readWordsFromFile(fileBPath, "\n| ");
        // ����ϲ�����
        List<String> mergedWords = mergeWords(wordsA, wordsB);
        // ���ϲ���ĵ���д�� c.txt �ļ�
        writeWordsToFile(mergedWords, fileCPath);
    }

    /**
     * ���ļ��ж�ȡ����
     * @param filePath �ļ�·��
     * @param delimiter �ָ���
     * @return �����б�
     */
    private static List<String> readWordsFromFile(String filePath, String delimiter) {
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineWords = line.split(delimiter);
                for (String word : lineWords) {
                    if (!word.trim().isEmpty()) {
                        words.add(word.trim());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("��ȡ�ļ� " + filePath + " ʱ����: " + e.getMessage());
        }
        return words;
    }

    /**
     * ����ϲ����������б�
     * @param wordsA ��һ�������б�
     * @param wordsB �ڶ��������б�
     * @return �ϲ���ĵ����б�
     */
    private static List<String> mergeWords(List<String> wordsA, List<String> wordsB) {
        List<String> mergedWords = new ArrayList<>();
        int maxSize = Math.max(wordsA.size(), wordsB.size());
        for (int i = 0; i < maxSize; i++) {
            if (i < wordsA.size()) {
                mergedWords.add(wordsA.get(i));
            }
            if (i < wordsB.size()) {
                mergedWords.add(wordsB.get(i));
            }
        }
        return mergedWords;
    }

    /**
     * �������б�д���ļ�
     * @param words �����б�
     * @param filePath �ļ�·��
     */
    private static void writeWordsToFile(List<String> words, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String word : words) {
                writer.write(word);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("д���ļ� " + filePath + " ʱ����: " + e.getMessage());
        }
    }
}
```

### ������ͣ�



1. `readWordsFromFile` ����

   ��

    - �˷��������ļ�·���ͷָ�����Ϊ���������ڴ�ָ���ļ��ж�ȡ���ʡ�
    - ʹ�� `BufferedReader` ���ж�ȡ�ļ����ݣ���ÿ������ʹ��ָ���ķָ������зָ
    - ���ָ��ķǿյ�����ӵ� `List` �У���󷵻ظ� `List`������ȡ�ļ������г��� `IOException`�������������Ϣ��

2. `mergeWords` ����

   ��

    - �÷����������������б���Ϊ�����������ǽ���ϲ���һ���µ� `List` �С�
    - �ҳ������б��е���󳤶ȣ������ó��ȷ�Χ�����ν������б��еĵ�����ӵ��ϲ��б��У����շ��غϲ���� `List`��

3. `writeWordsToFile` ����

   ��

    - �˷������յ����б���ļ�·����Ϊ�������������б��е�ÿ����������д��ָ���ļ���
    - ʹ�� `BufferedWriter` �����ļ�д���������д������г��� `IOException`�������������Ϣ��

4. `main` ����

   ��

    - �����������ļ���·����`a.txt`��`b.txt` �� `c.txt`��
    - ���� `readWordsFromFile` �����ֱ��ȡ `a.txt` �� `b.txt` �еĵ��ʡ�
    - ���� `mergeWords` ���������������б���ϲ���
    - ���� `writeWordsToFile` �������ϲ���ĵ����б�д�� `c.txt` �ļ���

### ע�����



- ��ȷ�� `a.txt` �� `b.txt` �ļ������ڳ������е�Ŀ¼�£����߸���ʵ������޸��ļ�·����
- �����д������ļ���ȡ��д��ʱ���ܳ��ֵ� `IOException`��������ļ�·��������ļ�Ȩ�޲��㣬�Ի������Ӧ�Ĵ�����Ϣ��


## 2����дһ�����򣬽� d:\javaĿ¼�µ�����.java �ļ����Ƶ� d:\jad Ŀ¼�£�����ԭ���ļ�����չ����.java��Ϊ.jad

������һ��ʹ�� Java ��д�ĳ������ڽ� `d:\java` Ŀ¼�µ����� `.java` �ļ����Ƶ� `d:\jad` Ŀ¼�£������ļ���չ���� `.java` ��Ϊ `.jad`��








```java
import java.io.*;

public class FileCopyAndRename {
    public static void main(String[] args) {
        // ԴĿ¼
        File sourceDir = new File("d:\\java");
        // Ŀ��Ŀ¼
        File targetDir = new File("d:\\jad");

        // ���ԴĿ¼�Ƿ����
        if (!sourceDir.exists() ||!sourceDir.isDirectory()) {
            System.out.println("ԴĿ¼�����ڻ���һ����Ч��Ŀ¼��");
            return;
        }

        // ���Ŀ��Ŀ¼�Ƿ���ڣ�����������򴴽�
        if (!targetDir.exists()) {
            if (!targetDir.mkdirs()) {
                System.out.println("�޷�����Ŀ��Ŀ¼��");
                return;
            }
        }

        // ��ȡԴĿ¼�µ������ļ�
        File[] files = sourceDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".java")) {
                    // ����Ŀ���ļ���·��
                    String targetFileName = file.getName().replace(".java", ".jad");
                    File targetFile = new File(targetDir, targetFileName);

                    try {
                        // �����ļ�
                        copyFile(file, targetFile);
                        System.out.println("�ɹ����Ʋ��������ļ�: " + file.getName() + " -> " + targetFileName);
                    } catch (IOException e) {
                        System.err.println("�����ļ� " + file.getName() + " ʱ����: " + e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * �����ļ��ķ���
     * @param source Դ�ļ�
     * @param target Ŀ���ļ�
     * @throws IOException ���ƹ����п��ܳ��ֵ� IO �쳣
     */
    private static void copyFile(File source, File target) throws IOException {
        try (InputStream in = new FileInputStream(source);
             OutputStream out = new FileOutputStream(target)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
    }
}
```

### ������ͣ�



1. Ŀ¼����봴��

   ��

    - ���ȶ�����ԴĿ¼ `d:\java` ��Ŀ��Ŀ¼ `d:\jad`��
    - ���ԴĿ¼�Ƿ������Ϊ��Ч��Ŀ¼��������������������Ϣ����ֹ����
    - ���Ŀ��Ŀ¼�Ƿ���ڣ�������������Դ�����Ŀ¼��������ʧ�������������Ϣ����ֹ����

2. �ļ�������ɸѡ

   ��

    - ʹ�� `listFiles()` ������ȡԴĿ¼�µ������ļ����ļ��С�
    - ������Щ�ļ���ɸѡ����չ��Ϊ `.java` ���ļ���

3. �ļ�������������

   ��

    - ����ÿ�� `.java` �ļ�������Ŀ���ļ���·��������չ���� `.java` ��Ϊ `.jad`��
    - ���� `copyFile()` ������Դ�ļ����Ƶ�Ŀ���ļ���
    - ������Ƴɹ�������ɹ���Ϣ��������ƹ����г��� `IOException`�������������Ϣ��

4. �ļ����Ʒ��� `copyFile()`

   ��

    - ʹ�� `FileInputStream` ��ȡԴ�ļ������ݣ�ʹ�� `FileOutputStream` ������д��Ŀ���ļ���
    - ʹ��һ���ֽ����� `buffer` ��Ϊ��������ÿ�ζ�ȡһ���������ݲ�д��Ŀ���ļ���ֱ����ȡ�������ļ���
    - ʹ�� `try-with-resources` ���ȷ�����������������ʹ����Ϻ��Զ��رա�

### ע�����



- ��ȷ���������㹻��Ȩ�޷���ԴĿ¼��Ŀ��Ŀ¼��
- ���Ŀ��Ŀ¼���Ѿ�����ͬ���� `.jad` �ļ����ᱻ���ǡ�
- �����м��� `d:\java` �� `d:\jad` �ǺϷ��� Windows Ŀ¼·�����������������ϵͳ�����У���Ҫ����ʵ������޸�·���ָ�����
## 3����дһ����ȡ�ַ����ĺ���������Ϊһ���ַ������ֽ��������Ϊ���ֽڽ�ȡ���ַ�������Ҫ��֤���ֲ�����ȡ������硰�� ABC"��4��Ӧ�ý�ȡ���� AB��,���롰�� ABC �� DEF����6��Ӧ��������� ABC"�������ǡ��� ABC+���İ����


### Java ʵ��



java











```java
public class StringCutter {
    public static String cutString(String s, int byteNum) {
        StringBuilder result = new StringBuilder();
        int currentBytes = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            // ��ȡ�ַ����ֽڳ���
            int charBytes = String.valueOf(c).getBytes().length;
            if (currentBytes + charBytes <= byteNum) {
                result.append(c);
                currentBytes += charBytes;
            } else {
                break;
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(cutString("�� ABC", 4));
        System.out.println(cutString("�� ABC �� DEF", 6));
    }
}
```

#### �������



- `cutString` ���������ַ������ֽ�����Ϊ������
- ʹ�� `StringBuilder` ��������ȡ����ַ�����
- �����ַ�������ȡÿ���ַ����ֽڳ��ȣ������ϸ��ַ�������ָ���ֽ�����������ӵ� `StringBuilder` �в������ֽ�����������ֹѭ����
- ��� `StringBuilder` ת��Ϊ�ַ������ء�

### JavaScript ʵ��














```javascript
function cutString(s, byteNum) {
    let result = '';
    let currentBytes = 0;
    for (let i = 0; i < s.length; i++) {
        const char = s[i];
        // ��ȡ�ַ����ֽڳ���
        const charBytes = encodeURIComponent(char).replace(/%[A-F\d]{2}/g, 'U').length;
        if (currentBytes + charBytes <= byteNum) {
            result += char;
            currentBytes += charBytes;
        } else {
            break;
        }
    }
    return result;
}

// ����ʾ��
console.log(cutString("�� ABC", 4));
console.log(cutString("�� ABC �� DEF", 6));
```





#### �������



- `cutString` ���������ַ������ֽ�����Ϊ������
- ͨ�� `encodeURIComponent` ���ַ����б��룬Ȼ�󽫱����İٷֺż�������λ�ַ��滻Ϊ `U`���Ӷ��õ��ַ����ֽڳ��ȡ�
- �����ϵ�ǰ�ַ����ֽڳ��Ȳ�����ָ���ֽ������򽫸��ַ���ӵ�����ַ����в������ֽ���������ֹͣ������
- ���շ��ؽ�ȡ����ַ�����






![img_2.png](img_2.png)