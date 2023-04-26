import thing.Rule
import thing.Thing
import thing.normalizer.trim
import thing.redactor.creditCard
import thing.validator.email
import thing.validator.maximum
import thing.validator.minimum

data class Person(
  val username: String,
  val email: String,
  val creditCard: String,
  val age: Int
): Thing<Person> {
  override val rule: Rule<Person>
    get() = Rule {
      Normalization {
        Person::username { trim() }
        Person::email { trim() }
        Person::creditCard { trim() }
      }

      Validation {
        Person::email { email() }
        Person::age {
          minimum(10)
          maximum(70)
        }
      }

      Redaction {
        Person::creditCard { creditCard() }
      }
    }
}


fun main() {
  val person = Person(" kciter ", " kciter@naver.com ", " 1234-1234-1234-1234 ", 100)
  println(person)
  person.normalize()
  println(person)
  println(person.validate())
  person.redact()
  println(person)
}
