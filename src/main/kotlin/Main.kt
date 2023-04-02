import thing.Rule
import thing.Thing
import thing.normalizer.trim
import thing.validator.email

data class Person(
  val name: String,
  val email: String,
  val age: Int
): Thing<Person> {
  override val rule: Rule<Person>
    get() = Rule {
      Normalization {
        Person::name { trim() }
        Person::email { trim() }
      }

      Validation {
        Person::email {
          email()
        }
      }
    }
}


fun main() {
  val person = Person("", "", 1)
  val person2 = person.normalize()
}
