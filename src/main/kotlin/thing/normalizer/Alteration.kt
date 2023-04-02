package thing.normalizer

class Alteration<T>(val fieldName: String, val transform: (T) -> T)