
它们的返回值都是整数，且都采用四舍五入法。运算规则如下：

1. 如果参数为正数，且小数点后第一位>=5，运算结果为参数的整数部分+1。
2. 如果参数为负数，且小数点后第一位>5，运算结果为参数的整数部分-1。
3. 如果参数为正数，且小数点后第一位<5；或者参数为负数，且小数点后第一位<=5，运算结果为参数的整数部分。
  
```java
   public class MathTest {
   public static void main(String[] args) {
   System.out.println("小数点后第一位=5");
   System.out.println("正数：Math.round(11.5)=" + Math.round(11.5));
   System.out.println("负数：Math.round(-11.5)=" + Math.round(-11.5));
   System.out.println();
   System.out.println("小数点后第一位<5");
   System.out.println("正数：Math.round(11.46)=" + Math.round(11.46));
   System.out.println("负数：Math.round(-11.46)=" + Math.round(-11.46));
   System.out.println();
   System.out.println("小数点后第一位>5");
   System.out.println("正数：Math.round(11.68)=" + Math.round(11.68));
   System.out.println("负数：Math.round(-11.68)=" + Math.round(-11.68));
   }
   }
```

   运行结果：
1. 小数点后第一位=5
2. 正数：Math.round(11.5)=12
3. 负数：Math.round(-11.5)=-11
4.
5. 小数点后第一位<5
6. 正数：Math.round(11.46)=11
7. 负数：Math.round(-11.46)=-11
8.
9. 小数点后第一位>5
10. 正数：Math.round(11.68)=12
11. 负数：Math.round(-11.68)=-12


根据上面例子的运行结果，我们还可以按照如下方式总结，或许更加容易记忆：
1. 参数的小数点后第一位<5，运算结果为参数整数部分。
2. 参数的小数点后第一位>5，运算结果为参数整数部分绝对值+1，符号（即正负）不变。
3. 参数的小数点后第一位=5，正数运算结果为整数部分+1，负数运算结果为整数部分。