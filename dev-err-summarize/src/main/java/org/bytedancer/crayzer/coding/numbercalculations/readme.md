## 数值计算：注意精度、舍入和溢出问题
### “危险”的Double：dangerousdouble
> 对于计算机而言，0.1 无法精确表达，这是浮点数计算造成精度损失的根源。

[IEEE 754 标准]: https://en.wikipedia.org/wiki/IEEE_754
[查看数值转化为二进制的结果]: http://www.binaryconvert.com/
1. **浮点数避坑第一原则：要精确表示浮点数应该使用 BigDecimal。并且，使用 BigDecimal 的 Double 入参
的构造方法同样存在精度丢失问题，应该使用 String 入参的构造方法或者 BigDecimal.valueOf 方法来初始
化。**
2. **对浮点数做精确计算，参与计算的各种数值应该始终使用 BigDecimal，所有的计算都要通过 BigDecimal 的
方法进行，切勿只是让 BigDecimal 来走过场。任何一个环节出现精度损失，最后的计算结果可能都会出现误
差。**

scale 表示小数点右边的位数，而 precision 表示精度，也就是有效数字的长度

Formatter 类的相关源码
java.util.Formatter.FormatSpecifier.print()
```
else if (c == Conversion.DECIMAL_FLOAT) {
    // Create a new BigDecimal with the desired precision.
    int prec = (precision == -1 ? 6 : precision);
    int scale = value.scale();

    if (scale > prec) {
        // more "scale" digits than the requested "precision"
        int compPrec = value.precision();
        if (compPrec <= scale) {
            // case of 0.xxxxxx
            value = value.setScale(prec, RoundingMode.HALF_UP);
        } else {
            compPrec -= (scale - prec);
            value = new BigDecimal(value.unscaledValue(),
                                   scale,
                                   new MathContext(compPrec));
        }
    }
```

### 考虑浮点数舍入和格式化的方式：rounding
- **对于浮点数的格式化，如果使用 String.format 的话，需要认识到它使用的是四舍五入，可以考虑使用 
DecimalFormat 来明确指定舍入方式。但考虑到精度问题，我更建议使用 BigDecimal 来表示浮点数，并使
用其 setScale 方法指定舍入的位数和方式。**

### 用equals做判等，就一定是对的吗？：equals
1. **equals 比较的是 BigDecimal 的 value 和 scale**
2. **只比较 BigDecimal 的 value，可以使用 compareTo 方法**

### 小心数值溢出问题：overflowissue
1. BigDecimal 是处理浮点数的专家，而 BigInteger 则是对大数进行科学计算的专家
2. Math 类的 addExact、subtractExact 等 xxExact 方法进行数值运算，这些方法可以在数值溢出时主动抛出
ArithmeticException 异常


