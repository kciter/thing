package so.kciter.thing.configuration

import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor
import org.springframework.aop.support.DefaultPointcutAdvisor
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

@Component
class ThingPostProcessor: AbstractBeanFactoryAwareAdvisingPostProcessor(), InitializingBean {
  override fun afterPropertiesSet() {
    val pointcut = AnnotationMatchingPointcut(ThingHandler::class.java, true)
    advisor = DefaultPointcutAdvisor(pointcut, ThingInterceptor())
  }
}

