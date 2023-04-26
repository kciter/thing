package so.kciter.thing.redactor

fun RedactionRuleBuilder<String>.email() =
  addRedactor("[REDACTED]") {
    "[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}".toRegex().matches(it)
  }

fun RedactionRuleBuilder<String>.creditCard() =
  addRedactor("[REDACTED]") {
    "\\d{4}[ -]?\\d{4}[ -]?\\d{4}[ -]?\\d{4}|\\d{4}[ -]?\\d{6}[ -]?\\d{4}\\d?".toRegex().matches(it)
  }

fun RedactionRuleBuilder<String>.url() =
  addRedactor("[REDACTED]") {
    "([^\\s:/?#]+):\\/\\/([^/?#\\s]*)([^?#\\s]*)(\\?([^#\\s]*))?(#(\\S*))?".toRegex().matches(it)
  }

fun RedactionRuleBuilder<String>.ipv4() =
  addRedactor("[REDACTED]") {
    "(?:[0-9]{1,3}\\.){3}[0-9]{1,3}".toRegex().matches(it)
  }

fun RedactionRuleBuilder<String>.ipv6() =
  addRedactor("[REDACTED]") {
    "([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}".toRegex().matches(it)
  }

fun RedactionRuleBuilder<String>.phoneNumber() =
  addRedactor("[REDACTED]") {
    "(\\(?\\+?[0-9]{1,2}\\)?[-. ]?)?(\\(?[0-9]{3}\\)?|[0-9]{3})[-. ]?([0-9]{3}[-. ]?[0-9]{4}|\\b[A-Z0-9]{7}\\b)".toRegex().matches(it)
  }
