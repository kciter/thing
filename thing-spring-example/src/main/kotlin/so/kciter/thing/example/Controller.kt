package so.kciter.thing.example

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import so.kciter.thing.configuration.ThingHandler

@ThingHandler
@RestController
@RequestMapping("/api")
class Controller {
  @PostMapping
  fun createPerson(@RequestBody person: Person) =
    person
}
