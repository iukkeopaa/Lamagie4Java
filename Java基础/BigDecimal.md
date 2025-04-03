### 1. `BigDecimal` �Ĵ洢

`BigDecimal` �� Java �����ھ�ȷ��ʾ�ͼ������⾫��ʮ���������ࡣ���ڲ�ͨ��������Ҫ�������洢��ֵ��

- **��������**��ʹ�� `int[]` ����洢�������������� `BigDecimal` ��ʾ�ǳ�����������֡�
- **��ȣ�scale��**����ʾС�����λ�á������һ�� `int` ���͵�ֵ������ָʾС����������������ֵ�λ�á�

���磬������ֵ `123.45`���������ִ洢Ϊ `12345`�����Ϊ `2`����ʾС�����ڴ����������� 2 λ֮��

�����Ǵ��� `BigDecimal` ����ĳ�����ʽ��

java











```java
import java.math.BigDecimal;

public class BigDecimalStorageExample {
    public static void main(String[] args) {
        // ʹ���ַ������� BigDecimal ����
        BigDecimal num1 = new BigDecimal("123.45");
        // ʹ���������� BigDecimal ����
        BigDecimal num2 = new BigDecimal(123);
        // ʹ��˫���ȸ��������� BigDecimal ���󣨲��Ƽ���
        BigDecimal num3 = new BigDecimal(123.45); 

        System.out.println("num1: " + num1);
        System.out.println("num2: " + num2);
        System.out.println("num3: " + num3);
    }
}
```

��Ҫע����ǣ�ʹ�� `double` ���͵Ĳ������� `BigDecimal` ����ʱ�����ܻ���־������⣬��Ϊ `double` ������ھ�����ʧ�������Ƽ�ʹ�� `String` ���͵Ĳ��������� `BigDecimal` ����

### 2. `BigDecimal` �ļ���

`BigDecimal` �ṩ��һϵ�еķ������ڽ��л�������ѧ���㣬��ӷ����������˷��ͳ����ȡ�

java











```java
import java.math.BigDecimal;

public class BigDecimalCalculationExample {
    public static void main(String[] args) {
        BigDecimal num1 = new BigDecimal("10.5");
        BigDecimal num2 = new BigDecimal("2.5");

        // �ӷ�
        BigDecimal sum = num1.add(num2);
        System.out.println("�ӷ����: " + sum);

        // ����
        BigDecimal difference = num1.subtract(num2);
        System.out.println("�������: " + difference);

        // �˷�
        BigDecimal product = num1.multiply(num2);
        System.out.println("�˷����: " + product);

        // ����
        // ��Ҫָ������ģʽ��������ܻ��׳� ArithmeticException
        BigDecimal quotient = num1.divide(num2, 2, BigDecimal.ROUND_HALF_UP); 
        System.out.println("�������: " + quotient);
    }
}
```

�ڽ��г�������ʱ����Ҫָ������ģʽ�ͱ�����С��λ������Ϊ��Щ����������ܻ��������ѭ��С������ָ����Щ�������׳� `ArithmeticException` �쳣��

### 3. ע������

- **���췽��ѡ��**����ǰ������������ʹ�� `String` ���͵Ĺ��췽�������� `BigDecimal` ���󣬱���ʹ�� `double` ���͵Ĺ��췽�����Է�ֹ���ȶ�ʧ��
- **����ģʽ**���ڽ��г�����ȡ���Ȳ���ʱ��������ȷָ������ģʽ��������ܻ��׳��쳣��Java �ṩ�˶�������ģʽ���� `ROUND_HALF_UP`���������룩��`ROUND_DOWN`��ֱ�ӽضϣ��ȡ�
- **���ܿ���**������ `BigDecimal` ��Ҫ�������⾫�ȵ���ֵ�������������Ի����������ͣ��� `int`��`double`�����һЩ���ڶ�����Ҫ�󼫸��ҶԾ���Ҫ�󲻸ߵĳ����£�Ӧ����ʹ�á�
- **���󲻿ɱ���**��`BigDecimal` �����ǲ��ɱ�ģ�ÿ�ν��м��㶼�᷵��һ���µ� `BigDecimal` ���󣬶������޸�ԭ������ˣ��ڽ��ж�μ���ʱ����Ҫע���������ø��¡�

### 4. Ϊʲô `BigDecimal` �ܱ�֤����ʧ����

- **����ʮ���Ʊ�ʾ**��`BigDecimal` ����ʮ���Ʊ�ʾ������������ `float` �� `double` �������ڶ����Ʊ�ʾ��ʮ���Ʊ�ʾ���Ծ�ȷ�ر�ʾ���е�ʮ���Ʒ����������˶����Ʊ�ʾ�ڱ�ʾĳЩʮ����С��ʱ�ľ��ȶ�ʧ���⡣
- **���⾫��֧��**��`BigDecimal` ���Ա�ʾ�����С�;��ȵ�ʮ����������ͨ���ڲ����������洢���������֣���ʹ�ñ������ʾС�����λ�ã���˿��Դ���ǳ����ǳ�С����ֵ���Ҳ��ᶪʧ���ȡ�

����������`BigDecimal` �� Java �д���߾���ʮ���Ƽ������Ҫ���ߣ�����ʹ��ʱ��Ҫע�⹹�췽����ѡ������ģʽ��ָ�������⡣



