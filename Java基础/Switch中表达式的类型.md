��Java�У�`switch`���ı��ʽ���Ϳ��������¼��֣�

1. **��������**��
    - `byte`
    - `short`
    - `int`
    - `char`
2. **ö������**����Java 5��ʼ����
    - ö�ٳ���
3. **�ַ�������**����Java 7��ʼ����
    - `String`
4. **��װ������**����Java 5��ʼ����
    - `Byte`
    - `Short`
    - `Integer`
    - `Character`

**����?����(long)��?ǰ���еİ汾�ж��ǲ����Ե�**
### ʾ������

#### 1. ��������



```
int day = 3;
switch (day) {
    case 1:
        System.out.println("Monday");
        break;
    case 2:
        System.out.println("Tuesday");
        break;
    case 3:
        System.out.println("Wednesday");
        break;
    default:
        System.out.println("Invalid day");
}
```

#### 2. ö������



```
enum Day { MONDAY, TUESDAY, WEDNESDAY }

Day day = Day.WEDNESDAY;
switch (day) {
    case MONDAY:
        System.out.println("Monday");
        break;
    case TUESDAY:
        System.out.println("Tuesday");
        break;
    case WEDNESDAY:
        System.out.println("Wednesday");
        break;
    default:
        System.out.println("Invalid day");
}
```

#### 3. �ַ�������



```
String day = "Wednesday";
switch (day) {
    case "Monday":
        System.out.println("Monday");
        break;
    case "Tuesday":
        System.out.println("Tuesday");
        break;
    case "Wednesday":
        System.out.println("Wednesday");
        break;
    default:
        System.out.println("Invalid day");
}
```

#### 4. ��װ������




```
Integer day = 3;
switch (day) {
    case 1:
        System.out.println("Monday");
        break;
    case 2:
        System.out.println("Tuesday");
        break;
    case 3:
        System.out.println("Wednesday");
        break;
    default:
        System.out.println("Invalid day");
}
```

### ע������

- `switch`���ʽ�����ͱ�����`case`��ǩ�����ͼ��ݡ�
- ��Java 12��ʼ��`switch`���ʽ��������Ϊ���ʽʹ�ã�����֧��`->`�﷨��`yield`�ؼ��֡�

### Java 12+ ʾ��



```
int day = 3;
String dayName = switch (day) {
    case 1 -> "Monday";
    case 2 -> "Tuesday";
    case 3 -> "Wednesday";
    default -> "Invalid day";
};
System.out.println(dayName);
```

