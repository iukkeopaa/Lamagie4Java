
���ǵķ���ֵ�����������Ҷ������������뷨������������£�

1. �������Ϊ��������С������һλ>=5��������Ϊ��������������+1��
2. �������Ϊ��������С������һλ>5��������Ϊ��������������-1��
3. �������Ϊ��������С������һλ<5�����߲���Ϊ��������С������һλ<=5��������Ϊ�������������֡�
  
```java
   public class MathTest {
   public static void main(String[] args) {
   System.out.println("С������һλ=5");
   System.out.println("������Math.round(11.5)=" + Math.round(11.5));
   System.out.println("������Math.round(-11.5)=" + Math.round(-11.5));
   System.out.println();
   System.out.println("С������һλ<5");
   System.out.println("������Math.round(11.46)=" + Math.round(11.46));
   System.out.println("������Math.round(-11.46)=" + Math.round(-11.46));
   System.out.println();
   System.out.println("С������һλ>5");
   System.out.println("������Math.round(11.68)=" + Math.round(11.68));
   System.out.println("������Math.round(-11.68)=" + Math.round(-11.68));
   }
   }
```

   ���н����
1. С������һλ=5
2. ������Math.round(11.5)=12
3. ������Math.round(-11.5)=-11
4.
5. С������һλ<5
6. ������Math.round(11.46)=11
7. ������Math.round(-11.46)=-11
8.
9. С������һλ>5
10. ������Math.round(11.68)=12
11. ������Math.round(-11.68)=-12


�����������ӵ����н�������ǻ����԰������·�ʽ�ܽᣬ����������׼��䣺
1. ������С������һλ<5��������Ϊ�����������֡�
2. ������С������һλ>5��������Ϊ�����������־���ֵ+1�����ţ������������䡣
3. ������С������һλ=5������������Ϊ��������+1������������Ϊ�������֡�