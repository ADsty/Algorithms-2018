@file:Suppress("UNUSED_PARAMETER")

package lesson2

import java.io.File

/**
 * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
 * Простая
 *
 * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
 * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
 *
 * 201
 * 196
 * 190
 * 198
 * 187
 * 194
 * 193
 * 185
 *
 * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
 * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
 * Вернуть пару из двух моментов.
 * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
 * Например, для приведённого выше файла результат должен быть Pair(3, 4)
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 * Сложность O(n) , Ресурсоемкость R(n)
 */
fun optimizeBuyAndSell(inputName: String): Pair<Int, Int> {
    val list = ArrayList<Int>()
    for (line in File(inputName).readLines()) {
        if (line.toInt() < 0) throw IllegalArgumentException()
        list.add(line.toInt())
    }
    val delta = IntArray(list.size - 1)
    for (i in 0..list.size - 2) {
        delta[i] = list[i + 1] - list[i]
    }
    return getMaxSubSum(delta)
}

fun getMaxSubSum(array: IntArray): Pair<Int, Int> {
    var max = 0
    var partial = 0
    var first = 0
    var result = Pair(-1, -1)
    for (i in 0..array.size - 1) {
        partial += array[i]
        if (partial > max) {
            max = partial
            result = Pair(first + 2, i + 2)
        }
        if (partial < 0) {
            partial = 0
            first = i
        }
    }
    return result
}

/**
 * Задача Иосифа Флафия.
 * Простая
 *
 * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
 *
 * 1 2 3
 * 8   4
 * 7 6 5
 *
 * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
 * Человек, на котором остановился счёт, выбывает.
 *
 * 1 2 3
 * 8   4
 * 7 6 х
 *
 * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
 * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
 *
 * 1 х 3
 * 8   4
 * 7 6 Х
 *
 * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
 *
 * 1 Х 3
 * х   4
 * 7 6 Х
 *
 * 1 Х 3
 * Х   4
 * х 6 Х
 *
 * х Х 3
 * Х   4
 * Х 6 Х
 *
 * Х Х 3
 * Х   х
 * Х 6 Х
 *
 * Х Х 3
 * Х   Х
 * Х х Х
 */
fun josephTask(menNumber: Int, choiceInterval: Int): Int {
    TODO()
}

/**
 * Наибольшая общая подстрока.
 * Средняя
 *
 * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
 * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
 * Если общих подстрок нет, вернуть пустую строку.
 * При сравнении подстрок, регистр символов *имеет* значение.
 * Если имеется несколько самых длинных общих подстрок одной длины,
 * вернуть ту из них, которая встречается раньше в строке first.
 * Сложность O(nm) , Ресурсоемкость R(nm)
 */
fun longestCommonSubstring(first: String, second: String): String {
    val matrix = Array(first.length) { IntArray(second.length) }
    var index = -1
    var length: Int
    var maxLength = 0
    for (i in 0..first.length - 1) {
        for (j in 0..second.length - 1) {
            if (first[i] == second[j]) {
                if (i > 0 && j > 0) {
                    length = matrix[i - 1][j - 1] + 1
                    if (length > maxLength) {
                        maxLength = length
                        index = i
                    }
                    matrix[i][j] = length
                } else {
                    matrix[i][j] = 1
                    if (maxLength == 0) {
                        maxLength = 1
                        index = i
                    }
                }
            }
        }
    }
    if (maxLength == 0) return ""
    return first.substring(index - maxLength + 1, index + 1)
}

/**
 * Число простых чисел в интервале
 * Простая
 *
 * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
 * Если limit <= 1, вернуть результат 0.
 *
 * Справка: простым считается число, которое делится нацело только на 1 и на себя.
 * Единица простым числом не считается.
 * Сложность O(n*sqrt(n)) , Ресурсоемкость R(1)
 */
fun calcPrimesNumber(limit: Int): Int {
    var result = 0;
    for (i in 1..limit) {
        if (isPrime(i)) result++
    }
    return result
}

fun isPrime(n: Int): Boolean {
    if (n < 2) return false
    for (m in 2..Math.sqrt(n.toDouble()).toInt()) {
        if (n % m == 0) return false
    }
    return true
}

/**
 * Балда
 * Сложная
 *
 * В файле с именем inputName задана матрица из букв в следующем формате
 * (отдельные буквы в ряду разделены пробелами):
 *
 * И Т Ы Н
 * К Р А Н
 * А К В А
 *
 * В аргументе words содержится множество слов для поиска, например,
 * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
 *
 * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
 * и вернуть множество найденных слов. В данном случае:
 * ТРАВА, КРАН, АКВА, НАРТЫ
 *
 * И т Ы Н     И т ы Н
 * К р а Н     К р а н
 * А К в а     А К В А
 *
 * Все слова и буквы -- русские или английские, прописные.
 * В файле буквы разделены пробелами, строки -- переносами строк.
 * Остальные символы ни в файле, ни в словах не допускаются.
 * Сложность O(nmh) , Ресурсоемкость R(n + h) , где n - количество букв , m - количество слов для поиска ,
 * h - длинна слов для поиска
 */
fun baldaSearcher(inputName: String, words: Set<String>): Set<String> {
    val map = HashMap<Int, String>()
    var cout = 1
    var lineLength = 0
    val output = LinkedHashSet<String>()
    for (line in File(inputName).readLines()) {
        val parts = line.split(" ")
        for (part in parts) {
            map.put(cout, part)
            cout++
        }
        lineLength = parts.size
    }
    val directions = intArrayOf(1, -1, lineLength, -lineLength)
    for (word in words) {
        for (i in map.keys) {
            val alreadyUsed = mutableListOf<Int>()
            var res = ""
            var cur = i
            cout = 1
            if (word[0] == map.get(i)!![0]) {
                res += word[0]
                alreadyUsed.add(cur)
                while (cout < word.length && search(word[cout], map, cur, directions, lineLength, alreadyUsed)
                        != Pair(-1, ' ')) {
                    res += word[cout]
                    cur += search(word[cout], map, cur, directions, lineLength, alreadyUsed).first
                    alreadyUsed.add(cur)
                    cout++
                }
            }
            if (res == word) {
                output.add(word)
                continue
            }
        }
    }
    return output
}

fun search(letter: Char, map: HashMap<Int, String>, cur: Int, dirs: IntArray, lineLength: Int, dir: MutableList<Int>)
        : Pair<Int, Char> {
    for (k in dirs) {
        if (dir.contains(k + cur)) continue
        if (map.containsKey(k + cur)) {
            if (k == 1 && cur % lineLength == 0 || k == -1 && cur % lineLength == 1) {
                continue
            }
            if (letter == map.get(cur + k)!![0]) {
                return Pair(k, letter)
            }
        }
    }
    return Pair(-1, ' ')
}