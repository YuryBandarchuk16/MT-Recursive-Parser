

Построим грамматику для арифметических выражений с плюсом, 
умножением, унарным минусом и скобками.

Приоритет операций обычный.

```
A -> A + M
A -> A - M
A -> M
M -> M * Q
M -> Q
Q -> E
Q -> -E
E -> (A)
E -> n
```

Избавимся от левой рекурсии и правого ветвления, если есть.

Тогда итоговая грамматика примет следующий вид:

```
A  -> M A'
A' -> +M A'
A' -> -M A'
A' -> eps

M  -> Q M'
M' -> *Q M'
M' -> eps

Q  -> E
Q  -> -E

E  -> (A)
E  -> n
```

В данном контексте `eps` обозначает *пустую строку*, а `n` целое неотрицательное число с ведущими нулями.

**FIRST** и **FOLLOW** *sets*

|  Нетерминал  |      FIRST       |          FOLLOW         |
|--------------|------------------|-------------------------|
| `A`          | `(`, `-`, `n`    | `$`, `)`                |
| `A'`         | `+`, `-`, `eps`  | `$`, `)`                |
| `E`          | `(`, `n`         | `$`, `)`, `*`, `+`, `-` |
| `M`          | `(`, `-`, `n`    | `$`, `)`, `+`, `-`      |
| `M'`         | `*`, `eps`       | `$`, `)`, `+`, `-`      |
| `Q`          | `(`, `-`, `n`    | `$`, `)`, `*`, `+`, `-` |  


**Пример** дерева разбора, которое нарисует программа для выражения 
```-3 *     (2 + 1) - 7 * 6 + (1 + (1 + 5))```


![Example of drawn tree](https://github.com/YuryBandarchuk16/MT-Recursive-Parser/blob/master/example/ex1.png)


