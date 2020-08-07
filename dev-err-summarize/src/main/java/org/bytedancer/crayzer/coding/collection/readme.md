## 集合类：坑满地的List列表操作
### 使用Arrays.asList把数据转换为List的三个坑：aslist
1. 不能直接使用 Arrays.asList 来转换基本类型数组
2. Arrays.asList 返回的 List 不支持增删操作(**Arrays.asList 返回的 List 并不是期望的 java.util.ArrayList，
而是 Arrays 的内部类 ArrayList，并没有覆写父类的 add 方法**)
3. 对原始数组的修改会影响到获得的那个 List (**把通过 Arrays.asList 获得的 List 交给其他方法处理，很容易
因为共享了数组，相互修改产生 Bug。**)

### 使用List.subList进行切片操作居然会导致OOM：sublist
//方式一：
`List<Integer> subList = new ArrayList<>(list.subList(1, 4));`
//方式二：
`List<Integer> subList = list.stream().skip(1).limit(3).collect(Collectors.toList());`

```java
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable {
  // 集合结构性修改的次数。所谓结构性修改，指的是影响 List 大小的修改
  protected transient int modCount = 0;
  private void ensureExplicitCapacity(int minCapacity) {
        modCount++;
        // overflow-conscious code
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }
  public void add(int index, E element) {
    rangeCheckForAdd(index);

    ensureCapacityInternal(size + 1);  // Increments modCount!!
    System.arraycopy(elementData, index, elementData, index + 1,
                     size - index);
    elementData[index] = element;
    size++;
  }
  
  // 获得的 List 其实是内部类 SubList，并不是普通的 ArrayList，在初始化的时候传入了 this
  public List<E> subList(int fromIndex, int toIndex) {
    subListRangeCheck(fromIndex, toIndex, size);
    return new SubList(this, offset, fromIndex, toIndex);
  }

  private class SubList extends AbstractList<E> implements RandomAccess {
    // SubList 初始化的时候，并没有把原始 List 中的元素复制到独立的变量中保存，可以认为 SubList 是
    // 原始 List 的视图，并不是独立的 List。双方对元素的修改会相互影响，而且 SubList 强引用了原始的 
    // List，所以大量保存这样的 SubList 会导致 OOM
    private final AbstractList<E> parent;
    private final int parentOffset;
    private final int offset;
    int size;

    SubList(AbstractList<E> parent,
          int offset, int fromIndex, int toIndex) {
        this.parent = parent;
        this.parentOffset = fromIndex;
        this.offset = offset + fromIndex;
        this.size = toIndex - fromIndex;
        this.modCount = ArrayList.this.modCount;
    }

    public E set(int index, E element) {
        rangeCheck(index);
        checkForComodification();
        return l.set(index+offset, element);
    }
    
    // 遍历 SubList 的时候会先获得迭代器，比较原始 ArrayList modCount 的值和 SubList 当前 modCount
    // 的值。获得了 SubList 后，我们为原始 List 新增了一个元素修改了其 modCount，所以判等失败抛出 
    // ConcurrentModificationException 异常。
    public ListIterator<E> listIterator(final int index) {
                checkForComodification();
                //...
    }
    private void checkForComodification() {
        if (ArrayList.this.modCount != this.modCount) 
            throw new ConcurrentModificationException();
    }
    //...
  }
}
```
### 一定要让合适的数据结构做合适的事情：listvsmap、linkedlist
1. 第一个误区是，使用数据结构不考虑平衡时间和空间。
 - 空间换时间   
    - **要对大 List 进行单值搜索的话，可以考虑使用 HashMap，其中 Key 是要搜索的值，Value 是原始对象，
    会比使用 ArrayList 有非常明显的性能优势。**
    - 搜索的不是单值而是条件区间，也可以尝试使用 HashMap 来进行“搜索性能优化”。如果条件区间是固定
    的话，可以提前把 HashMap 按照条件区间进行分组，Key 就是不同的区间。
    - 对大 ArrayList 进行去重操作，也不建议使用 contains 方法，而是可以考虑使用 HashSet 进行去重。
- 第二个误区是，过于迷信教科书的大 O 时间复杂度。
    - 在随机访问方面，我们看到了 ArrayList 的绝对优势，耗时只有 11 毫秒，而 LinkedList 耗时 6.6 秒，
    这符合上面所说的时间复杂度；但，随机插入操作居然也是 LinkedList 落败，耗时 9.3 秒，ArrayList 
    只要 1.5 秒.
    - LinkedList 源码发现，插入操作的时间复杂度是 O(1) 的前提是，已经有了那个要插入节点的指针。

### 思考题：listremove
