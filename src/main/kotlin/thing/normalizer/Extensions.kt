package thing.normalizer

import java.util.*

fun NormalizationRuleBuilder<String>.trim() =
  addNormalizer {
    it.trim()
  }

fun NormalizationRuleBuilder<String>.trimEnd() =
  addNormalizer {
    it.trimEnd()
  }

fun NormalizationRuleBuilder<String>.trimStart() =
  addNormalizer {
    it.trimStart()
  }

fun NormalizationRuleBuilder<String>.uppercase() =
  addNormalizer {
    it.uppercase()
  }

fun NormalizationRuleBuilder<String>.lowercase() =
  addNormalizer {
    it.lowercase()
  }

fun NormalizationRuleBuilder<String>.replace(oldValue: String, newValue: String) =
  addNormalizer {
    it.replace(oldValue, newValue)
  }

fun NormalizationRuleBuilder<String>.replace(oldValue: String, newValue: String, ignoreCase: Boolean) =
  addNormalizer {
    it.replace(oldValue, newValue, ignoreCase)
  }

fun NormalizationRuleBuilder<String>.replaceFirst(oldValue: String, newValue: String) =
  addNormalizer {
    it.replaceFirst(oldValue, newValue)
  }

fun NormalizationRuleBuilder<String>.replaceFirst(oldValue: String, newValue: String, ignoreCase: Boolean) =
  addNormalizer {
    it.replaceFirst(oldValue, newValue, ignoreCase)
  }

fun NormalizationRuleBuilder<String>.replaceRange(startIndex: Int, endIndex: Int, newValue: String) =
  addNormalizer {
    it.replaceRange(startIndex, endIndex, newValue)
  }

fun NormalizationRuleBuilder<String>.removeRange(startIndex: Int, endIndex: Int) =
  addNormalizer {
    it.removeRange(startIndex, endIndex)
  }

fun NormalizationRuleBuilder<String>.removePrefix(prefix: String) =
  addNormalizer {
    it.removePrefix(prefix)
  }

fun NormalizationRuleBuilder<String>.removeSuffix(suffix: String) =
  addNormalizer {
    it.removeSuffix(suffix)
  }

fun NormalizationRuleBuilder<String>.removeSurrounding(prefix: String, suffix: String) =
  addNormalizer {
    it.removeSurrounding(prefix, suffix)
  }

fun NormalizationRuleBuilder<Map<*, *>>.filterKeys(predicate: (key: Any?) -> Boolean) =
  addNormalizer {
    it.filterKeys(predicate)
  }

fun NormalizationRuleBuilder<Map<*, *>>.filterValues(predicate: (value: Any?) -> Boolean) =
  addNormalizer {
    it.filterValues(predicate)
  }

fun <T> NormalizationRuleBuilder<List<T>>.filter(predicate: (T) -> Boolean) =
  addNormalizer {
    it.filter(predicate)
  }

fun <T> NormalizationRuleBuilder<List<T>>.filterNotNull() =
  addNormalizer {
    it.filterNotNull()
  }

fun <T> NormalizationRuleBuilder<List<T>>.filterNot(predicate: (T) -> Boolean) =
  addNormalizer {
    it.filterNot(predicate)
  }

fun <T> NormalizationRuleBuilder<List<T>>.filterIndexed(predicate: (index: Int, T) -> Boolean) =
  addNormalizer {
    it.filterIndexed(predicate)
  }

fun <T> NormalizationRuleBuilder<List<T>>.map(transform: (T) -> T) =
  addNormalizer {
    it.map(transform)
  }

fun <T> NormalizationRuleBuilder<List<T>>.mapIndexed(transform: (index: Int, T) -> T) =
  addNormalizer {
    it.mapIndexed(transform)
  }

fun <T> NormalizationRuleBuilder<List<T>>.mapNotNull(transform: (T) -> T?) =
  addNormalizer {
    it.mapNotNull(transform)
  }

fun <T> NormalizationRuleBuilder<List<T>>.drop(n: Int) =
  addNormalizer {
    it.drop(n)
  }

fun <T> NormalizationRuleBuilder<List<T>>.dropWhile(predicate: (T) -> Boolean) =
  addNormalizer {
    it.dropWhile(predicate)
  }

fun <T> NormalizationRuleBuilder<List<T>>.take(n: Int) =
  addNormalizer {
    it.take(n)
  }

fun <T> NormalizationRuleBuilder<List<T>>.takeWhile(predicate: (T) -> Boolean) =
  addNormalizer {
    it.takeWhile(predicate)
  }

fun <T, R: Comparable<R>> NormalizationRuleBuilder<List<T>>.sortedBy(selector: (T) -> R?) =
  addNormalizer {
    it.sortedBy(selector)
  }

fun <T, R: Comparable<R>> NormalizationRuleBuilder<List<T>>.sortedByDescending(selector: (T) -> R?) =
  addNormalizer {
    it.sortedByDescending(selector)
  }

fun <T> NormalizationRuleBuilder<List<T>>.sortedWith(comparator: Comparator<in T>) =
  addNormalizer {
    it.sortedWith(comparator)
  }

fun <T> NormalizationRuleBuilder<List<T>>.reversed() =
  addNormalizer {
    it.reversed()
  }

fun <T> NormalizationRuleBuilder<List<T>>.shuffled() =
  addNormalizer {
    it.shuffled()
  }

fun <T> NormalizationRuleBuilder<List<T>>.shuffled(random: Random) =
  addNormalizer {
    it.shuffled(random)
  }

fun <T> NormalizationRuleBuilder<List<T>>.distinct() =
  addNormalizer {
    it.distinct()
  }

fun <T> NormalizationRuleBuilder<List<T>>.distinctBy(selector: (T) -> Any?) =
  addNormalizer {
    it.distinctBy(selector)
  }
