package so.kciter.thing.example

import so.kciter.thing.Rule
import so.kciter.thing.Thing
import so.kciter.thing.normalizer.trim
import so.kciter.thing.redactor.creditCard
import so.kciter.thing.validator.email
import so.kciter.thing.validator.maxLength
import so.kciter.thing.validator.minLength

data class Person(
  val username: String,
  val email: String,
  val creditCard: String
): Thing<Person> {
  override val rule: Rule<Person>
    get() = Rule {
      Normalization {
        Person::username { trim() }
        Person::email { trim() }
      }

      Validation {
        Person::username {
          minLength(2)
          maxLength(10)
        }
        Person::email { email() }
      }

      Redaction {
        Person::creditCard { creditCard() }
      }
    }
}
