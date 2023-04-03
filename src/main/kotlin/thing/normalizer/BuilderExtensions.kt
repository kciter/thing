package thing.normalizer

import java.util.*

fun NormalizationBuilder<String>.trim() =
  addAlteration {
    it.trim()
  }

fun NormalizationBuilder<String>.trimEnd() =
  addAlteration {
    it.trimEnd()
  }

fun NormalizationBuilder<String>.trimStart() =
  addAlteration {
    it.trimStart()
  }

fun NormalizationBuilder<String>.uppercase() =
  addAlteration {
    it.uppercase()
  }

fun NormalizationBuilder<String>.lowercase() =
  addAlteration {
    it.lowercase()
  }

fun NormalizationBuilder<String>.replace(oldValue: String, newValue: String) =
  addAlteration {
    it.replace(oldValue, newValue)
  }

fun NormalizationBuilder<String>.replace(oldValue: String, newValue: String, ignoreCase: Boolean) =
  addAlteration {
    it.replace(oldValue, newValue, ignoreCase)
  }

fun NormalizationBuilder<String>.replaceFirst(oldValue: String, newValue: String) =
  addAlteration {
    it.replaceFirst(oldValue, newValue)
  }

fun NormalizationBuilder<String>.replaceFirst(oldValue: String, newValue: String, ignoreCase: Boolean) =
  addAlteration {
    it.replaceFirst(oldValue, newValue, ignoreCase)
  }

fun NormalizationBuilder<String>.replaceRange(startIndex: Int, endIndex: Int, newValue: String) =
  addAlteration {
    it.replaceRange(startIndex, endIndex, newValue)
  }

fun NormalizationBuilder<String>.removeRange(startIndex: Int, endIndex: Int) =
  addAlteration {
    it.removeRange(startIndex, endIndex)
  }

fun NormalizationBuilder<String>.removePrefix(prefix: String) =
  addAlteration {
    it.removePrefix(prefix)
  }

fun NormalizationBuilder<String>.removeSuffix(suffix: String) =
  addAlteration {
    it.removeSuffix(suffix)
  }

fun NormalizationBuilder<String>.removeSurrounding(prefix: String, suffix: String) =
  addAlteration {
    it.removeSurrounding(prefix, suffix)
  }

fun NormalizationBuilder<Map<*, *>>.filterKeys(predicate: (key: Any?) -> Boolean) =
  addAlteration {
    it.filterKeys(predicate)
  }

fun NormalizationBuilder<Map<*, *>>.filterValues(predicate: (value: Any?) -> Boolean) =
  addAlteration {
    it.filterValues(predicate)
  }

fun <T> NormalizationBuilder<List<T>>.filter(predicate: (T) -> Boolean) =
  addAlteration {
    it.filter(predicate)
  }

fun <T> NormalizationBuilder<List<T>>.filterNotNull() =
  addAlteration {
    it.filterNotNull()
  }

fun <T> NormalizationBuilder<List<T>>.filterNot(predicate: (T) -> Boolean) =
  addAlteration {
    it.filterNot(predicate)
  }

fun <T> NormalizationBuilder<List<T>>.filterIndexed(predicate: (index: Int, T) -> Boolean) =
  addAlteration {
    it.filterIndexed(predicate)
  }

fun <T> NormalizationBuilder<List<T>>.map(transform: (T) -> T) =
  addAlteration {
    it.map(transform)
  }

fun <T> NormalizationBuilder<List<T>>.mapIndexed(transform: (index: Int, T) -> T) =
  addAlteration {
    it.mapIndexed(transform)
  }

fun <T> NormalizationBuilder<List<T>>.mapNotNull(transform: (T) -> T?) =
  addAlteration {
    it.mapNotNull(transform)
  }

fun <T> NormalizationBuilder<List<T>>.drop(n: Int) =
  addAlteration {
    it.drop(n)
  }

fun <T> NormalizationBuilder<List<T>>.dropWhile(predicate: (T) -> Boolean) =
  addAlteration {
    it.dropWhile(predicate)
  }

fun <T> NormalizationBuilder<List<T>>.take(n: Int) =
  addAlteration {
    it.take(n)
  }

fun <T> NormalizationBuilder<List<T>>.takeWhile(predicate: (T) -> Boolean) =
  addAlteration {
    it.takeWhile(predicate)
  }

fun <T, R: Comparable<R>> NormalizationBuilder<List<T>>.sortedBy(selector: (T) -> R?) =
  addAlteration {
    it.sortedBy(selector)
  }

fun <T, R: Comparable<R>> NormalizationBuilder<List<T>>.sortedByDescending(selector: (T) -> R?) =
  addAlteration {
    it.sortedByDescending(selector)
  }

fun <T> NormalizationBuilder<List<T>>.sortedWith(comparator: Comparator<in T>) =
  addAlteration {
    it.sortedWith(comparator)
  }

fun <T> NormalizationBuilder<List<T>>.reversed() =
  addAlteration {
    it.reversed()
  }

fun <T> NormalizationBuilder<List<T>>.shuffled() =
  addAlteration {
    it.shuffled()
  }

fun <T> NormalizationBuilder<List<T>>.shuffled(random: Random) =
  addAlteration {
    it.shuffled(random)
  }

fun <T> NormalizationBuilder<List<T>>.distinct() =
  addAlteration {
    it.distinct()
  }

fun <T> NormalizationBuilder<List<T>>.distinctBy(selector: (T) -> Any?) =
  addAlteration {
    it.distinctBy(selector)
  }
