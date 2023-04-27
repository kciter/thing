<h1 align='center'>
  Thing :dizzy:
</h1>

<p align="center"><strong>A rule-based entity management library written in Kotlin</strong></p>

<p align='center'>
  <a href="https://cobalt.run">
    <img src="https://cobalt-static.s3.ap-northeast-2.amazonaws.com/cobalt-badge.svg" />
  </a>
  <a href="">
    <img src='https://img.shields.io/maven-central/v/so.kciter/thing' alt='Latest version'>
  </a>
  <a href="https://github.com/cobaltinc/vortex/blob/master/.github/CONTRIBUTING.md">
    <img src="https://img.shields.io/badge/PRs-welcome-brightgreen.svg" alt="PRs welcome" />
  </a>
</p>

## :rocket: Getting started
depend via Maven:
```xml
<dependency>
  <groupId>so.kciter</groupId>
  <artifactId>thing</artifactId>
  <version>0.0.1</version>
</dependency>
```
or Gradle:
```kotlin
implementation("so.kciter:thing:1.0.0")
```

## :eyes: At a glance
```kotlin
data class Person(
  val email: String,
  val creditCard: String
): Thing<Person> {
  override val rule: Rule<Person>
    get() = Rule {
      Normalization {
        Person::email { trim() }
        Person::creditCard { trim() }
      }

      Validation {
        Person::email { email() }
      }

      Redaction {
        Person::creditCard { creditCard() }
      }
    }
}

val person = Person(
  email = " kciter@naver   ",
  creditCard = "1234-1234-1234-1234"
)

println(person)
// Person(email= kciter@naver   , creditCard=1234-1234-1234-1234)
println(person.normalize())
// Person(email=kciter@naver, creditCard=1234-1234-1234-1234)
println(person.validate())
// ValidationResult.Invalid(dataPath=.email, message=must be a valid email address)
println(person.redact())
// Person(email=kciter@naver, creditCard=[REDACTED])
```

## :sparkles: Usecase

### Validation
### Normalization
### Redaction
### With Spring Boot

## :love_letter: Reference
* [konform](https://github.com/konform-kt/konform)
* [kotlin-validator](https://github.com/amir1376/kotlin-validator)
* [redact-pii](https://github.com/solvvy/redact-pii)

## :page_facing_up: License

Thing is made available under the [MIT License](./LICENSE).
