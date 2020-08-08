## 20%的业务代码的Spring声明式事务，可能都没处理正确
### 针对 private 方法启用事务：transactionprivatemethod
- 动态代理方式的 AOP 不可行，需要使用静态织入方式的 AOP，也就是在编译期间织入事务增强代码，可以
配置 Spring 框架使用 AspectJ 来实现 AOP。

### 小心Spring的事务可能没有生效：transactionproxyfailed
//在UserService中通过this调用public的createUserPublic
[10:10:19.913] [http-nio-45678-exec-1] [DEBUG] [o.s.orm.jpa.JpaTransactionManager       :370 ] 
- Creating new transaction with name 
[org.springframework.data.jpa.repository.support.SimpleJpaRepository.save]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT

//在Controller中通过注入的UserService Bean调用createUserPublic
[10:10:47.750] [http-nio-45678-exec-6] [DEBUG] [o.s.orm.jpa.JpaTransactionManager       :370 ]
 - Creating new transaction with name 
 [org.geekbang.time.commonmistakes.transaction.demo1.UserService.createUserPublic]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT

### 事务即便生效也不一定能回滚：transactionrollbackfailed
> 通过 AOP 实现事务处理可以理解为，使用 try…catch…来包裹标记了 @Transactional 注解的方法，
> 当方法出现了异常并且满足一定条件的时候，在 catch 里面我们可以设置事务回滚，没有异常则直
> 接提交事务。
- 第一，只有异常传播出了标记了 @Transactional 注解的方法，事务才能回滚。
```
TransactionAspectSupport#invokeWithinTransaction()
try { 
    // This is an around advice: Invoke the next interceptor in the chain. 
    // This will normally result in a target object being invoked. 
    retVal = invocation.proceedWithInvocation();
} catch (Throwable ex) { 
    // target invocation exception 
    completeTransactionAfterThrowing(txInfo, ex); 
throw ex;
} finally { 
    cleanupTransactionInfo(txInfo);
}
```
- 第二，默认情况下，出现 RuntimeException（非受检异常）或 Error 的时候，Spring 才会回滚事务。
`参考 DefaultTransactionAttribute#rollbackOn`

### 请确认事务传播配置是否符合自己的业务逻辑：transactionpropagation
**createUserWrong2**
- Creating new transaction with name [...UserService.createUserWrong2]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
**第 1 行所示，对 createUserWrong2 方法开启了异常处理；**
- createSubUserWithExceptionWrong start
- Found thread-bound EntityManager [SessionImpl(1794007607<open>)] for JPA transaction
- Participating in existing transaction
- Participating transaction failed - marking existing transaction as rollback-only
**第 5 行所示，子方法因为出现了运行时异常，标记当前事务为回滚；**
- Setting JPA transaction on EntityManager [SessionImpl(1794007607<open>)] rollback-only
- create sub user error:invalid status
**第 7 行所示，主方法的确捕获了异常打印出了 create sub user error 字样；**
- Initiating transaction commit
- Committing JPA transaction on EntityManager [SessionImpl(1794007607<open>)]
**第 9 行所示，主方法提交了事务；**
- Closing JPA EntityManager [SessionImpl(1794007607<open>)] after transaction
 - createUserWrong2 failed, reason:Transaction silently rolled back because it has been marked as rollback-only
org.springframework.transaction.UnexpectedRollbackException: Transaction silently rolled back because it has been marked as rollback-only
**第 11 行和 12 行所示，Controller 里出现了一个 UnexpectedRollbackException，异常描述提示最终这个事务回滚了，而且是静默回滚的。之所以说是静默，
是因为 createUserWrong2 方法本身并没有出异常，只不过提交后发现子方法已经把当前事务设置为了回滚，无法完成提交。**

**createUserRight**
- Creating new transaction with name [...createUserRight]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
**第 1 行日志提示我们针对 createUserRight 方法开启了主方法的事务；**
- createMainUser finish
**第 2 行日志提示创建主用户完成；**
- Suspending current transaction, creating new transaction with name [...SubUserService.createSubUserWithExceptionRight]
**第 3 行日志可以看到主事务挂起了，开启了一个新的事务，针对 createSubUserWithExceptionRight 方案，也就是我们的创建子用户的逻辑；**
- Initiating transaction rollback
**第 4 行日志提示子方法事务回滚；**
- Resuming suspended transaction after completion of inner transaction
**第 5 行日志提示子方法事务完成，继续主方法之前挂起的事务；**
- create sub user error:invalid status
**第 6 行日志提示主方法捕获到了子方法的异常；**
- Initiating transaction commit
- Committing JPA transaction on EntityManager [SessionImpl(396441411<open>)]
**第 8 行日志提示主方法的事务提交了，随后我们在 Controller 里没看到静默回滚的异常。**

### （补充）使用MyBatis配合Propagation.NESTED事务传播模式的例子：nested